package util;

import java.util.Random;

/*
Clase: Randomizacion
Objetivo: Clase de utilidad para randomización.
*/
public class Randomizacion {

    // Constantes para mejor legibilidad.
    public static final int CHOQUE = 5;
    public static final int HUBO_EVENTO = 2;
    public static final int NO_HUBO_EVENTO = 1;

    public static final int PORCENTAJE_CHOQUE = 20;
    public static final int PORCENTAJE_EVENTO = 95;

    // Combinacion de los metodos para una sola llamada.
    public static int calcularEvento() {
        return huboEvento(randomizado());
    }

    //Devuelve numero random 1 al 100
    public static int randomizado() {
        return new Random().nextInt(100) + 1;//Numero random del 1 al 100;
    }

    //Recibe un numero que eran random y en base a el devuelve un numero que indica si hubo o no evento
    public static int huboEvento(int numero){
        if (numero < PORCENTAJE_CHOQUE){//Porcentaje 20 por ciento para choques
            return CHOQUE;
        }

        if(numero > PORCENTAJE_EVENTO){//Porcentaje eventos variados
            return HUBO_EVENTO;
        }
        return NO_HUBO_EVENTO;
    }
}
