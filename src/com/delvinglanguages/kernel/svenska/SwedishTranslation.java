package com.delvinglanguages.kernel.svenska;

import com.delvinglanguages.kernel.Translation;

public class SwedishTranslation extends Translation {

	private static final long serialVersionUID = -1011622386692750354L;

	public String[] forms;

	public SwedishTranslation(int id, String translation, int type, String... forms) {
		super(id, translation, type);
		this.forms = forms;
	}

	public SwedishTranslation(Translation T, String... forms) {
		super(T.id, T.name, T.type);
		this.forms = forms;
	}

	public boolean contains(String string) {
		for (String form : forms) {
			if (form.equalsIgnoreCase(string)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder res = new StringBuilder(" [ ");
		for (String form : forms) {
			res.append(form).append(", ");
		}
		return res.append("]").toString();
	}

}
