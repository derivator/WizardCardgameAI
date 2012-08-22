/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cardGame.wizard.bot.gameTree;

import cardGame.Card;
import cardGame.wizard.WizardGame;
import cardGame.wizard.WizardState;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Olli
 */
public class Node {
    
    Node Parent;
    List<Node> children;
    
    int currentPlayer;
    int currentTrick;
    int numberOfTricksLeft;
    
    int[] tricksByPlayer;

    List<Card> tableCards;
    List<Card>[] handsByPlayer;
    int[] numberOfTricksWonByPlayer;
    List<Card> cardsPlayed;
    
    public void randomExpand() {
        List<Card> hand = handsByPlayer[currentPlayer];
        Collections.shuffle(hand);
        for (Card card : hand) {
            if (WizardGame.cardLegallyPlayable(tableCards, card, handsByPlayer[currentPlayer])) {
                
                Card c = (Card) card.clone();
                handsByPlayer[currentPlayer].remove(card);
                c.setOwner(currentPlayer);
                tableCards.add(c);
            }
        }
    }
       
}
