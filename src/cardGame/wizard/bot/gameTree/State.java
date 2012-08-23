/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cardGame.wizard.bot.gameTree;

import cardGame.Card;
import cardGame.Game;
import cardGame.wizard.WizardComparator;
import cardGame.wizard.WizardGame;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Olli
 */
public class State {

    private int currentPlayer;
    private int currentTrick;
    
    private int[] tricksByPlayer;
    
    private ArrayList<Card> tableCards;
    private ArrayList<Card>[] handsByPlayer;

    public State(int currentPlayer, int currentTrick, int[] tricksByPlayer, ArrayList<Card> tableCards, ArrayList<Card>[] handsByPlayer) {
        this.currentPlayer = currentPlayer;
        this.currentTrick = currentTrick;
        this.tricksByPlayer = tricksByPlayer;
        this.tableCards = tableCards;
        this.handsByPlayer = handsByPlayer;
    }
    
    public State makeMove(Card card, int numberOfPlayers, int trumpSuit) {
        ArrayList<Card> hand = handsByPlayer[currentPlayer];  
        ArrayList<Card>[] newHandsByPlayer = handsByPlayer.clone();
        ArrayList<Card> newHand = newHandsByPlayer[currentPlayer];
        ArrayList<Card> newTableCards = null;
        int newCurrentPlayer = currentPlayer;
        int newCurrentTrick = currentTrick;
        int[] newTricksByPlayer = tricksByPlayer.clone();
        
        if (WizardGame.cardLegallyPlayable(tableCards, card, hand)) {
            newHand = (ArrayList<Card>) hand.clone();
            newHand.remove(card);
            newTableCards = (ArrayList<Card>) tableCards.clone();    
            card.setOwner(currentPlayer);
            newTableCards.add(card);
        }
        if (tableCards.size() == numberOfPlayers) {
            Card highestCard =  Collections.min(tableCards, new WizardComparator(trumpSuit, WizardGame.getFollowSuit(tableCards)));
            int trickWinner = highestCard.getOwner();
            tricksByPlayer[trickWinner]++;
            newCurrentPlayer = trickWinner;
            newTableCards = new ArrayList<>(numberOfPlayers);
            newCurrentTrick++;
           
        }
        return new State(newCurrentPlayer, newCurrentTrick, newTricksByPlayer, newTableCards, newHandsByPlayer);
    }
    
}
