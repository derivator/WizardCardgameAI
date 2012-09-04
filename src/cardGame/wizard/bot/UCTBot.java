package cardGame.wizard.bot;

import cardGame.Card;
import cardGame.GameState;
import cardGame.Player;
import cardGame.wizard.*;
import cardGame.wizard.bot.uct.State;
import cardGame.wizard.bot.uct.UCT;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class UCTBot implements WizardController {

    private final String name;
    protected Set<Card>[] hands;
    protected Set<Card> hand;
    protected WizardState gameState;
    private double exploitationParameter;

    public UCTBot() {
        Random rand = new Random();
        name = "Bot #" + rand.nextInt(500);
    }

    public UCTBot(String name, double exploitationParameter) {
        this.name = name;
        this.exploitationParameter =  exploitationParameter;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void move() {
        State initialState = new State(gameState);
        gameState.playCard(UCT.uctSearch(initialState, exploitationParameter).getCard());

    }

    @Override
    public void bid() {
        State initialState = new State(gameState);
        gameState.doBid(UCT.uctSearch(initialState, exploitationParameter).getBid());
    }

    @Override
    public void notifyHand(Set<Card> hand) {
        this.hand = hand;

    }

    @Override
    public void assignGameState(GameState state) {
        gameState = (WizardState) state;

    }

    @Override
    public void notifyMove() {
        //TODO: add code
    }

    @Override
    public void notifyTrickCompleted(List<Card> trick, Player player) {
        //TODO add code
    }
}
