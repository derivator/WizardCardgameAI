
public interface WizardState extends CardGameState {
	WizardCard getTrumpIndicator();
	
	boolean unevenBidsEnforced();
	int getBid(int player);
	int getTotalBids();		
	void doBid(int bid);

	byte getTrumpSuit();
	
	
}
