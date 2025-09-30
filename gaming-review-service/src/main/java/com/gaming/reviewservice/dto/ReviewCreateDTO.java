package com.gaming.reviewservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

public class ReviewCreateDTO {

    @NotNull(message = "El ID del juego es obligatorio")
    private Long gameId;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long userId;

    @NotNull(message = "La calificación es obligatoria")
    @Min(value = 1, message = "La calificación mínima es 1")
    @Max(value = 5, message = "La calificación máxima es 5")
    private Integer rating;

    @Size(max = 2000, message = "El comentario no puede exceder 2000 caracteres")
    private String comment;

    private Boolean isVerifiedPurchase = false;

    // Constructor vacío
    public ReviewCreateDTO() {}

    // Constructor básico
    public ReviewCreateDTO(Long gameId, Long userId, Integer rating, String comment) {
        this.gameId = gameId;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
        this.isVerifiedPurchase = false;
    }

    // Constructor completo
    public ReviewCreateDTO(Long gameId, Long userId, Integer rating, String comment, Boolean isVerifiedPurchase) {
        this.gameId = gameId;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
        this.isVerifiedPurchase = isVerifiedPurchase != null ? isVerifiedPurchase : false;
    }

    // Getters y Setters
    public Long getGameId() { return gameId; }
    public void setGameId(Long gameId) { this.gameId = gameId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Boolean getIsVerifiedPurchase() { return isVerifiedPurchase; }
    public void setIsVerifiedPurchase(Boolean isVerifiedPurchase) { this.isVerifiedPurchase = isVerifiedPurchase; }
}