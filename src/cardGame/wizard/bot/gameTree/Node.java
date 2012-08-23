/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cardGame.wizard.bot.gameTree;

import cardGame.Card;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Olli
 */
public class Node {

    private State state;
    private Node Parent;
    List<Node> children;
    // for UCT:
    int visits = 0;
    int value = 0;

    public Node(State state, Node Parent) {
        this.state = state;
        this.Parent = Parent;
    }

    public Node expandNode(Card card) {
        if (children == null) {
            children = new ArrayList<>();
        }
        State newState = state.makeMove(card);
        Node newNode = new Node(newState, this);
        children.add(newNode); 
        return newNode;
    }
    
    public void randomExpand() {
        Node node = this;
        while (!node.getState().isFinalState()) {
            List<Card> playable = node.getState().getPlayableCards();
            Collections.shuffle(playable);   
            node = node.expandNode(playable.get(0));
        }
    }

    public List<Node> getChildren() {
        return children;
    }

    public Node getParent() {
        return Parent;
    }

    public State getState() {
        return state;
    }

}
