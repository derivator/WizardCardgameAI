import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;


public class WizardGame extends CardGame implements WizardState {
	public enum RoundPhase{
		Bidding,
		Playing,
	}
	protected RoundPhase roundPhase;
	protected boolean unevenBids = true;
	protected boolean waitingForCard = false;
	protected int trickStarter;
	protected int overUnderBid;
	
	public WizardPlayer getWizardPlayer(int player)
	{
		return (WizardPlayer) players.get(player);
	}
	
	@Override
	public void addPlayer(PlayerController pc) {
		players.add(new WizardPlayer(this,(WizardController) pc));
	}
		
	@Override
	public void startGame() {
		Collections.shuffle(players);
		super.startGame();		
	}
	
	@Override
	public void startRound(){
		round++;
		if(round > 60/players.size())
		{
			inProgress=false;
			return;
		}
		tableCards.clear();
		turnPlayer=roundStarter;
		nextPlayer();
		roundStarter=turnPlayer;
		trickStarter=turnPlayer;
		
		setupDeck();
		dealCards(round);
		roundPhase=RoundPhase.Bidding;
		for(CardGamePlayer player : players)
		{
			((WizardPlayer)player).resetTricks();
			((WizardPlayer)player).currentBid=-1;
		}
	}
	
	private void printScore() {
		for(CardGamePlayer player : players)
		{
			WizardPlayer p = (WizardPlayer) player;
			System.out.println(p.controller.getName()+ ": " + p.score);
		}
		System.out.println("Over/Under-Bid: "+overUnderBid);
	}

	@Override
	public void startTurn() {
		getWizardPlayer(turnPlayer).notifyTurn();
	}

	public void dealCards(int amount){
		Iterator<CardGameCard> it = deck.iterator();
		for(CardGamePlayer p:players)
		{
			ArrayList<CardGameCard> dealtHand = new ArrayList<CardGameCard>();
			for(int i = 0;i<amount;i++)
			{
				dealtHand.add(it.next());
				it.remove();
			}
			p.setHand(dealtHand);
		}
		if(it.hasNext())
			System.out.println("Trump color is determined by: " + it.next().toString());
	}
	
	protected void setupDeck(){
		deck = new ArrayList<CardGameCard>();
		for(byte s=0;s<4;s++)
		{
			for(byte v=0;v<15;v++)
			{
				deck.add(new WizardCard(s,v));
			}
		}
		Collections.shuffle(deck);
	}

	@Override
	public int getBid(int player) {
		return getWizardPlayer(player).currentBid;
	}

	@Override
	public void doBid(int bid) {
		if(unevenBids && getNextPlayer()==roundStarter && getTotalBids() + bid == round)
		{
			
			bid += 2;
			try {
				throw new Exception("incorrect bid");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		getWizardPlayer(turnPlayer).currentBid = bid;
	}

	

	public int getTotalBids() {
		int total = 0;
		for(int i = 0;i<players.size();i++)
		{
			if(getBid(i) > 0)
				total+=getBid(i);
		}
		return total;
	}

	@Override
	public void advance() {
		switch(roundPhase){
			case Bidding:
				phaseBidding();
				break;
			case Playing:
				phasePlaying();
				break;
		}	
	}
	public void phaseBidding(){
		if(getBid(turnPlayer)==-1)
			getWizardPlayer(turnPlayer).notifyBid();
		else
		{
			nextPlayer();
			if(turnPlayer==roundStarter)
			{
				roundPhase = RoundPhase.Playing;
				waitingForCard = true;
			}
		}
	}
	public void phasePlaying(){
		if(waitingForCard)
		{
			startTurn();
		}
		else
		{
			nextPlayer();
			if(turnPlayer==trickStarter)
			{
				System.out.println(tableCards.toString());
				Collections.sort(tableCards, new WizardComparator(getTrumpSuit(),getFollowSuit(tableCards)));
				trickStarter = tableCards.get(0).owner;
				turnPlayer = tableCards.get(0).owner;
				getWizardPlayer(turnPlayer).addTrick();
				nextTrick();
				//TODO: tell controllers the final trick state
				
				
			}
			else
			{
				waitingForCard = true;
			}
		}
		
	}
	
	private void nextTrick() {
		tableCards.clear();
		if(getWizardPlayer(turnPlayer).getHand().isEmpty())
		{
			doScore();
			startRound();
			
		}
		else
			startTurn();
	}

	private void doScore() {
		for(CardGamePlayer player : players)
		{
			WizardPlayer p = (WizardPlayer) player;
			if(p.getTricks()==p.getCurrentBid())
				p.score+=20+10*p.getTricks();
			else
			{
				p.score -= 10*Math.abs(p.getTricks()-p.getCurrentBid());
				overUnderBid += p.getTricks()-p.getCurrentBid();
			}
		}
		
	}

	@Override
	public boolean unevenBidsEnforced() {
		return unevenBids;
	}

	@Override
	public void playCard(CardGameCard card) {
		ArrayList<CardGameCard> hand = getWizardPlayer(turnPlayer).getHand();
		if(hand.contains(card) && cardLegallyPlayable(card,hand))
		{
			CardGameCard c = (CardGameCard) card.clone();
			c.setOwner(turnPlayer);
			tableCards.add(c);
			hand.remove(card);
			getWizardPlayer(turnPlayer).setHand(hand);
			waitingForCard = false;
		}
		else
		{
			try {
				throw new Exception("illegal card played!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public WizardCard getTrumpIndicator() {
		if(deck.isEmpty())
			return null;
		else
			return (WizardCard) deck.get(0).clone();
	}
	
	public byte getTrumpSuit(){
		if(getTrumpIndicator() != null)
			return getTrumpIndicator().getSuit();
		
		return -128;
	}
	
	public static byte getFollowSuit(ArrayList<CardGameCard> table){
		byte followSuit = -128;
		for(CardGameCard c : table)
		{
			if(c.value!=0)
			{
				if(c.value == 14)
					return -128;
				
				followSuit = c.getSuit();
				break;
			}
		}
		return followSuit;
	}
	
	public boolean cardLegallyPlayable(CardGameCard card, ArrayList<CardGameCard> hand){				
		if(card.value==0||card.value==14)
			return true;
						
		byte followSuit = getFollowSuit(tableCards);
		
		if(followSuit == -128)
			return true;
		
		if(card.suit == followSuit)
			return true;
		
		for(CardGameCard c : hand)
		{
			if(c.suit == followSuit && c.value != 0 && c.value != 14)
				return false;
		}
		
		return true;
	}
	
	/**
	 * @param args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		WizardGame game = new WizardGame();
		game.addPlayer(new WizardBot("Burt"));
		game.addPlayer(new WizardBot("Clide"));
		game.addPlayer(new WizardBot("Charles Darwin"));
		game.addPlayer(new WizardBot("MC Fallhin"));
		if(false)
		{
			game.addPlayer(new WizardBot());
			game.addPlayer(new WizardBot());

		}
		for(int i = 0;i<100;i++)
		{
			game.startGame();
			while(game.isInProgress())
			{
				//wait for network/user input here?
				game.advance();
			}
		}
		
		
		game.printScore();
		
	}
	
}
