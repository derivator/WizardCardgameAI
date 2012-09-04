package cardGame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;


public abstract class Game implements GameState {

    public final int MINPLAYERS;
    public final int MAXPLAYERS;
    public final int DECKSIZE;  
     
    protected ArrayList<Player> players;
    protected ArrayList<Card> deck;
    protected ArrayList<Card> tableCards;
    
    protected int currentPlayer = -1;
    protected int roundStarter = -1;
    protected int round = -1;
    protected boolean inProgress = false;

    public Game(int minPlayers, int maxPlayers, int deckSize) {
        this.MINPLAYERS = minPlayers;
        this.MAXPLAYERS = maxPlayers;
        DECKSIZE = deckSize;
        tableCards = new ArrayList<>(maxPlayers);
        players = new ArrayList<>(maxPlayers);
    }

    @Override
    public int getNumberOfPlayers() {
        return players.size();
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public abstract void addPlayer(PlayerController pc);

    public void startGame() {
        tableCards.trimToSize();
        players.trimToSize();
        inProgress = true;
        currentPlayer = 0;
        roundStarter = 0;
        round = 0;
        Collections.shuffle(players);
        startRound();
    }

    public abstract void advance();
    
    public void startGame (int round) {
        tableCards.trimToSize();
        players.trimToSize();
        inProgress = true;
        currentPlayer = 0;
        roundStarter = 0;
        this.round = round-1;
        Collections.shuffle(players);
        startRound();
    }

    protected void nextPlayer() {
        currentPlayer = getNextPlayer();
    }

    @Override
    public int getNextPlayer() {
        return (currentPlayer + 1) % players.size();
    }

    protected abstract void startRound();

    protected abstract void startTurn();

    protected abstract void dealCards(int amount);

    @Override
    public int getRound() {
        return round;
    }

    @Override
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public int getRoundStarter() {
        return roundStarter;
    }

    @Override
    public List<Card> getTableCards() {
        return (List) Game.cloneCards(tableCards);
    }

    public static ArrayList<Card> cloneCards(Collection<Card> list) {
        if (list == null) {
            return null;
        }
        
        ArrayList<Card> copy = new ArrayList<>();
        for (Card card : list) {
            copy.add((Card) card.clone());
        }
        return copy;
    }
    public static HashSet<Card> cloneHand(HashSet<Card> hand) {
            if (hand == null) {
            return null;
        }
        
        HashSet<Card> copy = new HashSet<>();
        for (Card card : hand) {
            copy.add((Card) card.clone());
        }
        return copy;
    }    
    
}
