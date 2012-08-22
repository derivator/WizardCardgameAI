package cardGame;



import java.util.ArrayList;
import java.util.List;


public abstract class Game implements State {
        public final int minPlayers;
        public final int maxPlayers;
        
	protected List<Player> players = new ArrayList<>();
	protected int turnPlayer = -1;
	protected int roundStarter;
	protected int round =-1;
	protected List<Card> deck;
	protected List<Card> tableCards = new ArrayList<>();
	protected boolean inProgress = false;
        
        public int getNumberOfPlayers() {
            return players.size();
        }
        
	public boolean isInProgress() {
		return inProgress;
	}

    public Game(int minPlayers, int maxPlayers) {
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
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
	
	
	public List<Card> getTableCards(){
		return Game.cloneCards(tableCards);
	}
	
	
	public static List<Card> cloneCards(List<Card> list)
	{
		if(list==null)
			return null;
		
		List<Card> copy = new ArrayList<>();
		for(Card card : list)
		{
			copy.add((Card) card.clone());
		}
		return copy;
	}
	
}
