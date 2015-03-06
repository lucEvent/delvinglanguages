package com.delvinglanguages.core.game;

import java.util.ArrayList;

import com.delvinglanguages.core.DReference;

public class MatchGame extends Game {

	private static final long serialVersionUID = 712826081212762320L;

	public class QuestionModel {
		public DReference reference;
		public String[] answers;
		public Boolean[] correct;
	}
	
	private QuestionModel preguntaActual;

	public MatchGame(ArrayList<DReference> references) {
		super(references);
	}
	
	public QuestionModel nextQuestion(int nAnswers) {
		preguntaActual = new QuestionModel();

		DReference ref = nextReference();
		preguntaActual.reference = ref;
		preguntaActual.answers = new String[nAnswers];
		preguntaActual.correct = new Boolean[nAnswers];

		int randompos = nextInt(nAnswers);
		preguntaActual.answers[randompos] = ref.getTranslation();
		preguntaActual.correct[randompos] = true;
		for (int i = 0; i < nAnswers; ++i) {
			if (i != randompos) {
				DReference resp = references.get(nextInt(references.size()));
				if (resp == ref) {
					preguntaActual.correct[i] = true;
				} else {
					preguntaActual.correct[i] = false;
				}
				preguntaActual.answers[i] = resp.getTranslation();
			}
		}
		return preguntaActual;
	}
	
	public QuestionModel nextWord(boolean[] aciertos, int nAnswers) {
		preguntaActual = new QuestionModel();

		int cand = nextInt(references.size());

		while (aciertos[cand]) {
			cand = nextInt(aciertos.length);
		}
		DReference p = references.get(cand);
		preguntaActual.reference = p;
		preguntaActual.answers = new String[nAnswers];
		preguntaActual.correct = new Boolean[nAnswers];

		int cand2 = nextInt(nAnswers);
		preguntaActual.answers[cand2] = p.getTranslation();
		preguntaActual.correct[cand2] = true;
		for (int i = 0; i < nAnswers; ++i) {
			if (i != cand2) {
				DReference resp = references.get(nextInt(aciertos.length));
				if (resp == p) {
					preguntaActual.correct[i] = true;
				} else {
					preguntaActual.correct[i] = false;
				}
				preguntaActual.answers[i] = resp.getTranslation();
			}
		}
		return preguntaActual;
	}


}
