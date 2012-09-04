package cardGame;

import java.util.Iterator;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class Player {

    protected final Game game;
    protected PlayerController controller;
    private HashSet<Card> hand;
    protected int score;

    protected Player(Game game, PlayerController controller) {
        this.game = game;
        this.controller = controller;
        this.controller.assignGameState(game);
    }

    public void setHand(HashSet<Card> hand) {
        this.hand = hand;
        if (controller != null) {
            controller.notifyHand(getHandClone());
        }
    }

    public HashSet<Card> getHand() {
        return  hand;
    }

    public HashSet<Card> getHandClone() {
        return  (HashSet) Game.cloneHand(hand);
    }

    public PlayerController getController() {
        return controller;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    
    
    
    @Override
    public String toString() {
        String result;
        if (controller != null) {
            result = controller.getName();
        } else {
            result = "ERROR";
        }

        result += ":[";
        Iterator<? extends Card> it = hand.iterator();
        while (it.hasNext()) {
            result += it.next().toString();
            if (it.hasNext()) {
                result += ", ";
            }
        }
        result += "]";
        return result;
    }

    public void move() {
        controller.move();
    }
    
    public void notifyMove() {
        controller.notifyMove();
    }
    
}
