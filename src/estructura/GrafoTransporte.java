package estructura;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrafoTransporte {
    Map<Estacion, List<Ruta>> web;

    public GrafoTransporte() {
        this.web = new HashMap<>();
    }

    // Metodo para agregar una estacion o nodo
    public void agregarEstacion(Estacion estacion) {
        if(!web.containsKey(estacion)) {
            web.put(estacion, new ArrayList<>());
        }
    }

    // Metodo para eliminar una estacion o nodo.
    public void eliminarEstacion(Estacion estacion) {
        // Recorre el mapa para cada lista de rutas.
        for(Map.Entry<Estacion, List<Ruta>> entrada : web.entrySet()) {

            // Guarda la lista de rutas del nodo actual
            List<Ruta> rutas = entrada.getValue();

            // Elimina las rutas de la lista que tengan como destino la estacion a eliminar.
            rutas.removeIf(ruta -> ruta.getDestino().equals(estacion));
        }
        // Finalmente, elimina el nodo.
        web.remove(estacion);
    }

    // Metodo para agregar una nueva ruta o arista
    public void agregarRuta(Estacion origen, Estacion destino, int distancia, String id) {
        // Chequear si las estaciones/nodos ya existen, o añadirlos si no.
        agregarEstacion(origen);
        agregarEstacion(destino);

        Ruta nuevaRuta = new Ruta(destino, distancia, id);
        web.get(origen).add(nuevaRuta);
    }

    // Metodo para eliminar una ruta.
    public void eliminarRuta(Estacion origen, Estacion destino, String id) {
        if(web.containsKey(origen)) {
            List<Ruta> rutas = web.get(origen);
            // Elimina si la ruta tiene el mismo destino y el mismo id.
            rutas.removeIf(ruta -> ruta.getDestino().equals(destino) && ruta.getId().equals(id));
        }
    }

    // Metodo para chequear si existe una conexion entre dos estaciones.
    public boolean existeRuta(Estacion origen, Estacion destino) {
        if(!web.containsKey(origen)) return false;
        for(Ruta ruta : web.get(origen)) {
            if(ruta.getDestino().equals(destino)) return true;
        }
        return false;
    }

    // Metodo para chequear si existe una ruta especifica entre dos estaciones.
    public boolean existeRutaEspecifica(Estacion origen, Estacion destino, String id) {
        if(!web.containsKey(origen)) return false;
        for(Ruta ruta : web.get(origen)) {
            if(ruta.getDestino().equals(destino) && ruta.getId().equals(id)) return true;
        }
        return false;
    }
}
