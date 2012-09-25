package cardGame.wizard.bot.uct;

import cardGame.Card;
import java.util.Objects;


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
    
    @Override
    public String toString() {
        if (isCard()) {
            return card.toString();
        }
        return Integer.toString(bid);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Move other = (Move) obj;
        if (!Objects.equals(this.card,other.card)) {
            return false;
        }
        if (this.bid != other.bid) {
            return false;
        }
        return true;
        
    }

    
    
    
    
    
}
