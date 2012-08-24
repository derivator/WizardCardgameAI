package cardGame.wizard;

import cardGame.Card;

public class WizardCard extends Card {
	public final static String suits = "GYRB";
	WizardCard(int suit, int value){
		super(suit, value);
    }

    public String toString() {
        String s = "" + suits.charAt(suit) + "-";

        if (value == 0) {
            s += "N";
        } else if (value == 14) {
            s += "Z";
        } else {
            s += Integer.toString((int) value);
        }

        return s;

    }
}
