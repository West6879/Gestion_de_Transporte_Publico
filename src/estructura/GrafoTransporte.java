package estructura;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/*
Clase: Grafo Transporte
Objetivo: Clase objeto del grafo con funcionalidad de crear y eliminar estaciones y rutas.
*/
public class GrafoTransporte {
    Map<Estacion, List<Ruta>> web;
    private MatrizDistMinimas matrizDistancias;

    public GrafoTransporte() {
        this.web = new HashMap<>();
        this.matrizDistancias = new MatrizDistMinimas();
    }

    // Metodo para agregar una estacion o nodo
    public void agregarEstacion(Estacion estacion) {
        if(!web.containsKey(estacion)) {
            web.put(estacion, new ArrayList<>());
            matrizDistancias.agregarEstacion(estacion);
        }
    }

    // Metodo para eliminar una estacion o nodo.
    public void eliminarEstacion(Estacion estacion) {
        // Recorre el mapa para cada lista de rutas.
        for(Map.Entry<Estacion, List<Ruta>> entrada : web.entrySet()) {

            // Guarda la lista de rutas del nodo actual
            List<Ruta> rutas = entrada.getValue();

            // Elimina las rutas de la lista que tengan como destino la estacion a eliminar.
            rutas.removeIf(ruta -> ruta.getDestino().equals(estacion));
        }
        // Finalmente, elimina el nodo.
        web.remove(estacion);
    }

    // Metodo para agregar una nueva ruta o arista
    public void agregarRuta(Estacion origen, Estacion destino, int distancia, String id) {
        // Chequear si las estaciones/nodos ya existen, o añadirlos si no.
        agregarEstacion(origen);
        agregarEstacion(destino);

        Ruta nuevaRuta = new Ruta(origen, destino, distancia, id);
        web.get(origen).add(nuevaRuta);
    }

    // Metodo para eliminar una ruta.
    public void eliminarRuta(Estacion origen, Estacion destino, String id) {
        if(web.containsKey(origen)) {
            List<Ruta> rutas = web.get(origen);
            // Elimina si la ruta tiene el mismo destino y el mismo id.
            rutas.removeIf(ruta -> ruta.getDestino().equals(destino) && ruta.getId().equals(id));
        }
    }

    // Metodo para chequear si existe una conexión entre dos estaciones.
    public boolean existeRuta(Estacion origen, Estacion destino) {
        if(!web.containsKey(origen)) return false;
        for(Ruta ruta : web.get(origen)) {
            if(ruta.getDestino().equals(destino)) return true;
        }
        return false;
    }

    // Metodo para chequear si existe una ruta específica entre dos estaciones.
    public boolean existeRutaEspecifica(Estacion origen, Estacion destino, String id) {
        if(!web.containsKey(origen)) return false;
        for(Ruta ruta : web.get(origen)) {
            if(ruta.getDestino().equals(destino) && ruta.getId().equals(id)) return true;
        }
        return false;
    }

    // Cuenta la cantidad de estaciones en el grafo.
    public int contarEstaciones() {
        return web.size();
    }

    // Cuenta la cantidad de rutas que hay en la web
    public int contarRutas() {
        int totalRutas = 0;
        for (List<Ruta> rutas : web.values()) {
            totalRutas += rutas.size();
        }
        return totalRutas;
    }

    // Metodo para imprimir cada estacion y sus rutas salientes.
    public void imprimirGrafo() {
        for(Estacion estacion : web.keySet()) {
            System.out.print(estacion + " -> ");
            List<Ruta> rutas = web.get(estacion);
            if(rutas.isEmpty()) {
                System.out.print("Sin conexiones.");
            } else {
                for(int ind = 0; ind < rutas.size(); ind++) {
                    System.out.print(rutas.get(ind));
                    if(ind < rutas.size() - 1) System.out.print(", ");
                }
            }
            System.out.println();
        }
    }

    //Pasado un index devuelve la ruta en ese index identificado
    public Ruta getIndexRuta(int index){
        List<Ruta> todasLasRutas = new ArrayList<>();

        for(List<Ruta> rutas : web.values()){// Recolectar todas las rutas
            todasLasRutas.addAll(rutas);
        }

        if(index >= 0 && index < todasLasRutas.size()){// Verificar que el índice sea válido
            return todasLasRutas.get(index);
        }

        return null; // Si el índice está fuera de rango
    }

    //Metodo que actualiza el tiempo si hubo un evento.
    public void ActualizarTiempoPorEvento(int numero){
        int tamanyo = this.contarRutas(); //Cant rutas
        if(numero == 5 || numero == 2){ //Si hubo un evento
            int aristanumero = new Random().nextInt(tamanyo); //Indice de arista random
            Ruta ruta = getIndexRuta(aristanumero); //Busca la ruta con el indice
            if(ruta != null) { //Si no fue null
                ruta.setTiempo(ruta.getTiempo() * numero); //Multiplica por el valor del evento
            }
        }
    }

    // Implementación de Floyd-Warshall
    public void floydWarshall() {
        int n = matrizDistancias.getTamanyo();

        // Inicializar la matriz con infinito
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    matrizDistancias.setDistancia(i, j, 0);
                } else {
                    matrizDistancias.setDistancia(i, j, Float.POSITIVE_INFINITY);
                }
            }
        }

        // Llenar la matriz con las distancias directas de las rutas existentes
        for (int i = 0; i < n; i++) {
            Estacion origen = matrizDistancias.getEstacion(i);
            List<Ruta> rutas = web.get(origen);

            if (rutas != null) {
                for (Ruta ruta : rutas) {
                    // Buscar el índice de la estación destino
                    for (int j = 0; j < n; j++) {
                        if (matrizDistancias.getEstacion(j).equals(ruta.getDestino())) {
                            matrizDistancias.setDistancia(i, j, ruta.getDistancia());
                            break;
                        }
                    }
                }
            }
        }

        // Algoritmo de Floyd-Warshall
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    float distanciaActual = matrizDistancias.getDistancia(i, j);
                    float distanciaPorK = matrizDistancias.getDistancia(i, k) + matrizDistancias.getDistancia(k, j);

                    if (distanciaPorK < distanciaActual) {
                        matrizDistancias.setDistancia(i, j, distanciaPorK);
                    }
                }
            }
        }
    }

    // Metodo para imprimir la matriz de distancias mínimas
    public void imprimirMatrizDistancias() {
        int n = matrizDistancias.getTamanyo();

        // Imprimir encabezado de columnas
        System.out.print("           ");
        for (int j = 0; j < n; j++) {
            System.out.printf("%-10s ", matrizDistancias.getEstacion(j).getNombre());
        }
        System.out.println();

        // Imprimir filas con sus datos
        for (int i = 0; i < n; i++) {
            System.out.printf("%-10s ", matrizDistancias.getEstacion(i).getNombre());
            for (int j = 0; j < n; j++) {
                float distancia = matrizDistancias.getDistancia(i, j);
                if (distancia == Float.POSITIVE_INFINITY) {
                    System.out.print("∞          ");
                } else {
                    System.out.printf("%-10.1f ", distancia);
                }
            }
            System.out.println();
        }
    }

    // Metodo para obtener la distancia mínima entre dos estaciones
    public float getDistanciaMinima(Estacion origen, Estacion destino) {
        int n = matrizDistancias.getTamanyo();
        int indiceOrigen = -1;
        int indiceDestino = -1;

        // Buscar los índices de origen y destino
        for (int i = 0; i < n; i++) {
            if (matrizDistancias.getEstacion(i).equals(origen)) {
                indiceOrigen = i;
            }
            if (matrizDistancias.getEstacion(i).equals(destino)) {
                indiceDestino = i;
            }
        }

        if (indiceOrigen != -1 && indiceDestino != -1) {
            return matrizDistancias.getDistancia(indiceOrigen, indiceDestino);
        }

        return Float.POSITIVE_INFINITY;
    }

    public Map<Estacion, List<Ruta>> getWeb() {
        return web;
    }

    public MatrizDistMinimas getMatrizDistancias() {
        return matrizDistancias;
    }
}