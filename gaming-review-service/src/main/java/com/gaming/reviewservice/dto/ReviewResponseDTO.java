package com.gaming.reviewservice.dto;

import java.time.LocalDateTime;

public class ReviewResponseDTO {

    private Long id;
    private Long gameId;
    private Long userId;
    private Integer rating;
    private String comment;
    private Boolean isVerifiedPurchase;
    private Boolean isApproved;
    private Integer helpfulCount;
    private Integer unhelpfulCount;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor vacío
    public ReviewResponseDTO() {}

    // Constructor completo
    public ReviewResponseDTO(Long id, Long gameId, Long userId, Integer rating, String comment,
                             Boolean isVerifiedPurchase, Boolean isApproved, Integer helpfulCount,
                             Integer unhelpfulCount, Boolean isActive, LocalDateTime createdAt,
                             LocalDateTime updatedAt) {
        this.id = id;
        this.gameId = gameId;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
        this.isVerifiedPurchase = isVerifiedPurchase;
        this.isApproved = isApproved;
        this.helpfulCount = helpfulCount;
        this.unhelpfulCount = unhelpfulCount;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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
}