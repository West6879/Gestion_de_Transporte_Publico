package util;

import estructura.Estacion;
import estructura.GrafoTransporte;
import estructura.ResultadoRuta;
import estructura.Ruta;

import java.util.*;

import static util.Caminos.*;


/*
Clase: Bellman_Ford
Objetivo: Implementación del algoritmo de Bellman_Ford para calcular la ruta más barata.
*/
public class Bellman_Ford {

    // Metodo para encontrar la ruta más barata de una estacion a otra.
    public static ResultadoRuta bellmanFordBusqueda(GrafoTransporte grafo, Estacion origen, Estacion destino) {
        int cantVertices = grafo.contarEstaciones();

        // Inicialización de costos y predecesores.
        Map<Estacion, Double> costos = new HashMap<>();
        Map<Estacion, Estacion> predecesores = new HashMap<>();
        Map<Estacion, UUID> lineaAnterior = new HashMap<>();
        Map<Estacion, Integer> transbordos = new HashMap<>();

        for(Estacion estacion : grafo.getWeb().keySet()) {
            costos.put(estacion, Double.MAX_VALUE);
            predecesores.put(estacion, null);
            transbordos.put(estacion, Integer.MAX_VALUE);
        }
        costos.put(origen, 0D);
        transbordos.put(origen, 0);

        // Lista de todas las rutas.
        List<Ruta> todasLasRutas = new ArrayList<>();
        for(Estacion estacion : grafo.getWeb().keySet()) {
            todasLasRutas.addAll(grafo.getWeb().get(estacion));
        }

        // Recorre todas las rutas una cantidad de Vertices - 1 veces.
        for(int i = 1; i < cantVertices; i++) {
            boolean huboActualizacion = false; // Boolean para chequear si hubo cambios.
            for(Ruta ruta : todasLasRutas) {
                // Datos de la ruta.
                Estacion inicio = ruta.getOrigen();
                Estacion fin = ruta.getDestino();
                Double costo = ruta.getCosto();

                // Validación del costo del inicio más la ruta sea menor al costo del destino.
                if(costos.get(inicio) != Double.MAX_VALUE && costos.get(inicio) + costo < costos.get(fin)) {
                    // Calculo de transbordos.
                    int nuevosTransbordos = transbordos.get(inicio);
                    UUID lineaPrevia = lineaAnterior.get(inicio);
                    if(lineaPrevia != null && !lineaPrevia.equals(ruta.getId())) {
                        nuevosTransbordos++;
                    }

                    // Actualizacion de datos.
                    costos.put(fin, costos.get(inicio) + costo);
                    predecesores.put(fin, inicio);
                    transbordos.put(fin, nuevosTransbordos);
                    lineaAnterior.put(fin, ruta.getId());
                    huboActualizacion = true;
                }
            }
            if(!huboActualizacion) break;
        }

        for(Ruta ruta : todasLasRutas) {
            Estacion inicio = ruta.getOrigen();
            Estacion fin = ruta.getDestino();
            Double costo = ruta.getCosto();
            if(costos.get(inicio) != Double.MAX_VALUE && costos.get(inicio) + costo < costos.get(fin)) {
                throw new IllegalStateException("El grafo tiene una ruta con costo negativo.");
            }
        }

        return finalizacionRuta(grafo, predecesores, transbordos, origen, destino);
    }


}
