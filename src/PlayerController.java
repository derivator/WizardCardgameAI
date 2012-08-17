import java.util.ArrayList;


public interface PlayerController {
	public void assignGameState(CardGameState state);
	public String getName();
	public void notifyTurn();
	public void notifyHand(ArrayList<CardGameCard> hand);
	
}
