package com.gaming.reviewservice.service.impl;

import com.gaming.reviewservice.model.Review;
import com.gaming.reviewservice.repository.ReviewRepository;
import com.gaming.reviewservice.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class ReviewServiceImpl implements ReviewService {

    private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);
    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Mono<Review> createReview(Review review) {
        return reviewRepository.existsByGameIdAndUserIdAndIsActiveTrue(review.getGameId(), review.getUserId())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new IllegalArgumentException("El usuario ya ha reseñado este juego"));
                    }

                    review.setCreatedAt(LocalDateTime.now());
                    review.setUpdatedAt(LocalDateTime.now());
                    review.setIsActive(true);
                    review.setIsApproved(true); // Auto-aprobación por defecto
                    review.setHelpfulCount(0);
                    review.setUnhelpfulCount(0);

                    return reviewRepository.save(review)
                            .doOnSuccess(savedReview -> logger.info("Reseña creada exitosamente: Game ID {} por User ID {}",
                                    savedReview.getGameId(), savedReview.getUserId()))
                            .doOnError(error -> logger.error("Error al crear reseña: {}", error.getMessage()));
                });
    }

    @Override
    public Mono<Review> getReviewById(Long id) {
        return reviewRepository.findById(id)
                .filter(Review::getIsActive)
                .switchIfEmpty(Mono.error(new RuntimeException("Reseña no encontrada con ID: " + id)))
                .doOnSuccess(review -> logger.debug("Reseña encontrada: ID {}", review.getId()))
                .doOnError(error -> logger.error("Error al buscar reseña por ID {}: {}", id, error.getMessage()));
    }

    @Override
    public Flux<Review> getAllReviews() {
        return reviewRepository.findByIsActiveTrue()
                .doOnComplete(() -> logger.debug("Se obtuvieron todas las reseñas activas"))
                .doOnError(error -> logger.error("Error al obtener todas las reseñas: {}", error.getMessage()));
    }

    @Override
    public Mono<Review> updateReview(Long id, Review reviewUpdate) {
        return reviewRepository.findById(id)
                .filter(Review::getIsActive)
                .switchIfEmpty(Mono.error(new RuntimeException("Reseña no encontrada con ID: " + id)))
                .flatMap(existingReview -> {
                    existingReview.setRating(reviewUpdate.getRating());
                    existingReview.setComment(reviewUpdate.getComment());
                    existingReview.setUpdatedAt(LocalDateTime.now());

                    return reviewRepository.save(existingReview);
                })
                .doOnSuccess(updatedReview -> logger.info("Reseña actualizada exitosamente: ID {}", updatedReview.getId()))
                .doOnError(error -> logger.error("Error al actualizar reseña con ID {}: {}", id, error.getMessage()));
    }

    @Override
    public Mono<Void> deleteReview(Long id) {
        return reviewRepository.findById(id)
                .filter(Review::getIsActive)
                .switchIfEmpty(Mono.error(new RuntimeException("Reseña no encontrada con ID: " + id)))
                .flatMap(review -> reviewRepository.softDeleteById(id))
                .then()
                .doOnSuccess(unused -> logger.info("Reseña eliminada (soft delete) con ID: {}", id))
                .doOnError(error -> logger.error("Error al eliminar reseña con ID {}: {}", id, error.getMessage()));
    }

    @Override
    public Flux<Review> getActiveReviews() {
        return reviewRepository.findByIsActiveTrue()
                .doOnError(error -> logger.error("Error al obtener reseñas activas: {}", error.getMessage()));
    }

    @Override
    public Flux<Review> getReviewsByGame(Long gameId) {
        return reviewRepository.findByGameIdAndIsActiveTrue(gameId)
                .doOnError(error -> logger.error("Error al obtener reseñas del juego {}: {}", gameId, error.getMessage()));
    }

    @Override
    public Flux<Review> getReviewsByUser(Long userId) {
        return reviewRepository.findByUserIdAndIsActiveTrue(userId)
                .doOnError(error -> logger.error("Error al obtener reseñas del usuario {}: {}", userId, error.getMessage()));
    }

    @Override
    public Mono<Review> getReviewByGameAndUser(Long gameId, Long userId) {
        return reviewRepository.findByGameIdAndUserIdAndIsActiveTrue(gameId, userId)
                .doOnError(error -> logger.error("Error al obtener reseña del juego {} y usuario {}: {}", gameId, userId, error.getMessage()));
    }

    @Override
    public Flux<Review> getReviewsByRating(Integer rating) {
        return reviewRepository.findByRatingAndIsActiveTrue(rating)
                .doOnError(error -> logger.error("Error al obtener reseñas con calificación {}: {}", rating, error.getMessage()));
    }

    @Override
    public Flux<Review> getReviewsByRatingRange(Integer minRating, Integer maxRating) {
        return reviewRepository.findByRatingRange(minRating, maxRating)
                .doOnError(error -> logger.error("Error al obtener reseñas en rango {}-{}: {}", minRating, maxRating, error.getMessage()));
    }

    @Override
    public Flux<Review> getApprovedReviews() {
        return reviewRepository.findByIsApprovedTrueAndIsActiveTrue()
                .doOnError(error -> logger.error("Error al obtener reseñas aprobadas: {}", error.getMessage()));
    }

    @Override
    public Flux<Review> getPendingReviews() {
        return reviewRepository.findByIsApprovedFalseAndIsActiveTrue()
                .doOnError(error -> logger.error("Error al obtener reseñas pendientes: {}", error.getMessage()));
    }

    @Override
    public Flux<Review> getVerifiedPurchaseReviews() {
        return reviewRepository.findByIsVerifiedPurchaseTrueAndIsActiveTrue()
                .doOnError(error -> logger.error("Error al obtener reseñas de compras verificadas: {}", error.getMessage()));
    }

    @Override
    public Flux<Review> getReviewsByGameWithFilters(Long gameId, Integer rating, Boolean verifiedOnly) {
        return reviewRepository.findReviewsByGameWithFilters(gameId, rating, verifiedOnly != null ? verifiedOnly : false)
                .doOnError(error -> logger.error("Error al obtener reseñas filtradas del juego {}: {}", gameId, error.getMessage()));
    }

    @Override
    public Flux<Review> searchReviewsByComment(String searchText) {
        return reviewRepository.findByCommentContainingIgnoreCase(searchText)
                .doOnError(error -> logger.error("Error al buscar reseñas por texto '{}': {}", searchText, error.getMessage()));
    }

    @Override
    public Mono<Double> getAverageRatingByGame(Long gameId) {
        return reviewRepository.getAverageRatingByGame(gameId)
                .defaultIfEmpty(0.0)
                .doOnError(error -> logger.error("Error al calcular promedio del juego {}: {}", gameId, error.getMessage()));
    }

    @Override
    public Mono<Long> countReviewsByGame(Long gameId) {
        return reviewRepository.countReviewsByGame(gameId)
                .defaultIfEmpty(0L)
                .doOnError(error -> logger.error("Error al contar reseñas del juego {}: {}", gameId, error.getMessage()));
    }

    @Override
    public Mono<Long> countReviewsByGameAndRating(Long gameId, Integer rating) {
        return reviewRepository.countReviewsByGameAndRating(gameId, rating)
                .defaultIfEmpty(0L)
                .doOnError(error -> logger.error("Error al contar reseñas del juego {} con calificación {}: {}", gameId, rating, error.getMessage()));
    }

    @Override
    public Mono<Long> countPendingReviews() {
        return reviewRepository.countPendingReviews()
                .defaultIfEmpty(0L)
                .doOnError(error -> logger.error("Error al contar reseñas pendientes: {}", error.getMessage()));
    }

    @Override
    public Flux<Review> getMostHelpfulReviewsByGame(Long gameId, int limit) {
        return reviewRepository.findMostHelpfulReviewsByGame(gameId, limit)
                .doOnError(error -> logger.error("Error al obtener reseñas más útiles del juego {}: {}", gameId, error.getMessage()));
    }

    @Override
    public Flux<Review> getRecentReviewsByGame(Long gameId, int limit) {
        return reviewRepository.findRecentReviewsByGame(gameId, limit)
                .doOnError(error -> logger.error("Error al obtener reseñas recientes del juego {}: {}", gameId, error.getMessage()));
    }

    @Override
    public Flux<Review> getMostControversialReviews(int limit) {
        return reviewRepository.findMostControversialReviews(limit)
                .doOnError(error -> logger.error("Error al obtener reseñas controvertidas: {}", error.getMessage()));
    }

    @Override
    public Mono<Review> markReviewAsHelpful(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .filter(Review::getIsActive)
                .switchIfEmpty(Mono.error(new RuntimeException("Reseña no encontrada con ID: " + reviewId)))
                .flatMap(review -> reviewRepository.incrementHelpfulCount(reviewId).thenReturn(review))
                .doOnSuccess(review -> logger.info("Marcada como útil la reseña ID: {}", reviewId))
                .doOnError(error -> logger.error("Error al marcar como útil la reseña {}: {}", reviewId, error.getMessage()));
    }

    @Override
    public Mono<Review> markReviewAsUnhelpful(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .filter(Review::getIsActive)
                .switchIfEmpty(Mono.error(new RuntimeException("Reseña no encontrada con ID: " + reviewId)))
                .flatMap(review -> reviewRepository.incrementUnhelpfulCount(reviewId).thenReturn(review))
                .doOnSuccess(review -> logger.info("Marcada como no útil la reseña ID: {}", reviewId))
                .doOnError(error -> logger.error("Error al marcar como no útil la reseña {}: {}", reviewId, error.getMessage()));
    }

    @Override
    public Mono<Review> approveReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .filter(Review::getIsActive)
                .switchIfEmpty(Mono.error(new RuntimeException("Reseña no encontrada con ID: " + reviewId)))
                .flatMap(review -> reviewRepository.approveReview(reviewId).thenReturn(review))
                .doOnSuccess(review -> logger.info("Reseña aprobada ID: {}", reviewId))
                .doOnError(error -> logger.error("Error al aprobar reseña {}: {}", reviewId, error.getMessage()));
    }

    @Override
    public Mono<Review> rejectReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .filter(Review::getIsActive)
                .switchIfEmpty(Mono.error(new RuntimeException("Reseña no encontrada con ID: " + reviewId)))
                .flatMap(review -> reviewRepository.rejectReview(reviewId).thenReturn(review))
                .doOnSuccess(review -> logger.info("Reseña rechazada ID: {}", reviewId))
                .doOnError(error -> logger.error("Error al rechazar reseña {}: {}", reviewId, error.getMessage()));
    }

    @Override
    public Mono<Boolean> hasUserReviewedGame(Long gameId, Long userId) {
        return reviewRepository.existsByGameIdAndUserIdAndIsActiveTrue(gameId, userId)
                .doOnError(error -> logger.error("Error al verificar si usuario {} reseñó juego {}: {}", userId, gameId, error.getMessage()));
    }
}