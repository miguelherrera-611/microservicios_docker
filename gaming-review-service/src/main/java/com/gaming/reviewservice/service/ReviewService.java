package com.gaming.reviewservice.service;

import com.gaming.reviewservice.model.Review;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReviewService {

    // Operaciones CRUD básicas
    Mono<Review> createReview(Review review);
    Mono<Review> getReviewById(Long id);
    Flux<Review> getAllReviews();
    Mono<Review> updateReview(Long id, Review review);
    Mono<Void> deleteReview(Long id);

    // Búsquedas específicas
    Flux<Review> getActiveReviews();
    Flux<Review> getReviewsByGame(Long gameId);
    Flux<Review> getReviewsByUser(Long userId);
    Mono<Review> getReviewByGameAndUser(Long gameId, Long userId);
    Flux<Review> getReviewsByRating(Integer rating);
    Flux<Review> getReviewsByRatingRange(Integer minRating, Integer maxRating);

    // Filtros y búsquedas avanzadas
    Flux<Review> getApprovedReviews();
    Flux<Review> getPendingReviews();
    Flux<Review> getVerifiedPurchaseReviews();
    Flux<Review> getReviewsByGameWithFilters(Long gameId, Integer rating, Boolean verifiedOnly);
    Flux<Review> searchReviewsByComment(String searchText);

    // Estadísticas y métricas
    Mono<Double> getAverageRatingByGame(Long gameId);
    Mono<Long> countReviewsByGame(Long gameId);
    Mono<Long> countReviewsByGameAndRating(Long gameId, Integer rating);
    Mono<Long> countPendingReviews();

    // Reseñas destacadas
    Flux<Review> getMostHelpfulReviewsByGame(Long gameId, int limit);
    Flux<Review> getRecentReviewsByGame(Long gameId, int limit);
    Flux<Review> getMostControversialReviews(int limit);

    // Gestión de utilidad
    Mono<Review> markReviewAsHelpful(Long reviewId);
    Mono<Review> markReviewAsUnhelpful(Long reviewId);

    // Moderación
    Mono<Review> approveReview(Long reviewId);
    Mono<Review> rejectReview(Long reviewId);

    // Validaciones
    Mono<Boolean> hasUserReviewedGame(Long gameId, Long userId);
}