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
    private double tiempo;
    private double costo;
    private float ponderacion;

    public Ruta(Estacion origen,Estacion destino, int distancia) {
        this.origen = origen;
        this.destino = destino;
        this.id = UUID.randomUUID();
        this.distancia = distancia;
        this.tiempo = origen.getVelocidad() == 0 ? 1D : (double) distancia / origen.getVelocidad();
        this.costo = calculoDeCosto(origen, distancia, origen.getCostoBase());
        this.ponderacion = CalculoPonderacionArista(costo, tiempo);
    }

    // Constructor vacío para usarlo al cargar rutas de la base de datos.
    public Ruta() {

    }

    public static double calculoDeCosto(Estacion origen, int distancia, double costoBase) {
        double total = costoBase;
        int tipo = origen.getTipo().ordinal() + 1; // Más uno porque Ordinal retorna comenzando en 0.
        int divisor = Math.max(1, tipo * 100);  // Evita división por cero
        total += costoBase * ((double) distancia / divisor);
        return total;
    }

    //Calculo previo a contar los transbordos de la ponderacion de una arista
    public static float CalculoPonderacionArista(double costo, double tiempo){
        float suma = (float)(costo + tiempo);
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
        this.tiempo = origen.getVelocidad() == 0 ? 1D : (double) distancia / origen.getVelocidad();
        this.costo = calculoDeCosto(origen, distancia, origen.getCostoBase());
        this.ponderacion = CalculoPonderacionArista(costo, tiempo);
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

    public double getTiempo() {
        return tiempo;
    }

    public void setTiempo(double tiempo) {
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

    public String getTiempoCol() {
        int horas = (int) this.tiempo;
        double minutos = (this.tiempo - horas) * 60;
        return String.format("%02d:%02.0f", horas, minutos);
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
