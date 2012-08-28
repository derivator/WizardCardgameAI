package cardGame.wizard.bot.uct;

import cardGame.Card;


public class Move {
    
    Card card;
    int bid;

    public Move(Card card) {
        this.card = card;
        bid = -1;
    }

    public Move(int bid) {
        this.card = null;
        this.bid = bid;
    }

    public boolean isCard() {
        return card != null;
    }

    public int getBid() {
        return bid;
    }

    public Card getCard() {
        return card;
    }
    
    
    
}
