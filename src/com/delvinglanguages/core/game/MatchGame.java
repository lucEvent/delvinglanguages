package com.delvinglanguages.core.game;

import java.util.ArrayList;

import com.delvinglanguages.core.DReference;
import com.delvinglanguages.kernel.set.TestReferenceStates;

public class MatchGame extends Game {

	private static final long serialVersionUID = 712826081212762320L;

	public class QuestionModel {
		public DReference reference;
		public String[] answers;
		public Boolean[] correct;
	}

	public MatchGame(ArrayList<DReference> references) {
		super(references);
	}

	public QuestionModel nextQuestion(int nAnswers) {
		QuestionModel res = new QuestionModel();

		DReference ref = nextReference();
		res.reference = ref;
		res.answers = new String[nAnswers];
		res.correct = new Boolean[nAnswers];

		int randompos = nextInt(nAnswers);
		res.answers[randompos] = ref.getTranslation();
		res.correct[randompos] = true;
		for (int i = 0; i < nAnswers; ++i) {
			if (i != randompos) {
				DReference resp = references.get(nextInt(references.size()));
				if (resp == ref) {
					res.correct[i] = true;
				} else {
					res.correct[i] = false;
				}
				res.answers[i] = resp.getTranslation();
			}
		}
		return res;
	}

	public QuestionModel nextQuestion(TestReferenceStates refstates,
			int nAnswers) {
		QuestionModel res = new QuestionModel();

		int cand = nextPosition(refstates);
		DReference p = references.get(cand);
		res.reference = p;
		res.answers = new String[nAnswers];
		res.correct = new Boolean[nAnswers];

		int cand2 = nextInt(nAnswers);
		res.answers[cand2] = p.getTranslation();
		res.correct[cand2] = true;
		for (int i = 0; i < nAnswers; ++i) {
			if (i != cand2) {
				DReference resp = references.get(nextInt(refstates.size()));
				if (resp == p) {
					res.correct[i] = true;
				} else {
					res.correct[i] = false;
				}
				res.answers[i] = resp.getTranslation();
			}
		}
		return res;
	}

}
