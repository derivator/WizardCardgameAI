package cardGame;




import java.util.Iterator;
import java.util.List;

public abstract class CardGamePlayer {

    protected final CardGame game;
    protected PlayerController controller;
    private List<CardGameCard> hand;
    protected int score;

    protected CardGamePlayer(CardGame game, PlayerController controller) {
        this.game = game;
        this.controller = controller;
        this.controller.assignGameState(game);
    }

    public void setHand(List<CardGameCard> hand) {
        this.hand = hand;
        if (controller != null) {
            controller.notifyHand(getHandClone());
        }
    }

    public List<CardGameCard> getHand() {
        return hand;
    }

    public List<CardGameCard> getHandClone() {
        return CardGame.cloneCards(hand);
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
        Iterator<? extends CardGameCard> it = hand.iterator();
        while (it.hasNext()) {
            result += it.next().toString();
            if (it.hasNext()) {
                result += ", ";
            }
        }
        result += "]";
        return result;
    }

    public abstract void notifyTurn();
}
