package cardGame.wizard;

import cardGame.Card;

public class WizardCard extends Card {

    public final static String suits = "GYRB";

    public WizardCard(int suit, int value) {
        super(suit, value);
    }
    public WizardCard(Card card, int owner) {
        super(card, owner);
    }

    @Override
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
