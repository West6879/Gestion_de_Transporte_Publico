package estructura;

import java.util.ArrayList;
import java.util.List;

/*
Clase: Servicio
Objetivo: Clase controladora, que maneja todos los datos con patron singleton.
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
    }

    // Metodo para conseguir la instancia del servicio.
    public static Servicio getInstance() {
        return servicio;
    }

    public static void main(String[] args) {
//        GrafoTransporte grafo = new GrafoTransporte();
//        Estacion santiago = new Estacion("Santiago", 100);
//        Estacion puertoPlata = new Estacion("Puerto Plata", 150);
//        Estacion santoDomingo = new Estacion("Santo Domingo", 100);
//        Estacion monteCristi = new Estacion("Monte Cristi", 100);
//        Estacion samana = new Estacion("Samana", 200);
//
//        grafo.agregarEstacion(santiago);
//        grafo.agregarEstacion(puertoPlata);
//        grafo.agregarEstacion(santoDomingo);
//        grafo.agregarEstacion(monteCristi);
//        grafo.agregarEstacion(samana);
//
//        grafo.agregarRuta(santoDomingo, santiago, 150, "0001");
//        grafo.agregarRuta(santiago, samana, 100, "0002");
//        grafo.agregarRuta(samana, monteCristi, 50, "0003");
//        grafo.agregarRuta(santoDomingo, monteCristi, 600, "0004");
//        grafo.agregarRuta(samana, monteCristi, 50, "0005");
//
//
//
//        grafo.imprimirGrafo();
//
//        ResultadoRuta ruta1 = EncontrarMejoresRutas(grafo, santoDomingo, monteCristi);
//        System.out.println(ruta1);
//        ResultadoRuta ruta2 = bellmanFordBusqueda(grafo, santoDomingo, monteCristi);
//        System.out.println(ruta2);
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
