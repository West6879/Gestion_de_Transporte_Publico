package util;

import estructura.Estacion;
import estructura.GrafoTransporte;
import estructura.ResultadoRuta;
import estructura.Ruta;

import java.util.*;

import static util.Caminos.*;

public class Dijkstra {

    public enum Criterio {
        DISTANCIA,
        TIEMPO,
        COSTO,
        TRANSBORDOS
    }

    public static ResultadoRuta EncontrarMejorRuta(GrafoTransporte grafo, Estacion origen, Estacion destino, Criterio criterio) {
        if (!grafo.getWeb().containsKey(origen) || !grafo.getWeb().containsKey(destino) || origen.equals(destino)) {
            return null;
        }

        Map<Estacion, Double> valores = new HashMap<>();
        Map<Estacion, Estacion> predecesores = new HashMap<>();
        Map<Estacion, Integer> transbordos = new HashMap<>();
        Map<Estacion, UUID> lineaAnterior = new HashMap<>();
        Map<Estacion, String> tipoAnterior = new HashMap<>();

        for (Estacion estacion : grafo.getWeb().keySet()) {
            valores.put(estacion, Double.MAX_VALUE);
            transbordos.put(estacion, Integer.MAX_VALUE);
            lineaAnterior.put(estacion, null);
            tipoAnterior.put(estacion, null);
        }
        valores.put(origen, 0.0);
        transbordos.put(origen, 0);
        lineaAnterior.put(origen, null);
        tipoAnterior.put(origen, origen.getTipo().toString());

        PriorityQueue<Estacion> cola = new PriorityQueue<>(Comparator.comparingDouble(valores::get));
        cola.add(origen);

        while (!cola.isEmpty()) {
            Estacion actual = cola.poll();
            if (actual.equals(destino)) break;
            if (!grafo.getWeb().containsKey(actual)) continue;

            for (Ruta ruta : grafo.getWeb().get(actual)) {
                Estacion vecino = ruta.getDestino();
                double valorRuta = 0;
                switch (criterio) {
                    case DISTANCIA: valorRuta = ruta.getDistancia(); break;
                    case TIEMPO: valorRuta = ruta.getTiempo(); break;
                    case COSTO: valorRuta = ruta.getCosto(); break;
                    case TRANSBORDOS: valorRuta = 1; break;
                }

                double nuevoValor = valores.get(actual) + valorRuta;
                int nuevosTransbordos = transbordos.get(actual);
                UUID lineaPrevia = lineaAnterior.get(actual);
                String tipoPrevio = tipoAnterior.get(actual);
                boolean hayTransbordo = false;

                if (criterio == Criterio.TRANSBORDOS) {
                    String tipoVecino = vecino.getTipo().toString();
                    if (tipoPrevio != null && !tipoPrevio.equals(tipoVecino)) {
                        nuevosTransbordos++;
                        hayTransbordo = true;
                    }
                } else {
                    if (lineaPrevia != null && !lineaPrevia.equals(ruta.getId())) {
                        nuevosTransbordos++;
                        hayTransbordo = true;
                    }
                }

                if (nuevoValor < valores.get(vecino) ||
                        (nuevoValor == valores.get(vecino) && nuevosTransbordos < transbordos.get(vecino))) {
                    valores.put(vecino, nuevoValor);
                    transbordos.put(vecino, nuevosTransbordos);
                    predecesores.put(vecino, actual);
                    lineaAnterior.put(vecino, ruta.getId());
                    tipoAnterior.put(vecino, vecino.getTipo().toString());
                    cola.remove(vecino);
                    cola.add(vecino);
                }
            }
        }

        return finalizacionRuta(grafo, predecesores, transbordos, origen, destino);
    }
}