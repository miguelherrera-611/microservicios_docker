package com.gaming.userservice.repository;

import com.gaming.userservice.model.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<User, Long> {

    // Buscar usuarios activos
    Flux<User> findByIsActiveTrue();

    // Buscar por email
    Mono<User> findByEmailAndIsActiveTrue(String email);

    // Buscar por username
    Mono<User> findByUsernameAndIsActiveTrue(String username);

    // Buscar por email o username para login
    @Query("SELECT * FROM users WHERE (email = :identifier OR username = :identifier) AND is_active = true")
    Mono<User> findByEmailOrUsername(String identifier);

    // Verificar si existe un email
    Mono<Boolean> existsByEmailAndIsActiveTrue(String email);

    // Verificar si existe un username
    Mono<Boolean> existsByUsernameAndIsActiveTrue(String username);

    // Verificar si existe email o username (para validar duplicados)
    @Query("SELECT COUNT(*) > 0 FROM users WHERE (email = :email OR username = :username) AND is_active = true")
    Mono<Boolean> existsByEmailOrUsername(String email, String username);

    // Buscar por rol
    Flux<User> findByRoleAndIsActiveTrue(String role);

    // Buscar usuarios por nombre o apellido
    @Query("SELECT * FROM users WHERE (LOWER(first_name) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(last_name) LIKE LOWER(CONCAT('%', :name, '%'))) AND is_active = true")
    Flux<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String name);

    // Buscar usuarios verificados
    Flux<User> findByEmailVerifiedTrueAndIsActiveTrue();

    // Buscar usuarios no verificados
    Flux<User> findByEmailVerifiedFalseAndIsActiveTrue();

    // Buscar usuarios registrados en un rango de fechas
    @Query("SELECT * FROM users WHERE created_at BETWEEN :startDate AND :endDate AND is_active = true ORDER BY created_at DESC")
    Flux<User> findByCreatedAtBetween(String startDate, String endDate);

    // Actualizar último login
    @Query("UPDATE users SET last_login = CURRENT_TIMESTAMP, updated_at = CURRENT_TIMESTAMP WHERE id = :userId")
    Mono<Integer> updateLastLogin(Long userId);

    // Verificar email
    @Query("UPDATE users SET email_verified = true, updated_at = CURRENT_TIMESTAMP WHERE id = :userId")
    Mono<Integer> verifyEmail(Long userId);

    // Cambiar contraseña
    @Query("UPDATE users SET password = :newPassword, updated_at = CURRENT_TIMESTAMP WHERE id = :userId")
    Mono<Integer> updatePassword(Long userId, String newPassword);

    // Soft delete - marcar como inactivo
    @Query("UPDATE users SET is_active = false, updated_at = CURRENT_TIMESTAMP WHERE id = :id")
    Mono<Integer> softDeleteById(Long id);

    // Contar usuarios por rol
    @Query("SELECT COUNT(*) FROM users WHERE role = :role AND is_active = true")
    Mono<Long> countByRole(String role);

    // Contar usuarios registrados hoy
    @Query("SELECT COUNT(*) FROM users WHERE DATE(created_at) = CURRENT_DATE AND is_active = true")
    Mono<Long> countUsersRegisteredToday();

    // Obtener usuarios más recientes
    @Query("SELECT * FROM users WHERE is_active = true ORDER BY created_at DESC LIMIT :limit")
    Flux<User> findRecentUsers(int limit);
}