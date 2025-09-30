package com.gaming.userservice.service.impl;

import com.gaming.userservice.model.User;
import com.gaming.userservice.repository.UserRepository;
import com.gaming.userservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<User> createUser(User user) {
        return userRepository.existsByEmailOrUsername(user.getEmail(), user.getUsername())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new IllegalArgumentException("Ya existe un usuario con el email o nombre de usuario proporcionado"));
                    }

                    // Configurar valores por defecto
                    user.setCreatedAt(LocalDateTime.now());
                    user.setUpdatedAt(LocalDateTime.now());
                    user.setIsActive(true);
                    user.setEmailVerified(false);
                    user.setRole(user.getRole() != null ? user.getRole() : "USER");

                    return userRepository.save(user)
                            .doOnSuccess(savedUser -> logger.info("Usuario creado exitosamente: {}", savedUser.getUsername()))
                            .doOnError(error -> logger.error("Error al crear usuario: {}", error.getMessage()));
                });
    }

    @Override
    public Mono<User> getUserById(Long id) {
        return userRepository.findById(id)
                .filter(User::getIsActive)
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado con ID: " + id)))
                .doOnSuccess(user -> logger.debug("Usuario encontrado: {}", user.getUsername()))
                .doOnError(error -> logger.error("Error al buscar usuario por ID {}: {}", id, error.getMessage()));
    }

    @Override
    public Flux<User> getAllUsers() {
        return userRepository.findByIsActiveTrue()
                .doOnComplete(() -> logger.debug("Se obtuvieron todos los usuarios activos"))
                .doOnError(error -> logger.error("Error al obtener todos los usuarios: {}", error.getMessage()));
    }

    @Override
    public Mono<User> updateUser(Long id, User userUpdate) {
        return userRepository.findById(id)
                .filter(User::getIsActive)
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado con ID: " + id)))
                .flatMap(existingUser -> {
                    // Verificar si el nuevo email/username ya existen (excluyendo el usuario actual)
                    boolean emailChanged = !existingUser.getEmail().equals(userUpdate.getEmail());
                    boolean usernameChanged = !existingUser.getUsername().equals(userUpdate.getUsername());

                    if (emailChanged || usernameChanged) {
                        return userRepository.existsByEmailOrUsername(userUpdate.getEmail(), userUpdate.getUsername())
                                .flatMap(exists -> {
                                    if (exists) {
                                        return Mono.error(new IllegalArgumentException("Ya existe otro usuario con el email o nombre de usuario proporcionado"));
                                    }
                                    return updateUserFields(existingUser, userUpdate);
                                });
                    } else {
                        return updateUserFields(existingUser, userUpdate);
                    }
                })
                .doOnSuccess(updatedUser -> logger.info("Usuario actualizado exitosamente: {}", updatedUser.getUsername()))
                .doOnError(error -> logger.error("Error al actualizar usuario con ID {}: {}", id, error.getMessage()));
    }

    private Mono<User> updateUserFields(User existingUser, User userUpdate) {
        existingUser.setUsername(userUpdate.getUsername());
        existingUser.setEmail(userUpdate.getEmail());
        existingUser.setFirstName(userUpdate.getFirstName());
        existingUser.setLastName(userUpdate.getLastName());
        existingUser.setPhone(userUpdate.getPhone());
        existingUser.setAddress(userUpdate.getAddress());
        existingUser.setDateOfBirth(userUpdate.getDateOfBirth());
        existingUser.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(existingUser);
    }

    @Override
    public Mono<Void> deleteUser(Long id) {
        return userRepository.findById(id)
                .filter(User::getIsActive)
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado con ID: " + id)))
                .flatMap(user -> userRepository.softDeleteById(id))
                .then()
                .doOnSuccess(unused -> logger.info("Usuario eliminado (soft delete) con ID: {}", id))
                .doOnError(error -> logger.error("Error al eliminar usuario con ID {}: {}", id, error.getMessage()));
    }

    @Override
    public Mono<User> authenticateUser(String identifier, String password) {
        return userRepository.findByEmailOrUsername(identifier)
                .filter(user -> user.getPassword().equals(password)) // En producción usar hash
                .switchIfEmpty(Mono.error(new RuntimeException("Credenciales inválidas")))
                .flatMap(user -> updateLastLogin(user.getId()).thenReturn(user))
                .doOnSuccess(user -> logger.info("Usuario autenticado exitosamente: {}", user.getUsername()))
                .doOnError(error -> logger.error("Error en autenticación para identificador {}: {}", identifier, error.getMessage()));
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return userRepository.findByEmailAndIsActiveTrue(email)
                .doOnError(error -> logger.error("Error al buscar usuario por email '{}': {}", email, error.getMessage()));
    }

    @Override
    public Mono<User> findByUsername(String username) {
        return userRepository.findByUsernameAndIsActiveTrue(username)
                .doOnError(error -> logger.error("Error al buscar usuario por username '{}': {}", username, error.getMessage()));
    }

    @Override
    public Mono<User> findByEmailOrUsername(String identifier) {
        return userRepository.findByEmailOrUsername(identifier)
                .doOnError(error -> logger.error("Error al buscar usuario por identificador '{}': {}", identifier, error.getMessage()));
    }

    @Override
    public Mono<User> updatePassword(Long userId, String currentPassword, String newPassword) {
        return userRepository.findById(userId)
                .filter(User::getIsActive)
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado")))
                .filter(user -> user.getPassword().equals(currentPassword)) // En producción usar hash
                .switchIfEmpty(Mono.error(new RuntimeException("Contraseña actual incorrecta")))
                .flatMap(user -> userRepository.updatePassword(userId, newPassword).thenReturn(user))
                .doOnSuccess(user -> logger.info("Contraseña actualizada para usuario: {}", user.getUsername()))
                .doOnError(error -> logger.error("Error al actualizar contraseña para usuario ID {}: {}", userId, error.getMessage()));
    }

    @Override
    public Mono<User> verifyEmail(Long userId) {
        return userRepository.findById(userId)
                .filter(User::getIsActive)
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado")))
                .flatMap(user -> userRepository.verifyEmail(userId).thenReturn(user))
                .doOnSuccess(user -> logger.info("Email verificado para usuario: {}", user.getUsername()))
                .doOnError(error -> logger.error("Error al verificar email para usuario ID {}: {}", userId, error.getMessage()));
    }

    @Override
    public Mono<User> updateLastLogin(Long userId) {
        return userRepository.findById(userId)
                .filter(User::getIsActive)
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado")))
                .flatMap(user -> userRepository.updateLastLogin(userId).thenReturn(user))
                .doOnError(error -> logger.error("Error al actualizar último login para usuario ID {}: {}", userId, error.getMessage()));
    }

    @Override
    public Flux<User> getActiveUsers() {
        return userRepository.findByIsActiveTrue()
                .doOnError(error -> logger.error("Error al obtener usuarios activos: {}", error.getMessage()));
    }

    @Override
    public Flux<User> getUsersByRole(String role) {
        return userRepository.findByRoleAndIsActiveTrue(role)
                .doOnError(error -> logger.error("Error al obtener usuarios por rol '{}': {}", role, error.getMessage()));
    }

    @Override
    public Flux<User> searchUsersByName(String name) {
        return userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name)
                .doOnError(error -> logger.error("Error al buscar usuarios por nombre '{}': {}", name, error.getMessage()));
    }

    @Override
    public Flux<User> getVerifiedUsers() {
        return userRepository.findByEmailVerifiedTrueAndIsActiveTrue()
                .doOnError(error -> logger.error("Error al obtener usuarios verificados: {}", error.getMessage()));
    }

    @Override
    public Flux<User> getUnverifiedUsers() {
        return userRepository.findByEmailVerifiedFalseAndIsActiveTrue()
                .doOnError(error -> logger.error("Error al obtener usuarios no verificados: {}", error.getMessage()));
    }

    @Override
    public Flux<User> getRecentUsers(int limit) {
        return userRepository.findRecentUsers(limit)
                .doOnError(error -> logger.error("Error al obtener usuarios recientes: {}", error.getMessage()));
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return userRepository.existsByEmailAndIsActiveTrue(email)
                .doOnError(error -> logger.error("Error al verificar existencia del email '{}': {}", email, error.getMessage()));
    }

    @Override
    public Mono<Boolean> existsByUsername(String username) {
        return userRepository.existsByUsernameAndIsActiveTrue(username)
                .doOnError(error -> logger.error("Error al verificar existencia del username '{}': {}", username, error.getMessage()));
    }

    @Override
    public Mono<Boolean> existsByEmailOrUsername(String email, String username) {
        return userRepository.existsByEmailOrUsername(email, username)
                .doOnError(error -> logger.error("Error al verificar existencia del email '{}' o username '{}': {}", email, username, error.getMessage()));
    }

    @Override
    public Mono<Long> countUsersByRole(String role) {
        return userRepository.countByRole(role)
                .doOnError(error -> logger.error("Error al contar usuarios por rol '{}': {}", role, error.getMessage()));
    }

    @Override
    public Mono<Long> countUsersRegisteredToday() {
        return userRepository.countUsersRegisteredToday()
                .doOnError(error -> logger.error("Error al contar usuarios registrados hoy: {}", error.getMessage()));
    }
}