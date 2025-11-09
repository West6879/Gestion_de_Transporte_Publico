package util;

import estructura.Estacion;
import estructura.GrafoTransporte;
import estructura.ResultadoRuta;
import estructura.Ruta;

import java.util.*;

import static util.Caminos.*;

/*
Clase: Dijkstra
Objetivo: Implementación del algoritmo de Dijkstra para encontrar las mejores rutas
          según diferentes criterios (Distancia, Tiempo, Costo, Transbordos).
*/
public class Dijkstra {

    public enum Criterio {
        DISTANCIA,
        TIEMPO,
        COSTO,
        TRANSBORDOS
    }

    // Encuentra el Top 3 de mejores rutas según el criterio especificado
    public static List<ResultadoRuta> EncontrarTop3Rutas(GrafoTransporte grafo, Estacion origen, Estacion destino, Criterio criterio) {
        // Validaciones iniciales
        if (!grafo.getWeb().containsKey(origen) || !grafo.getWeb().containsKey(destino) || origen.equals(destino)) {
            return null;
        }

        // Almacena hasta 3 mejores caminos por estación
        Map<Estacion, List<DatoCamino>> mejoresCaminos = new HashMap<>();

        // Inicialización de todas las estaciones
        for (Estacion estacion : grafo.getWeb().keySet()) {
            mejoresCaminos.put(estacion, new ArrayList<>());
        }

        // Cola de prioridad para explorar caminos (ordena por valor y transbordos)
        PriorityQueue<DatoCamino> cola = new PriorityQueue<>();

        // Agrega el punto de partida
        DatoCamino inicio = new DatoCamino(origen, 0.0, null, 0, null, origen.getTipo().toString());
        cola.add(inicio);
        mejoresCaminos.get(origen).add(inicio);

        // Lista para almacenar los mejores caminos que llegaron al destino
        List<DatoCamino> caminosDestino = new ArrayList<>();

        // Explora todos los caminos posibles hasta encontrar 3 al destino
        while (!cola.isEmpty() && caminosDestino.size() < 3) {
            DatoCamino actual = cola.poll();
            Estacion estacionActual = actual.estacionActual;

            // Si llegamos al destino, guardamos este camino
            if (estacionActual.equals(destino)) {
                caminosDestino.add(actual);
                continue;
            }

            // Explora los vecinos de la estación actual
            if (!grafo.getWeb().containsKey(estacionActual)) continue;

            for (Ruta ruta : grafo.getWeb().get(estacionActual)) {
                Estacion vecino = ruta.getDestino();

                // Calcula el valor de la ruta según el criterio
                double valorRuta = obtenerValorRuta(ruta, criterio);
                double nuevoValor = actual.valor + valorRuta;

                // Calcula transbordos
                int nuevosTransbordos = actual.transbordos;
                boolean hayTransbordo = calcularTransbordo(criterio, actual, ruta, vecino);
                if (hayTransbordo) {
                    nuevosTransbordos++;
                }

                // Crea el nuevo DatoCamino con la información acumulada
                DatoCamino nuevoCamino = new DatoCamino(
                        vecino,
                        nuevoValor,
                        estacionActual,
                        nuevosTransbordos,
                        ruta.getId(),
                        vecino.getTipo().toString()
                );

                // Verifica si este camino debe ser guardado (top 3 por estación)
                if (debeGuardarCamino(mejoresCaminos.get(vecino), nuevoCamino)) {
                    agregarCamino(mejoresCaminos.get(vecino), nuevoCamino);
                    cola.add(nuevoCamino);
                }
            }
        }

        // Si no se encontraron caminos al destino
        if (caminosDestino.isEmpty()) {
            return null;
        }

        // Reconstruye los caminos encontrados usando la información de DatoCamino
        List<List<Estacion>> caminos = reconstruirCaminos(caminosDestino, mejoresCaminos);

        // Crea los ResultadoRuta para cada camino
        List<ResultadoRuta> resultados = new ArrayList<>();
        for (int i = 0; i < caminos.size(); i++) {
            List<Estacion> camino = caminos.get(i);
            DatoCamino datoDestino = caminosDestino.get(i);

            // Crea el resultado con las métricas calculadas automáticamente
            ResultadoRuta resultado = crearResultadoRuta(grafo, camino, datoDestino.transbordos);
            resultados.add(resultado);
        }

        return resultados;
    }

    // Obtiene el valor de una ruta según el criterio seleccionado
    private static double obtenerValorRuta(Ruta ruta, Criterio criterio) {
        switch (criterio) {
            case DISTANCIA: return ruta.getDistancia();
            case TIEMPO: return ruta.getTiempo();
            case COSTO: return ruta.getCosto();
            case TRANSBORDOS: return 1.0;
            default: return 0.0;
        }
    }

    // Calcula si hay un transbordo en esta ruta
    private static boolean calcularTransbordo(Criterio criterio, DatoCamino actual, Ruta ruta, Estacion vecino) {
        if (criterio == Criterio.TRANSBORDOS) {
            // Para criterio de transbordos, compara tipos de estación
            String tipoVecino = vecino.getTipo().toString();
            return actual.tipoAnterior != null && !actual.tipoAnterior.equals(tipoVecino);
        } else {
            // Para otros criterios, compara IDs de línea/ruta
            return actual.lineaAnterior != null && !actual.lineaAnterior.equals(ruta.getId());
        }
    }

    // Verifica si un nuevo camino debe ser guardado en el top 3
    private static boolean debeGuardarCamino(List<DatoCamino> caminos, DatoCamino nuevo) {
        // Si hay menos de 3 caminos, siempre guardamos
        if (caminos.size() < 3) {
            return true;
        }

        // Si hay 3 caminos, verificamos si el nuevo es mejor que alguno existente
        for (DatoCamino existente : caminos) {
            if (nuevo.compareTo(existente) < 0) {
                return true;
            }
        }

        return false;
    }

    // Agrega un camino a la lista manteniendo solo los 3 mejores
    private static void agregarCamino(List<DatoCamino> caminos, DatoCamino nuevo) {
        caminos.add(nuevo);
        Collections.sort(caminos);

        // Mantiene solo los 3 mejores (elimina el peor si hay más de 3)
        while (caminos.size() > 3) {
            caminos.remove(caminos.size() - 1);
        }
    }
}