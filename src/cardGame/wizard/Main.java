/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cardGame.wizard;

import cardGame.wizard.bot.WizardAI;
import cardGame.wizard.bot.WizardBot;
import cardGame.wizard.bot.uct.UCT;

/**
 *
 * @author Olli
 */
public class Main {
    
        public static void main(String[] args) {
        WizardGame game = new WizardGame();
        WizardAI UCT1000 = new UCT(1000, 0.5, false);
        game.addPlayer(new WizardBot("Burt", UCT1000));
        game.addPlayer(new WizardBot("Clide",UCT1000));
        game.addPlayer(new WizardBot("MC Fallhin",UCT1000));
        
        for (int i = 0; i < 1; i++) {
            game.startGame(10);
            while (game.getRound()<11) {           
                //wait for network/user input here?
                game.advance();
            }
        }
        game.printScore();
    }
    
}
