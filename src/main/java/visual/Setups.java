package visual;

import estructura.Estacion;
import estructura.Ruta;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextFormatter;

import java.io.IOException;
import java.net.URL;


/*
Clase: Setups
Objetivo: Organizar todas las llamadas de inicialización de fxml en un solo lugar, para limpieza de código.
*/
public class Setups {

    // Setup para la ventana principal.
    public static Scene setupPrincipal() throws IOException {
        URL fxmlUrl = Setups.class.getResource("/fxml/PaginaPrincipal.fxml");
        if(fxmlUrl == null){
            throw new IOException("IngresoEstaciones.fxml no encontrado.");
        }
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        setupCss(scene, "/css/principal.css");
        return scene;
    }

    // Setup para le ingreso de estaciones cuando se van a crear nuevas estaciones.
    public static Scene setupIngresoEstacion() throws IOException {
        URL fxmlUrl = Setups.class.getResource("/fxml/IngresoEstaciones.fxml");
        if(fxmlUrl == null){
            throw new IOException("IngresoEstaciones.fxml no encontrado.");
        }
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        setupCss(scene, "/css/IngresoStyle.css");
        return scene;
    }

    // Setup para el ingreso de estaciones cuando se va a modificar.
    public static Scene setupModificarEstacion(Estacion seleccionado) throws IOException {
        FXMLLoader loader = new FXMLLoader(Setups.class.getResource("/fxml/IngresoEstaciones.fxml"));
        Parent root = loader.load();
        EstacionController controller = loader.getController();
        controller.setEstacion(seleccionado);
        Scene scene = new Scene(root);
        setupCss(scene, "/css/IngresoStyle.css");
        return scene;
    }

    // Setup para el ingreso de rutas cuando se van a crear nuevas rutas.
    public static Scene setupIngresoRuta() throws IOException {
        URL fxmlUrl = Setups.class.getResource("/fxml/IngresoRuta.fxml");
        if(fxmlUrl == null){
            throw new IOException("IngresoRuta.fxml no encontrado.");
        }
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        setupCss(scene, "/css/IngresoStyle.css");
        return scene;
    }

    // Setup para el ingreso de rutas cuando se va a modificar.
    public static Scene setupModificarRuta(Ruta seleccionado) throws IOException {
        URL fxmlUrl = Setups.class.getResource("/fxml/IngresoRuta.fxml");
        if(fxmlUrl == null){
            throw new IOException("IngresoRuta.fxml no encontrado.");
        }
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();
        RutaController controller = loader.getController();
        controller.setRuta(seleccionado);
        Scene scene = new Scene(root);
        setupCss(scene, "/css/IngresoStyle.css");
        return scene;
    }

    // Setup para el listado de estaciones.
    public static Scene setupListEstacion() throws IOException{
        URL fxmlUrl = Setups.class.getResource("/fxml/ListadoEstaciones.fxml");
        if(fxmlUrl == null){
            throw new IOException("ListadoEstacion.fxml no encontrado.");
        }
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        setupCss(scene, "/css/ListadoStyle.css");
        return scene;
    }

    // Setup para el listado de rutas.
    public static Scene setupListRuta() throws IOException {
        URL fxmlUrl = Setups.class.getResource("/fxml/ListadoRutas.fxml");
        if(fxmlUrl == null){
            throw new IOException("ListadoRutas.fxml no encontrado.");
        }
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        setupCss(scene, "/css/ListadoStyle.css");
        return scene;
    }

    //Setup para la ventana de la Matriz de Distancias Mínimas.
    public static Scene setupMatriz() throws IOException {
        URL fxmlUrl = Setups.class.getResource("/fxml/Matriz.fxml");
        if(fxmlUrl == null){
            throw new IOException("Matriz.fxml no encontrado.");
        }
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        setupCss(scene, "/css/ListadoStyle.css");
        return scene;
    }

    // Setup para la búsqueda de la mejor ruta (nueva).
    public static Scene setupBusquedaRuta() throws IOException {
        URL fxmlUrl = Setups.class.getResource("/fxml/BusquedaRuta.fxml");
        if(fxmlUrl == null){
            throw new IOException("BusquedaRuta.fxml no encontrado.");
        }
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        setupCss(scene, "/css/BusquedaStyle.css");
        return scene;
    }

    public static Scene setupEstadisticas() throws IOException{
        URL fxmlUrl = Setups.class.getResource("/fxml/Estadisticas.fxml");
        if(fxmlUrl == null){
            throw new IOException("Estadisticas.fxml no encontrado.");
        }
        FXMLLoader loader  = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        setupCss(scene, "/css/EstadisticasStyle.css");
        return scene;
    }

    // Metodo para setear los css para cada ventana.
    public static void setupCss(Scene scene, String direccion) {
        URL cssUrl = Setups.class.getResource(direccion);
        if(cssUrl != null){
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }
    }

    // Metodo para llamar una alerta en el programa.
    public static void alerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    // Metodo para setear los formatters para los spinners.
    public static void setupFormatter(Spinner<Double> spinner) {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if(newText.isEmpty()) {
                spinner.getValueFactory().setValue(0D);
                return change;
            }
            if(newText.matches("-?\\d*\\.?\\d")) {
                return change;
            }
            return null;
        });
        spinner.getEditor().setTextFormatter(formatter);
    }
}