package com.delvinglanguages.kernel.util;

public class Statistics {
    //// TODO: 08/04/2016  More complete statistics :)
    public final int id;

    public int intentos;
    public int aciertos1;
    public int aciertos2;
    public int aciertos3;
    public int fallos;

    public Statistics(int id)
    {
        this(id, 0, 0, 0, 0, 0);
    }

    public Statistics(int id, int intentos, int aciertos1, int aciertos2, int aciertos3, int fallos)
    {
        this.id = id;
        this.intentos = intentos;
        this.aciertos1 = aciertos1;
        this.aciertos2 = aciertos2;
        this.aciertos3 = aciertos3;
        this.fallos = fallos;
    }

    public float porcentageAcertadas1()
    {
        return intentos == 0 ? 0 : (float) (aciertos1 * 100) / (float) intentos;
    }

    public float porcentageAcertadas2()
    {
        return intentos == 0 ? 0 : (float) (aciertos2 * 100) / (float) intentos;
    }

    public float porcentageAcertadas3()
    {
        return intentos == 0 ? 0 : (float) (aciertos3 * 100) / (float) intentos;
    }

    public float porcentageFalladas()
    {
        return intentos == 0 ? 0 : (float) (fallos * 100) / (float) intentos;
    }

    public void nuevoIntento(int resultado)
    {
        intentos++;
        switch (resultado) {
            case 1:
                aciertos1++;
                break;
            case 2:
                aciertos2++;
                break;
            case 3:
                aciertos3++;
                break;
            default:
                fallos++;
        }
    }

    public void reset()
    {
        intentos = 0;
        aciertos1 = 0;
        aciertos2 = 0;
        aciertos3 = 0;
        fallos = 0;
    }

}
