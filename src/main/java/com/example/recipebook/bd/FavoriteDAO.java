package com.example.recipebook.bd;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FavoriteDAO {

    public void addFavorite(int userId, int recipeId) throws SQLException {
        String sql = "INSERT INTO Favorites (user_id, recipe_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, recipeId);
            stmt.executeUpdate();
        }
    }

    public void removeFavorite(int userId, int recipeId) throws SQLException {
        String sql = "DELETE FROM Favorites WHERE user_id = ? AND recipe_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, recipeId);
            stmt.executeUpdate();
        }
    }

    public List<String> getFavoritesByUser(int userId) throws SQLException {
        List<String> favorites = new ArrayList<>();
        String sql = """
                SELECT r.recipe_name
                FROM Recipes r
                JOIN Favorites f ON r.recipe_id = f.recipe_id
                WHERE f.user_id = ?
                """;
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                favorites.add(rs.getString("recipe_name"));
            }
        }
        return favorites;
    }
}
