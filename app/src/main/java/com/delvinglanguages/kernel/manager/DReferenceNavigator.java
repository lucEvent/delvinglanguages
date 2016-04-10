package com.delvinglanguages.kernel.manager;

import android.content.Context;
import android.view.View;

import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.LanguageManager;
import com.delvinglanguages.kernel.util.DReferences;

import java.util.Locale;

public class DReferenceNavigator {

    private Language language;

    private int index;

    private DReferences references;

    private PronunciationManager delvedPronunciationManager, nativePronunciationManager;


    public DReferenceNavigator(Context context, Locale delvedLocale, Locale nativeLocale) {
        index = -1;
        references = new DReferences();
        language = new LanguageManager(context).getCurrentLanguage();

        delvedPronunciationManager = new PronunciationManager(context, delvedLocale);
        nativePronunciationManager = new PronunciationManager(context, nativeLocale);
    }

    public DReference back() {
        index--;
        return references.remove(index);
    }

    public DReference forward(String translation) {
        DReference ref;

        DReference bait = DReference.createBait(translation);
        if (index % 2 != 0)
            ref = language.getDiccionary().ceiling(bait);
        else
            ref = language.getDiccionaryInverse().ceiling(bait);
        System.out.println("->" + ref.name);
        forward(ref);
        return ref;
    }

    public void forward(DReference reference) {
        references.add(reference);
        index++;
    }

    public DReference current() {
        return references.get(index);
    }

    public boolean hasMore() {
        return index > 0;
    }

    public void destroy() {
        delvedPronunciationManager.destroy();
        nativePronunciationManager.destroy();
    }

    public View.OnClickListener onPronunciationAction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (index % 2 == 0) {
                delvedPronunciationManager.pronounce(current().name);
            } else
                nativePronunciationManager.pronounce(current().name);
        }
    };

}
