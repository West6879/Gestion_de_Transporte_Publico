/*package visual;

import estructura.Estacion;
import estructura.GrafoTransporte;
import estructura.ResultadoRuta;
import estructura.Servicio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import util.Bellman_Ford;
import util.Dijkstra;
import util.FloydWarshall;
import util.Randomizacion;

import java.net.URL;
import java.util.ResourceBundle;

public class BusquedaRutaController implements Initializable {

    @FXML private ComboBox<Estacion> cmbOrigen;
    @FXML private ComboBox<Estacion> cmbDestino;
    @FXML private TextArea txtResultado;

    private Servicio servicio;
    private GrafoTransporte grafo;
    private ObservableList<Estacion> estaciones;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        servicio = Servicio.getInstance();
        grafo = servicio.getMapa();
        cargarEstaciones();
    }

    private void cargarEstaciones() {
        estaciones = FXCollections.observableArrayList(servicio.getEstaciones());
        cmbOrigen.setItems(estaciones);
        cmbDestino.setItems(estaciones);
    }

    private boolean validarSeleccion() {
        if (cmbOrigen.getValue() == null || cmbDestino.getValue() == null) {
            mostrarAlerta("Error", "Debe seleccionar estacion origen y destino");
            return false;
        }
        if (cmbOrigen.getValue().equals(cmbDestino.getValue())) {
            mostrarAlerta("Error", "La estacion origen y destino deben ser diferentes");
            return false;
        }
        return true;
    }

    @FXML
    private void buscarRutaMasCorta() {
        if (!validarSeleccion()) return;
        try {
            ResultadoRuta resultado = Dijkstra.EncontrarMejorRuta(grafo, cmbOrigen.getValue(), cmbDestino.getValue(), Dijkstra.Criterio.DISTANCIA);
            mostrarResultado(resultado, "RUTA MAS CORTA (Distancia)");
        } catch (Exception e) {
            mostrarError("Error al buscar ruta mas corta: " + e.getMessage());
        }
    }

    @FXML
    private void buscarMenosCambios() {
        if (!validarSeleccion()) return;
        try {
            ResultadoRuta resultado = Dijkstra.EncontrarMejorRuta(grafo, cmbOrigen.getValue(), cmbDestino.getValue(), Dijkstra.Criterio.TRANSBORDOS);
            mostrarResultado(resultado, "RUTA CON MENOS CAMBIOS DE CARRO");
        } catch (Exception e) {
            mostrarError("Error al buscar ruta con menos cambios: " + e.getMessage());
        }
    }

    @FXML
    private void buscarRutaRapida() {
        if (!validarSeleccion()) return;
        try {
            ResultadoRuta resultado = Dijkstra.EncontrarMejorRuta(grafo, cmbOrigen.getValue(), cmbDestino.getValue(), Dijkstra.Criterio.TIEMPO);
            mostrarResultado(resultado, "RUTA MAS RAPIDA");
        } catch (Exception e) {
            mostrarError("Error al buscar ruta mas rapida: " + e.getMessage());
        }
    }

    @FXML
    private void buscarRutaBarata() {
        if (!validarSeleccion()) return;
        try {
            ResultadoRuta resultado = Bellman_Ford.bellmanFordBusqueda(grafo, cmbOrigen.getValue(), cmbDestino.getValue());
            mostrarResultado(resultado, "RUTA MAS BARATA (Costo)");
        } catch (Exception e) {
            mostrarError("Error al buscar ruta mas barata: " + e.getMessage());
        }
    }

    @FXML
    private void mostrarMatrizDistancias() {
        try {
            txtResultado.setText(""); // Limpiar primero
            var matriz = FloydWarshall.calcularDistanciasMinimas(grafo);
            FloydWarshall.imprimirMatrizDistanciasEnTextoArea(matriz, txtResultado);
        } catch (Exception e) {
            mostrarError("Error al generar matriz de distancias: " + e.getMessage());
        }
    }

    @FXML
    private void simularEvento() {
        try {
            int evento = Randomizacion.calcularEvento();
            StringBuilder sb = new StringBuilder();
            sb.append("SIMULACION DE EVENTO\n");
            sb.append("====================\n\n");

            switch (evento) {
                case Randomizacion.CHOQUE:
                    sb.append("🚨 EVENTO: CHOQUE DETECTADO\n");
                    sb.append("Se ha producido un choque en una ruta.\n");
                    break;
                case Randomizacion.HUBO_EVENTO:
                    sb.append("⚠️ EVENTO: INCIDENTE MENOR\n");
                    sb.append("Se ha producido un incidente menor en una ruta.\n");
                    break;
                case Randomizacion.NO_HUBO_EVENTO:
                    sb.append("✅ SIN EVENTOS\n");
                    sb.append("No se han producido eventos en las rutas.\n");
                    break;
            }

            grafo.ActualizarTiempoPorEvento(evento);
            sb.append("\nLos tiempos de viaje han sido actualizados segun el evento.");

            txtResultado.setText(sb.toString());
        } catch (Exception e) {
            mostrarError("Error al simular evento: " + e.getMessage());
        }
    }

    private void mostrarResultado(ResultadoRuta resultado, String titulo) {
        if (resultado == null) {
            txtResultado.setText("No se encontro una ruta entre las estaciones seleccionadas.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(titulo).append("\n");
        sb.append("=").append("=".repeat(titulo.length())).append("\n\n");

        sb.append("🏁 CAMINO (nombres): ");
        for (int i = 0; i < resultado.getCamino().size(); i++) {
            if (i > 0) sb.append(" → ");
            sb.append(resultado.getCamino().get(i).getNombre());
        }
        sb.append("\n\n");

        sb.append("📊 METRICAS:\n");
        sb.append(String.format("  • Distancia total: %.2f km\n", resultado.getDistanciaTotal()));
        sb.append(String.format("  • Tiempo total: %d minutos\n", resultado.getTiempoTotal()));
        sb.append(String.format("  • Costo total: $%.2f\n", resultado.getCostoTotal()));
        sb.append(String.format("  • Transbordos: %d\n", resultado.getTransbordos()));

        txtResultado.setText(sb.toString());
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}*/