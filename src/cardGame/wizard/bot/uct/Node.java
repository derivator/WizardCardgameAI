package cardGame.wizard.bot.uct;

import cardGame.Card;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Olli
 */
public class Node {

    private State state;
    // TODO : action might not be handled correctly
    private Move move; // action that led to this state/node;
    private Node Parent;
    List<Node> children;
    // for UCT:
    private int visits = 0;
    private double[] rewards;

    public Node(State state, Node Parent, Move move) {
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
            children = new ArrayList<>();
        }
        State newState = state.makeMove(move);
        Node newNode = new Node(newState, this, move);
        children.add(newNode); 
        return newNode;
    } 

    public Node randomExpand() {
        ArrayList<Move> playable = state.getPossibleMoves();
        Random rand = new Random();      
        Move move = playable.get(rand.nextInt(playable.size()));      
        return expand(move);
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
    public void addRewards(int[] r) {
        if (rewards==null) {
            rewards = new double[State.getPlayers()];
        }
        for (int i=0; i<rewards.length; i++) {
            rewards[i] += r[i];
        }
    }
    

}
