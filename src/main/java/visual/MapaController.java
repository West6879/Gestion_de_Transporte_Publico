package visual;

import estructura.Estacion;
import estructura.Ruta;
import estructura.Servicio;
import estructura.TipoEstacion;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.Group;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/*
Clase: MapaController
Objetivo: Controladora para manejar la visualizacion del mapa.
*/
public class MapaController {

    @FXML
    private AnchorPane mapaPane;

    private Map<UUID, FontIcon> iconosEstaciones = new HashMap<>();
    private Map<UUID, Label> etiquetasEstaciones = new HashMap<>();
    private Map<UUID, Group> gruposRutas = new HashMap<>();
    private VBox infoBox = null;

    // Metodo de inicializacion de fxml.
    @FXML
    public void initialize() {
        dibujarMapaCompleto();
    }

    // Metodo para dibujar todas las estaciones y rutas del servicio.
    public void dibujarMapaCompleto() {
        limpiarMapa();
        dibujarTodasLasRutas();
        dibujarTodasLasEstaciones();
    }

    // Metodo para limpiar todos los elementos del mapa.
    public void limpiarMapa() {
        mapaPane.getChildren().clear();
        iconosEstaciones.clear();
        etiquetasEstaciones.clear();
        gruposRutas.clear();
        infoBox = null;
    }

    // Metodo para dibujar todas las estaciones del servicio.
    private void dibujarTodasLasEstaciones() {
        for (Estacion estacion : Servicio.getInstance().getEstaciones().values()) {
            dibujarEstacion(estacion);
        }
    }

    // Metodo para dibujar una estación individual en el mapa.
    public void dibujarEstacion(Estacion estacion) {
        FontIcon icono = new FontIcon();
        icono.setIconSize(30);
        icono.setFill(estacion.getColor());
        icono.setStroke(Color.BLACK);
        icono.setStrokeWidth(2);
        icono.setLayoutX(estacion.getLongitud());
        icono.setLayoutY(estacion.getLatitud());
        icono.setIconLiteral(setearTipoIcono(estacion.getTipo()));

        Label etiqueta = new Label(estacion.getNombre());
        etiqueta.setFont(Font.font("System", FontWeight.BOLD, 12));
        etiqueta.setLayoutX(estacion.getLongitud() + 20);
        etiqueta.setLayoutY(estacion.getLatitud() - 10);

        icono.setOnMouseEntered(e -> {
            icono.setIconSize(36);
            icono.setStrokeWidth(3);
        });

        icono.setOnMouseExited(e -> {
            icono.setIconSize(30);
            icono.setStrokeWidth(2);
        });

        mapaPane.getChildren().addAll(icono, etiqueta);
        iconosEstaciones.put(estacion.getId(), icono);
        etiquetasEstaciones.put(estacion.getId(), etiqueta);
    }

    // Metodo para setear el tipo de icono de la estacion.
    private static String setearTipoIcono(TipoEstacion tipo) {
        String iconLiteral;
        if(tipo == TipoEstacion.TREN) {
            iconLiteral = "fas-train";
        } else if(tipo == TipoEstacion.METRO) {
            iconLiteral = "fas-subway";
        } else {
            iconLiteral = "fas-bus";
        }
        return iconLiteral;
    }

    // Metodo para dibujar todas las rutas del servicio.
    private void dibujarTodasLasRutas() {
        for (Ruta ruta : Servicio.getInstance().getRutas().values()) {
            dibujarRuta(ruta);
        }
    }

    // Metodo para dibujar una ruta individual en el mapa como una flecha.
    public void dibujarRuta(Ruta ruta) {
        Estacion origen = ruta.getOrigen();
        Estacion destino = ruta.getDestino();

        double x1 = origen.getLongitud();
        double y1 = origen.getLatitud();
        double x2 = destino.getLongitud();
        double y2 = destino.getLatitud();

        double angulo = Math.atan2(y2 - y1, x2 - x1);
        double radioEstacion = 15;
        double x1Ajustado = x1 + radioEstacion * Math.cos(angulo);
        double y1Ajustado = y1 + radioEstacion * Math.sin(angulo);
        double x2Ajustado = x2 - radioEstacion * Math.cos(angulo);
        double y2Ajustado = y2 - radioEstacion * Math.sin(angulo);

        Line linea = new Line();
        linea.setStartX(x1Ajustado);
        linea.setStartY(y1Ajustado);
        linea.setEndX(x2Ajustado);
        linea.setEndY(y2Ajustado);
        linea.setStroke(Color.GRAY);
        linea.setStrokeWidth(3);
        linea.setOpacity(0.6);

        Polygon flecha = crearPuntaFlecha(x2Ajustado, y2Ajustado, angulo);
        flecha.setFill(Color.GRAY);
        flecha.setOpacity(0.6);

        Group grupoRuta = new Group(linea, flecha);

        grupoRuta.setOnMouseEntered(e -> {
            linea.setStrokeWidth(5);
            linea.setOpacity(1.0);
            flecha.setOpacity(1.0);
            linea.setStroke(Color.DARKBLUE);
            flecha.setFill(Color.DARKBLUE);
        });

        grupoRuta.setOnMouseExited(e -> {
            linea.setStrokeWidth(3);
            linea.setOpacity(0.6);
            flecha.setOpacity(0.6);
            linea.setStroke(Color.GRAY);
            flecha.setFill(Color.GRAY);
        });

        grupoRuta.setOnMouseClicked(e -> {
            mostrarInfoRuta(ruta, e.getSceneX(), e.getSceneY());
        });

        mapaPane.getChildren().add(0, grupoRuta);
        gruposRutas.put(ruta.getId(), grupoRuta);
    }

    // Metodo para crear la punta de flecha para una ruta.
    private Polygon crearPuntaFlecha(double x, double y, double angulo) {
        double longitudFlecha = 15;

        double x1 = x;
        double y1 = y;
        double x2 = x - longitudFlecha * Math.cos(angulo - Math.PI / 6);
        double y2 = y - longitudFlecha * Math.sin(angulo - Math.PI / 6);
        double x3 = x - longitudFlecha * Math.cos(angulo + Math.PI / 6);
        double y3 = y - longitudFlecha * Math.sin(angulo + Math.PI / 6);

        return new Polygon(x1, y1, x2, y2, x3, y3);
    }

    // Metodo para mostrar un cuadro con información de la ruta.
    private void mostrarInfoRuta(Ruta ruta, double x, double y) {
        if (infoBox != null) {
            mapaPane.getChildren().remove(infoBox);
        }

        infoBox = new VBox(5);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        infoBox.setPadding(new Insets(10));
        infoBox.setBackground(new Background(new BackgroundFill(
                Color.WHITE, new CornerRadii(5), Insets.EMPTY)));
        infoBox.setBorder(new Border(new BorderStroke(
                Color.DARKBLUE, BorderStrokeStyle.SOLID,
                new CornerRadii(5), new BorderWidths(2))));
        infoBox.setMaxWidth(250);

        Label titulo = new Label("INFORMACIÓN DE RUTA");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 14));
        titulo.setTextFill(Color.DARKBLUE);

        Label lblOrigen = new Label("Origen: " + ruta.getOrigen().getNombre());
        Label lblDestino = new Label("Destino: " + ruta.getDestino().getNombre());
        Label lblDistancia = new Label("Distancia: " + ruta.getDistancia() + " km");
        Label lblTiempo = new Label("Tiempo: " + ruta.getTiempoCol());
        Label lblCosto = new Label(String.format("Costo: $%.2f", ruta.getCosto()));
        Label lblPonderacion = new Label(String.format("Ponderación: %.2f", ruta.getPonderacion()));

        lblOrigen.setFont(Font.font("System", FontWeight.NORMAL, 12));
        lblDestino.setFont(Font.font("System", FontWeight.NORMAL, 12));
        lblDistancia.setFont(Font.font("System", FontWeight.NORMAL, 12));
        lblTiempo.setFont(Font.font("System", FontWeight.NORMAL, 12));
        lblCosto.setFont(Font.font("System", FontWeight.NORMAL, 12));
        lblPonderacion.setFont(Font.font("System", FontWeight.NORMAL, 12));

        Label btnCerrar = new Label("✖ Cerrar");
        btnCerrar.setFont(Font.font("System", FontWeight.BOLD, 11));
        btnCerrar.setTextFill(Color.RED);
        btnCerrar.setOnMouseClicked(e -> {
            mapaPane.getChildren().remove(infoBox);
            infoBox = null;
        });
        btnCerrar.setOnMouseEntered(e -> btnCerrar.setUnderline(true));
        btnCerrar.setOnMouseExited(e -> btnCerrar.setUnderline(false));
        btnCerrar.setAlignment(Pos.CENTER_RIGHT);
        btnCerrar.setMaxWidth(Double.MAX_VALUE);

        infoBox.getChildren().addAll(titulo, lblOrigen, lblDestino,
                lblDistancia, lblTiempo, lblCosto, lblPonderacion, btnCerrar);

        double posX = x - 50;
        double posY = y - 50;

        if (posX < 10) posX = 10;
        if (posY < 10) posY = 10;
        if (posX + 250 > mapaPane.getWidth()) posX = mapaPane.getWidth() - 260;
        if (posY + 200 > mapaPane.getHeight()) posY = mapaPane.getHeight() - 210;

        infoBox.setLayoutX(posX);
        infoBox.setLayoutY(posY);

        mapaPane.getChildren().add(infoBox);
    }

    // Metodo para resaltar una ruta con un color específico y animación
    public void resaltarRuta(Ruta ruta, String colorHex) {
        if (ruta == null) {
            return;
        }

        Group grupoRuta = gruposRutas.get(ruta.getId());
        if (grupoRuta == null) {
            return;
        }

        // Obtener la línea y la flecha del grupo
        Line linea = (Line) grupoRuta.getChildren().get(0);
        Polygon flecha = (Polygon) grupoRuta.getChildren().get(1);

        // Convertir el color hexadecimal a Color
        Color colorResaltado = Color.web(colorHex);

        // Guardar los colores originales
        Color colorOriginal = (Color) linea.getStroke();
        double opacidadOriginal = linea.getOpacity();
        double anchoOriginal = linea.getStrokeWidth();

        //Cambiar al color del resaltado
        linea.setStroke(colorResaltado);
        flecha.setFill(colorResaltado);
        linea.setStrokeWidth(6);
        linea.setOpacity(1.0);
        flecha.setOpacity(1.0);

        FadeTransition fade1 = new FadeTransition(Duration.millis(400), grupoRuta);
        fade1.setFromValue(1.0);
        fade1.setToValue(0.3);

        FadeTransition fade2 = new FadeTransition(Duration.millis(400), grupoRuta);
        fade2.setFromValue(0.3);
        fade2.setToValue(1.0);

        FadeTransition fade3 = new FadeTransition(Duration.millis(400), grupoRuta);
        fade3.setFromValue(1.0);
        fade3.setToValue(0.3);

        FadeTransition fade4 = new FadeTransition(Duration.millis(400), grupoRuta);
        fade4.setFromValue(0.3);
        fade4.setToValue(1.0);

        SequentialTransition parpadeo = new SequentialTransition(fade1, fade2, fade3, fade4);

        // Al finalizar la animacion, restaurar colores originales
        parpadeo.setOnFinished(e -> {
            linea.setStroke(colorOriginal);
            flecha.setFill(colorOriginal);
            linea.setStrokeWidth(anchoOriginal);
            linea.setOpacity(opacidadOriginal);
            flecha.setOpacity(opacidadOriginal);
        });

        parpadeo.play();
    }

    // Metodo para eliminar una estación del mapa.
    public void eliminarEstacion(UUID idEstacion) {
        FontIcon icono = iconosEstaciones.get(idEstacion);
        Label etiqueta = etiquetasEstaciones.get(idEstacion);

        if (icono != null) {
            mapaPane.getChildren().remove(icono);
            iconosEstaciones.remove(idEstacion);
        }

        if (etiqueta != null) {
            mapaPane.getChildren().remove(etiqueta);
            etiquetasEstaciones.remove(idEstacion);
        }
    }

    // Metodo para eliminar una ruta del mapa.
    public void eliminarRuta(UUID idRuta) {
        Group grupo = gruposRutas.get(idRuta);

        if (grupo != null) {
            mapaPane.getChildren().remove(grupo);
            gruposRutas.remove(idRuta);
        }
    }

    // Metodo para actualizar la visualización de una estación.
    public void actualizarEstacion(Estacion estacion) {
        eliminarEstacion(estacion.getId());
        dibujarEstacion(estacion);
    }

    // Metodo para obtener el AnchorPane del mapa.
    public AnchorPane getMapaPane() {
        return mapaPane;
    }
}