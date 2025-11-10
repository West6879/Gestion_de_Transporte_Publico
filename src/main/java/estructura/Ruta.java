package estructura;

import java.util.Objects;
import java.util.UUID;

/*
Clase: Ruta
Objetivo: Representa las aristas que unen cada estación
*/
public class Ruta {
    private Estacion origen;
    private Estacion destino;
    private UUID id;
    private int distancia;
    private int tiempo;
    private double costo;
    private float ponderacion;

    public Ruta(Estacion origen,Estacion destino, int distancia) {
        this.origen = origen;
        this.destino = destino;
        this.id = UUID.randomUUID();
        this.distancia = distancia;
        this.tiempo = Math.max(1, destino.getVelocidad() == 0 ? 1 : distancia / destino.getVelocidad());
        this.costo = calculoDeCosto(destino, distancia, destino.getCostoBase());
        this.ponderacion = CalculoPonderacionArista();
    }

    // Constructor vacío para usarlo al cargar rutas de la base de datos.
    public Ruta() {

    }

    public static double calculoDeCosto(Estacion destino, int distancia, double costoBase) {
        double total = costoBase;
        int tipo = destino.getTipo().ordinal();
        int divisor = Math.max(1, tipo * 10);  // Evita división por cero
        total += costoBase * ((double) distancia / divisor);
        return total;
    }

    //Calculo previo a contar los transbordos de la ponderacion de una arista
    private float CalculoPonderacionArista(){
        float suma = (float)(this.costo + this.tiempo);
        return suma / 2.0f;
    }

    public float getPonderacion() {
        return ponderacion;
    }

    public void setPonderacion(float ponderacion) {
        this.ponderacion = ponderacion;
    }

    public Estacion getOrigen() {
        return origen;
    }

    public void setOrigen(Estacion origen) {
        this.origen = origen;
    }

    public Estacion getDestino() {
        return destino;
    }

    public void setDestino(Estacion destino) {
        this.destino = destino;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    // Metodo para conseguir el nombre de la estación origen.
    public String getOrigenNombre() {
        return origen.getNombre();
    }

    // Metodo para conseguir el nombre de la estación destino.
    public String getDestinoNombre() {
        return destino.getNombre();
    }

    @Override
    public String toString() {
        return "Ruta: " + origen + " -> " + destino;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ruta ruta = (Ruta) o;
        return Objects.equals(id, ruta.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
