
// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

public class Game implements Runnable {

    private final JFrame frame = new JFrame("Last Stand TD");
    private String inputName = null;

    public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for local
        // variables.

        // Top-level frame in which game components live
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

        // Create highScoreLabels
        JLabel[] highScoreLabels = new JLabel[10];
        for (int i = 0; i < highScoreLabels.length; i++) {
            highScoreLabels[i] = new JLabel("");
        }

        // Main playing area
        final GameMap gameMap = new GameMap(this, round_label, timer_label, coins_label, infoLabel,
                infoSubLabel, infoButton1, infoButton2, highScoreLabels);

        // Game Control Panel
        final JPanel game_panel = new JPanel();
        final JButton addShooterTower = new JButton(
                "Shooter Tower (-" + ShooterTower.INITIAL_COST + ")");
        addActionListenerTowerButton(gameMap, infoLabel, infoButton1, addShooterTower, "SHOOTER");

        game_panel.add(addShooterTower);

        // Score Panel
        final JPanel score_panel = new JPanel();
        final JButton reset = new JButton("RESET GAME");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameMap.clearAllInfoComps();
                gameMap.setSelectedTower(null);
                infoLabel.setText("RESET");
                gameMap.reset();
            }
        });
        final JButton start = new JButton("START GAME");
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameMap.clearAllInfoComps();
                gameMap.setSelectedTower(null);
                infoLabel.setText("GAME START!");
                if (gameMap.gameNeedsReset()) {
                    gameMap.reset();
                }
                gameMap.startGame();
            }
        });
        final JPanel score_box = new JPanel();
        score_box.setLayout(new BoxLayout(score_box, BoxLayout.PAGE_AXIS));
        score_box.add(start);
        score_box.add(Box.createRigidArea(new Dimension(0, 10)));
        score_box.add(reset);
        score_box.add(Box.createRigidArea(new Dimension(0, 150)));
        JLabel highScoreLabel = new JLabel("---------------- HIGH SCORES ----------------");
        score_box.add(highScoreLabel);
        for (int i = 0; i < highScoreLabels.length; i++) {
            score_box.add(highScoreLabels[i]);
        }
        score_panel.add(score_box);
        refreshHighScores(highScoreLabels);

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

    public void addActionListenerTowerButton(GameMap gameMap, JLabel infoLabel, JButton infoButton1,
            JButton button, String towerType) {
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

    public Frame getFrame() {
        return this.frame;
    }

    public static void refreshHighScores(JLabel[] highScoreLabels) {
        String[][] highScores = getHighScores("files/high_scores.txt");
        if (highScores != null) {
            for (int i = 0; i < highScores.length; i++) {
                highScoreLabels[i].setText(highScores[i][0] + ": " + highScores[i][1]);
            }
        }
    }

    public static String[][] getHighScores(String filePath) {
        BufferedReader buffReader = null;
        FileReader fileReader = null;
        ArrayList<String[]> highScores = new ArrayList<String[]>();
        String nextLine = null;

        try {
            if (filePath == null) {
                return null;
            } else {
                fileReader = new FileReader(filePath);
            }
        } catch (FileNotFoundException e) {
            System.out.println("file doesnt exist");
            return null;
        } finally {
            if (fileReader != null) {
                buffReader = new BufferedReader(fileReader);
            }

            try {
                if (buffReader != null) {
                    nextLine = buffReader.readLine();
                }

            } catch (IOException e) {
                return null;
            }
        }

        while (nextLine != null) {
            String[] splitLine = nextLine.split(" ");

            if (splitLine != null) {
                if (splitLine.length == 2) {
                    int score;
                    try {
                        score = Integer.parseInt(splitLine[1].trim());
                    } catch (NumberFormatException e) {
                        score = -1;
                    }

                    if (score > 0) {
                        highScores.add(splitLine);
                    }

                }
            }

            try {
                if (buffReader != null) {
                    nextLine = buffReader.readLine();
                }
            } catch (IOException e) {
                return null;
            }

        }

        try {
            buffReader.close();
        } catch (IOException e) {
        }

        for (String[] entry : highScores) {
            System.out.println(entry[0] + ", " + entry[1]);
        }

        return selectionSortTop10(listToStringArr(highScores));
    }

    public static String[][] listToStringArr(ArrayList<String[]> list) {
        String[][] stringArr = new String[list.size()][2];

        int index = 0;
        boolean makeNull = false;
        for (String[] entry : list) {
            if (entry.length != 2) {
                makeNull = true;
            }

            stringArr[index][0] = entry[0];
            stringArr[index][1] = entry[1];
            index++;
        }

        if (makeNull) {
            return null;
        } else {
            return stringArr;
        }
    }

    public static String[][] selectionSortTop10(String[][] stringArr) {
        int size = stringArr.length;

        for (int i = 0; i < size - 1; i++) {
            int maxIndex = i;

            for (int j = i + 1; j < size; j++) {
                int scoreMaxIndex;
                int scoreJ;

                try {
                    scoreMaxIndex = Integer.parseInt(stringArr[maxIndex][1]);
                } catch (NumberFormatException e) {
                    scoreMaxIndex = -1;
                }

                try {
                    scoreJ = Integer.parseInt(stringArr[j][1]);
                } catch (NumberFormatException e) {
                    scoreJ = -1;
                }

                if (scoreMaxIndex > 0 && scoreJ > 0) {
                    if (scoreJ > scoreMaxIndex) {
                        maxIndex = j;
                    }
                }

            }

            String nameTemp = stringArr[i][0];
            String scoreTemp = stringArr[i][1];

            stringArr[i][0] = stringArr[maxIndex][0];
            stringArr[i][1] = stringArr[maxIndex][1];

            stringArr[maxIndex][0] = nameTemp;
            stringArr[maxIndex][1] = scoreTemp;
        }

        if (size > 10) {
            String[][] modified = new String[10][2];

            for (int i = 0; i < 10; i++) {
                modified[i][0] = stringArr[i][0];
                modified[i][1] = stringArr[i][1];
            }

            return modified;
        } else {
            return stringArr;
        }
    }

    public static void updateHighScores(String filePath, Frame frame, JLabel[] highScoreLabels,
            int newScore) {
        BufferedWriter buffWriter = null;
        FileWriter fileWriter = null;
        String[][] highScores = getHighScores(filePath);
        String[][] newStringArr = new String[highScores.length + 1][2];

        if (highScores != null) {
            int lowestScore;

            if (highScores.length > 0) {
                try {
                    lowestScore = Integer.parseInt(highScores[highScores.length - 1][1]);
                } catch (NumberFormatException e) {
                    lowestScore = -1;
                }
            } else {
                lowestScore = -1;
            }

            System.out.println("lowest score: " + lowestScore);

            if (newScore > lowestScore || highScores.length < 10) {
                FileWriter testFileFound = null;

                try {
                    if (filePath != null) {
                        // Used to check if file found before deleting contents
                        testFileFound = new FileWriter(filePath, true);
                        System.out.println("Length high scores: " + highScores.length);
                    }
                } catch (FileNotFoundException e) {
                    testFileFound = null;
                    System.out.println("file doesnt exist");
                } catch (IOException e) {
                } finally {
                    if (testFileFound != null) {
                        try {
                            if (filePath != null) {
                                fileWriter = new FileWriter(filePath, false);
                            }
                        } catch (FileNotFoundException e) {
                            fileWriter = null;
                            System.out.println("file doesnt exist");
                        } catch (IOException e) {
                        } finally {
                            if (fileWriter != null) {
                                buffWriter = new BufferedWriter(fileWriter);

                                String inputName = JOptionPane.showInputDialog(frame,
                                        "Please enter a username.", "Username",
                                        JOptionPane.PLAIN_MESSAGE);

                                newStringArr[newStringArr.length - 1][0] = inputName;
                                newStringArr[newStringArr.length - 1][1] = "" + newScore;

                                if (highScores.length > 0) {
                                    for (int i = 0; i < highScores.length; i++) {
                                        System.out.println(
                                                "newStringArr.length: " + newStringArr.length);
                                        newStringArr[i][0] = highScores[i][0];
                                        newStringArr[i][1] = highScores[i][1];
                                    }
                                }

                                String[][] refinedArr = selectionSortTop10(newStringArr);

                                boolean continueWriting = true;
                                for (int i = 0; i < refinedArr.length && continueWriting; i++) {
                                    try {
                                        buffWriter.write(refinedArr[i][0] + " " + refinedArr[i][1]);
                                        buffWriter.newLine();
                                    } catch (IOException e) {
                                        continueWriting = false;
                                    }
                                }

                                try {
                                    buffWriter.close();
                                } catch (IOException e) {
                                }

                                refreshHighScores(highScoreLabels);
                            }
                        }
                    }
                }

            }
        }

    }

//    inputName = JOptionPane.showInputDialog(frame, "Please enter a username test", 
//            "Username", JOptionPane.PLAIN_MESSAGE);

    /**
     * Main method run to start and run the game. Initializes the GUI elements
     * specified in Game and runs it. IMPORTANT: Do NOT delete! You MUST include
     * this in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}
