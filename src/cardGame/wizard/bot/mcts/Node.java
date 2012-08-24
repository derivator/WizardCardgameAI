package cardGame.wizard.bot.mcts;

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
        for (Node child : this.children) {
            if (child.getState().equals(newState)) {
                return null;
            }
        }
        children.add(newNode); 
        return newNode;
    }
    
    public void randomExpand() {
        Node node = this;
            List<Card> playable = node.getState().getPlayableCards();
            Collections.shuffle(playable);   
            Node newNode = node.expandNode(playable.get(0));    
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
