module gestion_de_transporte_Publico {
    requires javafx.base;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.controlsEmpty;

    opens estructura to javafx.base;
    opens util to javafx.base;

    opens visual to javafx.fxml;
    exports visual;
}