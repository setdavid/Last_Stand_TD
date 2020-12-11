import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class FarmTower extends Tower {
    private GameMap gameMap;
    public static final int INITIAL_COST = 1000;
    public static final int INITIAL_COIN_RATE = 20;
    private int coinRate;
    private Timer timer;

    public FarmTower(GameMap gameMap, int mapSize, Tile homeTile) {
        super(mapSize, homeTile, INITIAL_COST);
        this.gameMap = gameMap;
        this.coinRate = INITIAL_COIN_RATE;
        this.timer = new Timer(5000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pay();
            }
        });
        this.timer.start();
    }

    public void pay() {
        if (this.gameMap.getRoundInProgress()) {
            this.gameMap.updateCoins(this.gameMap.getCoinCoint() + this.coinRate);
        }
    }

    public void stopPay() {
        if (this.timer.isRunning()) {
            this.timer.stop();
        }
    }

    @Override
    public void level1() {
        this.setUpgradeMessage("UPGRADE COIN RATE");
    }

    @Override
    public void level2() {
        this.setUpgradeMessage("UPGRADE COIN RATE");
        this.coinRate = INITIAL_COIN_RATE * 2;
    }

    @Override
    public void level3() {
        this.setUpgradeMessage("UPGRADE COIN RATE");
        this.coinRate = INITIAL_COIN_RATE * 3;
    }

    @Override
    public void level4() {
        this.setUpgradeMessage("ULTIMATE UPGRADE");
        this.coinRate = INITIAL_COIN_RATE * 4;
    }

    @Override
    public void level5() {
        this.setUpgradeMessage("MAX LEVEL");
        this.coinRate = INITIAL_COIN_RATE * 5;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(this.getPx(), this.getPy(), this.getHomeTile().getSize(),
                this.getHomeTile().getSize());
    }
}
