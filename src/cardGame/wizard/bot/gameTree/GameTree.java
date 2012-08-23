/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cardGame.wizard.bot.gameTree;

import cardGame.Card;
import cardGame.GameState;
import cardGame.wizard.WizardState;

/**
 *
 * @author Olli
 */
public class GameTree {
    
    public final int players;
    public final int round;
    public final int trumpSuit;
    public final int[] scores;
    public final int[] bids;
    
    public Node root;

    public GameTree(WizardState state, State initialState) {
        players = state.getNumberOfPlayers();
        round = state.getRound();
        scores = new int[players];
        bids = new int[players];
        for (int i = 0; i<players; i++) {
            scores[i] = state.getScore(i);
            bids[i] = state.getBid(i);
        }
        trumpSuit = state.getTrumpSuit();
        
        root = new Node(initialState, null);

    }
    
    public void expandNode(Node node, Card card) {
        State newState = node.getState().makeMove(card, players, trumpSuit);
        node.addChild(newState);
    }
    

}
