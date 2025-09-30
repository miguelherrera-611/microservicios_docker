package com.gaming.gameservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class GameResponseDTO {

    private Long id;
    private String title;
    private String description;
    private String genre;
    private String platform;
    private BigDecimal price;
    private Integer stock;
    private String imageUrl;
    private String developer;
    private Integer releaseYear;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor vacío
    public GameResponseDTO() {}

    // Constructor completo
    public GameResponseDTO(Long id, String title, String description, String genre, String platform,
                           BigDecimal price, Integer stock, String imageUrl, String developer,
                           Integer releaseYear, Boolean isActive, LocalDateTime createdAt,
                           LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.platform = platform;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.developer = developer;
        this.releaseYear = releaseYear;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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
}