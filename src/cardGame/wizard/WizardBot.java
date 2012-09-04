package cardGame.wizard;

import cardGame.Card;
import cardGame.Player;
import cardGame.GameState;
import cardGame.wizard.bot.uct.UCT;
import cardGame.wizard.bot.uct.State;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class WizardBot implements WizardController {
	private final String name;
	protected Set<Card> hand; 
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
	public void move() {
		boolean overBid = gameState.getTotalBids()>=gameState.getRound();
                List<Card> handList = new ArrayList<>(hand);
		Collections.sort(handList, new WizardComparator(gameState.getTrumpSuit()));
		if(myBid==gameState.getTricks(gameState.getCurrentPlayer()) || !overBid && gameState.getTricks(gameState.getCurrentPlayer())>myBid)
				Collections.reverse(handList);
		for(Card card : handList)
		{
			if(gameState.cardLegallyPlayable(card, handList))
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
	public void bid() {
		int trumpsuit = -128;
		trumpsuit = gameState.getTrumpSuit();
		
		int bid=0;
		for(Card card : hand)
		{
			if((card.getSuit()==trumpsuit && card.getValue()> 6) || card.getValue()==14 || card.getValue() > 10 )
				bid++;
		}
		
		if(gameState.getTotalBids() + bid == gameState.getRound() && gameState.unevenBidsEnforced() && gameState.getNextPlayer()==gameState.getRoundStarter())
			bid++;
		
		myBid=bid;
		gameState.doBid(bid);	
	}

	@Override
	public void notifyHand(Set<Card> hand) {
		this.hand=hand;
		
	}

	@Override
	public void assignGameState(GameState state) {
		gameState = (WizardState) state;
		
	}

    @Override
    public void notifyMove() {
        //TODO: add code
    }

    @Override
    public void notifyTrickCompleted(List<Card> trick, Player player) {
        //TODO add code
    }

}
