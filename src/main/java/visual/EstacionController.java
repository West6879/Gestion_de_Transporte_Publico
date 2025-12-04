package visual;

import database.EstacionDAO;
import estructura.Estacion;
import estructura.Servicio;
import estructura.TipoEstacion;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.awt.*;
import java.util.Objects;

import static visual.Setups.alerta;
import static visual.Setups.setupFormatter;

/*
Clase: EstacionController
Objetivo: Clase controladora para la ventana de ingreso de estaciones.
*/
public class EstacionController {

    @FXML private TextField fieldNombre;
    @FXML private TextField fieldZona;
    @FXML private Spinner<Double> spnCosto;
    @FXML private Spinner<Double> spnLatitud;
    @FXML private Spinner<Double> spnLongitud;
    @FXML private Button btnIngresar;
    @FXML private Button btnCancelar;
    @FXML private Slider sliderVelocidad;
    @FXML private Label lblVelocidad;
    @FXML private Label lblIngreso;
    @FXML private VBox tipoOpciones;
    @FXML private Label lblNombreMapa;
    @FXML private FontIcon iconoMapa;
    @FXML private ColorPicker miColorPicker;

    private Estacion editando = null;
    private final ToggleGroup tgTipo = new ToggleGroup();
    private TipoEstacion tipoElegido;

    private static final String[] imagenes = {
            "/imagenes/tipoTrenE.jpg", "/imagenes/tipoMetroE.jpg", "/imagenes/tipoBusE.jpg"
    };

    private static final String[] nombres = {
            "TREN", "METRO", "BUS"
    };


    // Metodo de inicialización de fxml.
    @FXML
    public void initialize() {
        setupSpinners();
        sliderVelocidad.valueProperty().addListener((observable, oldValue, newValue) -> {
            actualizarSlider(newValue.doubleValue());
            if(lblVelocidad != null) {
                lblVelocidad.setText(String.format("%.0fkm/h", newValue.doubleValue()));
            }
        });

        fieldNombre.textProperty().addListener((observable, oldValue, newValue) -> lblNombreMapa.setText(newValue));
        tgTipo.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                tipoElegido = TipoEstacion.valueOf(newValue.getUserData().toString());
                cambiarIconoMapa(tipoElegido);
                miColorPicker.setDisable(false);
            }
        });
        for (int i = 0; i < imagenes.length; i++) {
            StackPane botonTipo = crearBotonTipo(imagenes[i], nombres[i]);
            tipoOpciones.getChildren().add(botonTipo);
        }
    }

    // Metodo para setear los datos, por si se va a crear o modificar.
    public void setEstacion(Estacion estacion) {
        this.editando = estacion;

        if(this.editando == null) {
            btnIngresar.setText("Ingresar");
            lblIngreso.setText("Ingreso de Estación");
        } else {
            btnIngresar.setText("Modificar");
            lblIngreso.setText("Modificar Estación");
            fieldNombre.setText(editando.getNombre());
            fieldZona.setText(editando.getZona());
            tipoElegido = editando.getTipo();
            spnCosto.getValueFactory().setValue(editando.getCostoBase());
            sliderVelocidad.setValue(editando.getVelocidad());
            spnLatitud.getValueFactory().setValue(editando.getLatitud());
            spnLongitud.getValueFactory().setValue(editando.getLongitud());
            miColorPicker.setValue(editando.getColor());
            iconoMapa.setIconColor(editando.getColor());
            iconoMapa.setStroke(Color.BLACK);
            iconoMapa.setStrokeWidth(2);

            for(Toggle toggle : tgTipo.getToggles()) {
                if(toggle.getUserData().toString().equals(editando.getTipo().name())) {
                    toggle.setSelected(true);
                    break;
                }
            }
        }
    }

    // Metodo para recibir todos los datos ingresados y guardarlos.
    @FXML
    public void guardarDatos(ActionEvent event) {
        String nombre = fieldNombre.getText();
        String zona = fieldZona.getText();
        TipoEstacion tipo = tipoElegido;
        Color color =  miColorPicker.getValue();
        double costo;
        int velocidad;
        double latitud;
        double longitud;

        // Validaciones de campos de texto y ComboBox.
        if(nombre.isEmpty() || zona.isEmpty() || tipo == null) {
            alerta("Alerta!!","Por favor ingrese un nombre, zona y tipo de estación.", Alert.AlertType.ERROR);
            return;
        }

        // Bloque Try-Catch para validar el formato de todos los Spinners antes de continuar.
        try {
            // Intentar parsear el texto del editor para validar formato.
            costo = Double.parseDouble(spnCosto.getEditor().getText());
            velocidad = (int) sliderVelocidad.getValue();
            latitud = Double.parseDouble(spnLatitud.getEditor().getText());
            longitud = Double.parseDouble(spnLongitud.getEditor().getText());

        } catch(NumberFormatException e) {
            // Se captura el error si el texto no es un número válido.
            alerta("Error de Formato", "Favor ingresar números válidos en los campos numéricos (Costo, Velocidad, Latitud, Longitud).",
                    Alert.AlertType.ERROR);
            return;
        }

        // El formato es correcto, ahora se valida el rango.

        // Validaciones de rango para Velocidad.
        if (velocidad < 1) {
            alerta("Alerta!!", "La Velocidad debe ser mayor o igual a 1.", Alert.AlertType.ERROR);
            return;
        }

        // Validaciones de rango para Costo.
        if (costo < 50D) {
            alerta("Alerta!!", "El Costo Base debe ser mayor o igual a 50.", Alert.AlertType.ERROR);
            return;
        }

        // Validaciones de rango para Latitud.
        if (latitud < -2500 || latitud > 2500) {
            alerta("Alerta!!", "La Latitud debe estar entre -2500 y 2500.", Alert.AlertType.ERROR);
            return;
        }

        // Validaciones de rango para Longitud.
        if (longitud < -2500 || longitud > 2500) {
            alerta("Alerta!!", "La Longitud debe estar entre -2500 y 2500.", Alert.AlertType.ERROR);
            return;
        }

        // Validación de Posicion (Latitud y Longitud)
        if (Servicio.getInstance().getMapa().existeEnPos(latitud, longitud)) {
            // Si existe una estación con esa posición...
            if (editando == null || !(editando.getLatitud() == latitud && editando.getLongitud() == longitud)) {
                // ...y NO estamos editando la MISMA estación, entonces es un duplicado no permitido.
                alerta("Alerta!!", "Ya existe una estación con las coordenadas (Latitud, Longitud) ingresadas.", Alert.AlertType.ERROR);
                return;
            }
        }

        if(editando == null) {
            // CREACIÓN NUEVA ESTACIÓN
            Estacion nuevaEstacion = new Estacion(nombre, zona, latitud, longitud, costo, velocidad, tipo, color);
            Servicio.getInstance().getEstaciones().put(nuevaEstacion.getId(), nuevaEstacion);
            Servicio.getInstance().getMapa().agregarEstacion(nuevaEstacion);
            EstacionDAO.getInstance().save(nuevaEstacion); // Guardar en la base de datos.
            alerta("Enhorabuena!!", "Se ha creado la estación correctamente!", Alert.AlertType.INFORMATION);
            System.out.println("Ingreso hecho!!");
            limpiarCampos();
        } else {
            // MODIFICACIÓN
            setearDatos(nombre, zona, costo, velocidad, latitud, longitud, tipo, color);
            Servicio.getInstance().actualizarRutasPorEstacion(editando);
            EstacionDAO.getInstance().update(editando); // Actualizar en la base de datos.

            alerta("Enhorabuena!!", "Se ha modificado la estación correctamente!", Alert.AlertType.INFORMATION);
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

    // Metodo para cambiar el color del icono del mapa al seleccionar un color del colorPicker.
    @FXML
    private void cambiarColorIcono(ActionEvent event) {
        iconoMapa.setIconColor(miColorPicker.getValue());
        iconoMapa.setStroke(Color.BLACK);
        iconoMapa.setStrokeWidth(2);
    }

    // Metodo para cambiar el icono que aparece en el mapa dependiendo del tipo de estacion seleccionado.
    private void cambiarIconoMapa(TipoEstacion tipo) {
        if(tipo == TipoEstacion.TREN) {
            iconoMapa.setIconLiteral("fas-train");
        } else if(tipo == TipoEstacion.METRO) {
            iconoMapa.setIconLiteral("fas-subway");
        } else {
            iconoMapa.setIconLiteral("fas-bus");
        }
    }

    // Metodo para crear los botones para la selección de tipo.
    private StackPane crearBotonTipo(String imagePath, String hoverText) {
        Image imagen = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        // Crear el background del checkbox con la imagen.
        BackgroundImage background = new BackgroundImage(
                imagen,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(1.0, 1.0, true, true, false, true)
        );

        // Crear la checkbox.
        RadioButton rbTipo = new RadioButton();
        rbTipo.setBackground(new Background(background));
        rbTipo.setPrefSize(300, 75);
        rbTipo.setMaxWidth(Double.MAX_VALUE);
        rbTipo.setAlignment(Pos.CENTER_LEFT);
        rbTipo.getStyleClass().add("radio-imagen");
        rbTipo.setToggleGroup(tgTipo);
        rbTipo.setUserData(hoverText);

        // Crear el label que se mostrara cuando se pasa el mouse por encima.
        Label hoverLabel = new Label(hoverText.toUpperCase());
        hoverLabel.getStyleClass().add("hover-text-label");
        hoverLabel.setOpacity(0);
        hoverLabel.setMouseTransparent(true);

        // Crear un StackPane, para poner el label encima del checkbox.
        StackPane stack = new StackPane();
        stack.getChildren().addAll(rbTipo, hoverLabel);
        StackPane.setAlignment(hoverLabel, Pos.CENTER);

        // Listener para mostrar el texto cuando se pasa el mouse por encima.
        rbTipo.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered) {
                hoverLabel.setOpacity(1.0);
            } else {
                hoverLabel.setOpacity(0.0);
            }
        });

        return stack;
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

    // Metodo para setear todos los datos cuando se va a modificar.
    private void setearDatos(String nombre, String zona, double costo, int velocidad,
                             double latitud, double longitud, TipoEstacion tipo, Color color) {
        editando.setNombre(nombre);
        editando.setZona(zona);
        editando.setCostoBase(costo);
        editando.setVelocidad(velocidad);
        editando.setLatitud(latitud);
        editando.setLongitud(longitud);
        editando.setTipo(tipo);
        editando.setColor(color);
    }

    // Metodo para limpiar los campos después de haber ingresado una nueva estación.
    private void limpiarCampos() {
        fieldNombre.clear();
        fieldZona.clear();
        spnCosto.getValueFactory().setValue(50D);
        sliderVelocidad.setValue(1D);
        spnLatitud.getValueFactory().setValue(0D);
        spnLongitud.getValueFactory().setValue(0D);
        tgTipo.selectToggle(null);
        tipoElegido = null;
        iconoMapa.setIconLiteral("fas-question");
        iconoMapa.setIconColor(Color.BLACK);
        miColorPicker.setValue(Color.BLACK);
    }

    // Metodo para las propiedades de los spinners.
    private void setupSpinners() {
        spnCosto.setEditable(true);
        spnLatitud.setEditable(true);
        spnLongitud.setEditable(true);
        spnCosto.setValueFactory(
                new SpinnerValueFactory.DoubleSpinnerValueFactory(50, 5000, 50, 50)
        );
        spnLatitud.setValueFactory(
                new SpinnerValueFactory.DoubleSpinnerValueFactory(-2500, 2500, 0, 10)
        );
        spnLongitud.setValueFactory(
                new SpinnerValueFactory.DoubleSpinnerValueFactory(-2500, 2500, 0, 10)
        );
        setupFormatter(spnCosto);
        setupFormatter(spnLatitud);
        setupFormatter(spnLongitud);
    }


}