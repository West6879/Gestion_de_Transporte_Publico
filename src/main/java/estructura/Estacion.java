package estructura;

import java.util.Objects;
import java.util.UUID;

/*
Clase: Estación
Objetivo: Clase abstracta y padre de todos los tipos de estaciones
*/
public class Estacion {
    private UUID id;
    private String nombre;
    private String zona;
    private double latitud;
    private double longitud;
    private double costoBase;
    private int velocidad;
    private TipoEstacion tipo;

    public Estacion(String nombre, String zona, double latitud, double longitud,
                    double costoBase, int velocidad, TipoEstacion tipo) {
        this.id = UUID.randomUUID();
        this.nombre = nombre;
        this.zona = zona;
        this.latitud = latitud;
        this.longitud = longitud;
        this.costoBase = costoBase;
        this.velocidad = velocidad;
        this.tipo = tipo;
    }

    // Constructor con solo nombre para probar en la terminal.
    public Estacion(String nombre, double costoBase, String tipoEstacion) {
        this.id = null;
        this.nombre = nombre;
        this.zona = null;
        this.latitud = 0;
        this.longitud = 0;
        this.costoBase = costoBase;
        this.velocidad = 1;
        this.tipo = TipoEstacion.valueOf(tipoEstacion);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getCostoBase() {
        return costoBase;
    }

    public void setCostoBase(double costoBase) {
        this.costoBase = costoBase;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    public TipoEstacion getTipo() {
        return tipo;
    }

    public void setTipo(TipoEstacion tipo) {
        this.tipo = tipo;
    }

    // Metodo para conseguir la posición combinada para mostrar en javaFX.
    public String getPosicion() {
        return String.format("(%.2f,%.2f)", longitud, latitud);
    }

    @Override
    public String toString() {
        return tipo.toString() + ":'" + this.getNombre() + "'";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Estacion estacion = (Estacion) o;
        return Objects.equals(id, estacion.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
