package cardGame;

public class Card implements Comparable<Card>, Cloneable {

    protected final int suit;
    protected final int value;
    protected final int owner;

    protected Card() {
        suit = -128;
        value = -128;
        owner = 0;
    }

    protected Card(int suit, int value) {
        this.suit = suit;
        this.value = value;
        owner = 0;
    }

    protected Card(Card unowned, int owner) {
        this.suit = unowned.suit;
        this.value = unowned.value;
        this.owner = owner;
    }
    
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public int compareTo(Card arg0) {
        int myValue = (int) suit * 128 + (int) value;
        int otherValue = (int) arg0.suit * 128 + (int) arg0.value;

        return myValue - otherValue;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        Card c = (Card) o;
        return c.suit == suit && c.value == value;
    }

    public int getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }

    public int getOwner() {
        return owner;
    }
}
