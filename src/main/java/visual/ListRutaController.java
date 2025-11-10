package visual;

import database.RutaDAO;
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
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
        Map<UUID, Ruta> rutas = Servicio.getInstance().getRutas();
        setupColumnas();
        activarBotones();
        tablaRutas.getItems().addAll(rutas.values());
        // cargarRutas();
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
            tablaRutas.refresh();// Recargar la tabla después de modificar
        } catch (IOException e) {
            System.out.println("Error al abrir ventana de modificación de ruta: " + e.getMessage());
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

            Servicio.getInstance().getRutas().remove(seleccionado.getId()); // Eliminar de la lista.
            Servicio.getInstance().getMapa().eliminarRuta(seleccionado); // Eliminar la ruta del grafo.
            RutaDAO.getInstance().delete(seleccionado.getId()); // Eliminar de la base de datos.

            tablaRutas.getItems().remove(seleccionado); // Eliminar de la tabla.
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