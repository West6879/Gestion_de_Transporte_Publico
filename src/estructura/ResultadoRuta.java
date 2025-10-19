package estructura;

import java.util.ArrayList;
import java.util.List;

/*
Clase: ResultadoRuta
Objetivo: Servir como Objeto que describe el camino total y sus características
*/
public class ResultadoRuta {
    private List<Estacion> camino;
    private double distanciaTotal;
    private int tiempoTotal;
    private double costoTotal;
    private int transbordos;

    public ResultadoRuta(List<Estacion> camino, double distanciaTotal, int tiempoTotal, double costoTotal, int transbordos) {
        this.camino = new ArrayList<>(camino);
        this.distanciaTotal = distanciaTotal;
        this.tiempoTotal = tiempoTotal;
        this.costoTotal = costoTotal;
        this.transbordos = transbordos;
    }


    //Calcular y setear los datos faltantes de el resultado
    public void calcularMetricas(GrafoTransporte grafo) {
        this.distanciaTotal = calcularDistanciaTotal(grafo);
        this.tiempoTotal = calcularTiempoTotal(grafo);
        this.costoTotal = calcularCostoTotal(grafo);
    }


    //Calcula la distancia total sumando las distancias de cada ruta del camino
    //Ojo: Todas las funciones calcularAlgo... tienen un funcionamiento casi idéntico
    private double calcularDistanciaTotal(GrafoTransporte grafo) {
        double distancia = 0;
        // Recorrer cada par de estaciones consecutivas en el camino
        for (int i = 0; i < camino.size() - 1; i++) {
            Estacion origen = camino.get(i);
            Estacion destino = camino.get(i + 1);

            // Buscar la ruta que conecta estas dos estaciones
            for (Ruta ruta : grafo.web.get(origen)) {
                if (ruta.getDestino().equals(destino)) {
                    distancia += ruta.getDistancia();
                    break; // Encontrada la ruta, pasar al siguiente par
                }
            }
        }
        return distancia;
    }

    //Calcula el tiempo total sumando los tiempos de cada ruta del camino//
    private int calcularTiempoTotal(GrafoTransporte grafo) {
        int tiempo = 0;
        for (int i = 0; i < camino.size() - 1; i++) {
            Estacion origen = camino.get(i);
            Estacion destino = camino.get(i + 1);

            for (Ruta ruta : grafo.web.get(origen)) {
                if (ruta.getDestino().equals(destino)) {
                    tiempo += ruta.getTiempo();
                    break;
                }
            }
        }
        return tiempo;
    }

    //Calcula el costo total sumando los costos de cada ruta del camino//
    private double calcularCostoTotal(GrafoTransporte grafo) {
        double costo = 0;
        for (int i = 0; i < camino.size() - 1; i++) {
            Estacion origen = camino.get(i);
            Estacion destino = camino.get(i + 1);

            for (Ruta ruta : grafo.web.get(origen)) {
                if (ruta.getDestino().equals(destino)) {
                    costo += ruta.getCosto();
                    break;
                }
            }
        }
        return costo;
    }

    public List<Estacion> getCamino() {
        return camino;
    }

    public void setCamino(List<Estacion> camino) {
        this.camino = camino;
    }

    public double getDistanciaTotal() {
        return distanciaTotal;
    }

    public void setDistanciaTotal(double distanciaTotal) {
        this.distanciaTotal = distanciaTotal;
    }

    public int getTiempoTotal() {
        return tiempoTotal;
    }

    public void setTiempoTotal(int tiempoTotal) {
        this.tiempoTotal = tiempoTotal;
    }

    public double getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(double costoTotal) {
        this.costoTotal = costoTotal;
    }

    public int getTransbordos() {
        return transbordos;
    }

    public void setTransbordos(int transbordos) {
        this.transbordos = transbordos;
    }
}
