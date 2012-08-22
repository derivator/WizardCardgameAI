package cardGame.wizard;


import cardGame.State;




public interface WizardState extends State {
    
	public WizardCard getTrumpIndicator();
	
	public boolean unevenBidsEnforced();
	public int getBid(int player);
	public int getTricks(int player);
	public int getTotalBids();		
	public void doBid(int bid);

	int getTrumpSuit();
	
	
}
