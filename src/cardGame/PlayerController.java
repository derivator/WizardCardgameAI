package cardGame;

import java.util.Set;


public interface PlayerController {
	public void assignGameState(GameState state);
	public String getName();
	public void move();
	public void notifyHand(Set<Card> hand);
        public void notifyMove();
	
}
