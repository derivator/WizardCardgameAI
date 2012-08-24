package cardGame.wizard.bot.mcts;

import cardGame.Card;
import cardGame.wizard.WizardComparator;
import cardGame.wizard.WizardGame;
import cardGame.wizard.WizardState;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

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

    public State(int currentPlayer, int currentTrick, int[] tricksByPlayer, ArrayList<Card> tableCards, ArrayList<Card>[] handsByPlayer) {
        this.currentPlayer = currentPlayer;
        this.currentTrick = currentTrick;
        this.playerTricks = tricksByPlayer;
        this.tableCards = tableCards;
        this.playerHands = handsByPlayer;
    }

    public State makeMove(Card card) {
        ArrayList<Card> hand = playerHands[currentPlayer];
        ArrayList<Card>[] newPlayerHands = playerHands.clone();
        ArrayList<Card> newHand;
        ArrayList<Card> newTableCards = null;
        int newCurrentPlayer = currentPlayer;
        int newCurrentTrick = currentTrick;
        int[] newPlayerTricks = playerTricks.clone();

        if (WizardGame.cardLegallyPlayable(tableCards, card, hand)) {
            newHand = (ArrayList<Card>) hand.clone();
            newHand.remove(card);
            newPlayerHands[currentPlayer] = newHand;
            newTableCards = (ArrayList<Card>) tableCards.clone();
            card.setOwner(currentPlayer);
            newTableCards.add(card);
            newCurrentPlayer = (currentPlayer + 1) % players;
        } else {
            throw new IllegalArgumentException("Illegal card played!");
        }
        if (newTableCards.size() == players) {
            Card highestCard = Collections.min(tableCards, new WizardComparator(trumpSuit, WizardGame.getFollowSuit(tableCards)));
            int trickWinner = highestCard.getOwner();
            playerTricks[trickWinner]++;
            newCurrentPlayer = trickWinner;
            newTableCards = new ArrayList<>(players);
            newCurrentTrick++;
        }
        return new State(newCurrentPlayer, newCurrentTrick, newPlayerTricks, newTableCards, newPlayerHands);
    }
    
    public boolean isFinalState() {

        for (int i = 0; i < playerHands.length; i++) {
            if (!playerHands[i].isEmpty()) {
                return false;
            }
        }
        return true;
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
    
     public ArrayList<Card> getPlayableCards() {
         ArrayList<Card> playableCards = new ArrayList<>();
         for (Card c : playerHands[currentPlayer]) {
             if (cardLegallyPlayable(c)) {
                 playableCards.add(c);
             }    
         }
        return playableCards;
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
      
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final State other = (State) obj;
        if (this.currentPlayer != other.currentPlayer) {
            return false;
        }
        if (this.currentTrick != other.currentTrick) {
            return false;
        }
        if (!Arrays.equals(this.playerTricks, other.playerTricks)) {
            return false;
        }
        if (!Objects.equals(this.tableCards, other.tableCards)) {
            return false;
        }
        if (!Arrays.deepEquals(this.playerHands, other.playerHands)) {
            return false;
        }
        return true;
    }
}
