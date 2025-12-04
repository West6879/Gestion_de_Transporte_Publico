package visual;

import estructura.Servicio;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import java.io.IOException;
import java.util.*;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;
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
    @FXML private Button btnIngresarEstacion;
    @FXML private Button btnIngresarRuta;
    @FXML private Button btnBuscar;
    @FXML private Button btnLista;
    @FXML private Button btnStat;
    @FXML private Button btnOpcion;
    @FXML private Button btnMatriz; // Nuevo botón para la matriz

    // MenuItems FXML (Necesarios para la nueva funcionalidad)
    @FXML private MenuItem menuBusquedaRuta; // <-- NUEVO: Para la opción "Ruta más corta"

    /*
     Metodo de inicialización llamada automáticamente por el FXMLLoader
     después de que el FXML ha sido cargado y sus elementos inyectados.
    */
    @FXML
    public void initialize() {
        // Dibujar el mapa inicial con todas las estaciones y rutas existentes
        Platform.runLater(() -> {
            if (mapaIncludeController != null) {
                mapaIncludeController.dibujarMapaCompleto();
            }
        });
        setearIconosMenu();

        // Enlazar la acción al MenuItem de busqueda
        if (menuBusquedaRuta != null) {
            menuBusquedaRuta.setOnAction(this::mostrarBusquedaRuta);
        }
    }

    /*
    Metodo: mostrarBusquedaRuta
    Objetivo: Muestra la ventana modal para la busqueda de la mejor ruta.
    */
    @FXML
    public void mostrarBusquedaRuta(ActionEvent event) {
        try {
            // Carga el FXML de BusquedaRuta
            Scene busquedaScene = setupBusquedaRuta();
            Stage stage = new Stage();
            stage.setScene(busquedaScene);
            stage.setMaximized(true);
            stage.setTitle("Búsqueda de la Mejor Ruta");
            stage.initModality(Modality.WINDOW_MODAL);
            Image icono = new Image(Objects.requireNonNull(Main.class.getResource("/imagenes/busqueda.png")).toExternalForm());
            stage.getIcons().add(icono);
            // Obtener la ventana principal para centrar la modal
            Window owner = rootPane.getScene().getWindow();
            stage.initOwner(owner);

            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de Carga");
            alert.setHeaderText(null);
            alert.setContentText("No se pudo cargar la ventana de Búsqueda de Rutas. Verifique el archivo FXML.");
            alert.showAndWait();
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
        Image icono = new Image(Objects.requireNonNull(Main.class.getResource("/imagenes/estacion.png")).toExternalForm());
        stage.getIcons().add(icono);

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
        Image icono = new Image(Objects.requireNonNull(Main.class.getResource("/imagenes/ruta.png")).toExternalForm());
        stage.getIcons().add(icono);

        // Actualizar el mapa cuando se cierre la ventana
        stage.setOnHidden(e -> actualizarMapa());

        stage.show();
    }

    @FXML
    public void seleccionarListado(ActionEvent event) throws IOException {
        ButtonType listaEstacion = new ButtonType("Estaciones", ButtonBar.ButtonData.OK_DONE);
        ButtonType listaRuta = new ButtonType("Rutas",  ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelar  = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Elegir listado", listaEstacion, listaRuta, cancelar);
        alert.setTitle("Seleccionar listado");
        alert.setHeaderText(null);
        Optional<ButtonType> resultado = alert.showAndWait();
        if(resultado.isEmpty()) {
            return;
        }
        if (resultado.get() == listaEstacion) {
            listadoEstacion(null);
        } else if (resultado.get() == listaRuta) {
            listadoRuta(null);
        }
    }

    // Metodo para abrir la ventana de listado de estaciones.
    public void listadoEstacion(ActionEvent event) throws IOException {
        Scene scene = setupListEstacion();
        Stage stage = new Stage();
        Window owner = rootPane.getScene().getWindow();
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene(scene);
        stage.setTitle("Listado de Estaciones");
        Image icono = new Image(Objects.requireNonNull(Main.class.getResource("/imagenes/lista.png")).toExternalForm());
        stage.getIcons().add(icono);

        // Actualizar el mapa cuando se cierre la ventana
        stage.setOnHidden(e -> actualizarMapa());

        stage.show();
    }

    // Metodo para abrir la ventana de listado de rutas.
    public void listadoRuta(ActionEvent event) throws IOException {
        Scene scene = setupListRuta();
        Stage stage = new Stage();
        Window owner = rootPane.getScene().getWindow();
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene(scene);
        stage.setTitle("Listado de Rutas");
        Image icono = new Image(Objects.requireNonNull(Main.class.getResource("/imagenes/lista.png")).toExternalForm());
        stage.getIcons().add(icono);

        // Actualizar el mapa cuando se cierre la ventana
        stage.setOnHidden(e -> actualizarMapa());

        stage.show();
    }

    // Metodo para abrir la ventana de la Matriz de Distancias Mínimas.
    @FXML
    public void mostrarMatriz(ActionEvent event) throws IOException {
        Scene scene = setupMatriz(); // Se asume que esta función existe en Setups.java
        Stage stage = new Stage();
        Window owner = rootPane.getScene().getWindow();
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene(scene);
        stage.setTitle("Matriz de Distancias Mínimas");
        Image icono = new Image(Objects.requireNonNull(Main.class.getResource("/imagenes/matriz.png")).toExternalForm());
        stage.getIcons().add(icono);

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
                tipoEvento = "OCURRIÓ UN: CHOQUE";
            }
            else{tipoEvento = "OCURRIÓ UN :Evento Variado";}

            // Mostrar Alerta
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(tipoEvento);
            alert.setHeaderText("¡Ocurrió un evento en la via!");
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

    @FXML
    private void estadisticas(ActionEvent event) throws IOException {
        Scene scene = setupEstadisticas();
        Stage stage = new Stage();
        Window owner = rootPane.getScene().getWindow();
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Estadisticas");
        Image icono = new Image(Objects.requireNonNull(Main.class.getResource("/imagenes/stats.png")).toExternalForm());
        stage.getIcons().add(icono);
        stage.show();
    }

    // Metodo para setear los iconos de los botones de la barra de menu.
    private void setearIconosMenu() {
        // Agregamos btnMatriz a la lista de botones
        List<Button> listaButtons = Arrays.asList(btnIngresarEstacion, btnIngresarRuta, btnBuscar, btnLista,
                btnStat, btnOpcion, btnMatriz);

        List<FontAwesomeSolid> listaIconos = new ArrayList<>();
        listaIconos.add(FontAwesomeSolid.PLUS);
        listaIconos.add(FontAwesomeSolid.ROUTE);
        listaIconos.add(FontAwesomeSolid.SEARCH_LOCATION);
        listaIconos.add(FontAwesomeSolid.LIST);
        listaIconos.add(FontAwesomeSolid.CHART_PIE);
        listaIconos.add(FontAwesomeSolid.COG);
        listaIconos.add(FontAwesomeSolid.TABLE);

        for(int i = 0; i  < listaIconos.size(); i++) {
            FontIcon icono =  new FontIcon(listaIconos.get(i));
            icono.setIconSize(48);
            listaButtons.get(i).setGraphic(icono);
            listaButtons.get(i).setAlignment(Pos.CENTER);
            Tooltip.install(listaButtons.get(i), new Tooltip(listaButtons.get(i).getText()));
        }
    }

    //Getter público para acceder al MapaController desde otras clases si es necesario
    public MapaController getMapaController() {
        return mapaIncludeController;
    }
}