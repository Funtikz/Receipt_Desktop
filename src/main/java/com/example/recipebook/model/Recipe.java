package com.example.recipebook.model;

public class Recipe {
    private int id;
    private String name;
    private int categoryId;
    private byte[] image;
    private String ingredients;
    private String instructions;

    public Recipe(int id, String name, int categoryId, byte[] image, String ingredients, String instructions) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.image = image;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getCategoryId() { return categoryId; }
    public byte[] getImage() { return image; }
    public String getIngredients() { return ingredients; }
    public String getInstructions() { return instructions; }

    public Recipe(int id, String name, int categoryId, byte[] image) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.image = image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}