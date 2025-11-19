package visual;

import estructura.Servicio;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import java.io.IOException;
import util.Randomizacion;
import estructura.Ruta;


import static visual.Setups.*;

/*
Clase: PrincipalController
Objetivo: Clase controladora para la ventana principal del programa.
*/
public class PrincipalController {

    @FXML private AnchorPane rootPane;

    // Esto corresponde al fx:id="mapaInclude" en PaginaPrincipal.fxml.
    @FXML private AnchorPane mapaInclude;

    // Permite acceder a los métodos de MapaController para dibujar elementos en el mapa.
    @FXML private MapaController mapaIncludeController;

    @FXML private Button btnActualizar;


    /*
     Metodo de inicializacion llamado automaticamente por el FXMLLoader
     después de que el FXML ha sido cargado y sus elementos inyectados.
     */
    @FXML
    public void initialize() {
        // Dibujar el mapa inicial con todas las estaciones y rutas existentes
        if (mapaIncludeController != null) {
            mapaIncludeController.dibujarMapaCompleto();
        }
    }

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

        // Actualizar el mapa cuando se cierre la ventana
        stage.setOnHidden(e -> actualizarMapa());

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

        // Actualizar el mapa cuando se cierre la ventana
        stage.setOnHidden(e -> actualizarMapa());

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

        // Actualizar el mapa cuando se cierre la ventana
        stage.setOnHidden(e -> actualizarMapa());

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

        // Actualizar el mapa cuando se cierre la ventana
        stage.setOnHidden(e -> actualizarMapa());

        stage.show();
    }

    //Metodo para saltar alerta si se da un evento y actualizar los datos
    @FXML
    public void actualizarDatos(ActionEvent event){
        int numero = Randomizacion.calcularEvento();
        String tipoEvento;
        Ruta rutaCambio = Servicio.getInstance().getMapa().ActualizarTiempoPorEvento(numero);

        //Hubo evento
        if(rutaCambio != null) {
            if(numero == Randomizacion.CHOQUE){
                tipoEvento = "OCURRIO UN: CHOQUE";
            }
            else{tipoEvento = "OCURRIO UN :Evento Variado";}

            // Mostrar Alerta
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(tipoEvento);
            alert.setHeaderText("¡Ocurrió un evento en la vía!");
            alert.setContentText(
                    String.format("%s\nEn la ruta: %s -> %s\nEl tiempo de viaje ha aumentado.",
                            tipoEvento,
                            rutaCambio.getOrigen().getNombre(),
                            rutaCambio.getDestino().getNombre()
                    )
            );
            alert.showAndWait();

            if (mapaIncludeController != null) {
                actualizarMapa();
                mapaIncludeController.resaltarRuta(rutaCambio, "#FF0000");
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sin Eventos");
            alert.setHeaderText(null);
            alert.setContentText("No se detectaron eventos en las rutas.");
            alert.showAndWait();
        }
    }



    //Redibuja el mapa
    private void actualizarMapa() {
        if (mapaIncludeController != null) {
            mapaIncludeController.dibujarMapaCompleto();
        }
    }


     //Getter publico para acceder al MapaController desde otras clases si es necesario
    public MapaController getMapaController() {
        return mapaIncludeController;
    }
}