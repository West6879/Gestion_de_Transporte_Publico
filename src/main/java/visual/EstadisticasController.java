package visual;

import estructura.Estacion;
import estructura.Servicio;
import estructura.TipoEstacion;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

import java.util.Map;

public class EstadisticasController {
    @FXML private PieChart tiposPieChart;
    @FXML private PieChart costoPieChart;
    @FXML private BarChart<?, ?> masRutasBarChart;

    @FXML
    public void initialize(){
        Servicio instancia = Servicio.getInstance();
        ObservableList<PieChart.Data> dataTipo = FXCollections.observableArrayList(
                new PieChart.Data("Tren", instancia.cantidadTipoEstaciones(TipoEstacion.TREN)),
                new PieChart.Data("Metro", instancia.cantidadTipoEstaciones(TipoEstacion.METRO)),
                new PieChart.Data("Bus", instancia.cantidadTipoEstaciones(TipoEstacion.BUS))
        );
        dataTipo.forEach(data -> data.nameProperty().bind(
                Bindings.concat(
                    data.getName(), " Total: ", data.pieValueProperty()
                )
        ));

        tiposPieChart.setData(dataTipo);
        tiposPieChart.setLabelsVisible(false);

        ObservableList<PieChart.Data> dataCosto = FXCollections.observableArrayList(
                new PieChart.Data("500 > 0", instancia.cantRutasCosto(0, 500)),
                new PieChart.Data("1000 > 500", instancia.cantRutasCosto(500, 1000)),
                new PieChart.Data("2000 > 1000", instancia.cantRutasCosto(1000, 2000)),
                new PieChart.Data("2000+", instancia.cantRutasCosto(2000, Double.MAX_VALUE))
        );

        costoPieChart.setData(dataCosto);
        costoPieChart.setLabelsVisible(false);

        XYChart.Series seriesRutasSalientes = new XYChart.Series();
        seriesRutasSalientes.setName("Rutas Salientes");
        XYChart.Series seriesRutasEntrantes = new XYChart.Series();
        seriesRutasEntrantes.setName("Rutas Entrantes");

        // Conseguir el top 7 estaciones con más cantidad de rutas.
        Map<Estacion, Integer> masRutas = instancia.estacionesConMasRutas();

        for(Map.Entry<Estacion, Integer> entry : masRutas.entrySet()){
            // Setear las rutas salientes.
            int rutasSalientes = Servicio.getInstance().getMapa().rutasSalientesPorEstacion(entry.getKey());
            seriesRutasSalientes.getData().add(new XYChart.Data(entry.getKey().getNombre(), rutasSalientes));
            // Restarle al total las rutas salientes para conseguir las rutas entrantes.
            int rutasEntrantes = entry.getValue() - rutasSalientes;
            if(rutasEntrantes >= 0) {
                seriesRutasEntrantes.getData().add(new XYChart.Data(entry.getKey().getNombre(), rutasEntrantes));
            } else {
                // Si da un numero negativo significa que no hay rutas entrantes.
                seriesRutasEntrantes.getData().add(new XYChart.Data(entry.getKey().getNombre(), 0));
            }

        }

        masRutasBarChart.getData().addAll(seriesRutasSalientes,  seriesRutasEntrantes);


    }

}
