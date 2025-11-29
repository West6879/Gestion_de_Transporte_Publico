package visual;

import database.EstacionDAO;
import database.RutaDAO;
import estructura.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import static visual.Setups.alerta;

/*
Clase: RutaController
Objetivo: Clase controladora para la ventana de ingreso de rutas.
*/
public class RutaController {

    @FXML private ComboBox<Estacion> cmbOrigen;
    @FXML private ComboBox<Estacion> cmbDestino;
    @FXML private Spinner<Integer> spnDistancia;
    @FXML private Button btnIngresar;
    @FXML private Button btnCancelar;
    @FXML private Label lblIngreso;
    @FXML private Label lblTiempo;
    @FXML private Label lblVelocidad;
    @FXML private Slider sliderVelocidad;
    @FXML private FontIcon iconoOrigen;
    @FXML private FontIcon iconoDestino;
    @FXML private Label lblOrigenMapa;
    @FXML private Label lblDestinoMapa;
    @FXML private Line linea;
    @FXML private Polygon flecha;

    private Ruta editando = null;

    // Metodo de inicialización de fxml.
    @FXML
    public void initialize() {
        setupSpinners();
        cargarEstaciones();
        actualizarBotonEstado();
        cmbOrigen.setOnAction(event -> actualizarBotonEstado());
        cmbDestino.setOnAction(event -> actualizarBotonEstado());
        // Listener para cambiar el label de tiempo y el slider de velocidad cuando se elige una estacion origen.
        cmbOrigen.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(cmbOrigen.getSelectionModel().getSelectedIndex() != -1) {
                actualizarTiempo(spnDistancia.getValue());
                sliderVelocidad.setValue(cmbOrigen.getSelectionModel().getSelectedItem().getVelocidad());
                cambiarIconoMapa(iconoOrigen, newValue.getTipo(), newValue.getColor());
                lblOrigenMapa.setText(newValue.getNombre());
            }
        });
        cmbDestino.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(cmbDestino.getSelectionModel().getSelectedIndex() != -1) {
                cambiarIconoMapa(iconoDestino, newValue.getTipo(), newValue.getColor());
                lblDestinoMapa.setText(newValue.getNombre());
                linea.setVisible(true);
                flecha.setVisible(true);
            }
        });
        // Listener para cambiar el color del slider y el label de velocidad.
        sliderVelocidad.valueProperty().addListener((observable, oldValue, newValue) -> {
            actualizarSlider(sliderVelocidad.getValue());
            lblVelocidad.setText(String.format("%.0fkm/h", sliderVelocidad.getValue()));
        });
        // Listener para cambiar el label de tiempo dependiendo de la distancia.
        spnDistancia.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(cmbOrigen.getSelectionModel().getSelectedIndex() != -1) {
                actualizarTiempo(newValue);
            }
        });
    }

    // Metodo para setear los datos, por si se va a crear o modificar.
    public void setRuta(Ruta ruta) {
        this.editando = ruta;

        if(this.editando == null) {
            btnIngresar.setText("Agregar");
            lblIngreso.setText("Ingreso de Ruta");
        } else {
            btnIngresar.setText("Modificar");
            lblIngreso.setText("Modificar Ruta");
            cmbOrigen.setValue(editando.getOrigen());
            cmbDestino.setValue(editando.getDestino());
            spnDistancia.getValueFactory().setValue(editando.getDistancia());

            cmbOrigen.setDisable(true);
            cmbDestino.setDisable(true);

            // Habilitar el botón cuando se está modificando
            btnIngresar.setDisable(false);
        }
    }

    // Metodo para recibir todos los datos ingresados y guardarlos.
    @FXML
    public void guardarDatos(ActionEvent event) {
        Estacion origen = cmbOrigen.getValue();
        Estacion destino = cmbDestino.getValue();
        int distancia; // Se declara aquí para ser asignada dentro del try

        // Validaciones de campos de ComboBox.
        if(origen == null || destino == null) {
            alerta("Alerta!!","Por favor seleccione estación de origen y destino.",
                    Alert.AlertType.ERROR);
            return;
        }

        if(origen.equals(destino)) {
            alerta("Alerta!!","La estación de origen y destino no pueden ser la misma.",
                    Alert.AlertType.ERROR);
            return;
        }

        // Bloque Try-Catch para validar el formato de Distancia.
        try {
            // Intentar parsear el texto del editor para validar formato.
            distancia = Integer.parseInt(spnDistancia.getEditor().getText());

        } catch(NumberFormatException e) {
            // Se captura el error si el texto no es un número entero válido.
            alerta("Error de Formato", "Favor ingrese un número entero válido para la Distancia.",
                    Alert.AlertType.ERROR);
            return;
        }

        // Validaciones de rango para Distancia.
        if (distancia < 1) {
            alerta("Alerta!!", "La Distancia debe ser mayor o igual a 1.", Alert.AlertType.ERROR);
            return;
        }

        // Si el formato y el rango son válidos, se procede con la lógica de negocio.

        try {
            GrafoTransporte grafo = Servicio.getInstance().getMapa();

            if(editando == null) {
                // CREACIÓN NUEVA RUTA
                if(grafo.existeRuta(origen, destino)) {
                    alerta("Alerta!!","Ya existe una ruta entre estas estaciones. Puede modificarla pero no agregar una nueva.",
                            Alert.AlertType.ERROR);
                    return;
                }

                Ruta nuevaRuta = new Ruta(origen, destino, distancia);
                // Incrementar la cantidad de rutas de las estaciones.
                origen.setCantRutas(origen.getCantRutas() + 1);
                EstacionDAO.getInstance().update(origen);
                destino.setCantRutas(destino.getCantRutas() + 1);
                EstacionDAO.getInstance().update(destino);

                grafo.agregarRuta(nuevaRuta); // Agrega la ruta directamente al grafo.
                Servicio.getInstance().getRutas().put(nuevaRuta.getId(), nuevaRuta); // también agrégala al Servicio
                RutaDAO.getInstance().save(nuevaRuta); // Guardar en la base de datos.

                alerta("Enhorabuena!!", "Se ha creado la ruta correctamente!", Alert.AlertType.INFORMATION);
                System.out.println("Ruta agregada!!");
                limpiarCampos();
            } else {
                // MODIFICACIÓN
                editando.setDistancia(distancia);
                editando.setTiempo((double) distancia / editando.getOrigen().getVelocidad());
                editando.setCosto(Ruta.calculoDeCosto(editando.getOrigen(), distancia, editando.getOrigen().getCostoBase()));
                editando.setPonderacion((float)(editando.getCosto() + editando.getTiempo()) / 2.0f);
                RutaDAO.getInstance().update(editando); // Actualizar en la base de datos.

                alerta("Enhorabuena!!", "Se ha modificado la ruta correctamente!", Alert.AlertType.INFORMATION);
                System.out.println("Modificación de ruta hecha!!");
                Stage stage = (Stage) btnIngresar.getScene().getWindow();
                stage.close();
            }
        } catch(Exception e) {
            alerta("Error", "Ocurrió un error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Metodo para el botón de cancelar.
    @FXML
    public void btnCancelarClicked(ActionEvent event) {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    // Metodo para actualizar el tiempo en label de tiempo dependiendo del origen y la distancia.
    public void actualizarTiempo(int distancia) {
        int velocidad = cmbOrigen.getSelectionModel().getSelectedItem().getVelocidad();
        double tiempo = (double) distancia / velocidad;
        int horas = (int) tiempo;
        double minutos = (tiempo - horas) * 60;
        String tiempoTxt = String.format("%02d:%02.0f", horas, minutos);
        lblTiempo.setText(tiempoTxt);
    }

    // Metodo para limpiar los campos después de haber ingresado una nueva ruta.
    private void limpiarCampos() {
        cmbOrigen.setValue(null);
        cmbDestino.setValue(null);
        spnDistancia.getValueFactory().setValue(1);
        cmbOrigen.setDisable(false);
        cmbDestino.setDisable(false);
        sliderVelocidad.setValue(1D);
        lblTiempo.setText("00:00");
        lblOrigenMapa.setText(null);
        lblDestinoMapa.setText(null);
        iconoOrigen.setIconColor(Color.BLACK);
        iconoDestino.setIconColor(Color.BLACK);
        iconoOrigen.setIconLiteral("fas-question");
        iconoDestino.setIconLiteral("fas-question");
        linea.setVisible(false);
        flecha.setVisible(false);
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

        for(Estacion estacion : Servicio.getInstance().getEstaciones().values()) {
            cmbOrigen.getItems().add(estacion);
            cmbDestino.getItems().add(estacion);
        }
    }

    // Metodo para actualizar el estado del botón según las selecciones.
    private void actualizarBotonEstado() {
        // Si estamos editando, el botón siempre debe estar habilitado
        if(editando != null) {
            btnIngresar.setDisable(false);
            return;
        }

        // Si estamos creando nueva ruta, verificar que ambos ComboBox tengan valores
        boolean estacionesSeleccionadas = cmbOrigen.getValue() != null && cmbDestino.getValue() != null;
        btnIngresar.setDisable(!estacionesSeleccionadas);
    }

    // Metodo que cambia los iconos de origen y destino.
    private void cambiarIconoMapa(FontIcon icono, TipoEstacion tipo, Color color) {
        icono.setIconColor(color);
        if(tipo == TipoEstacion.TREN) {
            icono.setIconLiteral("fas-train");
        } else if(tipo == TipoEstacion.METRO) {
            icono.setIconLiteral("fas-subway");
        } else {
            icono.setIconLiteral("fas-bus");
        }
    }

    // Clases para el color del slider.
    private static final String LOW_CLASS = "low-value";
    private static final String MEDIUM_CLASS = "medium-value";
    private static final String HIGH_CLASS = "high-value";

    // Metodo para actualizar el color del slider de velocidad.
    private void actualizarSlider(double valor) {
        // Eliminar las styleClasses.
        sliderVelocidad.getStyleClass().removeAll(LOW_CLASS,  MEDIUM_CLASS, HIGH_CLASS);
        // Dependiendo del valor, cambia de color.
        if(valor < 100) {
            sliderVelocidad.getStyleClass().add(LOW_CLASS);
        } else if(valor < 200) {
            sliderVelocidad.getStyleClass().add(MEDIUM_CLASS);
        } else {
            sliderVelocidad.getStyleClass().add(HIGH_CLASS);
        }
    }
}