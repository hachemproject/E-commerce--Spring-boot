package com.beststore.beststore.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.sql.Date;

public class ProductDto {

    @NotEmpty(message = "Le nom du produit est requis")
    private String name;

    @NotEmpty(message = "La marque est requise")
    private String brand;

    @NotEmpty(message = "La catégorie est requise")
    private String category;

    @NotNull(message = "Le prix est requis")
    @Positive(message = "Le prix doit être un nombre positif")
    private Double price;
    
    @NotEmpty(message = "La description est requise")
    private String description;

    @NotEmpty(message = "La createAt est requise")
    private Date createdAt;
    
    private String imageFileName;

    // Getters et Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }
}
