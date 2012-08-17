import java.util.ArrayList;


public abstract class CardGame implements CardGameState {
	protected ArrayList<CardGamePlayer> players = new ArrayList<CardGamePlayer>();
	protected int turnPlayer;
	protected int roundStarter;
	protected int round;
	protected ArrayList<CardGameCard> deck;
	protected ArrayList<CardGameCard> tableCards = new ArrayList<CardGameCard>();
	protected boolean inProgress;
	public boolean isInProgress() {
		return inProgress;
	}

	CardGame(){
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
	
	
	public ArrayList<CardGameCard> getTableCards(){
		return CardGame.cloneCards(tableCards);
	}
	
	
	static ArrayList<CardGameCard> cloneCards(ArrayList<CardGameCard> list)
	{
		if(list==null)
			return null;
		
		ArrayList<CardGameCard> copy = new ArrayList<CardGameCard>();
		for(CardGameCard card : list)
		{
			copy.add((CardGameCard) card.clone());
		}
		return copy;
	}
	
}
