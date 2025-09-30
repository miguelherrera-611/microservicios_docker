package com.gaming.gameservice.service.impl;

import com.gaming.gameservice.model.Game;
import com.gaming.gameservice.repository.GameRepository;
import com.gaming.gameservice.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class GameServiceImpl implements GameService {

    private static final Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);
    private final GameRepository gameRepository;

    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public Mono<Game> createGame(Game game) {
        return gameRepository.existsByTitleIgnoreCaseAndIsActiveTrue(game.getTitle())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new IllegalArgumentException("Ya existe un juego con el título: " + game.getTitle()));
                    }
                    game.setCreatedAt(LocalDateTime.now());
                    game.setUpdatedAt(LocalDateTime.now());
                    game.setIsActive(true);

                    return gameRepository.save(game)
                            .doOnSuccess(savedGame -> logger.info("Juego creado exitosamente: {}", savedGame.getTitle()))
                            .doOnError(error -> logger.error("Error al crear juego: {}", error.getMessage()));
                });
    }

    @Override
    public Mono<Game> getGameById(Long id) {
        return gameRepository.findById(id)
                .filter(Game::getIsActive)
                .switchIfEmpty(Mono.error(new RuntimeException("Juego no encontrado con ID: " + id)))
                .doOnSuccess(game -> logger.debug("Juego encontrado: {}", game.getTitle()))
                .doOnError(error -> logger.error("Error al buscar juego por ID {}: {}", id, error.getMessage()));
    }

    @Override
    public Flux<Game> getAllGames() {
        return gameRepository.findByIsActiveTrue()
                .doOnComplete(() -> logger.debug("Se obtuvieron todos los juegos activos"))
                .doOnError(error -> logger.error("Error al obtener todos los juegos: {}", error.getMessage()));
    }

    @Override
    public Mono<Game> updateGame(Long id, Game gameUpdate) {
        return gameRepository.findById(id)
                .filter(Game::getIsActive)
                .switchIfEmpty(Mono.error(new RuntimeException("Juego no encontrado con ID: " + id)))
                .flatMap(existingGame -> {
                    // Verificar si el nuevo título ya existe (excluyendo el juego actual)
                    if (!existingGame.getTitle().equalsIgnoreCase(gameUpdate.getTitle())) {
                        return gameRepository.existsByTitleIgnoreCaseAndIsActiveTrue(gameUpdate.getTitle())
                                .flatMap(titleExists -> {
                                    if (titleExists) {
                                        return Mono.error(new IllegalArgumentException("Ya existe otro juego con el título: " + gameUpdate.getTitle()));
                                    }
                                    return updateGameFields(existingGame, gameUpdate);
                                });
                    } else {
                        return updateGameFields(existingGame, gameUpdate);
                    }
                })
                .doOnSuccess(updatedGame -> logger.info("Juego actualizado exitosamente: {}", updatedGame.getTitle()))
                .doOnError(error -> logger.error("Error al actualizar juego con ID {}: {}", id, error.getMessage()));
    }

    private Mono<Game> updateGameFields(Game existingGame, Game gameUpdate) {
        existingGame.setTitle(gameUpdate.getTitle());
        existingGame.setDescription(gameUpdate.getDescription());
        existingGame.setGenre(gameUpdate.getGenre());
        existingGame.setPlatform(gameUpdate.getPlatform());
        existingGame.setPrice(gameUpdate.getPrice());
        existingGame.setStock(gameUpdate.getStock());
        existingGame.setImageUrl(gameUpdate.getImageUrl());
        existingGame.setDeveloper(gameUpdate.getDeveloper());
        existingGame.setReleaseYear(gameUpdate.getReleaseYear());
        existingGame.setUpdatedAt(LocalDateTime.now());

        return gameRepository.save(existingGame);
    }

    @Override
    public Mono<Void> deleteGame(Long id) {
        return gameRepository.findById(id)
                .filter(Game::getIsActive)
                .switchIfEmpty(Mono.error(new RuntimeException("Juego no encontrado con ID: " + id)))
                .flatMap(game -> gameRepository.softDeleteById(id))
                .then()
                .doOnSuccess(unused -> logger.info("Juego eliminado (soft delete) con ID: {}", id))
                .doOnError(error -> logger.error("Error al eliminar juego con ID {}: {}", id, error.getMessage()));
    }

    @Override
    public Flux<Game> getActiveGames() {
        return gameRepository.findByIsActiveTrue()
                .doOnError(error -> logger.error("Error al obtener juegos activos: {}", error.getMessage()));
    }

    @Override
    public Flux<Game> searchGamesByTitle(String title) {
        return gameRepository.findByTitleContainingIgnoreCase(title)
                .doOnError(error -> logger.error("Error al buscar juegos por título '{}': {}", title, error.getMessage()));
    }

    @Override
    public Flux<Game> getGamesByGenre(String genre) {
        return gameRepository.findByGenreAndIsActiveTrue(genre)
                .doOnError(error -> logger.error("Error al obtener juegos por género '{}': {}", genre, error.getMessage()));
    }

    @Override
    public Flux<Game> getGamesByPlatform(String platform) {
        return gameRepository.findByPlatformAndIsActiveTrue(platform)
                .doOnError(error -> logger.error("Error al obtener juegos por plataforma '{}': {}", platform, error.getMessage()));
    }

    @Override
    public Flux<Game> getGamesByDeveloper(String developer) {
        return gameRepository.findByDeveloperContainingIgnoreCaseAndIsActiveTrue(developer)
                .doOnError(error -> logger.error("Error al obtener juegos por desarrollador '{}': {}", developer, error.getMessage()));
    }

    @Override
    public Flux<Game> getGamesByPriceRange(Double minPrice, Double maxPrice) {
        return gameRepository.findByPriceRange(minPrice, maxPrice)
                .doOnError(error -> logger.error("Error al obtener juegos por rango de precios {}-{}: {}", minPrice, maxPrice, error.getMessage()));
    }

    @Override
    public Flux<Game> getGamesByReleaseYear(Integer releaseYear) {
        return gameRepository.findByReleaseYearAndIsActiveTrue(releaseYear)
                .doOnError(error -> logger.error("Error al obtener juegos por año {}: {}", releaseYear, error.getMessage()));
    }

    @Override
    public Flux<Game> getGamesWithStock() {
        return gameRepository.findGamesWithStock()
                .doOnError(error -> logger.error("Error al obtener juegos con stock: {}", error.getMessage()));
    }

    @Override
    public Flux<Game> getMostPopularGames(int limit) {
        return gameRepository.findMostPopularGames(limit)
                .doOnError(error -> logger.error("Error al obtener juegos más populares: {}", error.getMessage()));
    }

    @Override
    public Flux<Game> searchGamesByMultipleCriteria(String genre, String platform, Double minPrice, Double maxPrice) {
        return gameRepository.findByMultipleCriteria(genre, platform, minPrice, maxPrice)
                .doOnError(error -> logger.error("Error en búsqueda con múltiples criterios: {}", error.getMessage()));
    }

    @Override
    public Mono<Boolean> existsByTitle(String title) {
        return gameRepository.existsByTitleIgnoreCaseAndIsActiveTrue(title)
                .doOnError(error -> logger.error("Error al verificar existencia del título '{}': {}", title, error.getMessage()));
    }

    @Override
    public Mono<Long> countGamesByGenre(String genre) {
        return gameRepository.countByGenre(genre)
                .doOnError(error -> logger.error("Error al contar juegos por género '{}': {}", genre, error.getMessage()));
    }
}