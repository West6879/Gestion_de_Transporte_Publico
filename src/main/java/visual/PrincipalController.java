package visual;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import java.io.IOException;

import static visual.Setups.setupIngresoEstacion;
import static visual.Setups.setupListEstacion;


/*
Clase: PrincipalController
Objetivo: Clase controladora para la ventana principal del programa.
*/
public class PrincipalController {

    @FXML private AnchorPane rootPane;

    // Metodo para abrir la ventana de ingreso de estaciones.
    @FXML
    public void ingresoEstacion(ActionEvent event) throws IOException {
        Scene scene = setupIngresoEstacion();
        Stage stage = new Stage();
        Window owner = rootPane.getScene().getWindow();
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Ingreso de Estacion");
        stage.show();
    }

    // Metodo para abrir la ventana de listado de estaciones.
    @FXML
    public void listadoEstacion(ActionEvent event) throws IOException {
        Scene scene = setupListEstacion();
        Stage stage = new Stage();
        Window owner = rootPane.getScene().getWindow();
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene(scene);
        stage.setTitle("Listado de Estaciones");
        stage.show();
    }

}
