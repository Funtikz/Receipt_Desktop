package com.example.recipebook;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class addOrEditCategoryDarkController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField inputCategory;

    @FXML
    private Button saveAddOrEditCategory;

    @FXML
    void initialize() {
        assert inputCategory != null : "fx:id=\"inputCategory\" was not injected: check your FXML file 'addOrEditCategoryDark.fxml'.";
        assert saveAddOrEditCategory != null : "fx:id=\"saveAddOrEditCategory\" was not injected: check your FXML file 'addOrEditCategoryDark.fxml'.";

    }

}
