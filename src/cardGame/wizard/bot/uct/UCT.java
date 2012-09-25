package cardGame.wizard.bot.uct;

import java.util.Iterator;
import java.util.Random;


public class UCT {
    
    private double exploitationParameter;
    private boolean useHeuristics;

    public UCT(double exploitationParameter, boolean useHeuristics) {
        this.exploitationParameter = exploitationParameter;
        this.useHeuristics = useHeuristics;
    }
    
    public Move uctSearch(State initialState) {
        Node root = new Node(initialState, null, null);
        
        while ((root.getVisits()) < 10000) {
           Node selected = treePolicy(root, exploitationParameter);
           double[] rewards = simulation(selected.getState());
           backup(selected, rewards);        
        }
        Move chosen = bestChild(root, 0).getMove();
        for (Node child : root.getChildren()) {
            System.out.println("Move "+child.getMove()+  " - Reward: " + child.getReward(root.getState().getCurrentPlayer())+", visits: " +child.getVisits());        
        }
        return chosen;
        
    }
    
    private Node treePolicy(Node node, double exploitationParameter) {
        Node next = node;
        State state = next.getState();
        while (!state.isFinalState()) {
            if (next.getChildren() == null || next.getChildren().size() < state.getPossibleMoves().size()) {
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
    
    private double[] simulation(State state) {
        State s = state;
        while (!s.isFinalState()) {
            if (useHeuristics) {
                //TODO add heuristic to simulation
                int tricksNeeded = s.getBids()[s.getCurrentPlayer()]- s.getPlayerTricks()[s.getCurrentPlayer()];
            }
            s = s.makeRandomMove();         
        }
        return s.evaluate();
    }
    private Node bestChild(Node node, double exploitationParameter) {
        Iterator<Node> it = node.getChildren().iterator();
        Node best = it.next();
        double bestUCTValue = uctValue(node, best, exploitationParameter);
        while (it.hasNext()) {
            Node next = it.next();
            if (bestUCTValue < uctValue(node, next, exploitationParameter)) {
                bestUCTValue = uctValue(node, next, exploitationParameter);
                best = next;
                
            } else if (Math.abs(bestUCTValue - uctValue(node, next, exploitationParameter)) < 0.1) {
                // brake ties;
                Random rand = new Random();
                if (rand.nextBoolean()) {
                    bestUCTValue = uctValue(node, next, exploitationParameter);
                    best = next;
                }
            }
        }
        return best;
    }
    
   
    private void backup(Node node, double[] rewards) {
        Node next = node;
        while (next != null) {
            next.incrementVisits();
            next.addRewards(rewards);
            next = next.getParent();
        }
    }

    private double uctValue(Node parent, Node child, double c) {
        double uctValue = child.getReward(parent.getState().getCurrentPlayer()) / child.getVisits();
        uctValue += c * Math.sqrt((2 * Math.log(parent.getVisits())) / (child.getVisits()));
        if (useHeuristics) {
        uctValue += heuricsticValue(child);      
        }
       // System.out.println(uctValue);
        return uctValue;
    }

    private static double heuricsticValue(Node node) {
        double value = 0;
        State state = node.getState();
        int roundsLeft = State.getRound()-state.getCurrentTrick()+1;
        int minimumOverBid = state.getBids()[state.getCurrentPlayer()]-roundsLeft;
        if (minimumOverBid >0) {
            value -= minimumOverBid;
        }
            
        return value;
    }

}
