package cardGame;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Player {

    protected final Game game;
    protected PlayerController controller;
    private ArrayList<Card> hand;
    protected int score;

    protected Player(Game game, PlayerController controller) {
        this.game = game;
        this.controller = controller;
        this.controller.assignGameState(game);
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
        if (controller != null) {
            controller.notifyHand(getHandClone());
        }
    }

    public ArrayList<Card> getHand() {
        return (ArrayList) getHandClone();
    }

    public List<Card> getHandClone() {
        return Game.cloneCards(hand);
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
