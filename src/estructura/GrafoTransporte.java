package estructura;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Comparator;
import java.util.PriorityQueue;

/*
Clase: Grafo Transporte
Objetivo: Crear y manejar Crud y otras funciones del grafo de aristas y paradas
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

    public int contarEstaciones() {
        return web.size();
    }

    //Cuenta la cantidad de rutas que hay en la web
    public int contarRutas() {
        int totalRutas = 0;
        for (List<Ruta> rutas : web.values()) {
            totalRutas += rutas.size();
        }
        return totalRutas;
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

    //Metodo que actualiza el tiempo si hubo un evento
    public void ActualizarTiempoPorEvento(int numero){
        int tamanyo = this.contarRutas(); //Cant rutas
        if(numero == 5 || numero == 2){ //Si hubo un evento
            int aristanumero = new Random().nextInt(tamanyo); //Indice de arista random
            Ruta ruta = getIndexRuta(aristanumero); //Busca la ruta con el indice
            if(ruta != null) { //Si no fue null
                ruta.setTiempo(ruta.getTiempo() * numero); //Multiplica por el valor del evento
            }
        }
    }

    //Metodo para encontrar las mejores rutas de una estacion a ottra
    public ResultadoRuta EncontrarMejoresRutas(Estacion origen, Estacion destino) {
        // Validacion de estaciones existentes
        if (!web.containsKey(origen) || !web.containsKey(destino) || origen.equals(destino)) {
            return null;
        }

        // Estructuras para el algoritmo
        Map<Estacion, Float> ponderaciones = new HashMap<>();
        Map<Estacion, Estacion> predecesores = new HashMap<>();
        Map<Estacion, String> lineaAnterior = new HashMap<>();
        Map<Estacion, Integer> transbordos = new HashMap<>();

        // Inicializacion de valores
        for (Estacion estacion : web.keySet()) {
            ponderaciones.put(estacion, Float.MAX_VALUE);
            transbordos.put(estacion, Integer.MAX_VALUE);
        }
        ponderaciones.put(origen, 0.0f);
        transbordos.put(origen, 0);

        // Configuracion de la cola de prioridad
        PriorityQueue<Estacion> cola = new PriorityQueue<>(
                Comparator.comparingDouble(estacion ->  (double)ponderaciones.get(estacion))
        );
        cola.add(origen);

        // Procesamiento de nodos
        while (!cola.isEmpty()) {
            Estacion actual = cola.poll();

            // Condicion de término
            if (actual.equals(destino)) break;

            // Verificacion de rutas existentes
            if (!web.containsKey(actual)) continue;

            // Exploracion de rutas adyacentes
            for (Ruta ruta : web.get(actual)) {
                Estacion vecino = ruta.getDestino();

                // Calculo de transbordos
                int nuevosTransbordos = transbordos.get(actual);
                String lineaPrevia = lineaAnterior.get(actual);
                boolean hayTransbordo = false;

                if (lineaPrevia != null && !lineaPrevia.equals(ruta.getId())) {
                    nuevosTransbordos++;
                    hayTransbordo = true;
                }

                // Calculo de nueva ponderacion
                float ponderacionBase = ruta.getPonderacion();
                float penalizacionTransbordo = hayTransbordo ? ponderacionBase * 0.3f : 0;
                float nuevaPonderacion = ponderaciones.get(actual) + ponderacionBase + penalizacionTransbordo;

                // Actualizacion si encontramos mejor camino
                if (nuevaPonderacion < ponderaciones.get(vecino)) {
                    ponderaciones.put(vecino, nuevaPonderacion);
                    transbordos.put(vecino, nuevosTransbordos);
                    predecesores.put(vecino, actual);
                    lineaAnterior.put(vecino, ruta.getId());

                    // Actualizacion en cola de prioridad
                    cola.remove(vecino);
                    cola.add(vecino);
                }
            }
        }

        // Reconstruccion y validacion del camino
        List<Estacion> camino = reconstruirCamino(predecesores, destino);
        if (camino.isEmpty() || !camino.get(0).equals(origen)) {
            return null;
        }

        // Calculo de metricas finales
        Integer transbordosDestino = transbordos.get(destino);
        ResultadoRuta resultado = new ResultadoRuta(camino, 0, 0, 0,
                transbordosDestino != null ? transbordosDestino : 0);
        resultado.calcularMetricas(this);

        return resultado;
    }

    // Reconstruye el camino desde el destino hasta el origen
    private List<Estacion> reconstruirCamino(Map<Estacion, Estacion> predecesores, Estacion destino) {
        List<Estacion> camino = new ArrayList<>();
        Estacion actual = destino;

        // Recorrido inverso desde destino hasta origen
        while (actual != null) {
            camino.add(0, actual);  // Inserta al inicio para mantener orden
            actual = predecesores.get(actual);
        }

        return camino;
    }
}
