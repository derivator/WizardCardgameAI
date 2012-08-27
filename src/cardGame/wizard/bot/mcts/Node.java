package cardGame.wizard.bot.mcts;

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
    private Card action; // action that led to this state/node;
    private Node Parent;
    List<Node> children;
    // for UCT:
    private int visits = 0;
    private double[] rewards;

    public Node(State state, Node Parent, Card action) {
        this.state = state;
        this.Parent = Parent;
        this.action = action;
    }
    /**
     * May create duplicate Nodes
     * @param action
     * @return 
     */
    public Node expand(Card action) {
        if (children == null) {
            children = new ArrayList<>();
        }
        State newState = state.makeMove(action);
        Node newNode = new Node(newState, this, action);
        children.add(newNode); 
        return newNode;
    } 

    public Node randomExpand() {
        ArrayList<Card> playable = state.getPlayableCards();
        int size = playable.size();
        Random rand = new Random();      
        Card playCard = playable.get(rand.nextInt(playable.size()));      
        return expand(playCard);
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

    public Card getAction() {
        return action;
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
