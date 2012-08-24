package cardGame.wizard;

import cardGame.Card;
import cardGame.Game;
import cardGame.Player;
import cardGame.PlayerController;
import cardGame.wizard.bot.mcts.UCT;
import cardGame.wizard.bot.mcts.State;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WizardGame extends Game implements WizardState {
    
   private static UCT test;


    public WizardGame() {
        super(3,6,60);
    }

    public enum RoundPhase {
        Bidding,
        Playing,}
    private RoundPhase roundPhase;
    
    private boolean unevenBids = true;
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
            ((WizardPlayer) player).bid = -1;
        }
    }

    @Override
    protected  void startTurn() {
        getWizardPlayer(currentPlayer).move();
    }

    @Override
    protected void dealCards(int amount) {
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
        return getWizardPlayer(player).bid;
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
        getWizardPlayer(currentPlayer).bid = bid;
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
                List<Card> trick = Game.cloneCards(tableCards);
                Card highestCard = Collections.min(tableCards, new WizardComparator(getTrumpSuit(), getFollowSuit(tableCards)));
                trickStarter = highestCard.getOwner();
                currentPlayer = highestCard.getOwner();          
                getWizardPlayer(currentPlayer).addTrick();
                System.out.println("Player " + getWizardPlayer(currentPlayer).getController().getName()+ " wins the trick with "+highestCard);
                for (Player p : players) {
                    ((WizardPlayer)p).notifyTrickCompleted(trick, getWizardPlayer(currentPlayer));
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
    public int getScore(int player) {
        return players.get(player).getScore();
    }
    @Override
    public void playCard(Card c) {
        List<Card> hand = getWizardPlayer(currentPlayer).getHand();
        if (cardLegallyPlayable(c, hand)) {
            c.setOwner(currentPlayer);
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
    
    private void printScore() {
        for (Player player : players) {
            WizardPlayer p = (WizardPlayer) player;
            System.out.println(p.getController().getName() + ": " + p.getScore());
        }
        System.out.println("Over/Under-Bid: " + overUnderBid);
    }

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
                /*
                if (game.roundPhase == RoundPhase.Playing && game.players.get(0).getHand() != null && game.round == 3) {
                    ArrayList<Card>[] hands = new ArrayList[game.players.size()];
                    int k = 0;
                    for (Player p : game.players) {
                        hands[k] = (ArrayList) p.getHand();
                        k++;
                    }
                    test = new UCT(new State(game.currentPlayer, 1, new int[game.getNumberOfPlayers()], game.tableCards , hands));
                    State.initialize(game);
                    try {
                        test.expand(10);
                    } catch (Exception ex) {
                        Logger.getLogger(WizardGame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                 
                    
                }
              */
               
            }
        }
        game.printScore();

    }
}
