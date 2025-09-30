package com.gaming.gameservice.controller;

import com.gaming.gameservice.model.Game;
import com.gaming.gameservice.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/games")
@CrossOrigin(origins = "*")
public class GameController {

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    // GET /api/games - Obtener todos los juegos
    @GetMapping
    public Flux<Game> getAllGames() {
        logger.info("Solicitud para obtener todos los juegos");
        return gameService.getAllGames();
    }

    // GET /api/games/{id} - Obtener juego por ID
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Game>> getGameById(@PathVariable Long id) {
        logger.info("Solicitud para obtener juego con ID: {}", id);
        return gameService.getGameById(id)
                .map(game -> ResponseEntity.ok(game))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    // POST /api/games - Crear nuevo juego
    @PostMapping
    public Mono<ResponseEntity<Game>> createGame(@Valid @RequestBody Game game) {
        logger.info("Solicitud para crear nuevo juego: {}", game.getTitle());
        return gameService.createGame(game)
                .map(createdGame -> ResponseEntity.status(HttpStatus.CREATED).body(createdGame))
                .onErrorReturn(ResponseEntity.badRequest().build());
    }

    // PUT /api/games/{id} - Actualizar juego
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Game>> updateGame(@PathVariable Long id, @Valid @RequestBody Game game) {
        logger.info("Solicitud para actualizar juego con ID: {}", id);
        return gameService.updateGame(id, game)
                .map(updatedGame -> ResponseEntity.ok(updatedGame))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    // DELETE /api/games/{id} - Eliminar juego (soft delete)
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteGame(@PathVariable Long id) {
        logger.info("Solicitud para eliminar juego con ID: {}", id);
        return gameService.deleteGame(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    // GET /api/games/search?title=xxx - Buscar por título
    @GetMapping("/search")
    public Flux<Game> searchGamesByTitle(@RequestParam String title) {
        logger.info("Búsqueda de juegos por título: {}", title);
        return gameService.searchGamesByTitle(title);
    }

    // GET /api/games/genre/{genre} - Buscar por género
    @GetMapping("/genre/{genre}")
    public Flux<Game> getGamesByGenre(@PathVariable String genre) {
        logger.info("Búsqueda de juegos por género: {}", genre);
        return gameService.getGamesByGenre(genre);
    }

    // GET /api/games/platform/{platform} - Buscar por plataforma
    @GetMapping("/platform/{platform}")
    public Flux<Game> getGamesByPlatform(@PathVariable String platform) {
        logger.info("Búsqueda de juegos por plataforma: {}", platform);
        return gameService.getGamesByPlatform(platform);
    }

    // GET /api/games/developer/{developer} - Buscar por desarrollador
    @GetMapping("/developer/{developer}")
    public Flux<Game> getGamesByDeveloper(@PathVariable String developer) {
        logger.info("Búsqueda de juegos por desarrollador: {}", developer);
        return gameService.getGamesByDeveloper(developer);
    }

    // GET /api/games/year/{year} - Buscar por año de lanzamiento
    @GetMapping("/year/{year}")
    public Flux<Game> getGamesByReleaseYear(@PathVariable Integer year) {
        logger.info("Búsqueda de juegos por año: {}", year);
        return gameService.getGamesByReleaseYear(year);
    }

    // GET /api/games/price-range?min=10&max=50 - Buscar por rango de precios
    @GetMapping("/price-range")
    public Flux<Game> getGamesByPriceRange(@RequestParam Double min, @RequestParam Double max) {
        logger.info("Búsqueda de juegos por rango de precios: {} - {}", min, max);
        return gameService.getGamesByPriceRange(min, max);
    }

    // GET /api/games/with-stock - Obtener juegos con stock disponible
    @GetMapping("/with-stock")
    public Flux<Game> getGamesWithStock() {
        logger.info("Búsqueda de juegos con stock disponible");
        return gameService.getGamesWithStock();
    }

    // GET /api/games/popular?limit=10 - Obtener juegos más populares
    @GetMapping("/popular")
    public Flux<Game> getMostPopularGames(@RequestParam(defaultValue = "10") int limit) {
        logger.info("Búsqueda de los {} juegos más populares", limit);
        return gameService.getMostPopularGames(limit);
    }

    // GET /api/games/filter?genre=xxx&platform=xxx&minPrice=10&maxPrice=50 - Búsqueda avanzada
    @GetMapping("/filter")
    public Flux<Game> filterGames(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String platform,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        logger.info("Búsqueda con filtros - Género: {}, Plataforma: {}, Precio: {} - {}",
                genre, platform, minPrice, maxPrice);
        return gameService.searchGamesByMultipleCriteria(genre, platform, minPrice, maxPrice);
    }

    // GET /api/games/exists?title=xxx - Verificar si existe un juego
    @GetMapping("/exists")
    public Mono<ResponseEntity<Boolean>> checkIfGameExists(@RequestParam String title) {
        logger.info("Verificando existencia del juego: {}", title);
        return gameService.existsByTitle(title)
                .map(exists -> ResponseEntity.ok(exists));
    }

    // GET /api/games/count/genre/{genre} - Contar juegos por género
    @GetMapping("/count/genre/{genre}")
    public Mono<ResponseEntity<Long>> countGamesByGenre(@PathVariable String genre) {
        logger.info("Contando juegos del género: {}", genre);
        return gameService.countGamesByGenre(genre)
                .map(count -> ResponseEntity.ok(count));
    }
}