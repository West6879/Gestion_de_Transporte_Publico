package util;

import estructura.Estacion;
import estructura.GrafoTransporte;
import estructura. ResultadoRuta;
import estructura.Ruta;

import java.util.*;

import static util.Caminos.*;

public class Dijkstra {

    public enum Criterio {
        DISTANCIA,
        TIEMPO,
        COSTO,
        TRANSBORDOS
    }

    private static final int CAMINOS_POR_ESTACION = 15;  // NUEVO: Aumentar el límite

    public static List<ResultadoRuta> EncontrarTop3Rutas(GrafoTransporte grafo, Estacion origen, Estacion destino, Criterio criterio) {
        if (!grafo.getWeb().containsKey(origen) || !grafo.getWeb().containsKey(destino) || origen.equals(destino)) {
            return null;
        }

        Map<Estacion, List<DatoCamino>> mejoresCaminos = new HashMap<>();

        for (Estacion estacion : grafo.getWeb().keySet()) {
            mejoresCaminos.put(estacion, new ArrayList<>());
        }

        PriorityQueue<DatoCamino> cola = new PriorityQueue<>();

        DatoCamino inicio = new DatoCamino(origen, 0.0, null, 0, null, origen.getTipo().toString());
        cola.add(inicio);
        mejoresCaminos.get(origen).add(inicio);

        List<DatoCamino> caminosDestino = new ArrayList<>();

        while (!cola.isEmpty()) {
            DatoCamino actual = cola.poll();
            Estacion estacionActual = actual.estacionActual;

            if (estacionActual.equals(destino)) {
                caminosDestino.add(actual);
                continue;
            }

            if (!grafo.getWeb().containsKey(estacionActual)) continue;

            for (Ruta ruta : grafo.getWeb().get(estacionActual)) {
                Estacion vecino = ruta.getDestino();

                double valorRuta = obtenerValorRuta(ruta, criterio);
                double nuevoValor = actual.valor + valorRuta;

                int nuevosTransbordos = actual.transbordos;
                boolean hayTransbordo = calcularTransbordo(criterio, actual, ruta, vecino);
                if (hayTransbordo) {
                    nuevosTransbordos++;
                }

                DatoCamino nuevoCamino = new DatoCamino(
                        vecino,
                        nuevoValor,
                        estacionActual,
                        nuevosTransbordos,
                        ruta.getId(),
                        vecino.getTipo().toString()
                );

                if (debeGuardarCamino(mejoresCaminos.get(vecino), nuevoCamino)) {
                    agregarCamino(mejoresCaminos.get(vecino), nuevoCamino);
                    cola.add(nuevoCamino);
                }
            }
        }

        if (caminosDestino.isEmpty()) {
            return null;
        }

        Collections.sort(caminosDestino);

        List<DatoCamino> top3Unicos = obtenerTop3Unicos(caminosDestino, mejoresCaminos);

        // Reconstruir solo los caminos únicos
        List<List<Estacion>> caminos = reconstruirCaminos(top3Unicos, mejoresCaminos);

        List<ResultadoRuta> resultados = new ArrayList<>();
        for (int i = 0; i < caminos.size(); i++) {
            List<Estacion> camino = caminos.get(i);
            DatoCamino datoDestino = top3Unicos.get(i);  // CAMBIO 4: Usar top3Unicos

            ResultadoRuta resultado = crearResultadoRuta(grafo, camino, datoDestino. transbordos);
            resultados.add(resultado);
        }

        return resultados;
    }

    // Metodo para Obtener top 3 únicos sin duplicados
    private static List<DatoCamino> obtenerTop3Unicos(List<DatoCamino> todosLosCaminos, Map<Estacion, List<DatoCamino>> mejoresCaminos) {
        List<DatoCamino> unicos = new ArrayList<>();
        Set<List<UUID>> caminosVistos = new HashSet<>();

        for (DatoCamino dato : todosLosCaminos) {
            if (unicos.size() >= 3) break;

            // Reconstruir el camino
            List<Estacion> camino = reconstruirUnCamino(dato, mejoresCaminos);

            // Crear firma del camino (secuencia de IDs de estaciones)
            List<UUID> ids = new ArrayList<>();
            for (Estacion e : camino) {
                ids.add(e.getId());
            }

            // Solo agregar si no es duplicado
            if (!caminosVistos.contains(ids)) {
                unicos.add(dato);
                caminosVistos.add(ids);
            }
        }

        return unicos;
    }

    // Reconstruir un solo camino
    private static List<Estacion> reconstruirUnCamino(DatoCamino destino, Map<Estacion, List<DatoCamino>> mejoresCaminos) {
        List<Estacion> camino = new ArrayList<>();
        DatoCamino actual = destino;

        while (actual != null) {
            camino.add(0, actual.estacionActual);

            if (actual.predecesor == null) {
                break;
            }

            List<DatoCamino> candidatos = mejoresCaminos.get(actual.predecesor);
            DatoCamino siguiente = null;

            if (candidatos != null) {
                for (DatoCamino c : candidatos) {
                    if (c.estacionActual.equals(actual.predecesor)) {
                        siguiente = c;
                        break;
                    }
                }
            }

            actual = siguiente;
        }

        return camino;
    }

    private static double obtenerValorRuta(Ruta ruta, Criterio criterio) {
        switch (criterio) {
            case DISTANCIA: return ruta.getDistancia();
            case TIEMPO: return ruta.getTiempo();
            case COSTO: return ruta.getCosto();
            case TRANSBORDOS: return 1.0;
            default: return 0.0;
        }
    }

    private static boolean calcularTransbordo(Criterio criterio, DatoCamino actual, Ruta ruta, Estacion vecino) {
        if (criterio == Criterio.TRANSBORDOS) {
            String tipoVecino = vecino.getTipo().toString();
            return actual.tipoAnterior != null && !actual.tipoAnterior.equals(tipoVecino);
        } else {
            return actual.lineaAnterior != null && !actual.lineaAnterior.equals(ruta.getId());
        }
    }

    //Metodo para guardar camino
    private static boolean debeGuardarCamino(List<DatoCamino> caminos, DatoCamino nuevo) {
        if (caminos.size() < CAMINOS_POR_ESTACION) {
            return true;
        }

        for (DatoCamino existente : caminos) {
            if (nuevo.compareTo(existente) < 0) {
                return true;
            }
        }

        return false;
    }

    private static void agregarCamino(List<DatoCamino> caminos, DatoCamino nuevo) {
        caminos.add(nuevo);
        Collections.sort(caminos);

        while (caminos.size() > CAMINOS_POR_ESTACION) {
            caminos.remove(caminos.size() - 1);
        }
    }
}