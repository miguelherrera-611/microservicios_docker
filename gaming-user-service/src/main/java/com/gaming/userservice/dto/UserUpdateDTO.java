package com.gaming.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

public class UserUpdateDTO {

    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "El nombre de usuario solo puede contener letras, números y guiones bajos")
    private String username;

    @Email(message = "El email debe tener un formato válido")
    @Size(max = 255, message = "El email no puede exceder 255 caracteres")
    private String email;

    @Size(min = 1, max = 100, message = "El nombre debe tener entre 1 y 100 caracteres")
    private String firstName;

    @Size(min = 1, max = 100, message = "El apellido debe tener entre 1 y 100 caracteres")
    private String lastName;

    @Size(max = 15, message = "El teléfono no puede exceder 15 caracteres")
    @Pattern(regexp = "^[+]?[0-9\\s-()]+$", message = "El teléfono debe tener un formato válido")
    private String phone;

    @Size(max = 200, message = "La dirección no puede exceder 200 caracteres")
    private String address;

    private LocalDateTime dateOfBirth;

    // Constructor vacío
    public UserUpdateDTO() {}

    // Constructor completo
    public UserUpdateDTO(String username, String email, String firstName, String lastName,
                         String phone, String address, LocalDateTime dateOfBirth) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }

    // Getters y Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public LocalDateTime getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDateTime dateOfBirth) { this.dateOfBirth = dateOfBirth; }
}