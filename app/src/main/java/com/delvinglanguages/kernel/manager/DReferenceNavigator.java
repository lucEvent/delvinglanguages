package com.delvinglanguages.kernel.manager;

import android.content.Context;
import android.view.View;

import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.DelvingList;
import com.delvinglanguages.kernel.LanguageCode;
import com.delvinglanguages.kernel.util.DReferences;

public class DReferenceNavigator {

    private DelvingList delvingList;

    private int index;

    private DReferences references;

    private PronunciationManager delvedPronunciationManager, nativePronunciationManager;

    private final int odd;

    public DReferenceNavigator(Context context, DelvingList delvingList, int from_code, int to_code, boolean inverse)
    {
        index = -1;
        references = new DReferences();
        this.delvingList = delvingList;
        odd = inverse ? 1 : 0;

        delvedPronunciationManager = new PronunciationManager(context, LanguageCode.getLocale(from_code), true);
        nativePronunciationManager = new PronunciationManager(context, LanguageCode.getLocale(to_code), true);
    }

    public DReference back()
    {
        references.remove(index);
        index--;
        return references.get(index);
    }

    public DReference forward(String translation)
    {
        index++;

        DReference ref;
        DReference bait = DReference.createBait(translation);
        if (index % 2 == odd)
            ref = delvingList.getDictionary().ceiling(bait);
        else
            ref = delvingList.getDictionaryInverse().ceiling(bait);

        references.add(ref);
        return ref;
    }

    public DReference current()
    {
        return references.get(index);
    }

    public boolean hasMore()
    {
        return index > 0;
    }

    public void destroy()
    {
        delvedPronunciationManager.destroy();
        nativePronunciationManager.destroy();
    }

    public View.OnClickListener onPronunciationAction = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            if (index % 2 == odd) {
                delvedPronunciationManager.pronounce(current().name);
            } else
                nativePronunciationManager.pronounce(current().name);
        }
    };

}
