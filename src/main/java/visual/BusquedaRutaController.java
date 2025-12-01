package visual;

import estructura.Estacion;
import estructura.GrafoTransporte;
import estructura.ResultadoRuta;
import estructura.Servicio;
import estructura.Ruta;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import util.Dijkstra;
import util.Dijkstra.Criterio;

import java.io.IOException;
import java.util.*;

/*
Clase: BusquedaRutaController
Objetivo: Controladora para manejar la logica y visualizacion de la busqueda de la
          mejor ruta en el sistema de transporte.
*/
public class BusquedaRutaController {

    // Componentes FXML de la interfaz de usuario
    @FXML
    private ComboBox<Estacion> cmbOrigen;
    @FXML
    private ComboBox<Estacion> cmbDestino;
    @FXML
    private ComboBox<String> cmbTopRutas;
    // ELIMINADO: @FXML private Button btnSeleccionarNodos;
    @FXML
    private Label lblRutaDetalles;
    @FXML
    private AnchorPane mapaInclude;

    // Botones de busqueda rápida
    @FXML
    private Button btnBusquedaCosto;
    @FXML
    private Button btnBusquedaDistancia;
    @FXML
    private Button btnBusquedaTiempo;


    // Controlador del mapa
    private MapaController mapaController;

    // Datos del sistema
    private GrafoTransporte grafo;
    private List<Estacion> estacionesDisponibles;
    private List<ResultadoRuta> top3Rutas;
    private Criterio criterioActual;

    // Color para resaltar rutas
    private static final String COLOR_RUTA = "#0000FF";

    // Metodo de inicialización fxml.
    @FXML
    public void initialize() {
        // Inicializar el grafo y las estaciones
        Servicio servicio = Servicio.getInstance();
        grafo = servicio.getMapa();
        estacionesDisponibles = new ArrayList<>(servicio.getEstaciones().values());

        // Configurar ComboBox de Origen y Destino
        configurarComboBoxEstaciones();

        // Cargar el mapa
        cargarMapa();

        // Configurar acciones de los botones de busqueda rapida
        configurarBotonesBusquedaRapida();

        // Configurar el listener del ComboBox de Top 3 Rutas
        configurarComboBoxTopRutas();

        // ELIMINADO: Dejar el botón de selección de nodos sin funcionalidad
        // ELIMINADO: btnSeleccionarNodos.setDisable(false);

        // Limpiar detalles y top rutas al inicio
        lblRutaDetalles.setText("Seleccione Origen y Destino y un criterio de búsqueda.");
        cmbTopRutas.setDisable(true);
    }

    // Carga el MapaController y su vista FXML.
    private void cargarMapa() {
        try {
            // Carga el FXML del mapa
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Mapa.fxml"));
            AnchorPane mapaPane = fxmlLoader.load();
            mapaController = fxmlLoader.getController();

            // Ajusta el tamano del mapa al contenedor
            AnchorPane.setTopAnchor(mapaPane, 0.0);
            AnchorPane.setBottomAnchor(mapaPane, 0.0);
            AnchorPane.setLeftAnchor(mapaPane, 0.0);
            AnchorPane.setRightAnchor(mapaPane, 0.0);

            // Anade el mapa al contenedor
            mapaInclude.getChildren().setAll(mapaPane);
        } catch (IOException e) {
            e.printStackTrace();
            lblRutaDetalles.setText("Error al cargar el mapa: " + e.getMessage());
        }
    }

    // Configura los ComboBox de Origen y Destino.
    private void configurarComboBoxEstaciones() {
        ObservableList<Estacion> estacionesObservable = FXCollections.observableArrayList(estacionesDisponibles);
        cmbOrigen.setItems(estacionesObservable);
        cmbDestino.setItems(estacionesObservable);

        // Listener para la busqueda automatica al cambiar Origen/Destino
        ChangeListener<Estacion> busquedaListener = (obs, oldVal, newVal) -> {
            if (cmbOrigen.getValue() != null && cmbDestino.getValue() != null && criterioActual != null) {
                realizarBusqueda(criterioActual);
            }
        };

        cmbOrigen.valueProperty().addListener(busquedaListener);
        cmbDestino.valueProperty().addListener(busquedaListener);
    }

    // Configura los botones de busqueda rapida.
    private void configurarBotonesBusquedaRapida() {
        // Asocia cada boton con el criterio de busqueda
        btnBusquedaCosto.setOnAction(e -> realizarBusqueda(Criterio.COSTO));
        btnBusquedaDistancia.setOnAction(e -> realizarBusqueda(Criterio.DISTANCIA));
        btnBusquedaTiempo.setOnAction(e -> realizarBusqueda(Criterio.TIEMPO));
    }

    // Ejecuta la busqueda de rutas Top 3.
    private void realizarBusqueda(Criterio criterio) {
        Estacion origen = cmbOrigen.getValue();
        Estacion destino = cmbDestino.getValue();

        // Validaciones basicas
        if (origen == null || destino == null || origen.equals(destino)) {
            if (origen == null || destino == null) {
                lblRutaDetalles.setText("Debe seleccionar una estacion de Origen y Destino.");
            } else {
                lblRutaDetalles.setText("Origen y Destino no pueden ser la misma estacion.");
            }
            cmbTopRutas.setDisable(true);
            mapaController.dibujarMapaCompleto();
            return;
        }

        try {
            criterioActual = criterio;
            // Ejecutar el algoritmo de Dijkstra para el Top 3
            top3Rutas = Dijkstra.EncontrarTop3Rutas(grafo, origen, destino, criterio);

            if (top3Rutas == null || top3Rutas.isEmpty()) {
                lblRutaDetalles.setText("No se encontro ninguna ruta entre " + origen.getNombre() + " y " + destino.getNombre() + " con el criterio " + criterio.name() + ".");
                cmbTopRutas.setDisable(true);
                cmbTopRutas.setItems(FXCollections.emptyObservableList());
                mapaController.dibujarMapaCompleto();
            } else {
                actualizarResultadosBusqueda(criterio);
            }
        } catch (Exception e) {
            e.printStackTrace();
            lblRutaDetalles.setText("Error en la busqueda de rutas: " + e.getMessage());
            cmbTopRutas.setDisable(true);
            cmbTopRutas.setItems(FXCollections.emptyObservableList());
            mapaController.dibujarMapaCompleto();
        }
    }

    // Procesa los resultados del Top 3 y actualiza el ComboBox.
    private void actualizarResultadosBusqueda(Criterio criterio) {
        ObservableList<String> opcionesRutas = FXCollections.observableArrayList();
        for (int i = 0; i < top3Rutas.size(); i++) {
            ResultadoRuta resultado = top3Rutas.get(i);
            String etiqueta;
            String valorFormato = obtenerValorFormato(resultado, criterio);

            if (i == 0) {
                etiqueta = "Mejor Ruta (" + valorFormato + ")";
            } else {
                etiqueta = "Ruta #" + (i + 1) + " (" + valorFormato + ")";
            }
            opcionesRutas.add(etiqueta);
        }

        cmbTopRutas.setItems(opcionesRutas);
        cmbTopRutas.getSelectionModel().selectFirst();
        cmbTopRutas.setDisable(false);

        // Muestra detalles y resalta la mejor ruta por defecto
        mostrarDetallesRuta(top3Rutas.getFirst());
        resaltarRuta(top3Rutas.getFirst());
    }

    // Configura el listener para el ComboBox de Top Rutas.
    private void configurarComboBoxTopRutas() {
        cmbTopRutas.valueProperty().addListener((obs, oldVal, newVal) -> {
            int index = cmbTopRutas.getSelectionModel().getSelectedIndex();
            if (index >= 0 && index < top3Rutas.size()) {
                ResultadoRuta rutaSeleccionada = top3Rutas.get(index);
                mostrarDetallesRuta(rutaSeleccionada);
                resaltarRuta(rutaSeleccionada);
            }
        });
    }

    // Muestra los detalles de la ruta en el Label.
    private void mostrarDetallesRuta(ResultadoRuta resultado) {
        // Formatear la secuencia de estaciones
        StringBuilder sb = new StringBuilder();
        sb.append("Secuencia de Estaciones:\n");
        for (int i = 0; i < resultado.getCamino().size(); i++) {
            sb.append("  ").append(i + 1).append(". ").append(resultado.getCamino().get(i).getNombre());
            if (i < resultado.getCamino().size() - 1) {
                sb.append(" ->\n");
            }
        }
        sb.append("\n");

        // Agregar las métricas
        sb.append("--- Métricas Totales ---\n");
        sb.append(String.format("Distancia Total: %.2f km\n", resultado.getDistanciaTotal()));

        int horas = (int) resultado.getTiempoTotal();
        double minutos = (resultado.getTiempoTotal() - horas) * 60;
        sb.append(String.format("Tiempo Estimado: %02d:%02.0f\n", horas, minutos));

        sb.append(String.format("Costo Estimado: $%.2f\n", resultado.getCostoTotal()));
        sb.append("Transbordos: ").append(resultado.getTransbordos()).append("\n");

        lblRutaDetalles.setText(sb.toString());
    }

    // Resalta la ruta seleccionada en el mapa.
    private void resaltarRuta(ResultadoRuta resultado) {
        // Limpia el mapa de resaltados
        mapaController.dibujarMapaCompleto();

        // Resalta cada ruta individual que compone el camino
        List<Estacion> camino = resultado.getCamino();

        // Recorre la lista de estaciones para obtener las rutas entre ellas
        for (int i = 0; i < camino.size() - 1; i++) {
            Estacion origen = camino.get(i);
            Estacion destino = camino.get(i + 1);

            // Busca la ruta especifica en el grafo
            for (Ruta ruta : grafo.getWeb().get(origen)) {
                if (ruta.getDestino().equals(destino)) {
                    // Resalta la ruta con color azul brillante
                    mapaController.resaltarRuta(ruta, COLOR_RUTA);
                    break;
                }
            }
        }
    }

    // Obtiene una cadena de texto formateada para el valor principal del criterio.
    private String obtenerValorFormato(ResultadoRuta resultado, Criterio criterio) {
        switch (criterio) {
            case DISTANCIA:
                return String.format("%.2f km", resultado.getDistanciaTotal());
            case TIEMPO:
                int horas = (int) resultado.getTiempoTotal();
                double minutos = (resultado.getTiempoTotal() - horas) * 60;
                return String.format("%02d:%02.0f", horas, minutos);
            case COSTO:
                return String.format("$%.2f", resultado.getCostoTotal());
            case TRANSBORDOS:
                return resultado.getTransbordos() + " Transbordos";
            default:
                return "N/A";
        }
    }
}