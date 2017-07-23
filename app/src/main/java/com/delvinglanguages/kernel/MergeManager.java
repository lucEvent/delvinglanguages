package com.delvinglanguages.kernel;

import android.content.Context;

import com.delvinglanguages.data.MergeDatabaseManager;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.theme.Theme;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.kernel.util.DrawerReferences;
import com.delvinglanguages.kernel.util.Tests;
import com.delvinglanguages.kernel.util.Themes;

public class MergeManager extends KernelManager {

    public class MergePlan {

        public int num_conflicts_accepted;
        public int num_conflicts;

        public Language src;
        public Language dst;

        public DReferences src_references;
        public DrawerReferences src_drawerReferences;
        //   public DReferences src_removedReferences;
        public Themes src_themes;
        public Tests src_tests;

        //   public DReferences src_hot_references;
        //   public DReferences src_hot_removedReferences;

    }

    private Context context;

    public MergeManager(Context context)
    {
        super(context);
        this.context = context;
    }

    public Language getLanguageContent(Language language)
    {
        loadContentOf(language);

        while (!language.isDictionaryCreated()) ;

        return language;
    }

    public MergePlan createMergePlan(Language src, Language dst)
    {
        MergePlan plan = new MergePlan();
        plan.src = src;
        plan.dst = dst;
        plan.num_conflicts = 0;

        plan.src_references = new DReferences();
        //      plan.src_hot_references = new DReferences();
        for (DReference ref : src.getReferences())
            if (dst.getReference(ref.name) == null)
                plan.src_references.add(ref);
            else
                plan.num_conflicts++;
        //            plan.src_hot_references.add(ref);

        plan.src_drawerReferences = new DrawerReferences();
        for (DrawerReference dref : src.drawer_references)
            if (!dst.drawer_references.contains(dref.name))
                plan.src_drawerReferences.add(dref);

   /*     plan.src_removedReferences = new DReferences();
//        plan.src_hot_removedReferences = new DReferences();
        for (DReference ref : src.removed_references)
            if (dst.removed_references.contains(ref.name))
                plan.src_removedReferences.add(ref);
            else
  //              plan.src_hot_removedReferences.add(ref);
                plan.num_conflicts++;
*/
        plan.src_themes = src.themes;
        plan.src_tests = src.tests;

        return plan;
    }

    public void merge(Language dst, MergePlan mergePlan)
    {
        MergeDatabaseManager database = new MergeDatabaseManager(context);
        database.openWritableDatabase();

        for (DReference ref : mergePlan.src_references)
            database.updateReferenceLanguage(ref.id, dst.id, mergePlan.src.id);

        for (DrawerReference dref : mergePlan.src_drawerReferences)
            database.updateDrawerReferenceLanguage(dref.id, dst.id, mergePlan.src.id);

        //      for (DReference ref : mergePlan.src_removedReferences)// TODO: 07/04/2016
        //        database.updateReferenceLanguage(ref.id, dst.id);

        for (Theme theme : mergePlan.src_themes)
            database.updateThemeLanguage(theme.id, dst.id, mergePlan.src.id);

        for (Test test : mergePlan.src_tests)
            database.updateTestLanguage(test.id, dst.id, mergePlan.src.id);

        database.closeWritableDatabase();

        RecordManager.languageIntegrated(mergePlan.src.id, mergePlan.src.code, mergePlan.src.language_name, mergePlan.dst.language_name);
//        synchronizeUpdate(dst.id);//// TODO: 20/08/2016
    }

}
