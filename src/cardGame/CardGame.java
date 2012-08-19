package cardGame;



import java.util.ArrayList;
import java.util.List;


public abstract class CardGame implements CardGameState {
	protected List<CardGamePlayer> players = new ArrayList<>();
	protected int turnPlayer;
	protected int roundStarter;
	protected int round;
	protected List<CardGameCard> deck;
	protected List<CardGameCard> tableCards = new ArrayList<>();
	protected boolean inProgress;
	public boolean isInProgress() {
		return inProgress;
	}

	protected CardGame(){
		round=-1;
		turnPlayer=-1;
		inProgress = false;
	}
	
	public abstract void addPlayer(PlayerController pc);
	
	public void startGame(){
		inProgress=true;
		turnPlayer = 0;
		roundStarter = 0;
		round = 0;
		startRound();
	}
	public abstract void advance();
	public void nextPlayer(){
		turnPlayer = getNextPlayer();
	}
	public int getNextPlayer(){
		return (turnPlayer+1)%players.size();
	}
	public abstract void startRound();
	public abstract void startTurn();
	public abstract void dealCards(int amount);
	
	public int getRound() {
		return round;
	}
	
	public int getTurnPlayer() {
		return turnPlayer;
	}
	
	public int getTurnStarter() {
		return roundStarter;
	}
	
	
	public List<CardGameCard> getTableCards(){
		return CardGame.cloneCards(tableCards);
	}
	
	
	static List<CardGameCard> cloneCards(List<CardGameCard> list)
	{
		if(list==null)
			return null;
		
		List<CardGameCard> copy = new ArrayList<>();
		for(CardGameCard card : list)
		{
			copy.add((CardGameCard) card.clone());
		}
		return copy;
	}
	
}
