package estructura;
import java.util.ArrayList;

public class MatrizDistMinimas {
    //El array sirve para saber que numero es cada nodo
    private ArrayList<Estacion> estaciones;

    //La matriz es solo de los valores minimos de distancia
    //Pero la primeraa fila y columna se usa para identificar la relacion entre nodo y nodo
    private float[][] distancias;

   public MatrizDistMinimas() {//Inicializacion
        estaciones = new ArrayList<>();
        distancias = new float[0][0];
    }
    //Metodo que agrega estacion al array
    public void agregarEstacion(Estacion estacion) {
        estaciones.add(estacion);
        redimensionar();
    }

    private void redimensionar() {
        int nuevoTamanyo = estaciones.size();
        float[][] nuevaMatriz = new float[nuevoTamanyo][nuevoTamanyo];

        // Copiar datos antiguos si existen
        for (int i = 0; i < Math.min(distancias.length, nuevoTamanyo); i++) {
            for (int j = 0; j < Math.min(distancias.length, nuevoTamanyo); j++) {
                nuevaMatriz[i][j] = distancias[i][j];
            }
        }

        distancias = nuevaMatriz;
    }

    public void setDistancia(int i, int j, float distancia) {
        distancias[i][j] = distancia;
    }

    public float getDistancia(int i, int j) {
        return distancias[i][j];
    }

    public Estacion getEstacion(int i) {
        return estaciones.get(i);
    }

    public int getTamanyo() {
        return estaciones.size();
    }
}