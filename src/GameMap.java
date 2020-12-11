
import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import javax.swing.*;

@SuppressWarnings("serial")

public class GameMap extends JPanel {

    private final Game game;

    public static final int MAP_SIZE = 600;
    public static final double BLOCK_PROB = 0.3;
    public static final int ARRAY_SIZE = 25;

    private Tile[][] tileMap;
    private static final int TILE_SIZE = MAP_SIZE / ARRAY_SIZE;

    private Tile selectedTile = null;
    private String selectedTower = null;
    private JLabel infoLabel;
    private JLabel infoSubLabel;
    private JButton infoButton1;
    private JButton infoButton2;

    private JLabel[] highScoreLabels;

    private Timer mainTimer = null;
    public static final int INTERVAL = 60;

    private boolean playing;
    private boolean gameNeedsReset = false;

    private JLabel timerLabel;
    private Timer roundTimer;
    private int timeRemaining;
    public static final int TIME_BETWEEN_ROUNDS = 10000;
    private JLabel roundLabel;
    private int roundCount;
    private boolean roundInProgress;

    private JLabel coinsLabel;
    private int coinCount;
    private int coinRate;
    private Timer coinTimer;
    public static final int INIT_COINS = 300;

    private Map<Integer, LinkedList<Tile>> paths = null;
    private final boolean showPaths = false;

    private HomeBaseTower homeBase;
    private TreeSet<Tower> towers;
    private TreeSet<Projectile> projectiles;
    private TreeSet<Enemy> enemies;
    private LinkedList<Enemy> enemyQueue;

    public GameMap(Game game, JLabel roundLabel, JLabel timerLabel, JLabel coinsLabel,
            JLabel infoLabel, JLabel infoSubLabel, JButton infoButton1, JButton infoButton2,
            JLabel[] highScoreLabels) {
        this.game = game;

        this.tileMap = new Tile[ARRAY_SIZE][ARRAY_SIZE];

        this.infoLabel = infoLabel;
        this.infoSubLabel = infoSubLabel;
        this.infoButton1 = infoButton1;
        this.infoButton2 = infoButton2;

        this.highScoreLabels = highScoreLabels;

        this.timerLabel = timerLabel;
        this.roundLabel = roundLabel;
        this.coinsLabel = coinsLabel;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();

                selectTile(tileMap[p.y / TILE_SIZE][p.x / TILE_SIZE], "");
                repaint();
            }
        });
        reset();
    }

    public void reset() {
        this.playing = false;
        this.gameNeedsReset = false;
        this.roundInProgress = false;

        stopAllTimers();

        Game.refreshHighScores(this.highScoreLabels);

        int re1 = randomEntrance();
        int re2 = randomEntrance();

        for (int r = 0; r < tileMap.length; r++) {
            for (int c = 0; c < tileMap[0].length; c++) {
                String type;
                if (Math.random() <= BLOCK_PROB) {
                    type = "block";
                } else {
                    type = "open";
                }

                if ((r == 0 && c == 0) || (r == ARRAY_SIZE - 1 && c == ARRAY_SIZE - 1)
                        || (r == 0 && c == re1) || (r == re2 && c == 0)) {
                    type = "open";
                }

                tileMap[r][c] = new Tile(c * TILE_SIZE, r * TILE_SIZE, r, c, TILE_SIZE, type);
            }
        }

        Tile[] startNodes = new Tile[3];
        startNodes[0] = tileMap[0][0];
        startNodes[1] = tileMap[0][re1];
        startNodes[2] = tileMap[re2][0];

        TreeMap<Integer, LinkedList<Tile>> paths = findPaths(startNodes,
                tileMap[ARRAY_SIZE - 1][ARRAY_SIZE - 1]);

        if (paths == null) {
            reset();
        } else {
            this.paths = paths;
            tileMap[ARRAY_SIZE - 1][ARRAY_SIZE - 1].setType("block");

            this.mainTimer = new Timer(INTERVAL, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    tick();
                }
            });

            updateRounds(1);
            updateTimer(0);
            this.roundTimer = new Timer(1000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    decrTimeRemaining();
                }
            });

            updateCoins(INIT_COINS);
            this.coinRate = 25;
            this.coinTimer = new Timer(1000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    incrCoins();
                }
            });

            this.homeBase = new HomeBaseTower(MAP_SIZE, tileMap[ARRAY_SIZE - 1][ARRAY_SIZE - 1]);
            tileMap[ARRAY_SIZE - 1][ARRAY_SIZE - 1].setTower(this.homeBase);

            this.towers = new TreeSet<Tower>();
            this.projectiles = new TreeSet<Projectile>();
            this.enemies = new TreeSet<Enemy>();
            this.enemyQueue = new LinkedList<Enemy>();

            this.mainTimer.start();

            setFocusable(true);
            requestFocusInWindow();
            repaint();
        }
    }

    private void tick() {
        if (towers != null && enemies != null && projectiles != null) {
            endRound();

            for (Tower tower : towers) {
                if (tower instanceof AttackTower) {
                    AttackTower aTower = (AttackTower) tower;
                    aTower.towerEngage(enemies);
                }
            }

            boolean addNewEnemy = true;
            Enemy nextEnemy = null;
            Tile lookupTile = null;

            if (!enemyQueue.isEmpty()) {
                nextEnemy = enemyQueue.getFirst();
                lookupTile = nextEnemy.getPath().getFirst();
            }

            LinkedList<Enemy> removeEnemies = new LinkedList<Enemy>();

            for (Enemy enemy : enemies) {
                enemy.move();

                if (enemy.isDead()) {
                    removeEnemies.add(enemy);
                } else {
                    if (this.homeBase.enemyWin(enemy)) {
                        playerLose();
                    }
                }

                if (lookupTile != null) {
                    if (lookupTile.objWithinTile(enemy)) {
                        addNewEnemy = false;
                    }
                }
            }

//            enemies.removeAll(removeEnemies);

            for (Enemy enemy : removeEnemies) {
                TreeSet<Enemy> updatedEnemies = new TreeSet<Enemy>();
                for (Enemy e : this.enemies) {
                    updatedEnemies.add(e);
                }
                this.enemies = updatedEnemies;

                enemies.remove(enemy);
            }

            if (lookupTile != null && nextEnemy != null && addNewEnemy) {
                enemies.add(deqEnemyQueue());
            }

            LinkedList<Projectile> removeProjs = new LinkedList<Projectile>();

            for (Projectile p : projectiles) {
                p.move();

                for (Enemy enemy : enemies) {
                    if (p.hitEnemy(enemy)) {
                        removeProjs.add(p);
                    }
                }

                if (p.outOfBounds()) {
                    removeProjs.add(p);
                }
            }

            if (!removeProjs.isEmpty()) {
                for (Projectile rP : removeProjs) {
                    this.projectiles.remove(rP);
                }
            }

            repaint();
        }

    }

    public void clearAllInfoComps() {
        this.infoLabel.setText(" ");
        this.infoSubLabel.setText(" ");
        infoButton1.setText(" ");
        for (ActionListener actionListener : this.infoButton1.getActionListeners()) {
            this.infoButton1.removeActionListener(actionListener);
        }
        infoButton2.setText(" ");
        for (ActionListener actionListener : this.infoButton2.getActionListeners()) {
            this.infoButton2.removeActionListener(actionListener);
        }
    }

    public void selectTile(Tile tile, String messageText) {
        clearAllInfoComps();

        String addTowerMessage = addSelectedTower(tile);
        this.selectedTower = null;

        if (messageText != "TOWER SOLD") {
            this.selectedTile = tile;

            String text = "";

            if (tile.getType() == "block") {
                if (tile.getTower() != null) {
                    Tower tower = tile.getTower();
                    String type = "";
                    if (tower instanceof HomeBaseTower) {
                        type = "ALMIGHTY COLORFUL BRICK";
                        text = type;
                    } else {
                        if (tower instanceof ShooterTower) {
                            type = "Shooter Tower";
                        } else if (tower instanceof SniperTower) {
                            type = "Sniper Tower";
                        } else if (tower instanceof FarmTower) {
                            type = "Farm Tower";
                        }

                        text = type + " - " + "Level: " + tower.getLevel();
                        this.infoSubLabel.setText(tower.getUpgradeMessage());

                        infoButton1.setText("UPGRADE (-" + tower.getUpgradeCost() + ")");
                        this.infoButton1.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                int cost = tower.getUpgradeCost();
                                String transaction = "";

                                if (coinCount >= cost) {
                                    updateCoins(coinCount - cost);
                                    transaction = tower.upgrade(cost);
                                } else {
                                    transaction = "INSUFFICIENT COINS";
                                }

                                selectTile(selectedTile, transaction);
                            }
                        });

                        infoButton2.setText("SELL(+" + tower.getInitialCost() + ")");
                        this.infoButton2.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                removeTower(tile);
                                selectTile(selectedTile, "TOWER SOLD");
                            }
                        });
                    }

                } else {
                    text = "EMPTY TILE";
                }
            } else {
                text = "TILE NOT SELECTABLE";
            }

            this.infoLabel.setText(addTowerMessage + " - " + text + " - " + messageText);
        } else {
            this.infoLabel.setText(addTowerMessage + " - " + "EMPTY TILE -");
        }

        repaint();
    }

    public void setSelectedTower(String tower) {
        this.selectedTower = tower;
    }

    public String addSelectedTower(Tile tile) {
        String message = "";

        if (this.selectedTower != null) {
            message = buyTower(tile, this.selectedTower);

            this.selectedTower = null;
        }

        return message;
    }

    private void stopAllTimers() {
        if (this.mainTimer != null) {
            this.mainTimer.stop();
        }

        if (this.roundTimer != null) {
            this.roundTimer.stop();
        }

        if (this.coinTimer != null) {
            this.coinTimer.stop();
        }

        if (this.towers != null) {
            for (Tower tower : this.towers) {
                if (tower instanceof AttackTower) {
                    ((AttackTower) tower).stopTower();
                } else if (tower instanceof FarmTower) {
                    ((FarmTower) tower).stopPay();
                }
            }
        }
    }

    public void startGame() {
        if (!this.playing) {
            this.playing = true;
            this.roundInProgress = true;
            this.coinTimer.start();
            enqEnemyQueue(chooseRandomEnemy());
        }
    }

    public boolean isPlaying() {
        return this.playing;
    }

    public boolean gameNeedsReset() {
        return this.gameNeedsReset;
    }

    public boolean getRoundInProgress() {
        return this.roundInProgress;
    }

    private void startNextRound() {
        this.roundInProgress = true;
        this.coinTimer.start();
        int enemyCount = this.roundCount;
        while (enemyCount > 0) {
            enqEnemyQueue(chooseRandomEnemy());
            enemyCount--;
        }
    }

    private void endRound() {
        if (roundInProgress && this.enemyQueue.isEmpty() && this.enemies.isEmpty()) {
            this.roundInProgress = false;
            this.coinTimer.stop();
            updateRounds(this.roundCount + 1);
            updateTimer(TIME_BETWEEN_ROUNDS / 1000);
            this.roundTimer.start();
        }
    }

    private void playerLose() {
        if (this.playing) {
            this.playing = false;
            this.gameNeedsReset = true;
            this.roundInProgress = false;
            stopAllTimers();
            this.roundLabel.setText("SURVIVED TO ROUND: " + this.roundCount);
            this.timerLabel.setText("(YOU");
            this.coinsLabel.setText("LOST!)");
            Game.updateHighScores("files/high_scores.txt", this.game.getFrame(),
                    this.highScoreLabels, this.roundCount);
        }
    }

    private Enemy chooseRandomEnemy() {
        double randomNum = Math.random();
        double redEnemyProb = 0.05;
        double blueEnemyProb = 0.2;

        if (randomNum <= redEnemyProb) {
            return new BasicEnemy(4, 15, Color.RED, chooseRandomPath(), MAP_SIZE);
        } else if (randomNum >= (1 - blueEnemyProb)) {
            return new BasicEnemy(3, 10, Color.BLUE, chooseRandomPath(), MAP_SIZE);
        } else {
            return new BasicEnemy(2, 5, Color.GREEN, chooseRandomPath(), MAP_SIZE);
        }
    }

    private LinkedList<Tile> chooseRandomPath() {
        double randomNum = Math.random();
        if (randomNum <= (1.0 / 3.0)) {
            return this.paths.get(1);
        } else if (randomNum > (1.0 / 3.0) && randomNum <= (2.0 / 3.0)) {
            return this.paths.get(2);
        } else {
            return this.paths.get(0);
        }
    }

    public void updateCoins(int coins) {
        this.coinCount = coins;
        this.coinsLabel.setText("- Coins: " + coins);
    }

    private void incrCoins() {
        updateCoins(this.coinCount + this.coinRate);
    }

    public int getCoinCoint() {
        return this.coinCount;
    }

    public void setCoinRate(int coinRate) {
        this.coinRate = coinRate;
    }

    private void updateRounds(int round) {
        this.roundCount = round;
        this.roundLabel.setText("Round: " + round + " -");
    }

    private void updateTimer(int time) {
        this.timeRemaining = time;
        this.timerLabel.setText("Next Round Starts in: " + time);
    }

    private void decrTimeRemaining() {
        if (this.timeRemaining <= 0) {
            this.roundTimer.stop();
            startNextRound();
        } else {
            updateTimer(this.timeRemaining - 1);
        }
    }

    public String buyTower(Tile tile, String towerType) {
        String message = "";

        if (tile.getTower() == null && tile.getType() == "block") {
            Tower tower = null;
            switch (towerType) {
                case "SHOOTER":
                    tower = new ShooterTower(this, MAP_SIZE, tile);
                    break;
                case "SNIPER":
                    tower = new SniperTower(this, MAP_SIZE, tile);
                    break;
                case "FARM":
                    tower = new FarmTower(this, MAP_SIZE, tile);
                    break;
                default:
                    break;
            }

            if (tower != null) {
                int cost = tower.getInitialCost();
                if (this.coinCount >= cost) {
                    updateCoins(this.coinCount - cost);

                    tile.setTower(tower);
                    this.towers.add(tower);

                    if (towerType == "FARM") {
                        ((FarmTower) tower).startPay();
                    }

                    message = "SUCCESSFUL PURCHASE";
                } else {
                    message = "INSUFFICIENT COINS";
                }
            }
        } else {
            message = "TILE UNAVAILABLE";
        }

        return message;
    }

    public void removeTower(Tile tile) {
        if (tile.getTower() != null) {
            Tower tower = tile.getTower();

            if (tower instanceof AttackTower) {
                ((AttackTower) tower).stopTower();
            } else if (tower instanceof FarmTower) {
                ((FarmTower) tower).stopPay();
            }

            tile.setTower(null);
            this.towers.remove(tower);

            updateCoins(this.coinCount + tower.getInitialCost());
        }
    }

    public void addProj(Projectile proj) {
        this.projectiles.add(proj);
    }

    public void enqEnemyQueue(Enemy enemy) {
        this.enemyQueue.addLast(enemy);
    }

    public Enemy deqEnemyQueue() {
        Enemy enemy = this.enemyQueue.getFirst();
        this.enemyQueue.remove(enemy);
        enemy.advancePath();
        return enemy;
    }

    public int randomEntrance() {
        return (int) ((ARRAY_SIZE - 2) * Math.random()) + 1;
    }

    public LinkedList<Tile> findPath(Tile startNode, Tile targetNode) {
        PathFinder pf = new PathFinder(tileMap, startNode, targetNode);
        pf.startPF();
        LinkedList<Tile> path = pf.getResultingPath();
        pf.reset();
        return path;
    }

    public TreeMap<Integer, LinkedList<Tile>> findPaths(Tile[] startNodes, Tile targetNode) {
        TreeMap<Integer, LinkedList<Tile>> paths = new TreeMap<Integer, LinkedList<Tile>>();
        boolean nullList = false;

        for (int i = 0; i < startNodes.length; i++) {
            LinkedList<Tile> path = findPath(startNodes[i], targetNode);

            if (path != null) {
                paths.put(i, path);
            } else {
                nullList = true;
            }

        }

        if (nullList) {
            return null;
        } else {
            System.out.println("found paths!");

            return paths;
        }

    }

    private void drawTiles(Graphics g) {
        for (int r = 0; r < tileMap.length; r++) {
            for (int c = 0; c < tileMap[0].length; c++) {
                tileMap[r][c].draw(g);
            }
        }
    }

    private void drawPath(Graphics g, LinkedList<Tile> path) {
        g.setColor(Color.GREEN);
        Iterator<Tile> iterator = path.listIterator();

        while (iterator.hasNext()) {
            Tile t = iterator.next();
            int pathSize = (int) (0.5 * t.getSize());

            g.fillRect((int) (t.getPx() + 0.5 * (t.getSize() - pathSize)),
                    (int) (t.getPy() + 0.5 * (t.getSize() - pathSize)), pathSize, pathSize);
        }

    }

    private void drawPaths(Graphics g, Map<Integer, LinkedList<Tile>> paths) {
        for (Entry<Integer, LinkedList<Tile>> e : paths.entrySet()) {
            drawPath(g, e.getValue());
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawTiles(g);

        if (showPaths) {
            drawPaths(g, this.paths);
        }

        if (enemies != null) {
            if (!enemies.isEmpty()) {
                for (Enemy enemy : enemies) {
                    enemy.draw(g);
                }
            }
        }

        if (projectiles != null) {
            if (!projectiles.isEmpty()) {
                for (Projectile p : projectiles) {
                    p.draw(g);
                }
            }
        }

        if (towers != null && enemies != null) {
            for (Tower tower : towers) {
                tower.draw(g);
            }
        }

        this.homeBase.changeColors();
        this.homeBase.draw(g);

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(MAP_SIZE, MAP_SIZE);
    }
}
