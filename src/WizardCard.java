
public class WizardCard extends CardGameCard {
	static String suits = "GYRB";
	WizardCard(byte suit, byte value){
		super(suit, value);
	}
	
	public String toString(){
		String s = "" + suits.charAt(suit) + "-";

		if(value==0)
			s+="N";
		else if(value==14)
			s+="Z";
		else
			s+=Integer.toString((int)value);
		
		return s;
		
	}
}
