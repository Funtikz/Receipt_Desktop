module com.example.recipebook {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.recipebook to javafx.fxml;
    opens com.example.recipebook.bd to javafx.fxml;

    exports com.example.recipebook;
    exports com.example.recipebook.bd;
}
