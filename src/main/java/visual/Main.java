package visual;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static visual.Setups.setupPrincipal;


/*
Clase: Main
Objetivo: Clase principal que inicia el programa.
*/
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Scene scene = setupPrincipal();
        primaryStage.setResizable(false);
        // primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Gestion de Transporte Publico");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}