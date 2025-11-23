package visual;

import estructura.Estacion;
import estructura.Ruta;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

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
        URL cssUrl = Setups.class.getResource("/css/principal.css");
        if(cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }
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
        URL cssUrl = Setups.class.getResource("/css/EstacionStyle.css");
        if(cssUrl != null){
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }
        return scene;
    }

    // Setup para el ingreso de estaciones cuando se va a modificar.
    public static Scene setupModificarEstacion(Estacion seleccionado) throws IOException {
        FXMLLoader loader = new FXMLLoader(Setups.class.getResource("/fxml/IngresoEstaciones.fxml"));
        Parent root = loader.load();
        EstacionController controller = loader.getController();
        controller.setEstacion(seleccionado);
        Scene scene = new Scene(root);
        URL cssUrl = Setups.class.getResource("/css/EstacionStyle.css");
        if(cssUrl != null){
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }
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
        return new Scene(root);
    }

    // Setup para el ingreso de rutas cuando se van a crear nuevas rutas.
    public static Scene setupIngresoRuta() throws IOException {
        URL fxmlUrl = Setups.class.getResource("/fxml/IngresoRuta.fxml");
        if(fxmlUrl == null){
            throw new IOException("IngresoRuta.fxml no encontrado.");
        }
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();
        return new Scene(root);
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
        return new Scene(root);
    }

    // Setup para el listado de rutas.
    public static Scene setupListRuta() throws IOException {
        URL fxmlUrl = Setups.class.getResource("/fxml/ListadoRutas.fxml");
        if(fxmlUrl == null){
            throw new IOException("ListadoRutas.fxml no encontrado.");
        }
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();
        return new Scene(root);
    }

    //Setup para la ventana de la Matriz de Distancias Minimas.
    public static Scene setupMatriz() throws IOException {
        URL fxmlUrl = Setups.class.getResource("/fxml/Matriz.fxml");
        if(fxmlUrl == null){
            throw new IOException("Matriz.fxml no encontrado.");
        }
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        return scene;
    }

    // Setup para la busqueda de la mejor ruta (nueva).
    public static Scene setupBusquedaRuta() throws IOException {
        URL fxmlUrl = Setups.class.getResource("/fxml/BusquedaRuta.fxml");
        if(fxmlUrl == null){
            throw new IOException("BusquedaRuta.fxml no encontrado.");
        }
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();
        return new Scene(root);
    }
}