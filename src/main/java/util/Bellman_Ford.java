package util;

import estructura.Estacion;
import estructura.GrafoTransporte;
import estructura.ResultadoRuta;
import estructura.Ruta;
import java.util.Map;
import java.util.*;

import static util.Caminos.*;

/*
Clase: Bellman_Ford
Objetivo: Implementación del algoritmo de Bellman_Ford para calcular las rutas más baratas.
          Ahora encuentra el Top 3 de rutas más económicas.
*/
public class Bellman_Ford {

    // Encuentra el Top 3 de rutas más baratas desde el origen al destino
    public static List<ResultadoRuta> bellmanFordTop3(GrafoTransporte grafo, Estacion origen, Estacion destino) {
        int cantVertices = grafo.contarEstaciones();

        // Almacena hasta 3 mejores caminos por estación
        Map<Estacion, List<DatoCamino>> mejoresCaminos = new HashMap<>();

        // Inicialización de estructuras para todas las estaciones
        for (Estacion estacion : grafo.getWeb().keySet()) {
            mejoresCaminos.put(estacion, new ArrayList<>());
        }

        // Camino inicial en el origen con costo 0
        DatoCamino caminoInicial = new DatoCamino(origen, 0.0, null, 0, null, origen.getTipo().toString());
        mejoresCaminos.get(origen).add(caminoInicial);

        // Lista de todas las rutas del grafo
        List<Ruta> todasLasRutas = new ArrayList<>();
        for (Estacion estacion : grafo.getWeb().keySet()) {
            todasLasRutas.addAll(grafo.getWeb().get(estacion));
        }

        // Recorre todas las rutas una cantidad de Vertices - 1 veces
        for (int i = 1; i < cantVertices; i++) {
            boolean huboActualizacion = false;

            // Procesa cada ruta del grafo
            for (Ruta ruta : todasLasRutas) {
                Estacion inicio = ruta.getOrigen();
                Estacion fin = ruta.getDestino();
                double costo = ruta.getCosto();

                // Obtiene todos los caminos conocidos para la estación de inicio
                List<DatoCamino> caminosInicio = mejoresCaminos.get(inicio);
                if (caminosInicio.isEmpty()) continue;

                // Evalúa cada camino conocido desde el inicio
                for (DatoCamino caminoInicio : new ArrayList<>(caminosInicio)) {
                    // Calcula el nuevo costo acumulado
                    double nuevoCosto = caminoInicio.valor + costo;

                    // Calcula transbordos si hay cambio de línea
                    int nuevosTransbordos = caminoInicio.transbordos;
                    if (caminoInicio.lineaAnterior != null && !caminoInicio.lineaAnterior.equals(ruta.getId())) {
                        nuevosTransbordos++;
                    }

                    // Crea el nuevo camino candidato
                    DatoCamino nuevoCamino = new DatoCamino(
                            fin,
                            nuevoCosto,
                            inicio,
                            nuevosTransbordos,
                            ruta.getId(),
                            fin.getTipo().toString()
                    );

                    // Verifica si este camino debe ser guardado (top 3 por estación)
                    List<DatoCamino> caminosFin = mejoresCaminos.get(fin);
                    if (debeGuardarCamino(caminosFin, nuevoCamino)) {
                        agregarCamino(caminosFin, nuevoCamino);
                        huboActualizacion = true;
                    }
                }
            }

            // Optimización: si no hubo cambios en esta iteración, termina antes
            if (!huboActualizacion) break;
        }

        // Detección de ciclos negativos (una iteración extra)
        for (Ruta ruta : todasLasRutas) {
            Estacion inicio = ruta.getOrigen();
            Estacion fin = ruta.getDestino();
            double costo = ruta.getCosto();

            List<DatoCamino> caminosInicio = mejoresCaminos.get(inicio);
            if (caminosInicio.isEmpty()) continue;

            // Si aún se puede mejorar, hay un ciclo negativo
            for (DatoCamino caminoInicio : caminosInicio) {
                double nuevoCosto = caminoInicio.valor + costo;

                List<DatoCamino> caminosFin = mejoresCaminos.get(fin);
                if (!caminosFin.isEmpty() && nuevoCosto < caminosFin.get(0).valor) {
                    throw new IllegalStateException("El grafo tiene una ruta con costo negativo.");
                }
            }
        }

        // Obtiene los mejores caminos que llegaron al destino
        List<DatoCamino> caminosDestino = mejoresCaminos.get(destino);
        if (caminosDestino.isEmpty()) {
            return null;
        }

        // Reconstruye los caminos completos desde origen hasta destino
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