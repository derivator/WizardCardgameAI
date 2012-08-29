package cardGame.wizard.bot.uct;

import java.util.Iterator;


public class UCT {
    
    public Node root;
    double exploitationParameter = 1/1.41;

    public UCT(State initialState) {   
        root = new Node(initialState, null, null);

    }
    
    public static Move uctSearch(State initialState) {
        Node root = new Node(initialState, null, null);
        final long startTime = System.nanoTime();
        
     //   while ((System.nanoTime()-startTime) < 1000000000) {
        while ((root.getVisits()) < 100000) {
           Node selected = treePolicy(root, 10000);
           int[] rewards = simulation(selected.getState());
           backup(selected, rewards);
           
        }
        Move chosen = bestChild(root, 0).getMove();
        for (Node child : root.getChildren()) {
            System.out.println(child.getMove()+  "  : " + child.getReward(root.getState().getCurrentPlayer()));
        }
        return chosen;
        
    }
    
    private static Node treePolicy(Node node, double exploitationParameter) {
        Node next = node;
        State state = next.getState();
        while (!state.isFinalState()) {
            if (next.getChildren() == null ||next.getChildren().size()<state.getPossibleMoves().size()) {
                // node not fully expanded
                return next.randomExpand();  
            } else {
                // use selection policy
                next = bestChild(next, exploitationParameter);
                state = next.getState();
            }
        }
        return next;
    }
    
    private static Node bestChild(Node node, double exploitationParameter) {
        Iterator<Node> it = node.getChildren().iterator();
        Node best = it.next();
        double bestUCTValue = uctValue(node, best, exploitationParameter);
        while (it.hasNext()) {
            Node next = it.next();
            if (bestUCTValue < uctValue(node, next, exploitationParameter)) {
                bestUCTValue = uctValue(node, next, exploitationParameter);
                best = next;             
            }    
        }
        return best;    
    }
    
    private static double uctValue(Node parent, Node child, double c) {
        double uctValue = child.getReward(parent.getState().getCurrentPlayer())/child.getVisits();
        uctValue += c*Math.sqrt((2*Math.log(parent.getVisits()))/(child.getVisits()));
        return uctValue;
    }
    
    private static int[] simulation(State state) {
        State s = state;
        while (!s.isFinalState()) {
            s = s.makeRandomMove();
        }
        return s.evaluate();
    }
    /**
     * Ascends the tree to backup the new values;
     * @param node starting node
     * @param rewards rewards to backup
     */
    private static void backup(Node node, int[] rewards) {
        Node next = node;
        while (next != null) {
            next.incrementVisits();
            next.addRewards(rewards);
            next = next.getParent();
        }
    }
    
    
    
    
}
