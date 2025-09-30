package com.gaming.reviewservice.repository;

import com.gaming.reviewservice.model.Review;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ReviewRepository extends R2dbcRepository<Review, Long> {

    // Buscar reseñas activas
    Flux<Review> findByIsActiveTrue();

    // Buscar reseñas por juego
    Flux<Review> findByGameIdAndIsActiveTrue(Long gameId);

    // Buscar reseñas por usuario
    Flux<Review> findByUserIdAndIsActiveTrue(Long userId);

    // Buscar reseñas por juego y usuario (para verificar si ya existe)
    Mono<Review> findByGameIdAndUserIdAndIsActiveTrue(Long gameId, Long userId);

    // Verificar si un usuario ya reseñó un juego
    Mono<Boolean> existsByGameIdAndUserIdAndIsActiveTrue(Long gameId, Long userId);

    // Buscar reseñas por calificación
    Flux<Review> findByRatingAndIsActiveTrue(Integer rating);

    // Buscar reseñas por rango de calificación
    @Query("SELECT * FROM reviews WHERE rating BETWEEN :minRating AND :maxRating AND is_active = true ORDER BY created_at DESC")
    Flux<Review> findByRatingRange(Integer minRating, Integer maxRating);

    // Buscar reseñas aprobadas
    Flux<Review> findByIsApprovedTrueAndIsActiveTrue();

    // Buscar reseñas pendientes de aprobación
    Flux<Review> findByIsApprovedFalseAndIsActiveTrue();

    // Buscar reseñas de compras verificadas
    Flux<Review> findByIsVerifiedPurchaseTrueAndIsActiveTrue();

    // Buscar reseñas por juego con filtros
    @Query("SELECT * FROM reviews WHERE game_id = :gameId AND " +
            "(:rating IS NULL OR rating = :rating) AND " +
            "(:verifiedOnly IS FALSE OR is_verified_purchase = true) AND " +
            "is_approved = true AND is_active = true " +
            "ORDER BY created_at DESC")
    Flux<Review> findReviewsByGameWithFilters(Long gameId, Integer rating, Boolean verifiedOnly);

    // Obtener calificación promedio por juego
    @Query("SELECT AVG(rating::decimal) FROM reviews WHERE game_id = :gameId AND is_approved = true AND is_active = true")
    Mono<Double> getAverageRatingByGame(Long gameId);

    // Contar reseñas por juego
    @Query("SELECT COUNT(*) FROM reviews WHERE game_id = :gameId AND is_approved = true AND is_active = true")
    Mono<Long> countReviewsByGame(Long gameId);

    // Contar reseñas por calificación y juego
    @Query("SELECT COUNT(*) FROM reviews WHERE game_id = :gameId AND rating = :rating AND is_approved = true AND is_active = true")
    Mono<Long> countReviewsByGameAndRating(Long gameId, Integer rating);

    // Obtener reseñas más útiles de un juego
    @Query("SELECT * FROM reviews WHERE game_id = :gameId AND is_approved = true AND is_active = true " +
            "ORDER BY helpful_count DESC, created_at DESC LIMIT :limit")
    Flux<Review> findMostHelpfulReviewsByGame(Long gameId, int limit);

    // Obtener reseñas más recientes de un juego
    @Query("SELECT * FROM reviews WHERE game_id = :gameId AND is_approved = true AND is_active = true " +
            "ORDER BY created_at DESC LIMIT :limit")
    Flux<Review> findRecentReviewsByGame(Long gameId, int limit);

    // Buscar reseñas por texto en comentario
    @Query("SELECT * FROM reviews WHERE LOWER(comment) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "AND is_approved = true AND is_active = true ORDER BY created_at DESC")
    Flux<Review> findByCommentContainingIgnoreCase(String searchText);

    // Incrementar contador de útil
    @Query("UPDATE reviews SET helpful_count = helpful_count + 1, updated_at = CURRENT_TIMESTAMP WHERE id = :reviewId")
    Mono<Integer> incrementHelpfulCount(Long reviewId);

    // Incrementar contador de no útil
    @Query("UPDATE reviews SET unhelpful_count = unhelpful_count + 1, updated_at = CURRENT_TIMESTAMP WHERE id = :reviewId")
    Mono<Integer> incrementUnhelpfulCount(Long reviewId);

    // Aprobar reseña
    @Query("UPDATE reviews SET is_approved = true, updated_at = CURRENT_TIMESTAMP WHERE id = :reviewId")
    Mono<Integer> approveReview(Long reviewId);

    // Rechazar reseña
    @Query("UPDATE reviews SET is_approved = false, updated_at = CURRENT_TIMESTAMP WHERE id = :reviewId")
    Mono<Integer> rejectReview(Long reviewId);

    // Soft delete - marcar como inactiva
    @Query("UPDATE reviews SET is_active = false, updated_at = CURRENT_TIMESTAMP WHERE id = :id")
    Mono<Integer> softDeleteById(Long id);

    // Obtener estadísticas de calificaciones por juego
    @Query("SELECT rating, COUNT(*) as count FROM reviews WHERE game_id = :gameId AND is_approved = true AND is_active = true GROUP BY rating ORDER BY rating")
    Flux<Object[]> getRatingStatsByGame(Long gameId);

    // Contar reseñas pendientes de moderación
    @Query("SELECT COUNT(*) FROM reviews WHERE is_approved = false AND is_active = true")
    Mono<Long> countPendingReviews();

    // Obtener reseñas más controvertidas (con muchos votos mixtos)
    @Query("SELECT * FROM reviews WHERE (helpful_count + unhelpful_count) > 10 AND is_approved = true AND is_active = true " +
            "ORDER BY ABS(helpful_count - unhelpful_count) ASC, (helpful_count + unhelpful_count) DESC LIMIT :limit")
    Flux<Review> findMostControversialReviews(int limit);
}