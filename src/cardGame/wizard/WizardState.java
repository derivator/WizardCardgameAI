package cardGame.wizard;

import cardGame.GameState;

public interface WizardState extends GameState {

    public void doBid(int bid);

    public boolean unevenBidsEnforced();

    public int getBid(int player);

    public int getTricks(int player);

    public int getScore(int player);

    public int getTotalBids();
       
    public WizardCard getTrumpIndicator();

    public int getTrumpSuit();
}
