/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cardGame.wizard.bot.gameTree;

import java.util.ArrayList;
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

    public void addChild(State state) {
        if (children == null)  {
            children = new ArrayList<>();          
        }
        children.add(new Node(state, this));
        
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
