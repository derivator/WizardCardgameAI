package cardGame.wizard;

import cardGame.Game;
import cardGame.Card;
import cardGame.Player;
import cardGame.PlayerController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class WizardGame extends Game implements WizardState {

    public WizardGame() {
        super(3,6);
    }
    
    

    public enum RoundPhase {

        Bidding,
        Playing,
    }
    protected RoundPhase roundPhase;
    protected boolean unevenBids = true;
    protected boolean waitingForCard = false;
    protected int trickStarter;
    protected int overUnderBid;

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
        if (round > 60 / players.size()) {
            inProgress = false;
            return;
        }
        tableCards.clear();
        turnPlayer = roundStarter;
        nextPlayer();
        roundStarter = turnPlayer;
        trickStarter = turnPlayer;

        setupDeck();
        dealCards(round);
        roundPhase = RoundPhase.Bidding;
        for (Player player : players) {
            ((WizardPlayer) player).resetTricks();
            ((WizardPlayer) player).currentBid = -1;
        }
    }

    private void printScore() {
        for (Player player : players) {
            WizardPlayer p = (WizardPlayer) player;
            System.out.println(p.getController().getName() + ": " + p.getScore());
        }
        System.out.println("Over/Under-Bid: " + overUnderBid);
    }

    @Override
    public void startTurn() {
        getWizardPlayer(turnPlayer).move();
    }

    public void dealCards(int amount) {
        Iterator<Card> it = deck.iterator();
        for (Player p : players) {
            List<Card> dealtHand = new ArrayList<>();
            for (int i = 0; i < amount; i++) {
                dealtHand.add(it.next());
                it.remove();
            }
            p.setHand(dealtHand);
        }
        if (it.hasNext()) {
            System.out.println("Trump color is determined by: " + it.next().toString());
        }
    }

    protected void setupDeck() {
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
        return getWizardPlayer(player).currentBid;
    }

    @Override
    public int getTricks(int player) {
        return this.getWizardPlayer(player).getTricks();
    }

    @Override
    public void doBid(int bid) {
        if (unevenBids && getNextPlayer() == roundStarter && getTotalBids() + bid == round) {

            bid += 2;
            try {
                throw new Exception("incorrect bid");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        getWizardPlayer(turnPlayer).currentBid = bid;
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

    public void phaseBidding() {
        if (getBid(turnPlayer) == -1) {
            getWizardPlayer(turnPlayer).notifyBid();
        } else {
            nextPlayer();
            if (turnPlayer == roundStarter) {
                roundPhase = RoundPhase.Playing;
                waitingForCard = true;
            }
        }
    }

    public void phasePlaying() {
        if (waitingForCard) {
            startTurn();
        } else {
            nextPlayer();
            if (turnPlayer == trickStarter) {
                System.out.println(tableCards.toString());
                List<Card> trick = Game.cloneCards(tableCards);
                Collections.sort(tableCards, new WizardComparator(getTrumpSuit(), getFollowSuit(tableCards)));
                trickStarter = tableCards.get(0).getOwner();
                turnPlayer = tableCards.get(0).getOwner();
                getWizardPlayer(turnPlayer).addTrick();
                for (Player p : players) {
                    ((WizardPlayer)p).notifyTrickCompleted(trick, getWizardPlayer(turnPlayer));
                }
                nextTrick();


            } else {
                waitingForCard = true;
            }
        }

    }

    private void nextTrick() {
        tableCards.clear();
        if (getWizardPlayer(turnPlayer).getHand().isEmpty()) {
            doScore();
            startRound();

        } else {
            startTurn();
        }
    }

    private void doScore() {
        for (Player player : players) {
            WizardPlayer p = (WizardPlayer) player;
            int newScore;
            if (p.getTricks() == p.getCurrentBid()) {
                newScore = p.getScore() + 20 + 10 * p.getTricks();
            } else {
                newScore = p.getScore() - (10 * Math.abs(p.getTricks() - p.getCurrentBid()));

                overUnderBid += p.getTricks() - p.getCurrentBid();
            }
            p.setScore(newScore);
        }

    }

    @Override
    public boolean unevenBidsEnforced() {
        return unevenBids;
    }

    @Override
    public void playCard(Card card) {
        List<Card> hand = getWizardPlayer(turnPlayer).getHand();
        if (hand.contains(card) && cardLegallyPlayable(card, hand)) {
            Card c = (Card) card.clone();
            c.setOwner(turnPlayer);
            tableCards.add(c);
            hand.remove(card);
            getWizardPlayer(turnPlayer).setHand(hand);
            waitingForCard = false;
            for (Player p : players) {
                p.getController().notifyMove();
            }
        } else {
            try {
                throw new Exception("illegal card played!");
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

    public int getTrumpSuit() {
        if (getTrumpIndicator() != null) {
            return getTrumpIndicator().getSuit();
        }

        return -128;
    }

    public static int getFollowSuit(List<Card> table) {
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
    public boolean cardLegallyPlayable(Card card, List<Card> hand) {
        return cardLegallyPlayable(tableCards, card, hand);
    }

    public static boolean cardLegallyPlayable(List<Card> tableCards, Card card, List<Card> hand) {
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

    /**
     * @param args
     */
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        WizardGame game = new WizardGame();
        game.addPlayer(new WizardBot("Burt"));
        game.addPlayer(new WizardBot("Clide"));
        game.addPlayer(new WizardBot("Charles Darwin"));
        game.addPlayer(new WizardBot("MC Fallhin"));
        if (false) {
            game.addPlayer(new WizardBot());
            game.addPlayer(new WizardBot());

        }
        for (int i = 0; i < 100; i++) {
            game.startGame();
            while (game.isInProgress()) {
                //wait for network/user input here?
                game.advance();
            }
        }


        game.printScore();

    }
}
