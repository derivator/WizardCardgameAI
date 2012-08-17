import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class WizardBot implements WizardController {
	private final String name;
	protected ArrayList<CardGameCard> hand; 
	protected WizardState gameState;
	private int myBid=0;
	WizardBot(){
		Random rand = new Random();
		name = "Bot #" + rand.nextInt(500);
	}
	
	WizardBot(String name){
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void notifyTurn() {
		if(gameState.getTotalBids()>=gameState.getRound());
		
		Collections.sort(hand, new WizardComparator(gameState.getTrumpSuit()));
		Collections.reverse(hand);
		for(CardGameCard card : hand)
		{
			if(gameState.cardLegallyPlayable(card, hand))
			{
				//gameState.getTableCards().
				gameState.playCard(card);
				
				break;
			}
			
		}
		
		
	}
	private void playLowCard(boolean trickDesired)
	{
	
	}
	
	@Override
	public void notifyBid() {
		int trumpsuit = -128;
		trumpsuit = gameState.getTrumpSuit();
		
		int bid=0;
		for(CardGameCard card : hand)
		{
			if((card.suit==trumpsuit && card.value > 6) || card.value==14 || card.value > 10 )
				bid++;
		}
		
		if(gameState.getTotalBids() + bid == gameState.getRound() && gameState.unevenBidsEnforced() && gameState.getNextPlayer()==gameState.getTurnStarter())
			bid++;
		
		myBid=bid;
		gameState.doBid(bid);	
	}

	@Override
	public void notifyHand(ArrayList<CardGameCard> hand) {
		this.hand=hand;
		
	}

	@Override
	public void assignGameState(CardGameState state) {
		gameState = (WizardState) state;
		
	}

}
