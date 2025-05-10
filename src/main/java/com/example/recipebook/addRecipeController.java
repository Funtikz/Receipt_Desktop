package com.example.recipebook;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.example.recipebook.bd.DBConnection;
import com.example.recipebook.bd.RecipeDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class addRecipeController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addNewCategoryBut;

    @FXML
    private Button addPhotoButton;

    @FXML
    private RadioButton breakfast;

    @FXML
    private ToggleGroup category;

    @FXML
    private ImageView deleteCategory;

    @FXML
    private ImageView deleteCategory1;

    @FXML
    private ImageView deleteCategory11;

    @FXML
    private ImageView deleteCategory12;

    @FXML
    private ImageView deleteCategory13;

    @FXML
    private RadioButton dessert;

    @FXML
    private RadioButton dinner;

    @FXML
    private ImageView editCategoryBut;

    @FXML
    private ImageView editCategoryBut1;

    @FXML
    private ImageView editCategoryBut11;

    @FXML
    private ImageView editCategoryBut12;

    @FXML
    private ImageView editCategoryBut13;

    @FXML
    private RadioButton lunch;

    @FXML
    private Button mainMenuButton;

    @FXML
    private AnchorPane recipeFoodButton;

    @FXML
    private TextArea recipeNameTextArea;

    @FXML
    private TextArea ingredientsTextArea;

    @FXML
    private TextArea instructionsTextArea;


    private File selectedImageFile; // Переменная для хранения выбранного файла

    @FXML
    private RadioButton salad;

    @FXML
    private Button saveButton;


    @FXML
    private ImageView recipeImageView;

    @FXML
    void get(MouseEvent event) {

    }

    @FXML
    void initialize() {

    }


    @FXML
    void handleSaveButton(ActionEvent event) throws SQLException {
        // Получаем данные из текстовых полей
        String name = recipeNameTextArea.getText().trim();
        String ingredients = ingredientsTextArea.getText().trim();
        String instructions = instructionsTextArea.getText().trim();

        // Получаем выбранную категорию
        RadioButton selectedCategory = (RadioButton) category.getSelectedToggle();
        String categoryName = selectedCategory != null ? selectedCategory.getText() : null;

        // Получаем изображение
        File imageFile = selectedImageFile;

        // Валидация данных
        if (name.isEmpty() || ingredients.isEmpty() || instructions.isEmpty() || categoryName == null || imageFile == null) {
            // Показываем предупреждающее окно
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Пожалуйста, заполните все поля и добавьте изображение.");
            alert.showAndWait();
            return;
        }

        try {
            // Преобразуем название категории в ID
            int categoryId = getCategoryIdByName(categoryName);

            // Создаем объект DAO и добавляем рецепт
            RecipeDAO recipeDAO = new RecipeDAO();
            recipeDAO.addRecipe(name, ingredients, instructions, categoryId, false, imageFile);

            // Успешное сохранение — показываем сообщение
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Успешно");
            alert.setHeaderText(null);
            alert.setContentText("Рецепт успешно сохранён!");
            alert.showAndWait();

            // (опционально) Очистить форму после сохранения
            recipeNameTextArea.clear();
            ingredientsTextArea.clear();
            instructionsTextArea.clear();
            category.selectToggle(null);
            recipeImageView.setImage(null);
            selectedImageFile = null;

        } catch (SQLException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Не удалось сохранить рецепт");
            alert.setContentText("Пожалуйста, проверьте соединение с базой данных или повторите попытку.");
            alert.showAndWait();
        }
    }


    public static int getCategoryIdByName(String name) throws SQLException {
        String query = "SELECT category_id FROM categories WHERE category_name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("category_id");
            } else {
                throw new SQLException("Категория не найдена: " + name);
            }
        }
    }

    public static String getCategoryNameById(int id) throws SQLException {
        String query = "SELECT category_name FROM categories WHERE category_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("category_name");
            } else {
                throw new SQLException("Категория с ID не найдена: " + id);
            }
        }
    }


    @FXML
    private void onMainMenuClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) mainMenuButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }






    @FXML
    private void handleAddPhotoButton(ActionEvent event) {
        // Создаем FileChooser для выбора изображения
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        // Открываем диалог для выбора файла
        Stage stage = (Stage) recipeImageView.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        // Если файл выбран, загружаем его в ImageView и сохраняем
        if (file != null) {
            selectedImageFile = file; // Сохраняем выбранный файл

            Image image = new Image(file.toURI().toString());
            recipeImageView.setImage(image);
            recipeImageView.setPreserveRatio(false); // Отключаем сохранение пропорций
            recipeImageView.setFitWidth(192.0);       // Устанавливаем точную ширину
            recipeImageView.setFitHeight(126.0);      // Устанавливаем точную высоту
        }
    }


}