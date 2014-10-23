package com.delvinglanguages.core;

public class Estadisticas {

	public final int id;

	public int npIntentadas;
	public int npAcertadas1;
	public int npAcertadas2;
	public int npAcertadas3;
	public int npFalladas;

	// Constructora
	public Estadisticas(int id) {
		this.id = id;
		npIntentadas = 0;
		npAcertadas1 = 0;
		npAcertadas2 = 0;
		npAcertadas3 = 0;
		npFalladas = 0;
	}

	public Estadisticas(int id, int nPI, int nPA1, int nPA2, int nPA3, int nPF) {
		this.id = id;
		npIntentadas = nPI;
		npAcertadas1 = nPA1;
		npAcertadas2 = nPA2;
		npAcertadas3 = nPA3;
		npFalladas = nPF;
	}

	// Consultoras

	public float porcentageAcertadas1() {
		if (npIntentadas == 0)
			return 0;
		return (float) npAcertadas1 / (float) npIntentadas;
	}

	public float porcentageAcertadas2() {
		if (npIntentadas == 0)
			return 0;
		return (float) npAcertadas2 / (float) npIntentadas;
	}

	public float porcentageAcertadas3() {
		if (npIntentadas == 0)
			return 0;
		return (float) npAcertadas3 / (float) npIntentadas;
	}

	public float porcentageFalladas() {
		if (npIntentadas == 0)
			return 0;
		return (float) npFalladas / (float) npIntentadas;
	}

	// Modificadoras
	public void nuevoIntento(int resultado) {
		npIntentadas++;
		switch (resultado) {
		case 1:
			npAcertadas1++;
			break;
		case 2:
			npAcertadas2++;
			break;
		case 3:
			npAcertadas3++;
			break;
		default:
			npFalladas++;
		}
	}

	public void clear() {
		npIntentadas = 0;
		npAcertadas1 = 0;
		npAcertadas2 = 0;
		npAcertadas3 = 0;
		npFalladas = 0;
	}

}
