package cardGame.wizard.bot;

import cardGame.Card;
import cardGame.GameState;
import cardGame.Player;
import cardGame.wizard.*;
import cardGame.wizard.bot.uct.FullyObservableState;
import cardGame.wizard.bot.uct.UCT;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class WizardBot implements WizardController {

    private final String name;
    private WizardState gameState;
    private WizardAI ai;

    public WizardBot() {
        Random rand = new Random();
        name = "Bot #" + rand.nextInt(500);
    }

    public WizardBot(String name, WizardAI ai) {
        this.name = name;
        this.ai = ai;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void move() {
        FullyObservableState initialState = new FullyObservableState(gameState);
        gameState.playCard(ai.makeDecision(initialState).getCard());

    }

    @Override
    public void bid() {
        FullyObservableState initialState = new FullyObservableState(gameState);
        gameState.doBid(ai.makeDecision(initialState).getBid());
    }

    @Override
    public void notifyHand(Set<Card> hand) {

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
