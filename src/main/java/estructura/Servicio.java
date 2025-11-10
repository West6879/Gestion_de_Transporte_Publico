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