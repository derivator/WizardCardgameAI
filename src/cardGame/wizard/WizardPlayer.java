package cardGame.wizard;

import cardGame.Game;
import cardGame.Card;
import cardGame.Player;
import java.util.List;

public class WizardPlayer extends Player {

    private int bid;
    private int tricks;

    public WizardPlayer(Game game, WizardController controller) {
        super(game, controller);
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int currentBid) {
        this.bid = currentBid;
    }

    public int getTricks() {
        return tricks;
    }

    public void resetTricks() {
        this.tricks = 0;
    }

    public void notifyBid() {
        ((WizardController) controller).bid();
    }

    public void notifyTrickCompleted(List<Card> trick, Player player) {
        ((WizardController) controller).notifyTrickCompleted(trick, player);
    }

    public void addTrick() {
        tricks++;

    }

    public String toString() {
        return super.toString() + "(" + tricks + "/" + bid + ")";
    }
}
