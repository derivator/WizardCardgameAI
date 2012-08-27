package cardGame.wizard.bot.mcts;

import cardGame.Card;
import cardGame.wizard.WizardComparator;
import cardGame.wizard.WizardGame;
import cardGame.wizard.WizardState;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class State {

    private static int players;
    private static int round;
    private static int trumpSuit;
    private static int[] scores;
    private static int[] bids;

    private int currentPlayer;
    private int currentTrick;
    private int[] playerTricks;
    private ArrayList<Card> tableCards;
    private ArrayList<Card>[] playerHands;

    public State(int currentPlayer, int currentTrick, int[] playerTricks, ArrayList<Card> tableCards, ArrayList<Card>[] handsByPlayer) {
        this.currentPlayer = currentPlayer;
        this.currentTrick = currentTrick;
        this.playerTricks = playerTricks;
        this.tableCards = tableCards;
        this.playerHands = handsByPlayer;
    }
    
    public State (WizardState state) {
        currentPlayer = state.getCurrentPlayer();
        currentTrick = 0;
        playerTricks = new int[state.getNumberOfPlayers()];
        tableCards = (ArrayList<Card>) state.getTableCards();
        playerHands = state.getHands();
        initialize(state);
        
    }

    public State makeMove(Card card) {
        ArrayList<Card> hand = playerHands[currentPlayer];
        ArrayList<Card>[] newPlayerHands = playerHands.clone();
        ArrayList<Card> newHand = (ArrayList<Card>) hand.clone();
        ArrayList<Card> newTableCards = null;
        int newCurrentPlayer = currentPlayer;
        int newCurrentTrick = currentTrick;
        int[] newPlayerTricks = playerTricks.clone();

        if (WizardGame.cardLegallyPlayable(tableCards, card, hand)) {
            newHand.remove(card);
            newPlayerHands[currentPlayer] = newHand;
            newTableCards = (ArrayList<Card>) tableCards.clone();
            card.setOwner(currentPlayer);
            newTableCards.add(card);
            newCurrentPlayer = (currentPlayer + 1) % players;

            if (newTableCards.size() == players) {
                Card highestCard = Collections.min(tableCards, new WizardComparator(trumpSuit, WizardGame.getFollowSuit(tableCards)));
                int trickWinner = highestCard.getOwner();
                playerTricks[trickWinner]++;
                newCurrentPlayer = trickWinner;
                newTableCards = new ArrayList<>(players);
                newCurrentTrick++;
            }
        } else {
            throw new IllegalArgumentException("Illegal card played!");
        }
        return new State(newCurrentPlayer, newCurrentTrick, newPlayerTricks, newTableCards, newPlayerHands);
    }
    
    public State makeRandomMove() {
        ArrayList<Card> playable = getPlayableCards();
        Random rand = new Random();
        Card playCard = playable.get(rand.nextInt(playable.size()));
      //  System.out.println(tableCards);
        return makeMove(playCard);
    }
    
    public ArrayList<Card> getPlayableCards() {
        ArrayList<Card> playableCards = new ArrayList<>();
        for (Card c : playerHands[currentPlayer]) {
            if (cardLegallyPlayable(c)) {
                playableCards.add(c);
            }          
        }   
        return playableCards;
    }
    public boolean isFinalState() {

        for (int i = 0; i < playerHands.length; i++) {
            if (!playerHands[i].isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    public int[] evaluate(){
        int[] reward = new int[players];
        for (int i=0; i<players; i++) {
            reward[i] = WizardGame.calculateScore(playerTricks[i], bids[i]);
        }         
        return reward;
    }
    
    public static void initialize(WizardState state) {
        players = state.getNumberOfPlayers();
        round = state.getRound();
        scores = new int[players];
        bids = new int[players];
        for (int i = 0; i < players; i++) {
            scores[i] = state.getScore(i);
            bids[i] = state.getBid(i);
        }
        trumpSuit = state.getTrumpSuit();
    }
    public static int getPlayers() {
        return players;
    }

    public static int getRound() {
        return round;
    }

    public static int getTrumpSuit() {
        return trumpSuit;
    }

    public static int[] getScores() {
        return scores;
    }

    public static int[] getBids() {
        return bids;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public int getCurrentTrick() {
        return currentTrick;
    }

    public int[] getPlayerTricks() {
        return playerTricks;
    }

    public ArrayList<Card> getTableCards() {
        return tableCards;
    }

    public ArrayList<Card>[] getPlayerHands() {
        return playerHands;
    }
    
    public boolean cardLegallyPlayable(Card c) {
        return WizardGame.cardLegallyPlayable(tableCards, c, playerHands[currentPlayer]);
    }
    
}
