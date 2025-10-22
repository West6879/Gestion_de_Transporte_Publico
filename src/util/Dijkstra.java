package util;

import estructura.Estacion;
import estructura.GrafoTransporte;
import estructura.ResultadoRuta;
import estructura.Ruta;
import estructura.EstacionDeMetro;
import estructura.EstacionDeTren;
import estructura.ParadaDeBus;

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

        // Inicializacion de valores
        for (Estacion estacion : grafo.getWeb().keySet()) {
            ponderaciones.put(estacion, Float.MAX_VALUE);
            transbordos.put(estacion, Integer.MAX_VALUE);
        }
        ponderaciones.put(origen, 0.0f);
        transbordos.put(origen, 0);

        // Configuracion de la cola de prioridad
        PriorityQueue<Estacion> cola = new PriorityQueue<>(
                Comparator.comparingDouble(estacion ->  (double)ponderaciones.get(estacion))
        );
        cola.add(origen);

        // Procesamiento de nodos
        while (!cola.isEmpty()) {
            Estacion actual = cola.poll();

            // Condicion de término
            if (actual.equals(destino)) break;

            // Verificacion de rutas existentes
            if (!grafo.getWeb().containsKey(actual)) continue;

            // Exploracion de rutas adyacentes
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

                    // Actualizacion en cola de prioridad
                    cola.remove(vecino);
                    cola.add(vecino);
                }
            }
        }
        return finalizacionRuta(grafo, predecesores, transbordos, origen, destino);
    }

    // Dijkstra que cuenta transbordos cuando cambia el TIPO de estación
    //Ojo se obvia gran parte de documentacion pues es igual al metodo de Dijkstra anterior
    public static ResultadoRuta EncontrarMejoresRutasPorTipoEstacion(GrafoTransporte grafo, Estacion origen, Estacion destino) {
        if (!grafo.getWeb().containsKey(origen) || !grafo.getWeb().containsKey(destino) || origen.equals(destino)) {
            return null;
        }

        Map<Estacion, Float> ponderaciones = new HashMap<>();
        Map<Estacion, Estacion> predecesores = new HashMap<>();
        Map<Estacion, String> tipoAnterior = new HashMap<>();
        Map<Estacion, Integer> transbordos = new HashMap<>();

        for (Estacion estacion : grafo.getWeb().keySet()) {
            ponderaciones.put(estacion, Float.MAX_VALUE);
            transbordos.put(estacion, Integer.MAX_VALUE);
        }
        ponderaciones.put(origen, 0.0f);
        transbordos.put(origen, 0);
        tipoAnterior.put(origen, getTipoEstacion(origen));
        PriorityQueue<Estacion> cola = new PriorityQueue<>(
                Comparator.comparingDouble(estacion -> (double)ponderaciones.get(estacion))
        );
        cola.add(origen);

        while (!cola.isEmpty()) {
            Estacion actual = cola.poll();
            if (actual.equals(destino)) break;
            if (!grafo.getWeb().containsKey(actual)) continue;

            for (Ruta ruta : grafo.getWeb().get(actual)) {
                Estacion vecino = ruta.getDestino();

                int nuevosTransbordos = transbordos.get(actual);
                String tipoPrevio = tipoAnterior.get(actual);
                String tipoActualEstacion = getTipoEstacion(actual);
                String tipoVecino = getTipoEstacion(vecino);
                boolean hayTransbordo = false;

                // Verificar si hay cambio de tipo de estación (transbordo)
                if (tipoPrevio != null && !tipoPrevio.equals(tipoVecino)) {
                    nuevosTransbordos++;
                    hayTransbordo = true;
                }

                float ponderacionBase = ruta.getPonderacion();
                float penalizacionTransbordo = hayTransbordo ? ponderacionBase * 0.3f : 0;
                float nuevaPonderacion = ponderaciones.get(actual) + ponderacionBase + penalizacionTransbordo;

                if (nuevaPonderacion < ponderaciones.get(vecino)) {
                    ponderaciones.put(vecino, nuevaPonderacion);
                    transbordos.put(vecino, nuevosTransbordos);
                    predecesores.put(vecino, actual);
                    tipoAnterior.put(vecino, tipoVecino);

                    cola.remove(vecino);
                    cola.add(vecino);
                }
            }
        }
        return finalizacionRuta(grafo, predecesores, transbordos, origen, destino);
    }

    // Metodo auxiliar para obtener el tipo de estación como String
    private static String getTipoEstacion(Estacion estacion) {
        if (estacion instanceof EstacionDeTren) {
            return "TREN";
        } else if (estacion instanceof EstacionDeMetro) {
            return "METRO";
        } else if (estacion instanceof ParadaDeBus) {
            return "BUS";
        } else {
            return "DESCONOCIDO";
        }
    }

}
