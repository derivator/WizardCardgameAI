package cardGame.wizard.bot.mcts;


public class GameTree {
    
    public Node root;
    int size;

    public GameTree(State initialState) {   
        root = new Node(initialState, null);

    }
    
    public void expand(int times) throws Exception {
        for (int i=0; i<times; i++) {
            Node node = select(SelectionPolicy.RANDOM);
            node.randomExpand();
        }
    }
    
    public Node select(SelectionPolicy p) throws Exception {
        Node node = root;
        
        while (node.getChildren() != null && node.getChildren().size() >= p.getThreshold()) {
            node = p.select(node.getChildren());
        }
        if (node.getState().isFinalState()) {
            return null;
        }
        return node;
    }

    
}
