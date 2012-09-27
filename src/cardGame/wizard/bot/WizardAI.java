/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cardGame.wizard.bot;

import cardGame.wizard.bot.uct.FullyObservableState;
import cardGame.wizard.bot.uct.Move;

/**
 *
 * @author Olli
 */
public abstract class WizardAI {
    
    public abstract Move makeDecision(FullyObservableState state);
    
}
