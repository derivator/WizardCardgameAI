public class CardGameCard implements Comparable<CardGameCard>, Cloneable {
	protected int suit;
	protected int value;
	protected int owner;
	CardGameCard(){suit=-128;value=-128;}
	CardGameCard(int suit, int value){
		this.suit = suit;
		this.value = value;
		owner = 0;
	}
	
	public Object clone(){
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}
	@Override
	public int compareTo(CardGameCard arg0) {
		int myValue = (int)suit*128+(int)value;
		int otherValue = (int)arg0.suit*128+(int)arg0.value;
		
		return myValue-otherValue;
	}
	
	@Override
	public boolean equals(Object o){
		if(o==null)
			return false;
		CardGameCard c = (CardGameCard) o;
		return c.suit==suit && c.value == value;
	}
	
	public void setOwner(int o){owner = o;}
	
	public int getOwner(){return owner;}

	public int getSuit(){return suit;}
	
	public int getValue(){return value;}
	
}
