package com.example.recipebook;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import com.example.recipebook.bd.FavoriteDAO;
import com.example.recipebook.bd.RecipeDAO;
import com.example.recipebook.bd.Session;
import com.example.recipebook.model.Recipe;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class mainMenuController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane addreceptButton;

    @FXML
    private RadioButton breakfast;

    @FXML
    private ToggleGroup category;

    @FXML
    private RadioButton darkTheme;

    @FXML
    private RadioButton dessert;

    @FXML
    private RadioButton dinner;

    @FXML
    private Button favoriteButton;

    @FXML
    private Button finallyButton;

    @FXML
    private Button finallyButton1;

    @FXML
    private ChoiceBox<String> fontSelection;

    @FXML
    private RadioButton lunch;

    @FXML
    private Button mainMenuButton;

    @FXML
    private Button resetFilterButton;

    @FXML
    private AnchorPane recipeFoodButton;

    @FXML
    private AnchorPane recipeFoodButton1;

    @FXML
    private AnchorPane recipeFoodButton2;

    @FXML
    private AnchorPane recipeFoodButton21;

    @FXML
    private RadioButton salad;

    @FXML
    private ToggleGroup theme;

    @FXML
    private RadioButton whiteTheme;

    @FXML
    private GridPane recipesContainer;

    // Обработка клика на кнопку "Добавить рецепт"
    @FXML
    void onAddRecipeClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addRecipe.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onResetFilterClicked(ActionEvent event) {
        category.selectToggle(null); // снимает выделение со всех радио-кнопок
        loadRecipes(); // загружает все рецепты
    }


    // Обработка клика по любой зоне (если необходимо)
    @FXML
    void get(MouseEvent event) {
        loadRecipes();
    }

    @FXML
    void getFontSelection(MouseEvent event) {
        String selectedFont = fontSelection.getValue();

        System.out.println("Выбранный шрифт в getFontSelection: " + selectedFont);
        if (selectedFont != null) {
            AppSettings.getInstance().setFontName(selectedFont);

            Parent root = ((Node) event.getSource()).getScene().getRoot();
            applyFontToAllNodes(root, selectedFont);
        }
    }





    // Обработка кнопки "Рецепты"
    @FXML
    void onMainMenuClicked(ActionEvent event) {
        pageTitle.setText("Рецепты"); // ← Вернуть надпись
        loadRecipes();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fontSelection.getItems().addAll("System", "TimesNewRoman");
        loadRecipes();
        // Применяем шрифт сразу при загрузке сцены
        Platform.runLater(() -> {
            String currentFont = AppSettings.getInstance().getFontName();
            if (currentFont != null) {
                Scene scene = ((Node) fontSelection).getScene();
                if (scene != null) {
                    applyFontToAllNodes(scene.getRoot(), currentFont);
                }
            }
        });
    }


    public static void applyFontToAllNodes(Parent parent, String fontName) {
        System.out.println("Применение шрифта: " + fontName);  // Логируем при применении шрифта

        for (Node node : parent.lookupAll("*")) {
            if (node instanceof Labeled labeled) {
                if (!labeled.fontProperty().isBound()) {
                    System.out.println("Применяем шрифт к Labeled: " + labeled.getText());
                    labeled.setFont(Font.font(fontName, labeled.getFont().getSize()));
                }
            } else if (node instanceof TextInputControl tic) {
                if (!tic.fontProperty().isBound()) {
                    System.out.println("Применяем шрифт к TextInputControl.");
                    tic.setFont(Font.font(fontName, tic.getFont().getSize()));
                }
            } else if (node instanceof Text textNode) {
                if (!textNode.fontProperty().isBound()) {
                    System.out.println("Применяем шрифт к Text: " + textNode.getText());
                    textNode.setFont(Font.font(fontName, textNode.getFont().getSize()));
                }
            }
        }
    }


    @FXML
    void onFontApplyClicked(ActionEvent event) {
        String selectedFont = fontSelection.getValue();
        if (selectedFont != null) {
            System.out.println("Шрифт выбран: " + selectedFont); // Логирование для проверки
            AppSettings.getInstance().setFontName(selectedFont);

            Parent root = ((Node) event.getSource()).getScene().getRoot();
            applyFontToAllNodes(root, selectedFont);
        } else {
            System.out.println("Шрифт не выбран!"); // Если ничего не выбрано
        }
    }






    @FXML
    void onCategoryChanged(ActionEvent event) throws SQLException {
        RadioButton selected = (RadioButton) category.getSelectedToggle();
        if (selected != null) {
            String categoryText = selected.getText();
            int categoryId = addRecipeController.getCategoryIdByName(categoryText); // этот метод должен возвращать ID по названию
            loadRecipesByCategory(categoryId);
        }
    }





    private void loadRecipesByCategory(int categoryId) {
        RecipeDAO dao = new RecipeDAO();
        try {
            List<Recipe> recipes = dao.getAllRecipes().stream()
                    .filter(r -> r.getCategoryId() == categoryId)
                    .toList();

            displayRecipes(recipes);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayRecipes(List<Recipe> recipes) throws SQLException {
        recipesContainer.getChildren().clear();
        int columns = 3;
        int col = 0, row = 0;

        for (Recipe recipe : recipes) {
            if (recipe.getImage() == null) continue;

            AnchorPane pane = new AnchorPane();
            pane.setPrefSize(190, 190);
            pane.setStyle("-fx-background-color: #fafafa; -fx-border-color: black; -fx-border-radius: 14;");

            ImageView imageView = new ImageView(new Image(new ByteArrayInputStream(recipe.getImage())));
            imageView.setFitHeight(135);
            imageView.setPreserveRatio(true);
            StackPane imageContainer = new StackPane(imageView);
            imageContainer.setPrefSize(190, 135);

            Text categoryText = new Text(addRecipeController.getCategoryNameById(recipe.getCategoryId()));
            Text nameText = new Text(recipe.getName());
            Button moreButton = new Button("Подробнее");
            moreButton.setOnAction(e -> openRecipeDetails(recipe));

            VBox textBox = new VBox(5, categoryText, nameText, moreButton);
            textBox.setAlignment(Pos.CENTER);
            textBox.setLayoutY(135);
            textBox.setPrefWidth(190);

            pane.getChildren().addAll(imageContainer, textBox);
            recipesContainer.add(pane, col, row);

            col++;
            if (col == columns) {
                col = 0;
                row++;
            }
        }

        // Добавить кнопку "Добавить рецепт"
        AnchorPane addPane = new AnchorPane();
        addPane.setPrefSize(190, 190);
        addPane.setStyle("-fx-background-color: #fafafa; -fx-border-color: black; -fx-border-radius: 14;");

        Button addButton = new Button("Добавить рецепт");
        addButton.setPrefWidth(150);
        addButton.setLayoutX(20);
        addButton.setLayoutY(75);
        addButton.setStyle("-fx-font-size: 15px;");
        addButton.setOnAction(this::onAddRecipeClicked);

        addPane.getChildren().add(addButton);
        recipesContainer.add(addPane, col, row);
    }





    private void loadRecipes() {
        System.out.println("Загрузка рецептов...");
        RecipeDAO dao = new RecipeDAO();
        try {
            List<Recipe> recipes = dao.getAllRecipes();

            recipesContainer.getChildren().clear();

            int columns = 3;
            int row = 0;
            int col = 0;

            if (recipes != null && !recipes.isEmpty()) {
                for (Recipe recipe : recipes) {
                    if (recipe.getImage() == null) continue;

                    AnchorPane pane = new AnchorPane();
                    pane.setPrefSize(190, 190);
                    pane.setStyle("-fx-background-color: #fafafa; -fx-border-color: black; -fx-border-radius: 14;");

                    ImageView imageView = new ImageView(new Image(new ByteArrayInputStream(recipe.getImage())));
                    imageView.setFitHeight(135);
                    imageView.setPreserveRatio(true);

                    StackPane imageContainer = new StackPane(imageView);
                    imageContainer.setLayoutX(0);
                    imageContainer.setLayoutY(0);
                    imageContainer.setPrefSize(190, 135);

                    Text categoryText = new Text(addRecipeController.getCategoryNameById(recipe.getCategoryId()));
                    categoryText.setStyle("-fx-font-size: 15px;");

                    Text nameText = new Text(recipe.getName());
                    nameText.setStyle("-fx-font-size: 15px;");

                    Button moreButton = new Button("Подробнее");
                    moreButton.setStyle("-fx-background-color: #F8E171; -fx-background-radius: 10; -fx-font-size: 12px;");
                    moreButton.setOnAction(e -> openRecipeDetails(recipe));

                    VBox textBox = new VBox(5, categoryText, nameText, moreButton);
                    textBox.setLayoutX(0);
                    textBox.setLayoutY(135);
                    textBox.setPrefWidth(190);
                    textBox.setStyle("-fx-alignment: center;");
                    textBox.setAlignment(Pos.CENTER);

                    pane.getChildren().addAll(imageContainer, textBox);
                    recipesContainer.add(pane, col, row);

                    col++;
                    if (col == columns) {
                        col = 0;
                        row++;
                    }
                }
            } else {
                System.out.println("Рецепты не найдены.");
            }

            // Кнопка "Добавить рецепт"
            AnchorPane addPane = new AnchorPane();
            addPane.setPrefSize(190, 190);
            addPane.setStyle("-fx-background-color: #fafafa; -fx-border-color: black; -fx-border-radius: 14;");

            Button addButton = new Button("Добавить рецепт");
            addButton.setPrefWidth(150);
            addButton.setLayoutX(20);
            addButton.setLayoutY(75);
            addButton.setStyle("-fx-font-size: 15px;");
            addButton.setOnAction(this::onAddRecipeClicked);

            addPane.getChildren().add(addButton);
            recipesContainer.add(addPane, col, row);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private Text pageTitle;

    @FXML
    void onFavoriteButtonClicked(ActionEvent event) {
        pageTitle.setText("Избранное");
        loadFavoriteRecipes(Session.getUserId());
    }




    private void loadFavoriteRecipes(int userId) {
        FavoriteDAO favoriteDAO = new FavoriteDAO();
        RecipeDAO recipeDAO = new RecipeDAO();
        try {
            List<String> favoriteNames = favoriteDAO.getFavoritesByUser(userId);
            List<Recipe> allRecipes = recipeDAO.getAllRecipes();
            List<Recipe> favoriteRecipes = allRecipes.stream()
                    .filter(r -> favoriteNames.contains(r.getName()))
                    .toList();
            displayRecipes(favoriteRecipes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    private void openRecipeDetails(Recipe recipe) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("firstRecipe.fxml"));
            Parent root = loader.load();

            firstRecipeController controller = loader.getController();
            controller.setRecipe(recipe); // передаем выбранный рецепт

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Подробнее о рецепте");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




}
