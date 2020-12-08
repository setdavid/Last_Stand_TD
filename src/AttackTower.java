import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeSet;

import javax.swing.Timer;

public abstract class AttackTower extends RangeTower {
    private Timer timer;
    private int fireInterval;
    private HashSet<Projectile> projs;

    public AttackTower(int mapSize, Tile homeTile, int initialCost, int range, int fireInterval) {
        super(mapSize, homeTile, initialCost, range);

        this.fireInterval = fireInterval;

        this.projs = new HashSet<Projectile>();
        this.timer = new Timer(fireInterval, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shoot();
            }
        });
    }

    public void towerEngage(Collection<Enemy> enemies) {
        TreeSet<Enemy> enemiesInRange = this.enemiesInRange(enemies);

        if (enemiesInRange != null) {
            if (!enemiesInRange.isEmpty()) {
                if (!this.timer.isRunning()) {
                    this.timer.start();
                }
            } else {
                if (this.timer.isRunning()) {
                    System.out.println("stop running");
                    this.timer.stop();
                }
            }
        }
    }

    public int getfireInterval() {
        return this.fireInterval;
    }

    public HashSet<Projectile> getProjs() {
        return this.projs;
    }

    public void setFireInterval(int fireInterval) {
        this.fireInterval = fireInterval;
        this.timer = new Timer(fireInterval, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shoot();
            }
        });
    }

    public void shoot() {
        if (!this.getTarget().isDead()) {
            Projectile proj = makeProj();
            this.projs.add(proj);
        }

//        LinkedList<Projectile> removeProjs = new LinkedList<Projectile>();
//        for (Projectile p : this.projs) {
//            if (p.willIntersect(p.getTarget()) || p.outOfBounds()) {
//                removeProjs.add(p);
//            }
//        }
//
//        if (!removeProjs.isEmpty()) {
//            for (Projectile p : removeProjs) {
//                this.projs.remove(p);
//            }
//        }
    }

    public abstract Projectile makeProj();

}
