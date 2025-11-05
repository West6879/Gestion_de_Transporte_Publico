package visual;

import estructura.Estacion;
import estructura.GrafoTransporte;
import estructura.Ruta;
import estructura.Servicio;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/*
Clase: RutaController
Objetivo: Clase controladora para la ventana de ingreso de rutas.
*/
public class RutaController {

    @FXML private ComboBox<Estacion> cmbOrigen;
    @FXML private ComboBox<Estacion> cmbDestino;
    @FXML private Spinner<Integer> spnDistancia;
    @FXML private Button btnAgregar;
    @FXML private Button btnCancelar;
    @FXML private Label titleLabel;

    private Ruta editando = null;

    // Metodo de inicialización de fxml.
    @FXML
    public void initialize() {
        setupSpinners();
        cargarEstaciones();
        actualizarBotonEstado();

        cmbOrigen.setOnAction(event -> actualizarBotonEstado());
        cmbDestino.setOnAction(event -> actualizarBotonEstado());
    }

    // Metodo para setear los datos, por si se va a crear o modificar.
    public void setRuta(Ruta ruta) {
        this.editando = ruta;

        if(this.editando == null) {
            btnAgregar.setText("Agregar");
            titleLabel.setText("Ruta:");
        } else {
            btnAgregar.setText("Modificar");
            titleLabel.setText("Modificar Ruta:");
            cmbOrigen.setValue(editando.getOrigen());
            cmbDestino.setValue(editando.getDestino());
            spnDistancia.getValueFactory().setValue(editando.getDistancia());

            cmbOrigen.setDisable(true);
            cmbDestino.setDisable(true);

            // Habilitar el botón cuando se está modificando
            btnAgregar.setDisable(false);
        }
    }

    @FXML
    public void guardarDatos(ActionEvent event) {
        try {
            Estacion origen = cmbOrigen.getValue();
            Estacion destino = cmbDestino.getValue();
            int distancia = spnDistancia.getValue();

            // Validaciones.
            if(origen == null || destino == null) {
                alerta("Alerta!!","Por favor seleccione estación de origen y destino.");
                return;
            }

            if(origen.equals(destino)) {
                alerta("Alerta!!","La estación de origen y destino no pueden ser la misma.");
                return;
            }

            GrafoTransporte grafo = Servicio.getInstance().getMapa();

            if(editando == null) {
                // CREACIÓN NUEVA RUTA
                if(grafo.existeRuta(origen, destino)) {
                    alerta("Alerta!!","Ya existe una ruta entre estas estaciones. Puede modificarla pero no agregar una nueva.");
                    return;
                }

                Ruta nuevaRuta = grafo.agregarRuta(origen, destino, distancia); // Suponiendo que este metodo devuelve la nueva Ruta
                // también agrégala al Servicio
                Servicio.getInstance().getRutas().add(nuevaRuta);

                alerta("Enhorabuena!!", "Se ha creado la ruta correctamente!");
                System.out.println("Ruta agregada!!");
                limpiarCampos();
            } else {
                // MODIFICACIÓN
                editando.setDistancia(distancia);
                editando.setTiempo(Math.max(1, distancia / editando.getDestino().getVelocidad()));
                editando.setCosto(Ruta.calculoDeCosto(editando.getDestino(), distancia, editando.getDestino().getCostoBase()));
                editando.setPonderacion((float)(editando.getCosto() + editando.getTiempo()) / 2.0f);

                // No hace falta volverla a agregar a Servicio si solo estás modificando el objeto.
                alerta("Enhorabuena!!", "Se ha modificado la ruta correctamente!");
                System.out.println("Modificación de ruta hecha!!");
                Stage stage = (Stage) btnAgregar.getScene().getWindow();
                stage.close();
            }
        } catch(Exception e) {
            e.printStackTrace();
            alerta("Error", "Ocurrió un error: " + e.getMessage());
        }
    }

    // Metodo para el botón de cancelar.
    @FXML
    public void btnCancelarClicked(ActionEvent event) {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    // Metodo para llamar una alerta, ya sea para errores o confirmaciones.
    public void alerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    // Metodo para limpiar los campos después de haber ingresado una nueva ruta.
    private void limpiarCampos() {
        cmbOrigen.setValue(null);
        cmbDestino.setValue(null);
        spnDistancia.getValueFactory().setValue(1);
        cmbOrigen.setDisable(false);
        cmbDestino.setDisable(false);
        editando = null; // Resetear el estado de edición
        actualizarBotonEstado(); // Actualizar el estado del botón
    }

    // Metodo para las propiedades de los spinners.
    private void setupSpinners() {
        spnDistancia.setEditable(true);
        spnDistancia.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 1, 1)
        );
    }

    // Metodo para cargar las estaciones en los combobox.
    private void cargarEstaciones() {
        cmbOrigen.getItems().clear();
        cmbDestino.getItems().clear();

        for(Estacion estacion : Servicio.getInstance().getEstaciones()) {
            cmbOrigen.getItems().add(estacion);
            cmbDestino.getItems().add(estacion);
        }
    }

    // Metodo para actualizar el estado del botón según las selecciones.
    private void actualizarBotonEstado() {
        // Si estamos editando, el botón siempre debe estar habilitado
        if(editando != null) {
            btnAgregar.setDisable(false);
            return;
        }

        // Si estamos creando nueva ruta, verificar que ambos ComboBox tengan valores
        boolean estacionesSeleccionadas = cmbOrigen.getValue() != null && cmbDestino.getValue() != null;
        btnAgregar.setDisable(!estacionesSeleccionadas);
    }
}