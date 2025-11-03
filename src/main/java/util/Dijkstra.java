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
// Ojo muchos detalles de documentación que sean repetidos a lo largo del código solo se explican en la primera función que aparezca de ese tipo
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
        Map<Estacion, UUID> lineaAnterior = new HashMap<>();
        Map<Estacion, Integer> transbordos = new HashMap<>();

        // Inicialización de valores
        inicializarEstructurasGenerales(grafo, ponderaciones, transbordos, origen);

        // Configuración de la cola de prioridad
        PriorityQueue<Estacion> cola = crearColaPrioridadGeneral(ponderaciones);
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
                UUID lineaPrevia = lineaAnterior.get(actual);
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

    // Dijkstra que cuenta transbordos cuando cambia el TIPO de estación
    public static ResultadoRuta EncontrarMejoresRutasPorTipoEstacion(GrafoTransporte grafo, Estacion origen, Estacion destino) {
        if (!grafo.getWeb().containsKey(origen) || !grafo.getWeb().containsKey(destino) || origen.equals(destino)) {
            return null;
        }

        Map<Estacion, Float> ponderaciones = new HashMap<>();
        Map<Estacion, Estacion> predecesores = new HashMap<>();
        Map<Estacion, String> tipoAnterior = new HashMap<>();
        Map<Estacion, Integer> transbordos = new HashMap<>();

        // Inicialización de estructuras
        inicializarEstructurasGenerales(grafo, ponderaciones, transbordos, origen);
        tipoAnterior.put(origen, origen.getTipo().toString());

        // Configuración de cola de prioridad
        PriorityQueue<Estacion> cola = crearColaPrioridadGeneral(ponderaciones);
        cola.add(origen);

        while (!cola.isEmpty()) {
            Estacion actual = cola.poll();
            if (actual.equals(destino)) break;
            if (!grafo.getWeb().containsKey(actual)) continue;

            for (Ruta ruta : grafo.getWeb().get(actual)) {
                Estacion vecino = ruta.getDestino();

                int nuevosTransbordos = transbordos.get(actual);
                String tipoPrevio = tipoAnterior.get(actual);
                String tipoVecino = vecino.getTipo().toString();
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

    // Dijkstra que solo considera TIEMPO como factor de ponderación
    public static ResultadoRuta EncontrarMejoresRutasPorTiempo(GrafoTransporte grafo, Estacion origen, Estacion destino) {
        if (!grafo.getWeb().containsKey(origen) || !grafo.getWeb().containsKey(destino) || origen.equals(destino)) {
            return null;
        }

        Map<Estacion, Integer> tiempos = new HashMap<>();
        Map<Estacion, Estacion> predecesores = new HashMap<>();
        Map<Estacion, Integer> transbordos = new HashMap<>();

        // Inicialización de estructuras para tiempo
        inicializarEstructurasTiempo(grafo, tiempos, transbordos, origen);

        // Configuración de cola de prioridad para tiempo
        PriorityQueue<Estacion> cola = crearColaPrioridadTiempo(tiempos);
        cola.add(origen);

        while (!cola.isEmpty()) {
            Estacion actual = cola.poll();
            if (actual.equals(destino)) break;
            if (!grafo.getWeb().containsKey(actual)) continue;

            for (Ruta ruta : grafo.getWeb().get(actual)) {
                Estacion vecino = ruta.getDestino();
                int nuevosTransbordos = transbordos.get(actual);

                //SOLO CONSIDERA TIEMPO
                int tiempoRuta = ruta.getTiempo();
                int nuevoTiempo = tiempos.get(actual) + tiempoRuta;

                if (nuevoTiempo < tiempos.get(vecino)) {
                    tiempos.put(vecino, nuevoTiempo);
                    transbordos.put(vecino, nuevosTransbordos);
                    predecesores.put(vecino, actual);
                    cola.remove(vecino);
                    cola.add(vecino);
                }
            }
        }
        return finalizacionRuta(grafo, predecesores, transbordos, origen, destino);
    }

    // Inicializa estructuras para algoritmos de ponderacion general
    private static void inicializarEstructurasGenerales(GrafoTransporte grafo, Map<Estacion, Float> ponderaciones,
                                                        Map<Estacion, Integer> transbordos, Estacion origen) {
        for (Estacion estacion : grafo.getWeb().keySet()) {
            ponderaciones.put(estacion, Float.MAX_VALUE);
            transbordos.put(estacion, Integer.MAX_VALUE);
        }
        ponderaciones.put(origen, 0.0f);
        transbordos.put(origen, 0);
    }

    // Inicializa estructuras para algoritmo de tiempo
    private static void inicializarEstructurasTiempo(GrafoTransporte grafo, Map<Estacion, Integer> tiempos,
                                                     Map<Estacion, Integer> transbordos, Estacion origen) {
        for (Estacion estacion : grafo.getWeb().keySet()) {
            tiempos.put(estacion, Integer.MAX_VALUE);
            transbordos.put(estacion, Integer.MAX_VALUE);
        }
        tiempos.put(origen, 0);
        transbordos.put(origen, 0);
    }

    // Crea cola de prioridad para algoritmos de ponderacion general
    private static PriorityQueue<Estacion> crearColaPrioridadGeneral(Map<Estacion, Float> ponderaciones) {
        return new PriorityQueue<>(Comparator.comparingDouble(estacion -> (double)ponderaciones.get(estacion)));
    }

    // Crea cola de prioridad para algoritmo de tiempo
    private static PriorityQueue<Estacion> crearColaPrioridadTiempo(Map<Estacion, Integer> tiempos) {
        return new PriorityQueue<>(Comparator.comparingInt(estacion -> tiempos.get(estacion)));
    }
}