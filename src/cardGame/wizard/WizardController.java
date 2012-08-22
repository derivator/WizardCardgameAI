package cardGame.wizard;


import cardGame.Card;
import cardGame.Player;
import cardGame.PlayerController;
import java.util.List;



public interface WizardController extends PlayerController {
	public void bid();
        public void notifyTrickCompleted(List<Card> trick, Player player);
}
