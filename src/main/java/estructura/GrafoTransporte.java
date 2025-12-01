package estructura;

import java.util.*;

/*
Clase: Grafo Transporte
Objetivo: Clase objeto del grafo con funcionalidad de crear y eliminar estaciones y rutas.
*/
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
    public void agregarRuta(Ruta ruta) {
        // Chequear si las estaciones/nodos ya existen, o añadirlos si no.
        agregarEstacion(ruta.getOrigen());
        agregarEstacion(ruta.getDestino());

        web.get(ruta.getOrigen()).add(ruta);
    }

    // Metodo para eliminar una ruta.
    public void eliminarRuta(Ruta rutaAEliminar) {
        Estacion origen = rutaAEliminar.getOrigen();
        Estacion destino = rutaAEliminar.getDestino();
        UUID id = rutaAEliminar.getId();
        if(web.containsKey(origen)) {
            List<Ruta> rutas = web.get(origen);
            // Elimina si la ruta tiene el mismo destino y el mismo id.
            rutas.removeIf(ruta -> ruta.getDestino().equals(destino) && ruta.getId().equals(id));
        }
    }

    // Metodo para chequear si existe una conexión entre dos estaciones.
    public boolean existeRuta(Estacion origen, Estacion destino) {
        if(!web.containsKey(origen)) return false;
        for(Ruta ruta : web.get(origen)) {
            if(ruta.getDestino().equals(destino)) return true;
        }
        return false;
    }

    // Metodo para chequear si existe una conexión entre dos estaciones. y retornarlo
    public Ruta getRutaEntreEstaciones(Estacion origen, Estacion destino) {
        if (!web.containsKey(origen)) {
            return null;
        }
        for (Ruta ruta : web.get(origen)) {
            if (ruta.getDestino().equals(destino)) {
                return ruta;
            }
        }
        return null;
    }

    // Metodo para chequear si existe una ruta específica entre dos estaciones.
    public boolean existeRutaEspecifica(Estacion origen, Estacion destino, UUID id) {
        if(!web.containsKey(origen)) return false;
        for(Ruta ruta : web.get(origen)) {
            if(ruta.getDestino().equals(destino) && ruta.getId().equals(id)) return true;
        }
        return false;
    }

    //Metodo para chequear que dos estaciones no tengan la misma latitud y longitud
    public boolean existeEnPos(double lat,double lon){
        for(Map.Entry<Estacion, List<Ruta>> entrada : web.entrySet()) {
            if(entrada.getKey().getLatitud() == lat && entrada.getKey().getLongitud() == lon) return true;
        }
        return false;
    }

    // Cuenta la cantidad de estaciones en el grafo.
    public int contarEstaciones() {
        return web.size();
    }

    // Cuenta la cantidad de rutas que hay en la web
    public int contarRutas() {
        int totalRutas = 0;
        for (List<Ruta> rutas : web.values()) {
            totalRutas += rutas.size();
        }
        return totalRutas;
    }

    // Metodo para conseguir las rutas que salen de una estación.
    public List<Ruta> rutasDeEstacion(Estacion estacion) {
        return web.get(estacion);
    }

    // Metodo para conseguir la cantidad de rutas salientes de una estación.
    public int rutasSalientesPorEstacion(Estacion estacion) {
        return web.get(estacion).size();
    }

    // Metodo para imprimir cada estacion y sus rutas salientes.
    public void imprimirGrafo() {
        for(Estacion estacion : web.keySet()) {
            System.out.print(estacion + " -> ");
            List<Ruta> rutas = web.get(estacion);
            if(rutas.isEmpty()) {
                System.out.print("Sin conexiones.");
            } else {
                for(int ind = 0; ind < rutas.size(); ind++) {
                    System.out.print(rutas.get(ind));
                    if(ind < rutas.size() - 1) System.out.print(", ");
                }
            }
            System.out.println();
        }
    }

    //Pasado un index devuelve la ruta en ese index identificado
    public Ruta getIndexRuta(int index){
        List<Ruta> todasLasRutas = new ArrayList<>();

        for(List<Ruta> rutas : web.values()){// Recolectar todas las rutas
            todasLasRutas.addAll(rutas);
        }

        if(index >= 0 && index < todasLasRutas.size()){// Verificar que el índice sea válido
            return todasLasRutas.get(index);
        }

        return null; // Si el índice está fuera de rango
    }

    //Metodo que actualiza el tiempo si hubo un evento.
    public Ruta ActualizarTiempoPorEvento(int numero){
        Ruta ruta = null;
        int tamanyo = this.contarRutas(); //Cant rutas
        if(numero == 5 || numero == 2){ //Si hubo un evento
            int aristanumero = new Random().nextInt(tamanyo); //Indice de arista random
            ruta = getIndexRuta(aristanumero); //Busca la ruta con el indice
            if(ruta != null) { //Si no fue null
                ruta.setTiempo(ruta.getTiempo() * numero); //Multiplica por el valor del evento
                ruta.setCosto(Ruta.calculoDeCosto(ruta.getDestino(),ruta.getDistancia(),ruta.getDestino().getCostoBase()));
                ruta.setPonderacion(Ruta.CalculoPonderacionArista(ruta.getCosto(),ruta.getTiempo()));
            }
        }
        return ruta;
    }

    public Map<Estacion, List<Ruta>> getWeb() {
        return web;
    }
}