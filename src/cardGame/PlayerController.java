package cardGame;



import java.util.List;


public interface PlayerController {
	public void assignGameState(CardGameState state);
	public String getName();
	public void move();
	public void notifyHand(List<CardGameCard> hand);
        public void notifyMove();
	
}
