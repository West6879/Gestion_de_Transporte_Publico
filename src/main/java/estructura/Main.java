package estructura;

import static util.Bellman_Ford.bellmanFordBusqueda;
import static util.Dijkstra.EncontrarMejoresRutas;

public class Main {

    public static void main(String[] args) {
        GrafoTransporte grafo = new GrafoTransporte();
        EstacionDeMetro santiago = new EstacionDeMetro("Santiago", 100);
        ParadaDeBus puertoPlata = new ParadaDeBus("Puerto Plata", 150);
        EstacionDeTren santoDomingo = new EstacionDeTren("Santo Domingo", 100);
        ParadaDeBus monteCristi = new ParadaDeBus("Monte Cristi", 100);
        EstacionDeMetro samana = new EstacionDeMetro("Samana", 200);

        grafo.agregarEstacion(santiago);
        grafo.agregarEstacion(puertoPlata);
        grafo.agregarEstacion(santoDomingo);
        grafo.agregarEstacion(monteCristi);
        grafo.agregarEstacion(samana);

        grafo.agregarRuta(santoDomingo, santiago, 150, "0001");
        grafo.agregarRuta(santiago, samana, 100, "0002");
        grafo.agregarRuta(samana, monteCristi, 50, "0003");
        grafo.agregarRuta(santoDomingo, monteCristi, 600, "0004");
        grafo.agregarRuta(samana, monteCristi, 50, "0005");



        grafo.imprimirGrafo();

        ResultadoRuta ruta1 = EncontrarMejoresRutas(grafo, santoDomingo, monteCristi);
        System.out.println(ruta1);
        ResultadoRuta ruta2 = bellmanFordBusqueda(grafo, santoDomingo, monteCristi);
        System.out.println(ruta2);


    }
}
