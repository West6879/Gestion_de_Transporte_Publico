package estructura;

/*
Clase: ParadaDeBus
Objetivo: Tipo de estación
*/
public class ParadaDeBus extends Estacion {

    public ParadaDeBus(String id, String nombre, String zona, double latitud, double longitud, double costoBase, int velocidad) {
        super(id, nombre, zona, latitud, longitud, costoBase, velocidad);
    }

    public ParadaDeBus(String nombre) {
        super(nombre);
    }

    @Override
    public String toString() {
        return "ParadaDeBus:'" + this.getNombre() + '\'';
    }

}
