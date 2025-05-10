package com.example.recipebook.bd;


import com.example.recipebook.model.Recipe;

import java.io.*;
import java.nio.file.Files;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeDAO {

    public void addRecipe(String name, String ingredients, String instructions, int categoryId, boolean favorite, File imageFile) throws SQLException {
        // Получаем userId из сессии
        int userId = Session.getUserId();
        if (userId == -1) {
            throw new SQLException("Пользователь не авторизован.");
        }

        // Теперь передаем userId в запрос
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "INSERT INTO culinaryguide.recipes (user_id, recipe_name, ingredients, instructions, category_id, image) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, userId);  // Используем userId из сессии
                stmt.setString(2, name);
                stmt.setString(3, ingredients);
                stmt.setString(4, instructions);
                stmt.setInt(5, categoryId);
                stmt.setBytes(6, Files.readAllBytes(imageFile.toPath()));  // Загружаем изображение

                stmt.executeUpdate();

                // Получаем id нового рецепта
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int recipeId = rs.getInt(1);

                    // Если рецепт нужно добавить в избранное
                    if (favorite) {
                        String favoritesSql = "INSERT INTO culinaryguide.favorites (user_id, recipe_id) VALUES (?, ?)";
                        try (PreparedStatement favoriteStmt = connection.prepareStatement(favoritesSql)) {
                            favoriteStmt.setInt(1, userId); // Пользователь, добавляющий в избранное
                            favoriteStmt.setInt(2, recipeId); // Добавленный рецепт
                            favoriteStmt.executeUpdate();
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }




    public List<String> getRecipesByCategory(int categoryId) throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "SELECT recipe_name FROM Recipes WHERE category_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("recipe_name"));
            }
        }
        return list;
    }

    public byte[] getRecipeImage(int recipeId) throws SQLException {
        String sql = "SELECT image FROM Recipes WHERE recipe_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, recipeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBytes("image");
            }
        }
        return null;
    }

    public void deleteRecipe(int recipeId) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            // Сначала удаляем записи из таблицы favorites, где связаны рецепты
            String deleteFromFavorites = "DELETE FROM favorites WHERE recipe_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteFromFavorites)) {
                stmt.setInt(1, recipeId);
                stmt.executeUpdate();
            }

            // Затем удаляем сам рецепт из таблицы recipes
            String deleteFromRecipes = "DELETE FROM recipes WHERE recipe_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteFromRecipes)) {
                stmt.setInt(1, recipeId);
                stmt.executeUpdate();
            }
        }
    }

    public List<Recipe> getAllRecipes() throws SQLException {
        List<Recipe> list = new ArrayList<>();
        String sql = "SELECT recipe_id, recipe_name, category_id, image, ingredients, instructions FROM Recipes";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("Found recipe: " + rs.getString("recipe_name")); // Выводим найденные рецепты
                list.add(new Recipe(
                                rs.getInt("recipe_id"),
                                rs.getString("recipe_name"),
                                rs.getInt("category_id"),
                                rs.getBytes("image"),
                                rs.getString("ingredients"),
                                rs.getString("instructions")
                        )
                );
            }
        }
        return list;
    }

    public void updateRecipe(Recipe recipe) throws SQLException {
        String sql = "UPDATE culinaryguide.recipes SET recipe_name = ?, ingredients = ?, instructions = ?, category_id = ?, image = ? WHERE recipe_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, recipe.getName());
            stmt.setString(2, recipe.getIngredients());
            stmt.setString(3, recipe.getInstructions());
            stmt.setInt(4, recipe.getCategoryId());
            stmt.setBytes(5, recipe.getImage());  // Обновление изображения
            stmt.setInt(6, recipe.getId());

            stmt.executeUpdate();
        }
    }



}
