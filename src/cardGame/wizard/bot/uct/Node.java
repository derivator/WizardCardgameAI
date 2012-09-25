package cardGame.wizard.bot.uct;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Olli
 */
public class Node {

    private FullyObservableState state;
    private Move move; // move that led to this state/node;
    private Node Parent;
    List<Node> children;
    // for UCT:
    private int visits = 0;
    private double[] rewards;

    public Node(FullyObservableState state, Node Parent, Move move) {
        this.state = state;
        this.Parent = Parent;
        this.move = move;
    }
    /**
     * May create duplicate Nodes
     * @param move
     * @return 
     */
    public Node expand(Move move) {
        if (children == null) {
            children = new ArrayList<>(5);
        }
        FullyObservableState newState = state.makeMove(move);
        Node newNode = new Node(newState, this, move);
        children.add(newNode); 
        return newNode;
    } 

    public Node randomExpand() {
        ArrayList<Move> playable = state.getPossibleMoves();
        ArrayList<Move> exclude = new ArrayList<>();
        if (getChildren() != null) {
            for (Node child : getChildren()) {
                for (Move m : playable) {
                    if (child.getMove().equals(m)) {
                        exclude.add(m);
                    }
                }
            }
            playable.removeAll(exclude);
        }
        
        Random rand = new Random();
        Move randomMove = playable.get(rand.nextInt(playable.size()));
        return expand(randomMove);
    }

    public List<Node> getChildren() {
        return children;
    }

    public Node getParent() {
        return Parent;
    }

    public FullyObservableState getState() {
        return state;
    }

    public Move getMove() {
        return move;
    }
    

    public double getReward(int player) {
        return rewards[player];
    }

    public void setReward(int reward, int player) {
        rewards[player] = reward;
    }
    
    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }
    
    public void incrementVisits() {
        visits++;
    }
    public void addRewards(double[] r) {
        if (rewards==null) {
            rewards = new double[FullyObservableState.getPlayers()];
        }
        for (int i=0; i<rewards.length; i++) {
            rewards[i] += r[i];
        }
    }
    
}
