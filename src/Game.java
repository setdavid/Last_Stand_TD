
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
        frame.setLocation(300, 0);

        // Info
        final JPanel info_panel = new JPanel();
        final JLabel info_label = new JLabel(" ");
        final JButton info_button_1 = new JButton(" ");
        final JButton info_button_2 = new JButton(" ");
        info_panel.add(info_label);
        info_panel.add(info_button_1);
        info_panel.add(info_button_2);

        // Status
        final JPanel status_panel = new JPanel();
        final JLabel round_label = new JLabel("Round: 0");
        final JLabel coins_label = new JLabel("Coins: 0");
        final JLabel timer_label = new JLabel("Next round starts in: 0");
//        timer.setFont(new Font(timer.getFont().getName(), timer.getFont().getStyle(), 25));
        status_panel.add(round_label);
        status_panel.add(timer_label);
        status_panel.add(coins_label);

        // Main playing area
        final GameMap gameMap = new GameMap(round_label, timer_label, coins_label, info_label, info_button_1, info_button_2);

        // Game Control Panel
        final JPanel game_panel = new JPanel();
        final JButton addShooterTower = new JButton("Shooter Tower (" + ShooterTower.INITIAL_COST + ")");
        addShooterTower.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameMap.clearAllInfoButtons();
                info_label.setText("Choose block to add shooter tower");
            }
        });

        game_panel.add(addShooterTower);

        // User High Score Panel
        final JPanel user_panel = new JPanel();
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameMap.clearAllInfoButtons();
                info_label.setText("RESET");
                gameMap.reset();
            }
        });
        user_panel.add(reset);

        frame.add(info_panel, BorderLayout.SOUTH);
        frame.add(game_panel, BorderLayout.EAST);
        frame.add(user_panel, BorderLayout.WEST);
        frame.add(status_panel, BorderLayout.NORTH);
        frame.add(gameMap, BorderLayout.CENTER);

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
