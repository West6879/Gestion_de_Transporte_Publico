package util;

import estructura.Estacion;
import estructura.GrafoTransporte;
import estructura.MatrizDistMinimas;
import java.util.List;
import estructura.Ruta;
import javafx.scene.control.TextArea;

/*
Clase: FloydWarshall
Objetivo: Clase utilidad para emplear el algoritmo de Floyd-Warshall en el sistema.
El algoritmo encuentra las distancias mínimas entre todos los pares de estaciones.
*/
public class FloydWarshall {

    // Metodo para ejecutar el algoritmo de Floyd-Warshall
    public static MatrizDistMinimas calcularDistanciasMinimas(GrafoTransporte grafo) {
        // Crear la matriz que almacenara las distancias mínimas
        MatrizDistMinimas matrizDistancias = new MatrizDistMinimas();

        // Esto asigna un índice a cada estacion
        for (Estacion estacion : grafo.getWeb().keySet()) {
            matrizDistancias.agregarEstacion(estacion);
        }

        // Obtener el número total de estaciones
        int n = matrizDistancias.getTamanyo();

        // Recorremos toda la matriz para establecer valores iniciales
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    // La distancia de un nodo a si mismo es 0
                    matrizDistancias.setDistancia(i, j, 0);
                } else {
                    // Inicialmente, la distancia entre nodos diferentes es infinito
                    // (asumimos que no hay conexión directa hasta que se demuestre lo contrario)
                    matrizDistancias.setDistancia(i, j, Float.POSITIVE_INFINITY);
                }
            }
        }

        // Recorremos cada estacion origen
        for (int i = 0; i < n; i++) {
            Estacion origen = matrizDistancias.getEstacion(i);
            List<Ruta> rutas = grafo.getWeb().get(origen);

            // Si la estación tiene rutas salientes
            if (rutas != null) {
                // Para cada ruta (arista) que sale de esta estación
                for (Ruta ruta : rutas) {
                    // Buscar el índice de la estación destino en la matriz
                    for (int j = 0; j < n; j++) {
                        if (matrizDistancias.getEstacion(j).equals(ruta.getDestino())) {
                            // Establecer la distancia directa entre origen i y destino j
                            matrizDistancias.setDistancia(i, j, ruta.getDistancia());
                            break;
                        }
                    }
                }
            }
        }

        // Verifica si es más corto ir directo de i a j, o pasar por un nodo intermedio k.

        // k es el nodo intermedio que estamos considerando
        for (int k = 0; k < n; k++) {
            // i es el nodo origen
            for (int i = 0; i < n; i++) {
                // j es el nodo destino
                for (int j = 0; j < n; j++) {
                    // Obtener la distancia actual de i a j (puede ser directa o ya optimizada)
                    float distanciaActual = matrizDistancias.getDistancia(i, j);

                    // Calcular la distancia si pasamos por k: dist(i,k) + dist(k,j)
                    float distanciaPorK = matrizDistancias.getDistancia(i, k) + matrizDistancias.getDistancia(k, j);

                    // Si pasar por k es más corto, actualizamos la distancia
                    if (distanciaPorK < distanciaActual) {
                        matrizDistancias.setDistancia(i, j, distanciaPorK);
                    }
                    // Al terminar todos los k, tendremos las distancias mínimas entre todos los pares
                }
            }
        }

        // Retornar la matriz con todas las distancias mínimas calculadas
        return matrizDistancias;
    }

    // Metodo para imprimir la matriz de distancias mínimas de forma legible
    public static void imprimirMatrizDistancias(MatrizDistMinimas matrizDistancias) {
        int n = matrizDistancias.getTamanyo();

        // Imprimir encabezado de columnas (nombres de estaciones destino)
        System.out.print("           ");
        for (int j = 0; j < n; j++) {
            System.out.printf("%-10s ", matrizDistancias.getEstacion(j).getNombre());
        }
        System.out.println();

        // Imprimir cada fila con el nombre de la estacion origen y sus distancias
        for (int i = 0; i < n; i++) {
            // Imprimir nombre de estacion origen (fila)
            System.out.printf("%-10s ", matrizDistancias.getEstacion(i).getNombre());

            // Imprimir distancias a cada destino
            for (int j = 0; j < n; j++) {
                float distancia = matrizDistancias.getDistancia(i, j);

                // Si la distancia es infinito, no hay camino posible
                if (distancia == Float.POSITIVE_INFINITY) {
                    System.out.print("∞          ");
                } else {
                    // Imprimir distancia con un decimal
                    System.out.printf("%-10.1f ", distancia);
                }
            }
            System.out.println();
        }
    }

    // Metodo para obtener la distancia mínima entre dos estaciones específicas
    public static float getDistanciaMinima(MatrizDistMinimas matrizDistancias, Estacion origen, Estacion destino) {
        int n = matrizDistancias.getTamanyo();
        int indiceOrigen = -1;
        int indiceDestino = -1;

        // Buscar el índice de la estación origen en la matriz
        for (int i = 0; i < n; i++) {
            if (matrizDistancias.getEstacion(i).equals(origen)) {
                indiceOrigen = i;
            }
            // Buscar el índice de la estación destino en la matriz
            if (matrizDistancias.getEstacion(i).equals(destino)) {
                indiceDestino = i;
            }
        }

        // Si ambas estaciones existen en la matriz
        if (indiceOrigen != -1 && indiceDestino != -1) {
            // Retornar la distancia mínima precalculada.
            return matrizDistancias.getDistancia(indiceOrigen, indiceDestino);
        }

        // Si alguna estación no existe, retornar infinito (no hay camino)
        return Float.POSITIVE_INFINITY;
    }

    public static void imprimirMatrizDistanciasEnTextoArea(MatrizDistMinimas matrizDistancias, TextArea txtArea) {
        int n = matrizDistancias.getTamanyo();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-15s", ""));
        for (int j = 0; j < n; j++) {
            sb.append(String.format("%-15s", matrizDistancias.getEstacion(j).getNombre()));
        }
        sb.append("\n");
        for (int i = 0; i < n; i++) {
            sb.append(String.format("%-15s", matrizDistancias.getEstacion(i).getNombre()));
            for (int j = 0; j < n; j++) {
                float distancia = matrizDistancias.getDistancia(i, j);
                if (distancia == Float.POSITIVE_INFINITY) {
                    sb.append(String.format("%-15s", "∞"));
                } else {
                    sb.append(String.format("%-15.1f", distancia));
                }
            }
            sb.append("\n");
        }
        txtArea.setText(sb.toString());
    }
}