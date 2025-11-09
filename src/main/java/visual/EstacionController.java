package visual;

import estructura.Estacion;
import estructura.Servicio;
import estructura.TipoEstacion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/*
Clase: EstacionController
Objetivo: Clase controladora para la ventana de ingreso de estaciones.
*/
public class EstacionController {

    @FXML private TextField fieldNombre;
    @FXML private TextField fieldZona;
    @FXML private Spinner<Double> spnCosto;
    @FXML private Spinner<Integer> spnVelocidad;
    @FXML private Spinner<Double> spnLatitud;
    @FXML private Spinner<Double> spnLongitud;
    @FXML private ComboBox<TipoEstacion> cmbTipo;
    @FXML private Button btnIngresar;
    @FXML private Button btnCancelar;

    private Estacion editando = null;

    // Metodo de inicialización de fxml.
    @FXML
    public void initialize() {
        cmbTipo.getItems().addAll(TipoEstacion.values());
        setupSpinners();
    }

    // Metodo para setear los datos, por si se va a crear o modificar.
    public void setEstacion(Estacion estacion) {
        this.editando = estacion;

        if(this.editando == null) {
            btnIngresar.setText("Ingresar");
        } else {
            btnIngresar.setText("Modificar");
            fieldNombre.setText(editando.getNombre());
            fieldZona.setText(editando.getZona());
            cmbTipo.setValue(editando.getTipo());
            spnCosto.getValueFactory().setValue(editando.getCostoBase());
            spnVelocidad.getValueFactory().setValue(editando.getVelocidad());
            spnLatitud.getValueFactory().setValue(editando.getLatitud());
            spnLongitud.getValueFactory().setValue(editando.getLongitud());
        }
    }

    // Metodo para recibir todos los datos ingresados y guardarlos.
    @FXML
    public void guardarDatos(ActionEvent event) {
        String nombre = fieldNombre.getText();
        String zona = fieldZona.getText();
        TipoEstacion tipo = cmbTipo.getValue();
        double costo;
        int velocidad;
        double latitud;
        double longitud;

        // Validaciones de campos de texto y ComboBox.
        if(nombre.isEmpty() || zona.isEmpty() || tipo == null) {
            alerta("Alerta!!","Por favor ingrese un nombre, zona y tipo de estación.");
            return;
        }

        // Bloque Try-Catch para validar el formato de todos los Spinners antes de continuar.
        try {
            // Intentar parsear el texto del editor para validar formato.
            costo = Double.parseDouble(spnCosto.getEditor().getText());
            velocidad = Integer.parseInt(spnVelocidad.getEditor().getText());
            latitud = Double.parseDouble(spnLatitud.getEditor().getText());
            longitud = Double.parseDouble(spnLongitud.getEditor().getText());

        } catch(NumberFormatException e) {
            // Se captura el error si el texto no es un número válido.
            alerta("Error de Formato", "Favor ingresar números válidos en los campos numéricos (Costo, Velocidad, Latitud, Longitud).");
            return;
        }

        // El formato es correcto, ahora se valida el rango.

        // Validaciones de rango para Velocidad.
        if (velocidad < 1) {
            alerta("Alerta!!", "La Velocidad debe ser mayor o igual a 1.");
            return;
        }

        // Validaciones de rango para Costo.
        if (costo < 50D) {
            alerta("Alerta!!", "El Costo Base debe ser mayor o igual a 50.");
            return;
        }

        // Validaciones de rango para Latitud.
        if (latitud < 0 || latitud > 1000) {
            alerta("Alerta!!", "La Latitud debe estar entre 0 y 1000.");
            return;
        }

        // Validaciones de rango para Longitud.
        if (longitud < 0 || longitud > 1000) {
            alerta("Alerta!!", "La Longitud debe estar entre 0 y 1000.");
            return;
        }

        if(editando == null) {
            Estacion nuevaEstacion = new Estacion(nombre, zona, latitud, longitud, costo, velocidad, tipo);
            Servicio.getInstance().getEstaciones().add(nuevaEstacion);
            Servicio.getInstance().getMapa().agregarEstacion(nuevaEstacion);
            alerta("Enhorabuena!!", "Se ha creado la estación correctamente!");
            System.out.println("Ingreso hecho!!");
            limpiarCampos();
        } else {
            setearDatos(nombre, zona, costo, velocidad, latitud, longitud, tipo);
            alerta("Enhorabuena!!", "Se ha modificado la estación correctamente!");
            System.out.println("Modificación hecha!!");
            Stage stage = (Stage) btnIngresar.getScene().getWindow();
            stage.close();
        }
    }

    // Metodo para el botón de cancelar.
    @FXML
    public void btnCancelarClicked(ActionEvent event) {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    // Metodo para setear todos los datos cuando se va a modificar.
    private void setearDatos(String nombre, String zona, double costo, int velocidad,
                            double latitud, double longitud, TipoEstacion tipo) {
        editando.setNombre(nombre);
        editando.setZona(zona);
        editando.setCostoBase(costo);
        editando.setVelocidad(velocidad);
        editando.setLatitud(latitud);
        editando.setLongitud(longitud);
        editando.setTipo(tipo);
    }

    // Metodo para llamar una alerta, ya sea para errores o confirmaciones.
    public void alerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    // Metodo para limpiar los campos después de haber ingresado una nueva estación.
    private void limpiarCampos() {
        fieldNombre.clear();
        fieldZona.clear();
        cmbTipo.setValue(null);
        spnCosto.getValueFactory().setValue(50D);
        spnVelocidad.getValueFactory().setValue(1);
        spnLatitud.getValueFactory().setValue(0D);
        spnLongitud.getValueFactory().setValue(0D);
    }

    // Metodo para las propiedades de los spinners.
    private void setupSpinners() {
        spnCosto.setEditable(true);
        spnVelocidad.setEditable(true);
        spnLatitud.setEditable(true);
        spnLongitud.setEditable(true);
        spnCosto.setValueFactory(
                new SpinnerValueFactory.DoubleSpinnerValueFactory(50, 5000, 50, 50)
        );
        spnVelocidad.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 300, 1, 1)
        );
        spnLatitud.setValueFactory(
                new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000, 0, 1)
        );
        spnLongitud.setValueFactory(
                new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000, 0, 1)
        );
    }
}
