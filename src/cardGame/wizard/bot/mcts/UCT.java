package cardGame.wizard.bot.mcts;

import java.util.Iterator;


public class UCT {
    
    public Node root;
    double exploitationParameter;

    public UCT(State initialState) {   
        root = new Node(initialState, null, null);

    }
    
    private Node treePolicy(Node node) {
        State state = node.getState();
        while (!state.isFinalState()) {
            if (node.getChildren().size()<state.getPlayableCards().size()) {
                // node not fully expanded
                return node.randomExpand();  
            } else {
                // use selection policy
                node = bestChild(node, exploitationParameter);
            }
        }
        return node;
    }
    
    private Node bestChild(Node node, double exploitationParameter) {
        Iterator<Node> it = node.getChildren().iterator();
        Node best = it.next();
        double bestUCTValue = uctValue(node, best, exploitationParameter);
        while (it.hasNext()) {
            Node next = it.next();
            State state = next.getState();
            if (bestUCTValue < uctValue(node, next, exploitationParameter)) {
                best = next;
            }    
        }
        return best;
        
    }
    
    private double uctValue(Node parent, Node child, double c) {
        double uctValue = child.getReward(child.getState().getCurrentPlayer())/child.getVisits();
        uctValue += c*Math.sqrt((2*Math.log(parent.getVisits()))/(child.getVisits()));
        return uctValue;
    }
    
    private int[] simulation(State state) {
        double reward = 0;
        State s = state;
        while (!s.isFinalState()) {
            s = s.makeRandomMove();
        }
        return s.evaluate();
    }
    
    
    
    
}
