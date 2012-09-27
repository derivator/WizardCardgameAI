package cardGame.wizard;

import cardGame.Card;
import cardGame.Game;
import cardGame.Player;
import cardGame.PlayerController;
import cardGame.wizard.bot.WizardBot;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class WizardGame extends Game implements WizardState {

    public WizardGame() {
        super(3,6,60);
    }

    public enum RoundPhase {
        Bidding,
        Playing,}
    private RoundPhase roundPhase;
    
    private boolean unevenBids = false;
    private boolean waitingForCard = false;
    private int trickStarter;
    private int overUnderBid;

    public WizardPlayer getWizardPlayer(int player) {
        return (WizardPlayer) players.get(player);
    }

    @Override
    public void addPlayer(PlayerController pc) {
        players.add(new WizardPlayer(this, (WizardController) pc));
    }

    @Override
    public void startGame() {
        Collections.shuffle(players);
        super.startGame();
    }

    @Override
    public void startRound() {
        round++;
        if (round > DECKSIZE / players.size()) {
            inProgress = false;
            return;
        }
        tableCards.clear();
        currentPlayer = roundStarter;
        nextPlayer();
        roundStarter = currentPlayer;
        trickStarter = currentPlayer;

        setupDeck();
        dealCards(round);
        roundPhase = RoundPhase.Bidding;
        for (Player player : players) {
            ((WizardPlayer) player).resetTricks();
            ((WizardPlayer) player).setBid(-1);
        }
    }

    @Override
    protected  void startTurn() {
        getWizardPlayer(currentPlayer).move();
    }

    @Override
    protected void dealCards(int amount) {
        Iterator<Card> it = deck.iterator();
        int playerNr = 0;
        for (Player p : players) {
            HashSet<Card> dealtHand = new HashSet<>(round);
            for (int i = 0; i < amount; i++) {
                dealtHand.add(new WizardCard(it.next(), playerNr));
                it.remove();
            }
            p.setHand(dealtHand);
            System.out.println(p.getController().getName()+": "+p.getHand());
            playerNr++;
        }

        if (it.hasNext()) {
            System.out.println("Trump color is determined by: " + it.next().toString());
        }
    }
    
    @Override
    public HashSet<Card>[] getHands() {
        HashSet<Card>[] hands = new HashSet[players.size()]; // for cheating bots;
        int i = 0;
        for (Player p : players) {         
            hands[i] = p.getHand();
            i++;
        }
        return hands;
    }

    private void setupDeck() {
        deck = new ArrayList<>();
        for (int s = 0; s < 4; s++) {
            for (int v = 0; v < 15; v++) {
                deck.add(new WizardCard(s, v));
            }
        }
        Collections.shuffle(deck);
    }

    @Override
    public int getBid(int player) {
        return getWizardPlayer(player).getBid();
    }

    @Override
    public int getTricks(int player) {
        return this.getWizardPlayer(player).getTricks();
    }

    @Override
    public void doBid(int bid) {
        if (unevenBids && getNextPlayer() == roundStarter && getTotalBids() + bid == round || bid < 0) {
            try {
                throw new Exception("incorrect bid");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        getWizardPlayer(currentPlayer).setBid(bid);
        System.out.println(getWizardPlayer(currentPlayer).getController().getName()+" bids " + bid);
    }

    public int getTotalBids() {
        int total = 0;
        for (int i = 0; i < players.size(); i++) {
            if (getBid(i) > 0) {
                total += getBid(i);
            }
        }
        return total;
    }

    @Override
    public void advance() {
        switch (roundPhase) {
            case Bidding:
                phaseBidding();
                break;
            case Playing:
                phasePlaying();
                break;
        }
    }

    private void phaseBidding() {
        if (getBid(currentPlayer) == -1) {
            getWizardPlayer(currentPlayer).notifyBid();
        } else {
            nextPlayer();
            if (currentPlayer == roundStarter) {
                roundPhase = RoundPhase.Playing;
                waitingForCard = true;
            }
        }
    }

    private void phasePlaying() {
        if (waitingForCard) {
            startTurn();
        } else {
            // trick is completed
            nextPlayer();
            if (currentPlayer == trickStarter) {
                System.out.println(tableCards.toString());
                Card highestCard = Collections.min(tableCards, new WizardComparator(getTrumpSuit(), getFollowSuit(tableCards)));
                trickStarter = highestCard.getOwner();
                currentPlayer = highestCard.getOwner();          
                getWizardPlayer(currentPlayer).addTrick();
                System.out.println("Player " + getWizardPlayer(currentPlayer).getController().getName()+ " wins the trick with "+highestCard);
                for (Player p : players) {
                    ((WizardPlayer)p).notifyTrickCompleted(Collections.unmodifiableList(tableCards), getWizardPlayer(currentPlayer));
                }
                nextTrick();
            } else {
                waitingForCard = true;
            }
        }

    }

    private void nextTrick() {
        tableCards.clear();
        if (getWizardPlayer(currentPlayer).getHand().isEmpty()) {
            doScore();
            startRound();

        } else {
            startTurn();
        }
    }
    
    public static int calculateScore(int tricks, int bid) {
        int score;
        if (tricks == bid) {
                score =  20 + 10 * tricks;
            } else {
                score =  - (10 * Math.abs(tricks - bid));
            }
        return score;
    }

    private void doScore() {
        for (Player player : players) {
            WizardPlayer p = (WizardPlayer) player;
            int newScore = p.getScore() + calculateScore(p.getTricks(), p.getBid());
            p.setScore(newScore);
            overUnderBid += p.getBid()- p.getTricks();
        }
    }

    @Override
    public boolean unevenBidsEnforced() {
        return unevenBids;
    }

    @Override
    public int getScore(int player) {
        return players.get(player).getScore();
    }
    @Override
    public void playCard(Card c) {
        HashSet<Card> hand = getWizardPlayer(currentPlayer).getHand();
        if (cardLegallyPlayable(c, hand)) {
            tableCards.add(c);
            hand.remove(c);
            getWizardPlayer(currentPlayer).setHand(hand);
            waitingForCard = false;
            for (Player p : players) {
                p.getController().notifyMove();
            }
            System.out.println(getWizardPlayer(currentPlayer).getController().getName()+" plays " + c);
        } else {
            try {
                throw new IllegalArgumentException("Illegal card played!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public WizardCard getTrumpIndicator() {
        if (deck.isEmpty()) {
            return null;
        } else {
            return (WizardCard) deck.get(0).clone();
        }
    }

    @Override
    public int getTrumpSuit() {
        if (getTrumpIndicator() != null) {
            return getTrumpIndicator().getSuit();
        }
        return -128;
    }

    public static int getFollowSuit(Collection<Card> table) {
        int followSuit = -128;
        for (Card c : table) {
            if (c.getValue() != 0) {
                if (c.getValue() == 14) {
                    return -128;
                }
                followSuit = c.getSuit();
                break;
            }
        }
        return followSuit;
    }
    
    @Override
    public boolean cardLegallyPlayable(Card card, Collection<Card> hand) {
        return cardLegallyPlayable(tableCards, card, hand);
    }

    public static boolean cardLegallyPlayable(Collection<Card> tableCards, Card card, Collection<Card> hand) {
        if (hand.isEmpty()) {
            throw new IllegalArgumentException("Empty Hand");
        }
        
        if (! hand.contains(card)) {
            return false;
        }
        
        if (card.getValue() == 0 || card.getValue() == 14) {
            return true;
        }

        int followSuit = getFollowSuit(tableCards);

        if (followSuit == -128) {
            return true;
        }

        if (card.getSuit() == followSuit) {
            return true;
        }

        for (Card c : hand) {
            if (c.getSuit() == followSuit && c.getValue() != 0 && c.getValue() != 14) {
                return false;
            }
        }

        return true;
    }
    
    public void printScore() {
        for (Player player : players) {
            WizardPlayer p = (WizardPlayer) player;
            System.out.println(p.getController().getName() + ": " + p.getScore());
        }
        System.out.println("Over/Under-Bid: " + overUnderBid);
    }
}
