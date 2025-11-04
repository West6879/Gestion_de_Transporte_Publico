package visual;

import estructura.Servicio;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import java.io.IOException;

import static visual.Setups.setupIngresoEstacion;
import static visual.Setups.setupListEstacion;
import static visual.Setups.setupIngresoRuta;
import static visual.Setups.setupListRuta;


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

    // Metodo para abrir la ventana de ingreso de rutas.
    @FXML
    public void ingresoRuta(ActionEvent event) throws IOException {
        if(Servicio.getInstance().getEstaciones().size() < 2) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText(null);
            alert.setContentText("Debe haber al menos 2 estaciones para crear una ruta.");
            alert.showAndWait();
            return;
        }

        Scene scene = setupIngresoRuta();
        Stage stage = new Stage();
        Window owner = rootPane.getScene().getWindow();
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Ingreso de Ruta");
        stage.show();
    }

    // Metodo para abrir la ventana de listado de rutas.
    @FXML
    public void listadoRuta(ActionEvent event) throws IOException {
        Scene scene = setupListRuta();
        Stage stage = new Stage();
        Window owner = rootPane.getScene().getWindow();
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene(scene);
        stage.setTitle("Listado de Rutas");
        stage.show();
    }

}