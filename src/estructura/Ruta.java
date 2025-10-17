package estructura;

/*
Clase: Ruta
Objetivo: Representa las aristas que unen cada estacion
*/
public class Ruta {
    private Estacion destino;
    private String id;
    private int distancia;
    private int tiempo;
    private double costo;

    public Ruta(Estacion destino, int distancia, String id) {
        this.destino = destino;
        this.id = id;
        this.distancia = distancia;
        this.tiempo = distancia / destino.getVelocidad();
        this.costo = calculoDeCosto(destino, distancia, destino.getCostoBase());
    }

    // Calculo rudimentario para el costo de diferentes estaciones.
    public static double calculoDeCosto(Estacion destino, int distancia, double costoBase) {
        double total = costoBase;
        if(destino instanceof EstacionDeTren) {
            total += costoBase * (distancia / 10f);
        } else if(destino instanceof EstacionDeMetro) {
            total += costoBase * (distancia / 25f);
        } else if(destino instanceof ParadaDeBus) {
            total += costoBase * (distancia / 50f);
        }
        return total;
    }

    public Estacion getDestino() {
        return destino;
    }

    public void setDestino(Estacion destino) {
        this.destino = destino;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }
}
