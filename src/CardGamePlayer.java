import java.util.ArrayList;
import java.util.Iterator;


public abstract class CardGamePlayer {
	protected final CardGame game;
	protected PlayerController controller;
	private ArrayList<CardGameCard> hand;
	protected int score;
	
	CardGamePlayer(CardGame game, PlayerController controller){
		this.game = game;
		this.controller = controller;
		this.controller.assignGameState(game);
	}
	
	public void setHand(ArrayList<CardGameCard> hand) {
		this.hand = hand;
		if(controller != null)
		{
			controller.notifyHand(getHandClone());
		}
	}
	
	public ArrayList<CardGameCard> getHand() {
		return hand;
	}

	public ArrayList<CardGameCard> getHandClone(){
		return CardGame.cloneCards(hand);
	}
	
	public String toString(){
		String result;
		if(controller != null)
			result=controller.getName();
		else
			result="ERROR";
		
		result += ":[";
		Iterator<? extends CardGameCard> it = hand.iterator();
		while(it.hasNext())
		{
			result += it.next().toString();
			if(it.hasNext())
				result += ", ";
		}
		result += "]";
		return result;
	}
	public abstract void notifyTurn();
}
