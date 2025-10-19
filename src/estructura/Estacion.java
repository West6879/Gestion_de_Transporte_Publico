package estructura;
/*
Clase: Estación
Objetivo: Clase abstracta y padre de todos los tipos de estaciones
*/
public abstract class Estacion {
    private String id;
    private String nombre;
    private String zona;
    private double latitud;
    private double longitud;
    private double costoBase;
    private int velocidad;
    public Estacion(String id, String nombre, String zona, double latitud, double longitud,
                    double costoBase, int velocidad) {
        this.id = id;
        this.nombre = nombre;
        this.zona = zona;
        this.latitud = latitud;
        this.longitud = longitud;
        this.costoBase = costoBase;
        this.velocidad = velocidad;
    }

    // Constructor con solo nombre para probar en la terminal.
    public Estacion(String nombre) {
        this.id = null;
        this.nombre = nombre;
        this.zona = null;
        this.latitud = 0;
        this.longitud = 0;
        this.costoBase = 0;
        this.velocidad = 1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    @Override
    public abstract String toString();
}
