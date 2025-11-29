package visual;


import estructura.GrafoTransporte;
import estructura.MatrizDistMinimas;
import estructura.Servicio; // Importar Servicio

import util.FloydWarshall;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


/*
Clase: MatrizController
Objetivo: Controla la vista de la matriz.
1. Carga el grafo existente del Servicio.
2. Aplica el algoritmo de Floyd-Warshall.
3. Muestra la matriz de distancias mínimas en la Tabla (TableView).
*/
public class MatrizController {

    @FXML
    private TableView<ObservableList<String>> TablaMatriz;

    @FXML
    private TableColumn<ObservableList<String>, String> columnaNodos;

    private GrafoTransporte grafo;

    @FXML
    public void initialize() {
        // La columna "NODOS" mostrara el nombre de la estacion de Origen (indice 0 en la lista de la fila).
        columnaNodos.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(0)));
        columnaNodos.setMinWidth(100);

        TablaMatriz.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // Opcional, pero bueno para ajustar el tamaño.
        TablaMatriz.getColumns().forEach(column -> column.setReorderable(false));

        //Establece un Cell Factory para aplicar negrita
        columnaNodos.setCellFactory(column -> new javafx.scene.control.TableCell<ObservableList<String>, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    // Aplica el estilo CSS para negrita
                    this.setStyle("-fx-font-weight: bold;");
                }
            }
        });

        //Cargar el grafo existente
        cargarGrafoExistente();

        //Calcular y mostrar la matriz solo si hay estaciones
        if (grafo.contarEstaciones() > 0) {
            calcularYMostrarMatriz();
        } else {
            //Si no hay datos
            TablaMatriz.setPlaceholder(new javafx.scene.control.Label("No hay estaciones ni rutas para calcular la matriz."));
        }
    }

    /*
    Metodo de CARGA: Obtiene el GrafoTransporte de la instancia de Servicio.
    */
    private void cargarGrafoExistente() {
        this.grafo = Servicio.getInstance().getMapa();
        // El grafo ya se carga en el Servicio al inicio.
    }

    /*
    Metodo principal:
    Ejecuta Floyd-Warshall.
    Crea las columnas (Estaciones Destino).
    Llena la tabla con los valores de la matriz.
    */
    private void calcularYMostrarMatriz() {
        if (grafo == null) return;

        // Calcular las distancias mínimas (basadas en distancia)
        MatrizDistMinimas matrizResultante = FloydWarshall.calcularDistanciasMinimas(grafo);
        int n = matrizResultante.getTamanyo();

        // Limpiar columnas previas (excepto la primera que es 'NODOS')
        while (TablaMatriz.getColumns().size() > 1) {
            TablaMatriz.getColumns().remove(1);
        }

        //Crear y añadir las columnas de Destino a la tabla
        for (int j = 0; j < n; j++) {
            // El encabezado de la columna será el nombre de la estacion de Destino
            TableColumn<ObservableList<String>, String> nuevaColumna =
                    new TableColumn<>(matrizResultante.getEstacion(j).getNombre());

            // ColIndex es la posicion del dato en la lista de la fila (1, 2, 3...)
            final int colIndex = j + 1;

            // Definir como obtener el valor: del índice 'colIndex' de la lista de strings de la fila
            nuevaColumna.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(colIndex)));
            nuevaColumna.setMinWidth(80);
            TablaMatriz.getColumns().add(nuevaColumna);
        }

        //Preparar los datos fila por fila
        ObservableList<ObservableList<String>> datosTabla = FXCollections.observableArrayList();

        for (int i = 0; i < n; i++) {
            // Una lista para todos los valores de una fila
            ObservableList<String> fila = FXCollections.observableArrayList();

            // Primer elemento (Índice 0): Nombre de la estacion de Origen (para la columna "NODOS")
            fila.add(matrizResultante.getEstacion(i).getNombre());

            // Distancias calculadas (índices 1 a n)
            for (int j = 0; j < n; j++) {
                float distancia = matrizResultante.getDistancia(i, j);
                String valor;

                // Formateo: 'inf' si es infinito, o el valor con un decimal
                if (distancia == Float.POSITIVE_INFINITY) {
                    valor = "inf"; // Usar 'inf' en lugar de 'infinito' o el símbolo 'oo'
                } else {
                    valor = String.format("%.1f", distancia);
                }
                fila.add(valor);
            }

            datosTabla.add(fila);
        }

        //Establecer todos los datos en la tabla
        TablaMatriz.setItems(datosTabla);
    }
}