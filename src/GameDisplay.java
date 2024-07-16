import javax.swing.*;
import java.awt.*;

public class GameDisplay extends JFrame {

    public GameDisplay(String title) throws HeadlessException {
        super(title);
    }


    public static void main(String[] args) {
        GameDisplay display = new GameDisplay("Space War");
        display.setResizable(false);
        display.setFocusable(false);
        display.setSize(910, 740);
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Game game = new Game();
        game.requestFocus();

        game.addKeyListener(game);

        game.setFocusable(true);
        game.setFocusTraversalKeysEnabled(false);
        display.add(game);
        display.setVisible(true);


    }
}
