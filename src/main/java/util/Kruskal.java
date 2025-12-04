package util;

import estructura.Estacion;
import estructura.GrafoTransporte;
import estructura.Ruta;

import java.util.*;

public class Kruskal {

    // Clase auxiliar para detectar ciclos eficientemente, Disjoint Set Union.
    static class Union {
        // Mapa de cada estacion a su padre.
        private Map<UUID, UUID> padre;

        private Map<UUID, Integer> rango;

        public Union(GrafoTransporte grafo) {
            padre = new HashMap<>();
            rango = new HashMap<>();
            // Inicializar cada estación en su propio conjunto.
            for(Estacion estacion : grafo.getWeb().keySet()) {
                UUID id = estacion.getId();
                padre.put(id, id); // Cada nodo es su propio padre al inicio.
                rango.put(id, 0);
            }
        }

        // Metodo para encontrar la raíz del conjunto en el que se encuentra un estacion.
        public UUID encontrar(UUID id) {
            if(!padre.containsKey(id)) {
                // Repetir la busqueda recursivamente hasta que se encuentre la raíz, en tal caso no entra al if.
                padre.put(id, encontrar(padre.get(id)));
            }
            return padre.get(id);
        }

        // Metodo para unir dos subconjuntos o subárboles en un solo árbol.
        public void unir(UUID id1, UUID id2) {
            // Buscar la raiz de cada uno primero.
            UUID raiz1 = encontrar(id1);
            UUID raiz2 = encontrar(id2);
            // Si las raíces son iguales, significa que ya se encuentra en el mismo árbol/conjunto.
            if(raiz1.equals(raiz2)) return;

            // Conseguir el rango de ambas raíces.
            int rango1 = rango.get(raiz1);
            int rango2 = rango.get(raiz2);

            if(rango1 < rango2) {
                // Si el rango1 es menor se vuelve hijo de la raiz2 para mantener un árbol comprimido.
                padre.put(raiz1, raiz2);
            } else if(rango1 > rango2) {
                padre.put(raiz2, raiz1);
            } else {
                // Si los rangos son iguales se elite cualquiera y se incrementa el rango de la que se vuelve padre.
                padre.put(raiz2, raiz1);
                rango.put(raiz1, rango1 + 1);
            }
        }

        // Metodo para verificar si dos estaciones están en el mismo conjunto.
        public boolean estanConectado(UUID id1, UUID id2) {
            return encontrar(id1).equals(encontrar(id2));
        }
    }

    public static GrafoTransporte kruskalBusqueda(GrafoTransporte grafo) {
        if (grafo == null || grafo.contarEstaciones() == 0) return null;
        GrafoTransporte arbolMin = new GrafoTransporte();

        // Añadir todas las estaciones/vertices al arbol de expansion minima.
        for(Estacion estacion : grafo.getWeb().keySet()) {
            arbolMin.agregarEstacion(estacion);
        }

        // Conseguir todas las rutas únicas y organizarlas por su ponderación.
        List<Ruta> todasLasRutas = obtenerTodasLasRutasUnicas(grafo);
        todasLasRutas.sort(Comparator.comparingDouble(Ruta::getPonderacion));

        Union union = new Union(grafo);

        int rutasAgregadas = 0;
        int totalEstaciones = grafo.contarEstaciones();

        for(Ruta ruta : todasLasRutas) {
            // Repetir solo hasta que se tenga n-1 rutas con relación a estaciones.
            if(rutasAgregadas >= totalEstaciones - 1) break;

            // Conseguir el id de la estación origen y destino.
            UUID idOrigen = ruta.getOrigen().getId();
            UUID idDestino = ruta.getDestino().getId();

            // Verificar si no estan conectados, porque si lo estan significa que se va a crear un ciclo.
            if(!union.estanConectado(idOrigen, idDestino)) {
                // Agregar la ruta al arbol si no se crea un ciclo.
                arbolMin.agregarRuta(ruta);

                // Unir los conjuntos de origen y destino.
                union.unir(idOrigen, idDestino);
                rutasAgregadas++; // Incrementar la cantidad de rutas agregadas.
            }
        }

        return arbolMin;
    }

    // Metodo para obtener todas las rutas únicas del grafo.
    private static List<Ruta> obtenerTodasLasRutasUnicas(GrafoTransporte grafo) {
        List<Ruta> rutasUnicas = new ArrayList<>();
        Set<UUID> rutasProcesadas = new HashSet<>();
        for(Map.Entry<Estacion, List<Ruta>> entrada : grafo.getWeb().entrySet()) {
            for(Ruta ruta : entrada.getValue()) {
                if(!rutasProcesadas.contains(ruta.getId())) {
                    rutasUnicas.add(ruta);
                    rutasProcesadas.add(ruta.getId());
                    Ruta rutaInversa = grafo.getRutaEntreEstaciones(ruta.getDestino(), ruta.getOrigen());
                    if(rutaInversa != null) {
                        rutasProcesadas.add(rutaInversa.getId());
                    }
                }

            }
        }
        return rutasUnicas;
    }
}
