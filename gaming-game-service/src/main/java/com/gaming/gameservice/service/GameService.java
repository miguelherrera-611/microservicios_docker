package com.gaming.gameservice.service;

import com.gaming.gameservice.model.Game;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GameService {

    // Operaciones CRUD básicas
    Mono<Game> createGame(Game game);
    Mono<Game> getGameById(Long id);
    Flux<Game> getAllGames();
    Mono<Game> updateGame(Long id, Game game);
    Mono<Void> deleteGame(Long id);

    // Búsquedas específicas
    Flux<Game> getActiveGames();
    Flux<Game> searchGamesByTitle(String title);
    Flux<Game> getGamesByGenre(String genre);
    Flux<Game> getGamesByPlatform(String platform);
    Flux<Game> getGamesByDeveloper(String developer);
    Flux<Game> getGamesByPriceRange(Double minPrice, Double maxPrice);
    Flux<Game> getGamesByReleaseYear(Integer releaseYear);

    // Operaciones avanzadas
    Flux<Game> getGamesWithStock();
    Flux<Game> getMostPopularGames(int limit);
    Flux<Game> searchGamesByMultipleCriteria(String genre, String platform, Double minPrice, Double maxPrice);

    // Validaciones
    Mono<Boolean> existsByTitle(String title);

    // Estadísticas
    Mono<Long> countGamesByGenre(String genre);
}