package estructura;

import database.EstacionDAO;
import database.RutaDAO;

import java.util.*;

/*
Clase: Servicio
Objetivo: Clase controladora, incorpora el mapa y guarda listas auxiliares de estaciones y rutas.
*/
public class Servicio {

    private static final Servicio servicio = new Servicio();
    private final GrafoTransporte mapa;
    private final Map<UUID, Estacion> estaciones;
    private final Map<UUID, Ruta> rutas;

    public static void main(String[] args) {

        Servicio servicio = Servicio.getInstance();

        System.out.println("Estaciones cargadas:");
        for (Estacion e : servicio.getEstaciones().values()) {
            System.out.println(e);
        }
        System.out.println();

        System.out.println("Rutas cargadas:");
        for (Ruta r : servicio.getRutas().values()) {
            System.out.println(r);
        }

        System.out.println("Total estaciones: " + servicio.getEstaciones().size());
        System.out.println("Total rutas: " + servicio.getRutas().size());
        System.out.println("Grafo cargado:");
        servicio.getMapa().imprimirGrafo();
    }

    private Servicio() {
        this.mapa = new GrafoTransporte();
        this.estaciones = new HashMap<>();
        this.rutas = new HashMap<>();

        cargarDatos(); // Cargar de la base de datos.
    }

    // Metodo para conseguir la instancia del servicio.
    public static Servicio getInstance() {
        return servicio;
    }

    // Metodo para cargar todos los datos de la base de datos.
    private void cargarDatos() {
        EstacionDAO estacionDAO = EstacionDAO.getInstance();
        RutaDAO rutaDAO = RutaDAO.getInstance();

        this.estaciones.putAll(estacionDAO.findAll());
        // Las rutas dependen de las estaciones por eso se manda el mapa de estaciones para cargar las rutas.
        this.rutas.putAll(rutaDAO.findAll(estaciones));

        // Cargar todas las estaciones y rutas al grafo.
        for(Estacion estacion : estaciones.values()) {
            this.mapa.agregarEstacion(estacion);
        }
        for(Ruta ruta : rutas.values()) {
            this.mapa.agregarRuta(ruta);
        }
    }

    // Metodo para eliminar una estacion del mapa y todas sus rutas en el mapa de rutas.
    public void eliminarEstacion(Estacion estacion) {
        // Usar un iterator para recorrer el mapa de rutas y eliminar las rutas que contengan la estacion.
        Iterator<Map.Entry<UUID, Ruta>> it = this.rutas.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<UUID, Ruta> entrada = it.next();
            Ruta ruta = entrada.getValue();
            if(ruta.getOrigen().equals(estacion) ||  ruta.getDestino().equals(estacion)) {
                it.remove();
            }
        }
        // Eliminar la estacion del mapa de estaciones al final.
        estaciones.remove(estacion.getId());
    }

    // Metodo para conseguir la cantidad de estaciones de cada tipo para mostrar como una estadística.
    public int cantidadTipoEstaciones(TipoEstacion tipo) {
        int total = 0;
        for(Estacion estacion : this.estaciones.values()) {
            if(estacion.getTipo().equals(tipo)) {
                total++;
            }
        }
        return total;
    }

    // Metodo para calcular la cantidad de rutas en un intervalo de costo.
    public int cantRutasCosto(double limInferior, double limSuperior) {
        int total = 0;
        for(Ruta ruta : this.rutas.values()) {
            if(ruta.getCosto() >= limInferior && ruta.getCosto() < limSuperior) {
                total++;
            }
        }
        return total;
    }

    // Metodo que retorna el nombre y la cantidad de rutas de las estaciones con más rutas.
    public Map<Estacion, Integer> estacionesConMasRutas() {
        Map<Estacion, Integer> masRutas = new HashMap<>();
        List<Estacion> lista = new ArrayList<>(this.estaciones.values()); // Cojer las estaciones y ponerlas en una lista.
        // Organizar la lista con base en las estaciones con más rutas.
        lista.sort(Comparator.comparingInt(Estacion::getCantRutas).reversed());
        for(int i = 0; i < lista.size() && i < 7; i++) {
            masRutas.put(lista.get(i), lista.get(i).getCantRutas());
        }
        return masRutas;
    }

    // Metodo para actualizar todas las rutas de la estación cuando se cambia.
    public void actualizarRutasPorEstacion(Estacion estacion) {
        for(Ruta ruta : this.mapa.rutasDeEstacion(estacion)) {
            ruta.setOrigen(estacion);
            RutaDAO.getInstance().update(ruta);
        }
    }

    public GrafoTransporte getMapa() {
        return mapa;
    }

    public Map<UUID, Estacion> getEstaciones() {
        return estaciones;
    }

    public Map<UUID, Ruta> getRutas() {
        return rutas;
    }
}