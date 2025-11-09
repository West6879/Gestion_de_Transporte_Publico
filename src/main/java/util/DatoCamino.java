package util;

import estructura.Estacion;
import java.util.UUID;

/*
  Clase: DatoCamino
  Objetivo: Estructura de datos para almacenar la información completa
  de un camino parcial desde el origen hasta una estación,
  permitiendo encontrar los Top 3 caminos en una sola ejecución.
 */
public class DatoCamino implements Comparable<DatoCamino> {

    // La estación a la que llega este camino parcial
    public final Estacion estacionActual;

    // El valor acumulado del camino (Distancia, Costo, Tiempo, etc.)
    public final double valor;

    // La estación inmediatamente anterior en este camino
    public final Estacion predecesor;

    // Número de transbordos acumulados
    public final int transbordos;

    // ID de la última ruta/línea utilizada
    public final UUID lineaAnterior;

    // Tipo de la estación anterior (necesario para el criterio de Transbordos)
    public final String tipoAnterior;

    public DatoCamino(Estacion actual, double val, Estacion pred, int trans, UUID linea, String tipo) {
        this.estacionActual = actual;
        this.valor = val;
        this.predecesor = pred;
        this.transbordos = trans;
        this.lineaAnterior = linea;
        this.tipoAnterior = tipo;
    }

    /*
     Compara este DatoCamino con otro.
     Prioriza el menor 'valor' (criterio principal de Dijkstra).
     Usa el menor 'transbordos' como criterio de desempate.
     */
    @Override
    public int compareTo(DatoCamino other) {
        // 1. Criterio Principal: Valor (menor es mejor)
        if (this.valor != other.valor) {
            return Double.compare(this.valor, other.valor);
        }

        // 2. Criterio de Desempate: Transbordos (menor es mejor)
        return Integer.compare(this.transbordos, other.transbordos);
    }
}