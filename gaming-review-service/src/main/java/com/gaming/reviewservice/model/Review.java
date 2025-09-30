package com.gaming.reviewservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

import java.time.LocalDateTime;

@Table("reviews")
public class Review {

    @Id
    private Long id;

    @NotNull(message = "El ID del juego es obligatorio")
    @Column("game_id")
    private Long gameId;

    @NotNull(message = "El ID del usuario es obligatorio")
    @Column("user_id")
    private Long userId;

    @NotNull(message = "La calificación es obligatoria")
    @Min(value = 1, message = "La calificación mínima es 1")
    @Max(value = 5, message = "La calificación máxima es 5")
    @Column("rating")
    private Integer rating;

    @Size(max = 2000, message = "El comentario no puede exceder 2000 caracteres")
    @Column("comment")
    private String comment;

    @Column("is_verified_purchase")
    private Boolean isVerifiedPurchase = false;

    @Column("is_approved")
    private Boolean isApproved = true;

    @Column("helpful_count")
    private Integer helpfulCount = 0;

    @Column("unhelpful_count")
    private Integer unhelpfulCount = 0;

    @Column("is_active")
    private Boolean isActive = true;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private LocalDateTime updatedAt;

    // Constructor vacío
    public Review() {}

    // Constructor básico
    public Review(Long gameId, Long userId, Integer rating, String comment) {
        this.gameId = gameId;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
        this.isVerifiedPurchase = false;
        this.isApproved = true;
        this.helpfulCount = 0;
        this.unhelpfulCount = 0;
        this.isActive = true;
    }

    // Constructor completo
    public Review(Long gameId, Long userId, Integer rating, String comment,
                  Boolean isVerifiedPurchase, Boolean isApproved) {
        this.gameId = gameId;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
        this.isVerifiedPurchase = isVerifiedPurchase != null ? isVerifiedPurchase : false;
        this.isApproved = isApproved != null ? isApproved : true;
        this.helpfulCount = 0;
        this.unhelpfulCount = 0;
        this.isActive = true;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public Boolean getIsApproved() { return isApproved; }
    public void setIsApproved(Boolean isApproved) { this.isApproved = isApproved; }

    public Integer getHelpfulCount() { return helpfulCount; }
    public void setHelpfulCount(Integer helpfulCount) { this.helpfulCount = helpfulCount; }

    public Integer getUnhelpfulCount() { return unhelpfulCount; }
    public void setUnhelpfulCount(Integer unhelpfulCount) { this.unhelpfulCount = unhelpfulCount; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Métodos de utilidad
    public Integer getTotalVotes() {
        return helpfulCount + unhelpfulCount;
    }

    public Double getHelpfulPercentage() {
        int total = getTotalVotes();
        if (total == 0) return 0.0;
        return (helpfulCount.doubleValue() / total) * 100;
    }

    public boolean isHighRating() {
        return rating >= 4;
    }

    public boolean isLowRating() {
        return rating <= 2;
    }

    public void incrementHelpfulCount() {
        this.helpfulCount = this.helpfulCount + 1;
    }

    public void incrementUnhelpfulCount() {
        this.unhelpfulCount = this.unhelpfulCount + 1;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", gameId=" + gameId +
                ", userId=" + userId +
                ", rating=" + rating +
                ", isVerifiedPurchase=" + isVerifiedPurchase +
                ", isApproved=" + isApproved +
                ", helpfulCount=" + helpfulCount +
                ", unhelpfulCount=" + unhelpfulCount +
                ", isActive=" + isActive +
                '}';
    }
}