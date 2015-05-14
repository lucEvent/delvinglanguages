package com.delvinglanguages.kernel;

import com.delvinglanguages.kernel.set.Translations;

public class PhrasalVerb extends Word {

	public static String[] english_prepositions = { "About", "Across", "After", "Against", "Ahead", "Along", "Apart", "Around", "As", "Aside", "At",
			"Away", "Back", "By", "Down", "For", "Forth", "Forward", "From", "In", "Into", "It", "Of", "Off", "On", "Onto", "Out", "Over", "Round",
			"Through", "To", "Together", "Towards", "Under", "Up", "Upon", "With" };

	public static String[] svenska_preposition = { };

	   /*	
		*	av i med till
		*	bort ihop om upp
		*	fram in pÂ ut 
		*/
	
	private String verb;
	private String partikel;

	public PhrasalVerb(int id, String name, Translations translations, String pronunciation, int priority) {
		super(id, name, translations, pronunciation, priority);

		// Extract verb and partikel from name
		String[] parts = name.split(" ");
		verb = parts[0];
	}

}
