package com.delvinglanguages.kernel.game;


import android.util.Pair;

import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.util.DReferences;

public class MatchGame extends Game {

    public class RoundData {
        public DReference reference;
        public Pair<String, Boolean>[] options;
    }

    public MatchGame(DReferences references) {
        super(references);
    }

    public RoundData nextRound(int n_options, int own_options_max) { // own_options_max <= n_options
        return nextRound(nextReference(), n_options, own_options_max);
    }

    public RoundData nextRound(DReference reference, int n_options, int own_options_max) { // own_options_max <= n_options
        RoundData res = new RoundData();

        res.reference = reference;
        res.options = new Pair[n_options];

        String[] translations = res.reference.getTranslations();
        own_options_max = Math.min(own_options_max, translations.length);
        for (int i = 0; i < own_options_max; i++) {
            int randomPosition;
            do {
                randomPosition = nextInt(n_options);
            } while (res.options[randomPosition] != null);

            int randomTranslationPosition;
            do {
                randomTranslationPosition = nextInt(translations.length);
            } while (translations[randomTranslationPosition] == null);

            res.options[randomPosition] = new Pair<>(translations[randomTranslationPosition], true);
            translations[randomTranslationPosition] = null;
        }

        for (int i = 0; i < n_options; ++i)
            if (res.options[i] == null) {
                String[] hooks = references.get(nextInt(references.size())).getTranslations();
                res.options[i] = new Pair<>(hooks[nextInt(hooks.length)], false);//podria poner una solucion valida a false // TODO: 02/04/2016
            }

        return res;
    }

}
