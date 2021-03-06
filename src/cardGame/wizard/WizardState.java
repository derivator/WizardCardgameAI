package cardGame.wizard;

import cardGame.Card;
import cardGame.GameState;
import java.util.HashSet;

public interface WizardState extends GameState {

    public void doBid(int bid);

    public boolean unevenBidsEnforced();

    public int getBid(int player);
    
    public int getTotalBids();

    public int getTricks(int player);

    public int getScore(int player);

    public WizardCard getTrumpIndicator();

    public int getTrumpSuit();

    public HashSet<Card>[] getHands(); // for cheating bots
}
