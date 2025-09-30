package com.gaming.userservice.service;

import com.gaming.userservice.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

    // Operaciones CRUD básicas
    Mono<User> createUser(User user);
    Mono<User> getUserById(Long id);
    Flux<User> getAllUsers();
    Mono<User> updateUser(Long id, User user);
    Mono<Void> deleteUser(Long id);

    // Autenticación y autorización
    Mono<User> authenticateUser(String identifier, String password);
    Mono<User> findByEmail(String email);
    Mono<User> findByUsername(String username);
    Mono<User> findByEmailOrUsername(String identifier);

    // Gestión de cuenta
    Mono<User> updatePassword(Long userId, String currentPassword, String newPassword);
    Mono<User> verifyEmail(Long userId);
    Mono<User> updateLastLogin(Long userId);

    // Búsquedas específicas
    Flux<User> getActiveUsers();
    Flux<User> getUsersByRole(String role);
    Flux<User> searchUsersByName(String name);
    Flux<User> getVerifiedUsers();
    Flux<User> getUnverifiedUsers();
    Flux<User> getRecentUsers(int limit);

    // Validaciones
    Mono<Boolean> existsByEmail(String email);
    Mono<Boolean> existsByUsername(String username);
    Mono<Boolean> existsByEmailOrUsername(String email, String username);

    // Estadísticas
    Mono<Long> countUsersByRole(String role);
    Mono<Long> countUsersRegisteredToday();
}