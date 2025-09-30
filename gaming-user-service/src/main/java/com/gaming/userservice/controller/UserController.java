package com.gaming.userservice.controller;

import com.gaming.userservice.model.User;
import com.gaming.userservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET /api/users - Obtener todos los usuarios
    @GetMapping
    public Flux<User> getAllUsers() {
        logger.info("Solicitud para obtener todos los usuarios");
        return userService.getAllUsers();
    }

    // GET /api/users/{id} - Obtener usuario por ID
    @GetMapping("/{id}")
    public Mono<ResponseEntity<User>> getUserById(@PathVariable Long id) {
        logger.info("Solicitud para obtener usuario con ID: {}", id);
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(user))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    // POST /api/users - Crear nuevo usuario
    @PostMapping
    public Mono<ResponseEntity<User>> createUser(@Valid @RequestBody User user) {
        logger.info("Solicitud para crear nuevo usuario: {}", user.getUsername());
        return userService.createUser(user)
                .map(createdUser -> ResponseEntity.status(HttpStatus.CREATED).body(createdUser))
                .onErrorReturn(ResponseEntity.badRequest().build());
    }

    // PUT /api/users/{id} - Actualizar usuario
    @PutMapping("/{id}")
    public Mono<ResponseEntity<User>> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        logger.info("Solicitud para actualizar usuario con ID: {}", id);
        return userService.updateUser(id, user)
                .map(updatedUser -> ResponseEntity.ok(updatedUser))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    // DELETE /api/users/{id} - Eliminar usuario (soft delete)
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable Long id) {
        logger.info("Solicitud para eliminar usuario con ID: {}", id);
        return userService.deleteUser(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    // POST /api/users/authenticate - Autenticar usuario
    @PostMapping("/authenticate")
    public Mono<ResponseEntity<User>> authenticateUser(@RequestBody AuthRequest authRequest) {
        logger.info("Solicitud de autenticación para: {}", authRequest.getIdentifier());
        return userService.authenticateUser(authRequest.getIdentifier(), authRequest.getPassword())
                .map(user -> ResponseEntity.ok(user))
                .onErrorReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    // GET /api/users/email/{email} - Buscar por email
    @GetMapping("/email/{email}")
    public Mono<ResponseEntity<User>> getUserByEmail(@PathVariable String email) {
        logger.info("Búsqueda de usuario por email: {}", email);
        return userService.findByEmail(email)
                .map(user -> ResponseEntity.ok(user))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    // GET /api/users/username/{username} - Buscar por username
    @GetMapping("/username/{username}")
    public Mono<ResponseEntity<User>> getUserByUsername(@PathVariable String username) {
        logger.info("Búsqueda de usuario por username: {}", username);
        return userService.findByUsername(username)
                .map(user -> ResponseEntity.ok(user))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    // GET /api/users/search?name=xxx - Buscar por nombre
    @GetMapping("/search")
    public Flux<User> searchUsersByName(@RequestParam String name) {
        logger.info("Búsqueda de usuarios por nombre: {}", name);
        return userService.searchUsersByName(name);
    }

    // GET /api/users/role/{role} - Buscar por rol
    @GetMapping("/role/{role}")
    public Flux<User> getUsersByRole(@PathVariable String role) {
        logger.info("Búsqueda de usuarios por rol: {}", role);
        return userService.getUsersByRole(role);
    }

    // GET /api/users/verified - Obtener usuarios verificados
    @GetMapping("/verified")
    public Flux<User> getVerifiedUsers() {
        logger.info("Búsqueda de usuarios verificados");
        return userService.getVerifiedUsers();
    }

    // GET /api/users/unverified - Obtener usuarios no verificados
    @GetMapping("/unverified")
    public Flux<User> getUnverifiedUsers() {
        logger.info("Búsqueda de usuarios no verificados");
        return userService.getUnverifiedUsers();
    }

    // GET /api/users/recent?limit=10 - Obtener usuarios más recientes
    @GetMapping("/recent")
    public Flux<User> getRecentUsers(@RequestParam(defaultValue = "10") int limit) {
        logger.info("Búsqueda de los {} usuarios más recientes", limit);
        return userService.getRecentUsers(limit);
    }

    // POST /api/users/{id}/verify-email - Verificar email de usuario
    @PostMapping("/{id}/verify-email")
    public Mono<ResponseEntity<User>> verifyUserEmail(@PathVariable Long id) {
        logger.info("Solicitud para verificar email del usuario ID: {}", id);
        return userService.verifyEmail(id)
                .map(user -> ResponseEntity.ok(user))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    // PUT /api/users/{id}/password - Cambiar contraseña
    @PutMapping("/{id}/password")
    public Mono<ResponseEntity<User>> updatePassword(@PathVariable Long id, @RequestBody PasswordChangeRequest request) {
        logger.info("Solicitud para cambiar contraseña del usuario ID: {}", id);
        return userService.updatePassword(id, request.getCurrentPassword(), request.getNewPassword())
                .map(user -> ResponseEntity.ok(user))
                .onErrorReturn(ResponseEntity.badRequest().build());
    }

    // GET /api/users/exists/email?email=xxx - Verificar si existe email
    @GetMapping("/exists/email")
    public Mono<ResponseEntity<Boolean>> checkIfEmailExists(@RequestParam String email) {
        logger.info("Verificando existencia del email: {}", email);
        return userService.existsByEmail(email)
                .map(exists -> ResponseEntity.ok(exists));
    }

    // GET /api/users/exists/username?username=xxx - Verificar si existe username
    @GetMapping("/exists/username")
    public Mono<ResponseEntity<Boolean>> checkIfUsernameExists(@RequestParam String username) {
        logger.info("Verificando existencia del username: {}", username);
        return userService.existsByUsername(username)
                .map(exists -> ResponseEntity.ok(exists));
    }

    // GET /api/users/count/role/{role} - Contar usuarios por rol
    @GetMapping("/count/role/{role}")
    public Mono<ResponseEntity<Long>> countUsersByRole(@PathVariable String role) {
        logger.info("Contando usuarios del rol: {}", role);
        return userService.countUsersByRole(role)
                .map(count -> ResponseEntity.ok(count));
    }

    // GET /api/users/count/today - Contar usuarios registrados hoy
    @GetMapping("/count/today")
    public Mono<ResponseEntity<Long>> countUsersRegisteredToday() {
        logger.info("Contando usuarios registrados hoy");
        return userService.countUsersRegisteredToday()
                .map(count -> ResponseEntity.ok(count));
    }

    // DTOs para requests específicos
    public static class AuthRequest {
        private String identifier; // email o username
        private String password;

        public String getIdentifier() { return identifier; }
        public void setIdentifier(String identifier) { this.identifier = identifier; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class PasswordChangeRequest {
        private String currentPassword;
        private String newPassword;

        public String getCurrentPassword() { return currentPassword; }
        public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }

        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }
}