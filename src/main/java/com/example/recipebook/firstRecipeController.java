package com.example.recipebook;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.example.recipebook.bd.FavoriteDAO;
import com.example.recipebook.bd.RecipeDAO;
import com.example.recipebook.bd.Session;
import com.example.recipebook.model.Recipe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class firstRecipeController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private Button mainMenuButton;

    @FXML
    private AnchorPane recipeFoodButton;

    @FXML
    void get(MouseEvent event) {

    }
    @FXML
    private Text recipeTitle;

    @FXML
    private Text ingredientsText;

    @FXML
    private Text instructionsText;

    @FXML
    private Text category;

    @FXML
    private ImageView recipeImage;


    @FXML
    private Button addFavoriteButton;  // Кнопка "Добавить в избранное"

    private Recipe currentRecipe;
    private int currentUserId = Session.getUserId();  // Идентификатор текущего пользователя, например, его можно передавать через setter

    @FXML
    private void onAddToFavoritesClicked(ActionEvent event) {
        if (currentRecipe != null && currentUserId > 0) {
            try {
                FavoriteDAO favoriteDAO = new FavoriteDAO();
                favoriteDAO.addFavorite(currentUserId, currentRecipe.getId());

                // Отображение сообщения об успешном добавлении рецепта в избранное
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Успех");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Рецепт \"" + currentRecipe.getName() + "\" был добавлен в избранное!");
                successAlert.showAndWait();

            } catch (SQLException e) {
                e.printStackTrace();

                // Отображение сообщения об ошибке
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Ошибка");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Произошла ошибка при добавлении рецепта в избранное.");
                errorAlert.showAndWait();
            }
        } else {
            // Вывод сообщения, если рецепт или пользователь не выбран
            Alert warningAlert = new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Предупреждение");
            warningAlert.setHeaderText(null);
            warningAlert.setContentText("Рецепт или пользователь не выбран.");
            warningAlert.showAndWait();
        }
    }


    @FXML
    void initialize() {
        assert deleteButton != null : "fx:id=\"deleteButton\" was not injected: check your FXML file 'firstRecipe.fxml'.";
        assert editButton != null : "fx:id=\"editButton\" was not injected: check your FXML file 'firstRecipe.fxml'.";
        assert mainMenuButton != null : "fx:id=\"mainMenuButton\" was not injected: check your FXML file 'firstRecipe.fxml'.";
        assert recipeFoodButton != null : "fx:id=\"recipeFoodButton\" was not injected: check your FXML file 'firstRecipe.fxml'.";

    }



    // Метод для установки рецепта
    public void setRecipe(Recipe recipe) throws SQLException {
        this.currentRecipe = recipe;
        System.out.println("Открыт рецепт: " + recipe.getName());

        // Устанавливаем данные из объекта Recipe в элементы интерфейса
        category.setText(addRecipeController.getCategoryNameById(recipe.getCategoryId()));
        recipeTitle.setText(recipe.getName());
        ingredientsText.setText(recipe.getIngredients());
        instructionsText.setText(recipe.getInstructions());


        // Если изображение существует, преобразуем его и показываем
        if (recipe.getImage() != null) {
            Image img = new Image(new ByteArrayInputStream(recipe.getImage()));
            recipeImage.setImage(img);
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
    private void onDeleteClicked(ActionEvent event) {
        if (currentRecipe != null) {
            try {
                RecipeDAO dao = new RecipeDAO();
                dao.deleteRecipe(currentRecipe.getId());

                // Возврат на главный экран
                FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) deleteButton.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onEditClicked(ActionEvent event) {
        try {
            // Загружаем экран редактирования
            FXMLLoader loader = new FXMLLoader(getClass().getResource("editRecipe.fxml"));
            Parent root = loader.load();

            // Получаем контроллер для редактирования
            editRecipeController editController = loader.getController();

            // Передаем текущий рецепт в контроллер редактирования
            if (currentRecipe != null) {
                editController.setRecipe(currentRecipe);
            }

            // Открываем экран редактирования
            Stage stage = (Stage) editButton.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



}
