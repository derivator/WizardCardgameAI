
public class WizardPlayer extends CardGamePlayer {
	protected int currentBid;
	protected int tricks;
	
	WizardPlayer(CardGame game,WizardController controller) {
		super(game, controller);
		
	}
	
	public int getCurrentBid() {
		return currentBid;
	}

	public void setCurrentBid(int currentBid) {
		this.currentBid = currentBid;
	}

	public int getTricks() {
		return tricks;
	}

	public void resetTricks() {
		this.tricks = 0;
	}

	@Override
	public void notifyTurn() {
		controller.notifyTurn();		
	}
	
	public void notifyBid(){
		((WizardController)controller).notifyBid();
	}

	public void addTrick() {
		tricks++;
		
	}
	
	public String toString(){
		return super.toString() + "("+tricks+"/" + currentBid +")";
	}

}
