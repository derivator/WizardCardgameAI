package cardGame.wizard.bot.uct;

import cardGame.wizard.bot.WizardAI;
import java.util.Iterator;
import java.util.Random;


public class UCT extends WizardAI{
    
    private int visits;
    private double exploitationParameter;
    private boolean useHeuristics;

    public UCT(int visits, double exploitationParameter, boolean useHeuristics) {
        this.visits = visits;
        this.exploitationParameter = exploitationParameter;
        this.useHeuristics = useHeuristics;
    }
    
    @Override
    public Move makeDecision(FullyObservableState initialState) {
        Node root = new Node(initialState, null, null);
        long time = System.nanoTime();
        while ((root.getVisits()) < visits) {
           Node selected = treePolicy(root, exploitationParameter);
           double[] rewards = simulation(selected.getState());
           backup(selected, rewards);        
        }
        Move chosen = bestChild(root, 0).getMove();
        for (Node child : root.getChildren()) {
            System.out.println("Move "+child.getMove()+  " - Reward: " + child.getReward(root.getState().getCurrentPlayer())+", visits: " +child.getVisits());        
        }
        long delta = System.nanoTime()-time;
        System.out.println(delta/1000000);
        return chosen;
        
    }
    
    private Node treePolicy(Node node, double exploitationParameter) {
        Node next = node;
        FullyObservableState state = next.getState();
        while (!state.isFinal()) {
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
    
    private double[] simulation(FullyObservableState state) {
        FullyObservableState s = state;
        while (!s.isFinal()) {
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
        FullyObservableState state = node.getState();
        int roundsLeft = FullyObservableState.getRound()-state.getCurrentTrick()+1;
        int minimumOverBid = state.getBids()[state.getCurrentPlayer()]-roundsLeft;
        if (minimumOverBid >0) {
            value -= minimumOverBid;
        }
            
        return value;
    }

}
