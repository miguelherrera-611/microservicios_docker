package com.gaming.gameservice.repository;

import com.gaming.gameservice.model.Game;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface GameRepository extends R2dbcRepository<Game, Long> {

    // Buscar juegos activos
    Flux<Game> findByIsActiveTrue();

    // Buscar por título (ignorando mayúsculas)
    @Query("SELECT * FROM games WHERE LOWER(title) LIKE LOWER(CONCAT('%', :title, '%')) AND is_active = true")
    Flux<Game> findByTitleContainingIgnoreCase(String title);

    // Buscar por género
    Flux<Game> findByGenreAndIsActiveTrue(String genre);

    // Buscar por plataforma
    Flux<Game> findByPlatformAndIsActiveTrue(String platform);

    // Buscar por desarrollador
    Flux<Game> findByDeveloperContainingIgnoreCaseAndIsActiveTrue(String developer);

    // Buscar por rango de precios
    @Query("SELECT * FROM games WHERE price BETWEEN :minPrice AND :maxPrice AND is_active = true ORDER BY price ASC")
    Flux<Game> findByPriceRange(Double minPrice, Double maxPrice);

    // Buscar por año de lanzamiento
    Flux<Game> findByReleaseYearAndIsActiveTrue(Integer releaseYear);

    // Buscar juegos con stock disponible
    @Query("SELECT * FROM games WHERE stock > 0 AND is_active = true")
    Flux<Game> findGamesWithStock();

    // Buscar juegos más populares (por stock vendido - calculado como diferencia)
    @Query("SELECT * FROM games WHERE is_active = true ORDER BY (100 - stock) DESC LIMIT :limit")
    Flux<Game> findMostPopularGames(int limit);

    // Verificar si existe un juego por título (para evitar duplicados)
    Mono<Boolean> existsByTitleIgnoreCaseAndIsActiveTrue(String title);

    // Buscar por múltiples criterios
    @Query("SELECT * FROM games WHERE " +
            "(:genre IS NULL OR genre = :genre) AND " +
            "(:platform IS NULL OR platform = :platform) AND " +
            "(:minPrice IS NULL OR price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR price <= :maxPrice) AND " +
            "is_active = true " +
            "ORDER BY created_at DESC")
    Flux<Game> findByMultipleCriteria(String genre, String platform, Double minPrice, Double maxPrice);

    // Soft delete - marcar como inactivo en lugar de eliminar
    @Query("UPDATE games SET is_active = false, updated_at = CURRENT_TIMESTAMP WHERE id = :id")
    Mono<Integer> softDeleteById(Long id);

    // Contar juegos por género
    @Query("SELECT COUNT(*) FROM games WHERE genre = :genre AND is_active = true")
    Mono<Long> countByGenre(String genre);
}