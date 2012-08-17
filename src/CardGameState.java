import java.util.ArrayList;

public interface CardGameState {
	public ArrayList<CardGameCard> getTableCards();
	public boolean cardLegallyPlayable(CardGameCard card, ArrayList<CardGameCard> hand);
	
	public int getRound();
	public int getTurnPlayer();
	public int getTurnStarter();
	public int getNextPlayer();
	void playCard(CardGameCard card);
	
}
