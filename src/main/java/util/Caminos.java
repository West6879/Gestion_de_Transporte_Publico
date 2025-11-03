package util;

import estructura.Estacion;
import estructura.GrafoTransporte;
import estructura.ResultadoRuta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
Clase: Caminos
Objetivo: Clase de utilidad con metodos generales para la implementación
          de los diferentes algoritmos de búsqueda.
*/
public class Caminos {

    // Metodo para finalizar la creación de una ruta tomando la información necesaria para ello.
    public static ResultadoRuta finalizacionRuta(GrafoTransporte grafo, Map<Estacion, Estacion> predecesores,
                                                 Map<Estacion, Integer> transbordos, Estacion origen, Estacion destino) {
        // Reconstrucción y validación del camino
        List<Estacion> camino = reconstruirCamino(predecesores, destino);
        if (camino.isEmpty() || !camino.getFirst().equals(origen)) {
            return null;
        }

        // Cálculo de métricas finales
        ResultadoRuta resultado = calcularTransbordos(transbordos, camino, destino);
        resultado.calcularMetricas(grafo);
        return resultado;
    }

    // Reconstruye el camino desde el destino hasta el origen
    public static List<Estacion> reconstruirCamino(Map<Estacion, Estacion> predecesores, Estacion destino) {
        List<Estacion> camino = new ArrayList<>();
        Estacion actual = destino;

        // Recorrido inverso desde destino hasta origen
        while (actual != null) {
            camino.addFirst(actual);  // Inserta al inicio para mantener orden
            actual = predecesores.get(actual);
        }

        return camino;
    }

    // Metodo para calcular los transbordos con el camino ya creado, retorna la ruta ya inicializada con transbordos.
    public static ResultadoRuta calcularTransbordos(Map<Estacion, Integer> transbordos,
                                                    List<Estacion> camino, Estacion destino) {
        Integer transbordosDestino = transbordos.get(destino);
        return new ResultadoRuta(camino, 0, 0, 0,
                transbordosDestino != null ? transbordosDestino : 0);
    }
}
