package util;

import estructura.Estacion;
import estructura.GrafoTransporte;
import estructura.ResultadoRuta;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/*
Clase: Caminos
Objetivo: Clase de utilidad con metodos generales para la implementación
          de los diferentes algoritmos de búsqueda.
*/
public class Caminos {

    // Reconstruye múltiples caminos desde una lista de DatoCamino
    // Retorna una lista donde posición 0 es el mejor camino, 1 el segundo mejor, etc.
    public static List<List<Estacion>> reconstruirCaminos(List<DatoCamino> datosCaminos,
                                                          Map<Estacion, List<DatoCamino>> todosCaminos) {
        List<List<Estacion>> caminos = new ArrayList<>();

        // Procesa cada DatoCamino final para reconstruir su camino completo
        for (DatoCamino datoFinal : datosCaminos) {
            List<Estacion> camino = new ArrayList<>();
            DatoCamino actual = datoFinal;

            // Recorre hacia atrás desde el destino hasta el origen
            while (actual != null) {
                camino.addFirst(actual.estacionActual);

                // Si no hay predecesor, llegamos al origen
                if (actual.predecesor == null) {
                    break;
                }

                // Encuentra el DatoCamino correspondiente al predecesor
                actual = encontrarDatoCamino(todosCaminos, actual.predecesor, actual);
            }

            caminos.add(camino);
        }

        return caminos;
    }

    // Busca el DatoCamino específico que conecta con el siguiente nodo
    private static DatoCamino encontrarDatoCamino(Map<Estacion, List<DatoCamino>> todosCaminos,
                                                  Estacion estacion, DatoCamino siguiente) {
        List<DatoCamino> datos = todosCaminos.get(estacion);
        if (datos == null || datos.isEmpty()) {
            return null;
        }

        // Busca el DatoCamino que mejor coincida con las características del camino
        for (DatoCamino dato : datos) {
            if (dato.estacionActual.equals(estacion)) {
                // Si hay línea anterior, intenta coincidir
                if (dato.lineaAnterior != null && siguiente.lineaAnterior != null) {
                    if (dato.lineaAnterior.equals(siguiente.lineaAnterior)) {
                        return dato;
                    }
                } else if (dato.lineaAnterior == null && siguiente.predecesor.equals(estacion)) {
                    // Caso especial: estación origen
                    return dato;
                }
            }
        }

        // Si no se encuentra coincidencia exacta, retorna el primer disponible.
        return datos.getFirst();
    }

    // Crea ResultadoRuta a partir de un camino y sus datos
    public static ResultadoRuta crearResultadoRuta(GrafoTransporte grafo, List<Estacion> camino, int transbordos) {
        ResultadoRuta resultado = new ResultadoRuta(camino, 0, 0, 0, transbordos);
        resultado.calcularMetricas(grafo);
        return resultado;
    }
}