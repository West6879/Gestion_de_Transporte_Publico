package estructura;

import java.util.ArrayList;
import java.util.List;

/*
Clase: Servicio
Objetivo: Clase controladora, incorpora el mapa y guarda listas auxiliares de estaciones y rutas.
*/
public class Servicio {

    private static final Servicio servicio = new Servicio();
    private final GrafoTransporte mapa;
    private final List<Estacion> estaciones;
    private final List<Ruta> rutas;

    private Servicio() {
        this.mapa = new GrafoTransporte();
        this.estaciones = new ArrayList<>();
        this.rutas = new ArrayList<>();
        cargarDatos(); // Cargar datos de ejemplo al iniciar
    }

    // Metodo para conseguir la instancia del servicio.
    public static Servicio getInstance() {
        return servicio;
    }

    private void cargarDatos() {
        // Creación de estaciones (usa el constructor más completo de tu clase Estacion)
        Estacion santoDomingo = new Estacion("Santo Domingo", "Zona Sur", 18.4861, -69.9312, 100.0, 60, TipoEstacion.BUS);
        Estacion santiago = new Estacion("Santiago", "Zona Norte", 19.4792, -70.6931, 90.0, 55, TipoEstacion.METRO);
        Estacion puertoPlata = new Estacion("Puerto Plata", "Zona Costa", 19.7957, -70.6941, 80.0, 50, TipoEstacion.TREN);
        Estacion samana = new Estacion("Samana", "Zona Este", 19.2056, -69.3369, 120.0, 65, TipoEstacion.BUS);
        Estacion monteCristi = new Estacion("Monte Cristi", "Zona Frontera", 19.8576, -71.6496, 110.0, 53, TipoEstacion.METRO);

        // Añadir a la lista y al mapa
        estaciones.add(santoDomingo);
        estaciones.add(santiago);
        estaciones.add(puertoPlata);
        estaciones.add(samana);
        estaciones.add(monteCristi);

        for (Estacion e : estaciones) {
            mapa.agregarEstacion(e);
        }

        // Creación de rutas con sentido realista (solo usa los argumentos válidos según tu clase Ruta)
        // Constructor de Ruta válido: Ruta(Estacion origen, Estacion destino, int distancia)
        Ruta r1 = new Ruta(santoDomingo, santiago, 160);
        Ruta r2 = new Ruta(santiago, puertoPlata, 55);
        Ruta r3 = new Ruta(santoDomingo, samana, 140);
        Ruta r4 = new Ruta(santiago, samana, 120);
        Ruta r5 = new Ruta(samana, monteCristi, 230);
        Ruta r6 = new Ruta(santoDomingo, monteCristi, 295);
        Ruta r7 = new Ruta(puertoPlata, monteCristi, 110);

        rutas.add(r1);
        rutas.add(r2);
        rutas.add(r3);
        rutas.add(r4);
        rutas.add(r5);
        rutas.add(r6);
        rutas.add(r7);

        // Añadir rutas al grafo
        mapa.getWeb().get(santoDomingo).add(r1);
        mapa.getWeb().get(santiago).add(r2);
        mapa.getWeb().get(santoDomingo).add(r3);
        mapa.getWeb().get(santiago).add(r4);
        mapa.getWeb().get(samana).add(r5);
        mapa.getWeb().get(santoDomingo).add(r6);
        mapa.getWeb().get(puertoPlata).add(r7);
    }

    public GrafoTransporte getMapa() {
        return mapa;
    }

    public List<Estacion> getEstaciones() {
        return estaciones;
    }

    public List<Ruta> getRutas() {
        return rutas;
    }
}