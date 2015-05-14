package com.delvinglanguages.kernel.svenska;

import com.delvinglanguages.kernel.Word;
import com.delvinglanguages.kernel.set.Translations;

public class SVWord extends Word {

	private String[] forms;

	public SVWord(int id, String name, Translations translations, String pronunciation, String[] forms, int priority) {
		super(id, name, translations, pronunciation, priority);

		this.forms = forms;
	}

	public String[] getForms() {
		return forms;
	}
}
