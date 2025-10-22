package estructura;

/*
Clase: EstacionDeTren
Objetivo: Tipo de estación
*/
public class EstacionDeTren extends Estacion {
    public EstacionDeTren(String id, String nombre, String zona, double latitud, double longitud, double costoBase, int velocidad) {
        super(id, nombre, zona, latitud, longitud, costoBase, velocidad);
    }

    public EstacionDeTren(String nombre, double costoBase) {
        super(nombre, costoBase);
    }

    @Override
    public String toString() {
        return "EstacionDeTren:'" + this.getNombre() + '\'';
    }
}
