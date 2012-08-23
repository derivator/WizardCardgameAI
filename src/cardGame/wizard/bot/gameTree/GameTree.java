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
    
    
    public Node root;

    public GameTree(State initialState) {   
        root = new Node(initialState, null);

    }

    
}
