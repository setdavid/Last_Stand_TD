
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
        final JPanel info_box = new JPanel();
        info_box.setLayout(new BoxLayout(info_box, BoxLayout.PAGE_AXIS));
        final JLabel infoLabel = new JLabel(" ");
        final JLabel infoSubLabel = new JLabel(" ");
        final JButton infoButton1 = new JButton(" ");
        final JButton infoButton2 = new JButton(" ");
        info_box.add(infoLabel);
        info_box.add(infoSubLabel);
        info_box.add(infoButton1);
        info_box.add(infoButton2);
        info_panel.add(info_box);

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
        final GameMap gameMap = new GameMap(round_label, timer_label, coins_label, 
                infoLabel, infoSubLabel, infoButton1, infoButton2);

        // Game Control Panel
        final JPanel game_panel = new JPanel();
        final JButton addShooterTower = new JButton("Shooter Tower (-" + 
                ShooterTower.INITIAL_COST + ")");
        addActionListenerTowerButton(gameMap, infoLabel, infoButton1, addShooterTower, "SHOOTER");

        game_panel.add(addShooterTower);

        // Score Panel
        final JPanel score_panel = new JPanel();
        final JButton reset = new JButton("RESET GAME");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameMap.clearAllInfoComps();
                infoLabel.setText("RESET");
                gameMap.reset();
            }
        });
        final JButton start = new JButton("START GAME");
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameMap.clearAllInfoComps();
                infoLabel.setText("GAME START!");
                if (!gameMap.isPlaying() && !gameMap.gameNeedsReset()) {
                    gameMap.startGame();
                }
            }
        });
        final JPanel score_box = new JPanel();
        score_box.setLayout(new BoxLayout(score_box, BoxLayout.PAGE_AXIS));
        score_box.add(start);
        score_box.add(Box.createRigidArea(new Dimension(0,10)));
        score_box.add(reset);
        score_box.add(Box.createRigidArea(new Dimension(0,150)));
        JLabel highScoreLabel = new JLabel("---------------- HIGH SCORES ----------------");
        score_box.add(highScoreLabel);
        JLabel[] highScores = new JLabel[10];
        for (int i = 0; i < highScores.length; i++) {
            highScores[i] = new JLabel("This is a test ");
            score_box.add(highScores[i]);
        }
        score_panel.add(score_box);

        frame.add(info_panel, BorderLayout.SOUTH);
        frame.add(game_panel, BorderLayout.EAST);
        frame.add(score_panel, BorderLayout.WEST);
        frame.add(status_panel, BorderLayout.NORTH);
        frame.add(gameMap, BorderLayout.CENTER);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
//        court.reset();
    }

    public void addActionListenerTowerButton(GameMap gameMap, JLabel infoLabel, 
            JButton infoButton1, JButton button, String towerType) {
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameMap.clearAllInfoComps();
                gameMap.setSelectedTower(towerType);
                infoLabel.setText("CHOOSE BLOCK TO ADD " + towerType + " TOWER");
                infoButton1.setText("CANCEL");
                infoButton1.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        gameMap.clearAllInfoComps();
                        gameMap.setSelectedTower(null);
                    }
                });
            }
        });
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
