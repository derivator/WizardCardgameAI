import java.util.ArrayList;
import java.util.List;


public interface PlayerController {
	public void assignGameState(CardGameState state);
	public String getName();
	public void notifyTurn();
	public void notifyHand(List<CardGameCard> hand);
	
}
