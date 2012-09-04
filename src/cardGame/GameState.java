package cardGame;

import java.util.Collection;
import java.util.List;

public interface GameState {

    public List<Card> getTableCards();

    public boolean cardLegallyPlayable(Card card, Collection<Card> hand);

    public void playCard(Card card);

    public int getNumberOfPlayers();

    public int getRound();

    public int getCurrentPlayer();

    public int getRoundStarter();

    public int getNextPlayer();
}
