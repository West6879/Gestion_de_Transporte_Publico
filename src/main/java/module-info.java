module gestion_de_transporte_Publico {
    requires javafx.base;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.controlsEmpty;
    requires java.sql;
    requires java.desktop;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;

    opens estructura to javafx.base;
    opens util to javafx.base;

    opens visual to javafx.fxml;
    exports visual;
    exports estructura;
}