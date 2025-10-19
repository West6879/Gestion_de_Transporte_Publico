package estructura;
/*
Clase: EstacionDeMetro
Objetivo: Tipo de estación
*/
public class EstacionDeMetro extends Estacion {
    public EstacionDeMetro(String id, String nombre, String zona, double latitud, double longitud, double costoBase, int velocidad) {
        super(id, nombre, zona, latitud, longitud, costoBase, velocidad);
    }

    public EstacionDeMetro(String nombre) {
        super(nombre);
    }

    @Override
    public String toString() {
        return "EstacionDeMetro:'" + this.getNombre() + '\'';
    }
}
