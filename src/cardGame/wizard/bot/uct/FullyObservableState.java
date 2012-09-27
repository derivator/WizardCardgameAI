package cardGame.wizard.bot.uct;

import cardGame.Card;
import cardGame.wizard.WizardComparator;
import cardGame.wizard.WizardGame;
import cardGame.wizard.WizardState;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

public class FullyObservableState {

    private static int players;
    private static int round;
    private static int roundStarter;
    private static int trumpSuit;
    private static boolean unevenBids;
    private static int[] scores;
    
    private int[] bids;
    private int currentPlayer;
    private int currentTrick;
    private int[] playerTricks;
    private ArrayList<Card> tableCards;
    private HashSet<Card>[] playerHands;

    public FullyObservableState(int currentPlayer, int currentTrick, int[] playerTricks, ArrayList<Card> tableCards, HashSet<Card>[] handsByPlayer, int[] bids) {
        this.currentPlayer = currentPlayer;
        this.currentTrick = currentTrick;
        this.playerTricks = playerTricks;
        this.tableCards = tableCards;
        this.playerHands = handsByPlayer;
        this.bids = bids;
    }
    
    public FullyObservableState (WizardState state) {
        currentPlayer = state.getCurrentPlayer();
        currentTrick = 1;
        playerTricks = new int[state.getNumberOfPlayers()];
        tableCards = new ArrayList(state.getTableCards());
        playerHands = state.getHands();
        initialize(state);
        bids = new int[players];
        for (int i=0; i<players; i++) {
            bids[i] = state.getBid(i);
        }
        
    }
    
    public FullyObservableState makeMove(Move move) {
        if (move.isCard()) {
            return makeMove(move.getCard());
        } else {
            return makeMove(move.getBid());
        }
    }
    
    public FullyObservableState makeRandomMove() {
        ArrayList<Move> playable = getPossibleMoves();
        Random rand = new Random();
        Move playCard = playable.get(rand.nextInt(playable.size()));
        return makeMove(playCard);
    }

    private FullyObservableState makeMove(Card card) {
        HashSet<Card> hand = playerHands[currentPlayer];
        HashSet<Card>[] newPlayerHands = playerHands.clone();
        HashSet<Card> newHand = new HashSet<>(hand);
        ArrayList<Card> newTableCards = null;
        int newCurrentPlayer = currentPlayer;
        int newCurrentTrick = currentTrick;
        int[] newPlayerTricks = playerTricks.clone();

        if (WizardGame.cardLegallyPlayable(tableCards, card, hand)) {
            newHand.remove(card);
            newPlayerHands[currentPlayer] = newHand;
            newTableCards = new ArrayList<>(tableCards);
            newTableCards.add(card);
            newCurrentPlayer = (currentPlayer + 1) % players;

            if (newTableCards.size() == players) {
                Card highestCard = Collections.min(newTableCards, new WizardComparator(trumpSuit, WizardGame.getFollowSuit(newTableCards)));
                int trickWinner = highestCard.getOwner();
                newPlayerTricks[trickWinner]++;
                newCurrentPlayer = trickWinner;
                newCurrentTrick++;
                newTableCards = new ArrayList<>(players);
            }
        } else {
            throw new IllegalArgumentException("Illegal card played!");
        }
        return new FullyObservableState(newCurrentPlayer, newCurrentTrick, newPlayerTricks, newTableCards, newPlayerHands, this.bids);
    }

    private FullyObservableState makeMove(int bid) {
        if (bids[currentPlayer] == -1) {
            int[] newBids = bids.clone();
            newBids[currentPlayer] = bid;
            int newCurrentPlayer = (currentPlayer + 1) % players;
            ArrayList<Card> newTableCards = (ArrayList<Card>) this.tableCards.clone();
            HashSet<Card>[] newPlayerHands = (HashSet<Card>[]) this.playerHands.clone();
            return new FullyObservableState(newCurrentPlayer, currentTrick, this.playerTricks.clone(), newTableCards, newPlayerHands, newBids);
        }
        throw new IllegalArgumentException("Cannot bid");
    }
    
    public ArrayList<Move> getPossibleMoves() {
        ArrayList<Move> moves = new ArrayList();
        if (bids[currentPlayer] == -1) {
            // player has to bid
            for (int i=0; i<=round; i++) {
                if (! (unevenBids && currentPlayer == roundStarter && getTotalBids() +i == round)) {
                    moves.add(new Move(i));
                }
            }
        } else {
            // player has to play a card
            return getPlayableCards();
        }
        return moves;
    }
    
    private ArrayList<Move> getPlayableCards() {
        ArrayList<Move> playableCards = new ArrayList<>();
        for (Card c : playerHands[currentPlayer]) {
            if (cardLegallyPlayable(c)) {
                playableCards.add(new Move(c));
            }          
        }   
        return playableCards;
    }
    
    public boolean isFinal() {
        for (int i = 0; i < playerHands.length; i++) {
            if (!playerHands[i].isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    public double[] evaluate(){
        if (isFinal()) {
        double[] reward = new double[players];
        for (int i=0; i<players; i++) {
            reward[i] = WizardGame.calculateScore(playerTricks[i], bids[i])/(FullyObservableState.getRound()*10.);
        }         
        return reward; }
        throw new UnsupportedOperationException("Cannot evaluate non terminal state!");
    }
    
    public static void initialize(WizardState state) {
        players = state.getNumberOfPlayers();
        round = state.getRound();
        scores = new int[players];
        trumpSuit = state.getTrumpSuit();
        unevenBids = state.unevenBidsEnforced();
    }

    public int getTotalBids() {
        int total = 0;
        for (int i = 0; i < players; i++) {
            if (bids[i] > 0) {
                total += bids[i];
            }
        }
        return total;
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

    public int[] getBids() {
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

    public HashSet<Card>[] getPlayerHands() {
        return playerHands;
    }
    
    public boolean cardLegallyPlayable(Card c) {
        return WizardGame.cardLegallyPlayable(tableCards, c, playerHands[currentPlayer]);
    }
    
}
