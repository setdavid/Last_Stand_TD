
import java.awt.*;
import java.awt.event.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import javax.swing.*;

@SuppressWarnings("serial")

public class GameMap extends JPanel {

    public static final int MAP_SIZE = 600;
    public static final double BLOCK_PROB = 0.3;
    public static final int ARRAY_SIZE = 25;

    private Tile[][] tileMap;
    private static final int TILE_SIZE = MAP_SIZE / ARRAY_SIZE;

    private Tile selectedTile = null;
    private JPanel info_panel;
    private JLabel info_label;
    private JButton info_button;

    private Timer mainTimer = null;
    public static final int INTERVAL = 60;

    private boolean playing;

    private JLabel timer_label;
    private Timer roundTimer;
    private int timeRemaining;
    public static final int TIME_BETWEEN_ROUNDS = 1000;
    private JLabel round_label;
    private int roundCount;
    private boolean roundInProgress;

    private JLabel coins_label;
    private int coinCount;
    private int coinRate;
    private Timer coinTimer;
    public static final int INIT_COINS = 300;

    private Map<Integer, LinkedList<Tile>> paths = null;
    private boolean SHOW_PATHS = false;

    private HomeBaseTower homeBase;
    private HashSet<Tower> towers;
    private LinkedList<HashSet<Projectile>> projectiles;
    private HashSet<Enemy> enemies;
    private LinkedList<Enemy> enemyQueue;

    public GameMap(JLabel round_label, JLabel timer_label, JLabel coins_label, JPanel info_panel, JLabel info_label) {
        this.tileMap = new Tile[ARRAY_SIZE][ARRAY_SIZE];
        
        this.info_label = info_label;
        this.info_button = new JButton("");
        
        this.timer_label = timer_label;
        this.round_label = round_label;
        this.coins_label = coins_label;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();

                selectTile(tileMap[p.y / TILE_SIZE][p.x / TILE_SIZE]);
//                System.out.println("SelectedTile: " + selectedTile.getCol() + ", " + selectedTile.getRow());
//                System.out.println("SelectedTile: " + selectedTile.getType());

                repaint(); 
            }
        });
        reset();
    }

    public void reset() {
        this.playing = false;

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

                if ((r == 0 && c == 0) || (r == ARRAY_SIZE - 1 && c == ARRAY_SIZE - 1) || (r == 0 && c == re1)
                        || (r == re2 && c == 0)) {
                    type = "open";
                }

                tileMap[r][c] = new Tile(c * TILE_SIZE, r * TILE_SIZE, r, c, TILE_SIZE, type);
            }
        }

        Tile[] startNodes = new Tile[3];
        startNodes[0] = tileMap[0][0];
        startNodes[1] = tileMap[0][re1];
        startNodes[2] = tileMap[re2][0];

        TreeMap<Integer, LinkedList<Tile>> paths = findPaths(startNodes, tileMap[ARRAY_SIZE - 1][ARRAY_SIZE - 1]);

        if (paths == null) {
            reset();
        } else {
            this.paths = paths;
            tileMap[ARRAY_SIZE - 1][ARRAY_SIZE - 1].setType("block");

            stopAllTimers();

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
            this.coinRate = 10;
            this.coinTimer = new Timer(1000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    incrCoins();
                }
            });

            this.homeBase = new HomeBaseTower(MAP_SIZE, tileMap[ARRAY_SIZE - 1][ARRAY_SIZE - 1]);
            tileMap[ARRAY_SIZE - 1][ARRAY_SIZE - 1].setTower(this.homeBase);

            this.towers = new HashSet<Tower>();
            this.projectiles = new LinkedList<HashSet<Projectile>>();
            this.enemies = new HashSet<Enemy>();
            this.enemyQueue = new LinkedList<Enemy>();

            addTower(tileMap[ARRAY_SIZE - 2][ARRAY_SIZE - 2], "shooter");
            addTower(tileMap[1][ARRAY_SIZE - 3], "shooter");
            addTower(tileMap[1][ARRAY_SIZE - 10], "shooter");

//          Enemy enemyTest = new BasicEnemy(10, 10, Color.RED, this.paths.get(0), MAP_SIZE);
//          Enemy enemyTest2 = new BasicEnemy(10, 10, Color.BLUE, this.paths.get(0), MAP_SIZE);
//          Enemy enemyTest3 = new BasicEnemy(5, 10, Color.YELLOW, this.paths.get(0), MAP_SIZE);
//
//          enqEnemyQueue(enemyTest);
//          enqEnemyQueue(enemyTest2);
//          enqEnemyQueue(enemyTest3);

//            ShooterTower towerTest1 = new ShooterTower(tileMap[ARRAY_SIZE - 2][ARRAY_SIZE - 2], MAP_SIZE);
//            ShooterTower towerTest2 = new ShooterTower(tileMap[1][ARRAY_SIZE - 3], MAP_SIZE);
//            ShooterTower towerTest3 = new ShooterTower(tileMap[1][ARRAY_SIZE - 10], MAP_SIZE);
//
//            this.towers.add(towerTest1);
//            this.towers.add(towerTest2);
//            this.towers.add(towerTest3);

//            this.projectiles.add(towerTest2.getProjs());
//            this.projectiles.add(towerTest1.getProjs());
//            this.projectiles.add(towerTest3.getProjs());

            this.mainTimer.start();

            // TEMPROARY!!
            startGame();

            setFocusable(true);
            requestFocusInWindow();
            repaint();
        }
    }

    private void tick() {
        endRound();

        if (towers != null && enemies != null) {
            for (Tower tower : towers) {
                if (tower instanceof AttackTower) {
                    AttackTower aTower = (AttackTower) tower;
                    aTower.towerEngage(enemies);
                }
            }
        }

        if (enemies != null) {
            LinkedList<Enemy> removeEnemies = new LinkedList<Enemy>();

            boolean addNewEnemy = true;
            Enemy nextEnemy = null;
            Tile lookupTile = null;

            if (!enemyQueue.isEmpty()) {
                nextEnemy = enemyQueue.getFirst();
                lookupTile = nextEnemy.getPath().getFirst();
            }

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

            for (Enemy enemy : removeEnemies) {
                enemies.remove(enemy);
//                System.out.println(enemies.remove(enemy));
            }

            if (lookupTile != null && nextEnemy != null && addNewEnemy) {
//                System.out.println("Contains: " + enemies.contains(nextEnemy));
                enemies.add(deqEnemyQueue());
//                System.out.println("EQ: " + enemyQueue.size());
//                System.out.println("Enemies: " + enemies.size());
            }
        }

        if (projectiles != null) {
            int tracker = 1;

            for (HashSet<Projectile> group : projectiles) {
//                System.out.println(tracker + ": " + group.size());
//                System.out.println("Num in proj: " + projectiles.size());

                if (group != null) {
                    LinkedList<Projectile> removeProjs = new LinkedList<Projectile>();

                    for (Projectile p : group) {
                        p.move();
//                        p.hitEnemy(p.getTarget());

                        for (Enemy enemy : enemies) {
                            if (p.hitEnemy(enemy) || p.outOfBounds()) {
                                removeProjs.add(p);
                            }
                        }
                    }

                    if (!removeProjs.isEmpty()) {
                        for (Projectile p : removeProjs) {
                            group.remove(p);
                        }
                    }
                }

                tracker++;
            }
        }

        // check for the game end conditions
//        if (square.intersects(poison)) {
//            playing = false;
//            status.setText("You lose!");
//        } else if (square.intersects(snitch)) {
//            playing = false;
//            status.setText("You win!");
//        }

        repaint();
    }

    public void selectTile(Tile tile) {
        this.selectedTile = tile;
        if (tile.getType() == "block") {
            if (tile.getTower() != null) {
                Tower tower = tile.getTower();
                String type = "";
                if (tower instanceof HomeBaseTower) {
                    type = "Home Base";
                    this.info_label.setText(type);
                } else {
                    if (tower instanceof ShooterTower) {
                        type = "Shooter Tower";
                    } 
                    
                    this.info_label.setText(type + " - " + "Level: " + tower.getLevel());
                }
                
            } else {
                this.info_label.setText("Empty tile");
            }
        } else {
            this.info_label.setText("Tile not selectable");
        }
//        this.info_label.setText("SelectedTile: " + selectedTile.getCol() + ", " + selectedTile.getRow());
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
    }

    public void startGame() {
        if (!this.playing) {
            this.playing = true;
            this.roundInProgress = true;
            this.coinTimer.start();
            enqEnemyQueue(chooseRandomEnemy());
//            startNextRound();
        }
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
//        System.out.println("Round in progress" + roundInProgress);
//        System.out.println("Enemy Queue Empty: " + this.enemyQueue.size());
//        System.out.println("Enemies empty: " + this.enemies.size());

        if (roundInProgress && this.enemyQueue.isEmpty() && this.enemies.isEmpty()) {
//            System.out.println("============================================================================");
            this.roundInProgress = false;
            this.coinTimer.stop();
            updateRounds(this.roundCount + 1);
            updateTimer(TIME_BETWEEN_ROUNDS / 1000);
            this.roundTimer.start();
        }
    }

    private void playerLose() {
        this.playing = false;
        stopAllTimers();
        this.timer_label.setText("(YOU");
        this.coins_label.setText("LOST!)");
    }

    private Enemy chooseRandomEnemy() {
        double randomNum = Math.random();
        if (randomNum <= 0.2) {
            return new BasicEnemy(7, 50, Color.RED, chooseRandomPath(), MAP_SIZE);
        } else if (randomNum > 0.2 && randomNum <= 0.5) {
            return new BasicEnemy(5, 30, Color.BLUE, chooseRandomPath(), MAP_SIZE);
        } else {
            return new BasicEnemy(3, 10, Color.GREEN, chooseRandomPath(), MAP_SIZE);
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

    private void updateCoins(int coins) {
        this.coinCount = coins;
        this.coins_label.setText("Coins: " + coins);
//        this.coins_label.repaint();
    }

    private void incrCoins() {
        updateCoins(this.coinCount + this.coinRate);
    }

    private void updateRounds(int round) {
        this.roundCount = round;
        this.round_label.setText("Round: " + round);
//        this.coins_label.repaint();
    }

    private void updateTimer(int time) {
        this.timeRemaining = time;
        this.timer_label.setText("Next Round Starts in: " + time);
//        this.timer_label.repaint();
    }

    private void decrTimeRemaining() {
        if (this.timeRemaining <= 0) {
            this.roundTimer.stop();
            startNextRound();
        } else {
            updateTimer(this.timeRemaining - 1);
            System.out.println(this.timeRemaining);
        }
    }

    public void addTower(Tile tile, String towerType) {
        if (tile.getTower() == null && tile.getType() == "block") {
            Tower tower = null;
            switch (towerType) {
            case "shooter":
                tower = new ShooterTower(MAP_SIZE, tile);
                break;
            default:
                break;
            }

            if (tower != null) {
                tile.setTower(tower);
                this.towers.add(tower);

                if (tower instanceof AttackTower) {
                    this.projectiles.add(((AttackTower) tower).getProjs());
                }
            }
        }
    }

    public void removeTower(Tile tile) {
        if (tile.getTower() != null) {
            Tower tower = tile.getTower();

            tile.setTower(null);
            this.towers.remove(tower);

            if (tower instanceof AttackTower) {
                this.projectiles.remove(((AttackTower) tower).getProjs());
            }
        }
    }

    public void enqEnemyQueue(Enemy enemy) {
        this.enemyQueue.addLast(enemy);
    }

    public Enemy deqEnemyQueue() {
        Enemy enemy = this.enemyQueue.getFirst();
        this.enemyQueue.remove(enemy);
        enemies.add(enemy);
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

//            for (int i = 0; i < paths.size(); i++) {
//                PathFinder.printPath(paths.get(i));
//            }

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
        for (Entry e : paths.entrySet()) {
            drawPath(g, (LinkedList<Tile>) e.getValue());
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawTiles(g);

        if (SHOW_PATHS) {
            drawPaths(g, this.paths);
        }

        if (enemies != null) {
            if (!enemies.isEmpty()) {
//                System.out.println("Enemies size: " + enemies.size());
                for (Enemy enemy : enemies) {
                    enemy.draw(g);
                }
            }
        }

        if (projectiles != null) {
            if (!projectiles.isEmpty()) {
                for (HashSet<Projectile> group : projectiles) {
                    if (group != null) {
                        if (!group.isEmpty()) {
//                            System.out.println("Proj size: " + group.size());
                            for (Projectile p : group) {
//                                System.out.println("Proj pos: " + p.getPx() + ", " + p.getPy());
                                p.draw(g);
                            }
                        }
                    }
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
