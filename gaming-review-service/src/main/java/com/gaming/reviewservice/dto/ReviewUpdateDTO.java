package com.gaming.reviewservice.dto;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

public class ReviewUpdateDTO {

    @Min(value = 1, message = "La calificación mínima es 1")
    @Max(value = 5, message = "La calificación máxima es 5")
    private Integer rating;

    @Size(max = 2000, message = "El comentario no puede exceder 2000 caracteres")
    private String comment;

    // Constructor vacío
    public ReviewUpdateDTO() {}

    // Constructor completo
    public ReviewUpdateDTO(Integer rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }

    // Getters y Setters
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}