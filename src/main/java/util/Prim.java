package util;

import estructura.Estacion;
import estructura.GrafoTransporte;
import estructura.Ruta;

import java.util.*;

/*
Clase: Prim
Objetivo: Implementación del algoritmo de Prim que crea un árbol de expansion minima
            comenzando desde un vértice cualquiera.
*/
public class Prim {

    // Clase auxiliar que extiende de DatoCamino para incluir la ruta actual también.
    static class DatoCaminoConRuta extends DatoCamino {
        public final Ruta ruta;
        public DatoCaminoConRuta(Estacion actual, double ponderacion, Estacion predecesor,
                                 int transbordos, UUID linea, String tipo, Ruta ruta) {
            super(actual, ponderacion, predecesor, transbordos, linea, tipo);
            this.ruta = ruta;
        }
    }

    // Algoritmo de prim basado en la ponderación de las rutas.
    public static GrafoTransporte primBusqueda(GrafoTransporte grafo, Estacion origen) {
        // Chequear que nada sea null.
        if(origen == null || grafo == null) return null;

        // Crear un nuevo grafo que será el árbol de expansión minima.
        GrafoTransporte nuevoGrafo = new GrafoTransporte();
        // Agregar todas las estaciones del grafo al árbol.
        for(Estacion estacion : grafo.getWeb().keySet()) {
            nuevoGrafo.agregarEstacion(estacion);
        }

        // Set para guardar las estaciones ya visitadas.
        Set<UUID> visitados = new HashSet<>();

        // PriorityQueue para conseguir las rutas con menor ponderación.
        PriorityQueue<DatoCaminoConRuta> cola = new PriorityQueue<>();
        // Añadir la estación origen.
        visitados.add(origen.getId());

        // Agregar todas las rutas de la estacion origen a la cola.
        List<Ruta> rutasOrigen = grafo.rutasDeEstacion(origen);
        agregarRutasACola(rutasOrigen, cola, origen);

        // Recorrer mientras no se hayan visitado todas las estaciones.
        while(!cola.isEmpty() && visitados.size() < grafo.contarEstaciones()) {
            // Conseguir el mejor camino/ruta.
            DatoCaminoConRuta mejorCamino = cola.poll();
            // Volarse el ciclo si es null.
            if(mejorCamino == null) continue;
            // Conseguir la estacion destino del mejor camino.
            Estacion destino = mejorCamino.estacionActual;

            // Si la estacion destino ya se ha visitado, continuar.
            if(visitados.contains(destino.getId())) {
                // Si no se hiciera esto, se crearía un ciclo, haciendo que este algoritmo no cree un árbol.
                continue;
            }
            // Si pasa la verificación la estacion destino se añade a los visitados.
            visitados.add(destino.getId());

            // Finalmente, se agregan todas las rutas de la estacion destino a la cola para seguir encontrando la mejor ruta.
            nuevoGrafo.agregarRuta(mejorCamino.ruta);
            List<Ruta> rutasDestino = grafo.rutasDeEstacion(destino);
            agregarRutasACola(rutasDestino, cola, destino);
        }
        return nuevoGrafo;
    }

    // Metodo auxiliar para añadir todas las rutas de una estación a la cola.
    private static void agregarRutasACola(List<Ruta> rutas, PriorityQueue<DatoCaminoConRuta> cola, Estacion estacion) {
        if(rutas != null) {
            for(Ruta ruta : rutas) {
                cola.add(new DatoCaminoConRuta(
                        ruta.getDestino(),
                        ruta.getPonderacion(),
                        estacion,
                        0,
                        ruta.getId(),
                        estacion.getTipo().toString(),
                        ruta));
            }
        }
    }

}
