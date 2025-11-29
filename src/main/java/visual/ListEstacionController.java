package visual;

import database.EstacionDAO;
import estructura.Estacion;
import estructura.Servicio;
import estructura.TipoEstacion;
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

import static visual.Setups.setupModificarEstacion;

public class ListEstacionController {

    @FXML TableView<Estacion> tablaEstaciones;
    @FXML TableColumn<Estacion, String> colNombre;
    @FXML TableColumn<Estacion, String> colZona;
    @FXML TableColumn<Estacion, TipoEstacion> colTipo;
    @FXML TableColumn<Estacion, Double> colCosto;
    @FXML TableColumn<Estacion, Integer> colVelocidad;
    @FXML TableColumn<Estacion, String> colPosicion;
    @FXML TableColumn<Estacion, Integer> colRutas;

    @FXML Button btnModificar;
    @FXML Button btnEliminar;
    @FXML Button btnCancelar;

    @FXML
    public void initialize() {
        Map<UUID, Estacion> estaciones = Servicio.getInstance().getEstaciones();
        setupColumnas();
        activarBotones();

        tablaEstaciones.getItems().addAll(estaciones.values());
    }

    // Metodo para cuando el botón modificar es seleccionado, inicializa el ingreso de estacion para modificar.
    @FXML
    public void btnModificarClicked(ActionEvent event) {
        Estacion seleccionado = tablaEstaciones.getSelectionModel().getSelectedItem();
        try {
            Scene scene = setupModificarEstacion(seleccionado);
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.setTitle("Editar Estación");
            stage.showAndWait();
            tablaEstaciones.refresh();
        } catch (IOException e) {
            System.out.println("Error al abrir ventana de modificar estación: " +  e.getMessage());
        }

    }

    // Metodo para eliminar la estacion seleccionada cuando se presiona el botón eliminar.
    @FXML
    public void btnEliminarClicked(ActionEvent event) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Eliminar");
        alerta.setHeaderText(null);
        alerta.setContentText("¿Estas seguro de que deseas eliminar esta estación?");
        Optional<ButtonType> resultado = alerta.showAndWait();
        if(resultado.isPresent() && (resultado.get() == ButtonType.OK)) {
            Estacion seleccionado = tablaEstaciones.getSelectionModel().getSelectedItem();

            Servicio.getInstance().eliminarEstacion(seleccionado); // Eliminar de la lista.
            Servicio.getInstance().getMapa().eliminarEstacion(seleccionado); // Eliminar del grafo.
            EstacionDAO.getInstance().delete(seleccionado.getId()); // Eliminar de la base de datos.


            tablaEstaciones.getItems().remove(seleccionado); // Eliminar de la tabla.
            System.out.println("Estación eliminada correctamente.");
        } else {
            alerta.close();
            System.out.println("No se elimino la estación!");
        }
    }

    // Metodo para el botón de cancelar cuando es seleccionado.
    @FXML
    public void btnCancelarClicked(ActionEvent event) {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    // Metodo para activar y desactivar botones dependiendo si hay una estacion de la lista seleccionada.
    public void activarBotones() {
        btnModificar.disableProperty().bind(
                tablaEstaciones.getSelectionModel().selectedItemProperty().isNull()
        );
        btnEliminar.disableProperty().bind(
                tablaEstaciones.getSelectionModel().selectedItemProperty().isNull()
        );
    }

    // Metodo para setear las columnas con los valores correspondientes.
    public void setupColumnas() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colZona.setCellValueFactory(new PropertyValueFactory<>("zona"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colCosto.setCellValueFactory(new PropertyValueFactory<>("costoBase"));
        colVelocidad.setCellValueFactory(new PropertyValueFactory<>("velocidad"));
        colPosicion.setCellValueFactory(new PropertyValueFactory<>("posicion"));
        colRutas.setCellValueFactory(new PropertyValueFactory<>("cantRutas"));
    }

}
