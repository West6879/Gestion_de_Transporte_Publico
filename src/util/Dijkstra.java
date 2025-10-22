package util;

import estructura.Estacion;
import estructura.GrafoTransporte;
import estructura.ResultadoRuta;
import estructura.Ruta;

import java.util.*;

import static util.Caminos.*;

/*
Clase: Dijkstra
Objetivo: Clase utilidad para emplear el algoritmo de dijkstra en el sistema.
*/
public class Dijkstra {

    // Metodo para encontrar las mejores rutas de una estacion a otra.
    public static ResultadoRuta EncontrarMejoresRutas(GrafoTransporte grafo, Estacion origen, Estacion destino) {
        // Validación de estaciones existentes
        if (!grafo.getWeb().containsKey(origen) || !grafo.getWeb().containsKey(destino) || origen.equals(destino)) {
            return null;
        }

        // Estructuras para el algoritmo
        Map<Estacion, Float> ponderaciones = new HashMap<>();
        Map<Estacion, Estacion> predecesores = new HashMap<>();
        Map<Estacion, String> lineaAnterior = new HashMap<>();
        Map<Estacion, Integer> transbordos = new HashMap<>();

        // Inicialización de valores
        for (Estacion estacion : grafo.getWeb().keySet()) {
            ponderaciones.put(estacion, Float.MAX_VALUE);
            transbordos.put(estacion, Integer.MAX_VALUE);
        }
        ponderaciones.put(origen, 0.0f);
        transbordos.put(origen, 0);

        // Configuración de la cola de prioridad
        PriorityQueue<Estacion> cola = new PriorityQueue<>(
                Comparator.comparingDouble(estacion ->  (double)ponderaciones.get(estacion))
        );
        cola.add(origen);

        // Procesamiento de nodos
        while (!cola.isEmpty()) {
            Estacion actual = cola.poll();

            // Condición de término
            if (actual.equals(destino)) break;

            // Verificación de rutas existentes
            if (!grafo.getWeb().containsKey(actual)) continue;

            // Exploración de rutas adyacentes
            for (Ruta ruta : grafo.getWeb().get(actual)) {
                Estacion vecino = ruta.getDestino();

                // Calculo de transbordos
                int nuevosTransbordos = transbordos.get(actual);
                String lineaPrevia = lineaAnterior.get(actual);
                boolean hayTransbordo = false;

                if (lineaPrevia != null && !lineaPrevia.equals(ruta.getId())) {
                    nuevosTransbordos++;
                    hayTransbordo = true;
                }

                // Calculo de nueva ponderacion
                float ponderacionBase = ruta.getPonderacion();
                float penalizacionTransbordo = hayTransbordo ? ponderacionBase * 0.3f : 0;
                float nuevaPonderacion = ponderaciones.get(actual) + ponderacionBase + penalizacionTransbordo;

                // Actualización si encontramos mejor camino
                if (nuevaPonderacion < ponderaciones.get(vecino)) {
                    ponderaciones.put(vecino, nuevaPonderacion);
                    transbordos.put(vecino, nuevosTransbordos);
                    predecesores.put(vecino, actual);
                    lineaAnterior.put(vecino, ruta.getId());

                    // Actualización en cola de prioridad
                    cola.remove(vecino);
                    cola.add(vecino);
                }
            }
        }
        return finalizacionRuta(grafo, predecesores, transbordos, origen, destino);
    }
}
