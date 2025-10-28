module gestion_de_transporte_Publico {
    requires javafx.base;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.controlsEmpty;

    opens visual to javafx.fxml;
    exports visual;
}