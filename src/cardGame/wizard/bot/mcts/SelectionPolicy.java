/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cardGame.wizard.bot.mcts;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum SelectionPolicy {

    RANDOM(1);
    private final int threshold;

    private SelectionPolicy(int threshold) {
        this.threshold = threshold;
    }

    public Node select(List<Node> nodes) throws Exception {
        switch (this) {
            case RANDOM:
                Random rand = new Random();
                return nodes.get(rand.nextInt(nodes.size())); 
        }
        throw new Exception("Could not select a node");
    }

    public int getThreshold() {
        return threshold;
    }
    
}
