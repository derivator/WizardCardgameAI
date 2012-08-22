package cardGame;



import java.util.List;


public interface PlayerController {
	public void assignGameState(State state);
	public String getName();
	public void move();
	public void notifyHand(List<Card> hand);
        public void notifyMove();
	
}
