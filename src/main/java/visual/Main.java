package visual;

import estructura.Servicio;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static visual.Setups.setupPrincipal;

/*
Clase: Main
Objetivo: Clase principal que inicia el programa.
*/
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Inicializa el Servicio y carga estaciones/rutas si corresponde
        Scene scene = setupPrincipal();
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Gestion de Transporte Publico");
        Image icono = new Image(Objects.requireNonNull(Main.class.getResource("/imagenes/logoPrincipalHD.png")).toExternalForm());
        primaryStage.getIcons().add(icono);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}