import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.TreeSet;

import javax.swing.Timer;

public abstract class AttackTower extends RangeTower {
    private Timer timer;
    private int fireInterval;
    private int accuracy;
    private GameMap gameMap;

    public AttackTower(GameMap gameMap, int mapSize, Tile homeTile, int initialCost, int range,
            int fireInterval, int accuracy) {
        super(mapSize, homeTile, initialCost, range);

        this.gameMap = gameMap;
        this.fireInterval = fireInterval;
        this.accuracy = accuracy;

        this.timer = new Timer(fireInterval, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shoot();
            }
        });
    }

    public void towerEngage(Collection<Enemy> enemies) {
        TreeSet<Enemy> enemiesInRange = this.enemiesInRange(enemies);

        if (enemiesInRange != null) {
            if (!this.timer.isRunning()) {
                this.timer.start();
            }
        } else {
            if (this.timer.isRunning()) {
                this.timer.stop();
            }
        }
    }

    public void stopTower() {
        if (this.timer.isRunning()) {
            this.timer.stop();
        }
    }

    public int getfireInterval() {
        return this.fireInterval;
    }

    public int getAccuracy() {
        return this.accuracy;
    }

    public void setFireInterval(int fireInterval) {
        this.fireInterval = fireInterval;
        if (this.timer.isRunning()) {
            this.timer.stop();
        }
        this.timer = new Timer(fireInterval, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shoot();
            }
        });
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public void shoot() {
//        System.out.println("Shooting");
        if (!this.getTarget().isDead()) {
            gameMap.addProj(makeProj());
        }
    }

    public abstract Projectile makeProj();

}
