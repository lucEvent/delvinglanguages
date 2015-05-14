package com.delvinglanguages.kernel;

public class Estadisticas {

	public final int id;

	public int intentos;
	public int aciertos1;
	public int aciertos2;
	public int aciertos3;
	public int fallos;

	public Estadisticas(int id) {
		this.id = id;
		intentos = 0;
		aciertos1 = 0;
		aciertos2 = 0;
		aciertos3 = 0;
		fallos = 0;
	}

	public Estadisticas(int id, int nPI, int nPA1, int nPA2, int nPA3, int nPF) {
		this.id = id;
		intentos = nPI;
		aciertos1 = nPA1;
		aciertos2 = nPA2;
		aciertos3 = nPA3;
		fallos = nPF;
	}

	public float porcentageAcertadas1() {
		if (intentos == 0)
			return 0;
		return (float) aciertos1 / (float) intentos;
	}

	public float porcentageAcertadas2() {
		if (intentos == 0)
			return 0;
		return (float) aciertos2 / (float) intentos;
	}

	public float porcentageAcertadas3() {
		if (intentos == 0)
			return 0;
		return (float) aciertos3 / (float) intentos;
	}

	public float porcentageFalladas() {
		if (intentos == 0)
			return 0;
		return (float) fallos / (float) intentos;
	}

	public void nuevoIntento(int resultado) {
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

	public void clear() {
		intentos = 0;
		aciertos1 = 0;
		aciertos2 = 0;
		aciertos3 = 0;
		fallos = 0;
	}

}
