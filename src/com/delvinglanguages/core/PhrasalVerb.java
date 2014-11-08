package com.delvinglanguages.core;

import java.util.ArrayList;

import android.util.Log;

public class PhrasalVerb extends Word {

	private static final String DEBUG = "##Word##";


	public PhrasalVerb(int id, String name, String trad, String pron, int type,
			boolean thrown, int priority) {
		this(id, name, new StringBuilder(trad), pron, type, thrown, priority);
	}

	public PhrasalVerb(int id, String name, StringBuilder trad, String pron,
			int type, boolean thrown, int priority) {
		super(id, name, trad, pron, type, thrown, priority);
	}



}
