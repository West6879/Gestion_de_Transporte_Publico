package visual;

import estructura.Ruta;
import estructura.Servicio;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;

import static visual.Setups.setupModificarRuta;

/*
Clase: ListRutaController
Objetivo: Clase controladora para la ventana de listado de rutas.
*/
public class ListRutaController {

    @FXML TableView<Ruta> tablaRutas;
    @FXML TableColumn<Ruta, String> colOrigen;
    @FXML TableColumn<Ruta, String> colDestino;
    @FXML TableColumn<Ruta, Integer> colDistancia;
    @FXML TableColumn<Ruta, Integer> colTiempo;
    @FXML TableColumn<Ruta, Double> colCosto;
    @FXML TableColumn<Ruta, Float> colPonderacion;

    @FXML Button btnModificar;
    @FXML Button btnEliminar;
    @FXML Button btnCancelar;

    // Metodo de inicialización de fxml.
    @FXML
    public void initialize() {
        setupColumnas();
        activarBotones();
        cargarRutas();
    }

    // Metodo para cargar todas las rutas en la tabla.
    private void cargarRutas() {
        tablaRutas.getItems().clear();
        // Recorrer todas las estaciones y sus rutas
        Servicio.getInstance().getMapa().getWeb().forEach((estacion, rutas) -> {
            tablaRutas.getItems().addAll(rutas);
        });
    }

    // Metodo para cuando el botón modificar es seleccionado.
    @FXML
    public void btnModificarClicked(ActionEvent event) {
        Ruta seleccionado = tablaRutas.getSelectionModel().getSelectedItem();
        try {
            Scene scene = setupModificarRuta(seleccionado);
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.setTitle("Editar Ruta");
            stage.showAndWait();
            cargarRutas(); // Recargar la tabla después de modificar
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Metodo para eliminar la ruta seleccionada.
    @FXML
    public void btnEliminarClicked(ActionEvent event) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Eliminar");
        alerta.setHeaderText(null);
        alerta.setContentText("¿Estas seguro de que deseas eliminar esta ruta?");
        Optional<ButtonType> resultado = alerta.showAndWait();
        if(resultado.isPresent() && (resultado.get() == ButtonType.OK)) {
            Ruta seleccionado = tablaRutas.getSelectionModel().getSelectedItem();
            Servicio.getInstance().getRutas().remove(seleccionado); // Eliminar de la lista.
            // Eliminar la ruta del grafo
            Servicio.getInstance().getMapa().eliminarRuta(
                    seleccionado.getOrigen(),
                    seleccionado.getDestino(),
                    seleccionado.getId()
            );
            tablaRutas.getItems().remove(seleccionado);
            System.out.println("Ruta eliminada correctamente.");
        } else {
            System.out.println("No se eliminó la ruta!");
        }
    }

    // Metodo para el botón de cancelar.
    @FXML
    public void btnCancelarClicked(ActionEvent event) {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    // Metodo para activar y desactivar botones.
    public void activarBotones() {
        btnModificar.disableProperty().bind(
                tablaRutas.getSelectionModel().selectedItemProperty().isNull()
        );
        btnEliminar.disableProperty().bind(
                tablaRutas.getSelectionModel().selectedItemProperty().isNull()
        );
    }

    // Metodo para setear las columnas con los valores correspondientes.
    public void setupColumnas() {
        colOrigen.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getOrigen().getNombre()));
        colDestino.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDestino().getNombre()));
        colDistancia.setCellValueFactory(new PropertyValueFactory<>("distancia"));
        colTiempo.setCellValueFactory(new PropertyValueFactory<>("tiempo"));
        colCosto.setCellValueFactory(new PropertyValueFactory<>("costo"));
        colPonderacion.setCellValueFactory(new PropertyValueFactory<>("ponderacion"));
    }
}