package com.example.recipebook;

import com.example.recipebook.bd.RecipeDAO;
import com.example.recipebook.model.Recipe;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;

import static com.example.recipebook.mainMenuController.applyFontToAllNodes;

public class editRecipeController {

    @FXML
    private RadioButton breakfast;

    @FXML
    private ToggleGroup category;

    @FXML
    private RadioButton dessert;

    @FXML
    private RadioButton dinner;

    @FXML
    private RadioButton lunch;

    @FXML
    private RadioButton salad;

    @FXML
    private TextArea recipeNameField;

    @FXML
    private TextArea ingredientsField;

    @FXML
    private TextArea instructionsField;

    @FXML
    private Button mainMenuButton;

    @FXML
    private Button saveButton;

    @FXML
    private AnchorPane recipeFoodButton;

    private Recipe currentRecipe;

    @FXML
    private ImageView recipeImageView; // Поле для отображения изображения

    public void setRecipe(Recipe recipe) throws SQLException {
        this.currentRecipe = recipe;
        recipeNameField.setText(recipe.getName());
        ingredientsField.setText(recipe.getIngredients());
        instructionsField.setText(recipe.getInstructions());

        // Устанавливаем категорию
        switch (addRecipeController.getCategoryNameById(recipe.getCategoryId())) {
            case "Завтрак": breakfast.setSelected(true); break;
            case "Обед": lunch.setSelected(true); break;
            case "Ужин": dinner.setSelected(true); break;
            case "Салат": salad.setSelected(true); break;
            case "Десерт": dessert.setSelected(true); break;
        }

        // Отображаем картинку, если она есть
        if (recipe.getImage() != null) {
            Image img = new Image(new ByteArrayInputStream(recipe.getImage()));
            recipeImageView.setImage(img);
        }
    }

    @FXML
    void get(MouseEvent event) {
        // Предпросмотр или выбор изображения (если нужно)
    }

    @FXML
    private Button chooseImageButton;

    @FXML
    void initialize() {
        // Обработчик для кнопки выбора изображения
        chooseImageButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

            File file = fileChooser.showOpenDialog(null);  // Показываем диалог выбора файла

            if (file != null) {
                try {
                    // Чтение выбранного файла в байтовый массив
                    byte[] imageBytes = Files.readAllBytes(file.toPath());

                    // Устанавливаем картинку в ImageView
                    Image img = new Image(new ByteArrayInputStream(imageBytes));
                    recipeImageView.setImage(img);

                    // Сохраняем выбранное изображение в объект рецепта
                    currentRecipe.setImage(imageBytes);  // Предполагается, что у вас есть метод setImage в модели Recipe
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        saveButton.setOnAction(event -> {
            String name = recipeNameField.getText();
            String ingredients = ingredientsField.getText();
            String instructions = instructionsField.getText();
            String categoryText = ((RadioButton) category.getSelectedToggle()).getText();
            int categoryId = 0;
            try {
                categoryId = addRecipeController.getCategoryIdByName(categoryText);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            currentRecipe.setName(name);
            currentRecipe.setIngredients(ingredients);
            currentRecipe.setInstructions(instructions);
            currentRecipe.setCategoryId(categoryId);

            // Сохранение нового изображения в базе данных
            try {
                RecipeDAO dao = new RecipeDAO();
                dao.updateRecipe(currentRecipe);  // Здесь нужно передавать объект с изображением
                System.out.println("Рецепт успешно обновлён!");  // Выводим сообщение в консоль

                // Переход в главное меню
                FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) saveButton.getScene().getWindow();
                stage.setScene(new Scene(root));

            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        });
        Platform.runLater(() -> {
            String currentFont = AppSettings.getInstance().getFontName(); // Получаем шрифт из настроек
            if (currentFont != null) { // Если шрифт выбран
                Scene scene = ((Node) mainMenuButton).getScene(); // Получаем сцену через любой элемент
                if (scene != null) {
                    applyFontToAllNodes(scene.getRoot(), currentFont);
                }
            }
        });
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
}
