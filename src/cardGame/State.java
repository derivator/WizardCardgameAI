package cardGame;

import java.util.List;

public interface State {
        
        public int getNumberOfPlayers();
	public List<Card> getTableCards();
	public boolean cardLegallyPlayable(Card card, List<Card> hand);
        
	public int getRound();
	public int getTurnPlayer();
	public int getTurnStarter();
	public int getNextPlayer();
	void playCard(Card card);
	
}
