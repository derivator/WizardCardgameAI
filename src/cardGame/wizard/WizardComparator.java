package cardGame.wizard;

import cardGame.Card;
import java.util.Comparator;


public class WizardComparator implements Comparator<Card> {
	private final int trumpsuit;
	private final int followsuit;
	public WizardComparator(int trumpsuit, int followsuit){
		this.trumpsuit = trumpsuit;
		this.followsuit = followsuit;
	}
	public WizardComparator(int trumpsuit){
		this(trumpsuit, -127);
	}
	@Override
	public int compare(Card arg0, Card arg1) {
		if(arg1.getValue()==14)
		{
			if(arg0.getValue()==14)
				return 0;
			
			return 1;
		}
		else if(arg0.getValue()==14)
			return -1;
		
		if(arg1.getSuit()==trumpsuit && arg1.getValue() != 0)
		{
			if(arg0.getSuit() == trumpsuit)
			{
				return arg1.getValue()-arg0.getValue();
			}
			return 1;
		}
		else if(arg0.getSuit() == trumpsuit && arg0.getValue() != 0)
			return -1;
		
		if(arg1.getSuit()==followsuit && arg1.getValue() != 0)
		{
			if(arg0.getSuit() == followsuit)
			{
				return arg1.getValue()-arg0.getValue();
			}
			return 1;
		}
		else if(arg0.getSuit() == followsuit && arg0.getValue() != 0)
			return -1;
		
		
		
		return arg1.getValue()-arg0.getValue();
	}
	
	
}
