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
                // node not fully expanded, play first playable card
                return node.expandNode(state.getPlayableCards().get(0));  
            } else {
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
}
