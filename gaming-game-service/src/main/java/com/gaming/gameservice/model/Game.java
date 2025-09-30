package com.gaming.gameservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("games")
public class Game {

    @Id
    private Long id;

    @NotBlank(message = "El título es obligatorio")
    @Size(min = 1, max = 255, message = "El título debe tener entre 1 y 255 caracteres")
    @Column("title")
    private String title;

    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    @Column("description")
    private String description;

    @NotBlank(message = "El género es obligatorio")
    @Size(min = 1, max = 100, message = "El género debe tener entre 1 y 100 caracteres")
    @Column("genre")
    private String genre;

    @NotBlank(message = "La plataforma es obligatoria")
    @Size(min = 1, max = 100, message = "La plataforma debe tener entre 1 y 100 caracteres")
    @Column("platform")
    private String platform;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El precio debe ser mayor o igual a 0")
    @Column("price")
    private BigDecimal price;

    @Positive(message = "El stock debe ser un número positivo")
    @Column("stock")
    private Integer stock;

    @Size(max = 500, message = "La URL de la imagen no puede exceder 500 caracteres")
    @Column("image_url")
    private String imageUrl;

    @NotBlank(message = "El desarrollador es obligatorio")
    @Size(min = 1, max = 150, message = "El desarrollador debe tener entre 1 y 150 caracteres")
    @Column("developer")
    private String developer;

    @Column("release_year")
    private Integer releaseYear;

    @Column("is_active")
    private Boolean isActive = true;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private LocalDateTime updatedAt;

    // Constructor vacío
    public Game() {}

    // Constructor completo
    public Game(String title, String description, String genre, String platform,
                BigDecimal price, Integer stock, String imageUrl, String developer,
                Integer releaseYear) {
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.platform = platform;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.developer = developer;
        this.releaseYear = releaseYear;
        this.isActive = true;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getDeveloper() { return developer; }
    public void setDeveloper(String developer) { this.developer = developer; }

    public Integer getReleaseYear() { return releaseYear; }
    public void setReleaseYear(Integer releaseYear) { this.releaseYear = releaseYear; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", platform='" + platform + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", developer='" + developer + '\'' +
                ", releaseYear=" + releaseYear +
                ", isActive=" + isActive +
                '}';
    }
}