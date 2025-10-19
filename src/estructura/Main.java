package estructura;

public class Main {

    public static void main(String[] args) {
        GrafoTransporte grafo = new GrafoTransporte();
        EstacionDeMetro santiago = new EstacionDeMetro("Santiago");
        ParadaDeBus puertoPlata = new ParadaDeBus("Puerto Plata");
        EstacionDeTren santoDomingo = new EstacionDeTren("Santo Domingo");
        ParadaDeBus monteCristi = new ParadaDeBus("Monte Cristi");
        EstacionDeMetro samana = new EstacionDeMetro("Samana");

        grafo.agregarEstacion(santiago);
        grafo.agregarEstacion(puertoPlata);
        grafo.agregarEstacion(santoDomingo);
        grafo.agregarEstacion(monteCristi);
        grafo.agregarEstacion(samana);

        grafo.agregarRuta(santoDomingo, santiago, 100, "0001");
        grafo.agregarRuta(santiago, puertoPlata, 80, "0002");
        grafo.agregarRuta(santoDomingo, puertoPlata, 200, "0003");
        grafo.agregarRuta(monteCristi, samana, 250, "0004");
        grafo.agregarRuta(samana, santoDomingo, 300, "0005");
        grafo.agregarRuta(puertoPlata, santiago, 80, "0006");

        grafo.imprimirGrafo();



    }
}
