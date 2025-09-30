package com.gaming.reviewservice.controller;

import com.gaming.reviewservice.model.Review;
import com.gaming.reviewservice.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // GET /api/reviews - Obtener todas las reseñas
    @GetMapping
    public Flux<Review> getAllReviews() {
        logger.info("Solicitud para obtener todas las reseñas");
        return reviewService.getAllReviews();
    }

    // GET /api/reviews/{id} - Obtener reseña por ID
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Review>> getReviewById(@PathVariable Long id) {
        logger.info("Solicitud para obtener reseña con ID: {}", id);
        return reviewService.getReviewById(id)
                .map(review -> ResponseEntity.ok(review))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    // POST /api/reviews - Crear nueva reseña
    @PostMapping
    public Mono<ResponseEntity<Review>> createReview(@Valid @RequestBody Review review) {
        logger.info("Solicitud para crear nueva reseña: Game ID {} por User ID {}", review.getGameId(), review.getUserId());
        return reviewService.createReview(review)
                .map(createdReview -> ResponseEntity.status(HttpStatus.CREATED).body(createdReview))
                .onErrorReturn(ResponseEntity.badRequest().build());
    }

    // PUT /api/reviews/{id} - Actualizar reseña
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Review>> updateReview(@PathVariable Long id, @Valid @RequestBody Review review) {
        logger.info("Solicitud para actualizar reseña con ID: {}", id);
        return reviewService.updateReview(id, review)
                .map(updatedReview -> ResponseEntity.ok(updatedReview))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    // DELETE /api/reviews/{id} - Eliminar reseña (soft delete)
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteReview(@PathVariable Long id) {
        logger.info("Solicitud para eliminar reseña con ID: {}", id);
        return reviewService.deleteReview(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    // GET /api/reviews/game/{gameId} - Obtener reseñas por juego
    @GetMapping("/game/{gameId}")
    public Flux<Review> getReviewsByGame(@PathVariable Long gameId) {
        logger.info("Búsqueda de reseñas para el juego: {}", gameId);
        return reviewService.getReviewsByGame(gameId);
    }

    // GET /api/reviews/user/{userId} - Obtener reseñas por usuario
    @GetMapping("/user/{userId}")
    public Flux<Review> getReviewsByUser(@PathVariable Long userId) {
        logger.info("Búsqueda de reseñas del usuario: {}", userId);
        return reviewService.getReviewsByUser(userId);
    }

    // GET /api/reviews/game/{gameId}/user/{userId} - Obtener reseña específica de usuario para juego
    @GetMapping("/game/{gameId}/user/{userId}")
    public Mono<ResponseEntity<Review>> getReviewByGameAndUser(@PathVariable Long gameId, @PathVariable Long userId) {
        logger.info("Búsqueda de reseña del juego {} por usuario {}", gameId, userId);
        return reviewService.getReviewByGameAndUser(gameId, userId)
                .map(review -> ResponseEntity.ok(review))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // GET /api/reviews/rating/{rating} - Obtener reseñas por calificación
    @GetMapping("/rating/{rating}")
    public Flux<Review> getReviewsByRating(@PathVariable Integer rating) {
        logger.info("Búsqueda de reseñas con calificación: {}", rating);
        return reviewService.getReviewsByRating(rating);
    }

    // GET /api/reviews/rating-range?min=1&max=5 - Obtener reseñas por rango de calificación
    @GetMapping("/rating-range")
    public Flux<Review> getReviewsByRatingRange(@RequestParam Integer min, @RequestParam Integer max) {
        logger.info("Búsqueda de reseñas en rango de calificación: {} - {}", min, max);
        return reviewService.getReviewsByRatingRange(min, max);
    }

    // GET /api/reviews/approved - Obtener reseñas aprobadas
    @GetMapping("/approved")
    public Flux<Review> getApprovedReviews() {
        logger.info("Búsqueda de reseñas aprobadas");
        return reviewService.getApprovedReviews();
    }

    // GET /api/reviews/pending - Obtener reseñas pendientes de aprobación
    @GetMapping("/pending")
    public Flux<Review> getPendingReviews() {
        logger.info("Búsqueda de reseñas pendientes de aprobación");
        return reviewService.getPendingReviews();
    }

    // GET /api/reviews/verified-purchase - Obtener reseñas de compras verificadas
    @GetMapping("/verified-purchase")
    public Flux<Review> getVerifiedPurchaseReviews() {
        logger.info("Búsqueda de reseñas de compras verificadas");
        return reviewService.getVerifiedPurchaseReviews();
    }

    // GET /api/reviews/game/{gameId}/filter?rating=5&verifiedOnly=true - Filtrar reseñas por juego
    @GetMapping("/game/{gameId}/filter")
    public Flux<Review> getReviewsByGameWithFilters(
            @PathVariable Long gameId,
            @RequestParam(required = false) Integer rating,
            @RequestParam(required = false) Boolean verifiedOnly) {
        logger.info("Búsqueda filtrada de reseñas para juego {}: rating={}, verifiedOnly={}", gameId, rating, verifiedOnly);
        return reviewService.getReviewsByGameWithFilters(gameId, rating, verifiedOnly);
    }

    // GET /api/reviews/search?text=excelente - Buscar reseñas por texto
    @GetMapping("/search")
    public Flux<Review> searchReviewsByComment(@RequestParam String text) {
        logger.info("Búsqueda de reseñas por texto: {}", text);
        return reviewService.searchReviewsByComment(text);
    }

    // GET /api/reviews/game/{gameId}/average - Obtener calificación promedio de un juego
    @GetMapping("/game/{gameId}/average")
    public Mono<ResponseEntity<Double>> getAverageRatingByGame(@PathVariable Long gameId) {
        logger.info("Calculando calificación promedio para el juego: {}", gameId);
        return reviewService.getAverageRatingByGame(gameId)
                .map(average -> ResponseEntity.ok(average));
    }

    // GET /api/reviews/game/{gameId}/count - Contar reseñas de un juego
    @GetMapping("/game/{gameId}/count")
    public Mono<ResponseEntity<Long>> countReviewsByGame(@PathVariable Long gameId) {
        logger.info("Contando reseñas para el juego: {}", gameId);
        return reviewService.countReviewsByGame(gameId)
                .map(count -> ResponseEntity.ok(count));
    }

    // GET /api/reviews/game/{gameId}/count/rating/{rating} - Contar reseñas por calificación específica
    @GetMapping("/game/{gameId}/count/rating/{rating}")
    public Mono<ResponseEntity<Long>> countReviewsByGameAndRating(@PathVariable Long gameId, @PathVariable Integer rating) {
        logger.info("Contando reseñas con calificación {} para el juego: {}", rating, gameId);
        return reviewService.countReviewsByGameAndRating(gameId, rating)
                .map(count -> ResponseEntity.ok(count));
    }

    // GET /api/reviews/game/{gameId}/helpful?limit=5 - Obtener reseñas más útiles
    @GetMapping("/game/{gameId}/helpful")
    public Flux<Review> getMostHelpfulReviewsByGame(@PathVariable Long gameId, @RequestParam(defaultValue = "5") int limit) {
        logger.info("Búsqueda de las {} reseñas más útiles del juego: {}", limit, gameId);
        return reviewService.getMostHelpfulReviewsByGame(gameId, limit);
    }

    // GET /api/reviews/game/{gameId}/recent?limit=10 - Obtener reseñas más recientes
    @GetMapping("/game/{gameId}/recent")
    public Flux<Review> getRecentReviewsByGame(@PathVariable Long gameId, @RequestParam(defaultValue = "10") int limit) {
        logger.info("Búsqueda de las {} reseñas más recientes del juego: {}", limit, gameId);
        return reviewService.getRecentReviewsByGame(gameId, limit);
    }

    // GET /api/reviews/controversial?limit=10 - Obtener reseñas más controvertidas
    @GetMapping("/controversial")
    public Flux<Review> getMostControversialReviews(@RequestParam(defaultValue = "10") int limit) {
        logger.info("Búsqueda de las {} reseñas más controvertidas", limit);
        return reviewService.getMostControversialReviews(limit);
    }

    // POST /api/reviews/{id}/helpful - Marcar reseña como útil
    @PostMapping("/{id}/helpful")
    public Mono<ResponseEntity<Review>> markReviewAsHelpful(@PathVariable Long id) {
        logger.info("Marcando reseña {} como útil", id);
        return reviewService.markReviewAsHelpful(id)
                .map(review -> ResponseEntity.ok(review))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    // POST /api/reviews/{id}/unhelpful - Marcar reseña como no útil
    @PostMapping("/{id}/unhelpful")
    public Mono<ResponseEntity<Review>> markReviewAsUnhelpful(@PathVariable Long id) {
        logger.info("Marcando reseña {} como no útil", id);
        return reviewService.markReviewAsUnhelpful(id)
                .map(review -> ResponseEntity.ok(review))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    // POST /api/reviews/{id}/approve - Aprobar reseña (moderación)
    @PostMapping("/{id}/approve")
    public Mono<ResponseEntity<Review>> approveReview(@PathVariable Long id) {
        logger.info("Aprobando reseña: {}", id);
        return reviewService.approveReview(id)
                .map(review -> ResponseEntity.ok(review))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    // POST /api/reviews/{id}/reject - Rechazar reseña (moderación)
    @PostMapping("/{id}/reject")
    public Mono<ResponseEntity<Review>> rejectReview(@PathVariable Long id) {
        logger.info("Rechazando reseña: {}", id);
        return reviewService.rejectReview(id)
                .map(review -> ResponseEntity.ok(review))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    // GET /api/reviews/check?gameId=1&userId=1 - Verificar si usuario ya reseñó juego
    @GetMapping("/check")
    public Mono<ResponseEntity<Boolean>> hasUserReviewedGame(@RequestParam Long gameId, @RequestParam Long userId) {
        logger.info("Verificando si usuario {} ya reseñó juego {}", userId, gameId);
        return reviewService.hasUserReviewedGame(gameId, userId)
                .map(hasReviewed -> ResponseEntity.ok(hasReviewed));
    }

    // GET /api/reviews/count/pending - Contar reseñas pendientes
    @GetMapping("/count/pending")
    public Mono<ResponseEntity<Long>> countPendingReviews() {
        logger.info("Contando reseñas pendientes de moderación");
        return reviewService.countPendingReviews()
                .map(count -> ResponseEntity.ok(count));
    }
}