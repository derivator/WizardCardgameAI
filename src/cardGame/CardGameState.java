package cardGame;



import java.util.ArrayList;
import java.util.List;

public interface CardGameState {
	public List<CardGameCard> getTableCards();
	public boolean cardLegallyPlayable(CardGameCard card, List<CardGameCard> hand);
        
	public int getRound();
	public int getTurnPlayer();
	public int getTurnStarter();
	public int getNextPlayer();
	void playCard(CardGameCard card);
	
}
