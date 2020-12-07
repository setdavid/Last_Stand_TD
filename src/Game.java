
// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Game implements Runnable {

    public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for local
        // variables.

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Last Stand TD");
        frame.setLocation(500, 0);

        // Status
        final JPanel status_panel = new JPanel();
        final JLabel round_label = new JLabel("Round: 0");
        final JLabel coins_label = new JLabel("Coins: 0");
        final JLabel timer_label = new JLabel("Next round starts in: 0");
//        timer.setFont(new Font(timer.getFont().getName(), timer.getFont().getStyle(), 25));
        status_panel.add(round_label);
        status_panel.add(timer_label);
        status_panel.add(coins_label);
        frame.add(status_panel, BorderLayout.NORTH);

        // Main playing area
        final GameMap gameMap = new GameMap(round_label, timer_label, coins_label);
        frame.add(gameMap, BorderLayout.CENTER);

        // Game Control Panel
        final JPanel game_panel = new JPanel();
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameMap.reset();
            }
        });
        game_panel.add(reset);
        frame.add(game_panel, BorderLayout.EAST);

        // User High Score Panel
        final JPanel user_panel = new JPanel();
        frame.add(user_panel, BorderLayout.WEST);

//        // Status panel
//        final JPanel status_panel = new JPanel();
//        frame.add(status_panel, BorderLayout.SOUTH);
//        final JLabel status = new JLabel("Running...");
//        status_panel.add(status);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
//        court.reset();
    }

    /**
     * Main method run to start and run the game. Initializes the GUI elements
     * specified in Game and runs it. IMPORTANT: Do NOT delete! You MUST include
     * this in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}
