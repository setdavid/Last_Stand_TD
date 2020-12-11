import java.util.Collection;
import java.util.TreeSet;

public abstract class RangeTower extends Tower {
    private int range;
    private Enemy target;

    public RangeTower(int mapSize, Tile homeTile, int initialCost, int range) {
        super(mapSize, homeTile, initialCost);
        this.range = range;
        this.target = null;
    }

    public Enemy getTarget() {
        return this.target;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public boolean enemyWithinRange(Enemy enemy) {
        int distance = (int) (Math.sqrt(Math.pow((Math.abs(this.getPx() - enemy.getPx())), 2)
                + Math.pow((Math.abs(this.getPy() - enemy.getPy())), 2)));
        return (distance <= this.range);
    }

    public TreeSet<Enemy> enemiesInRange(Collection<Enemy> enemies) {
        TreeSet<Enemy> enemiesInRange = new TreeSet<Enemy>();
        boolean atLeastOneEnemy = false;

        for (Enemy enemy : enemies) {
            if (enemyWithinRange(enemy) && !enemy.isDead()) {
                atLeastOneEnemy = true;
                enemiesInRange.add(enemy);
            }
        }

        if (atLeastOneEnemy) {
            this.target = enemiesInRange.last();
            return enemiesInRange;
        } else {
            return null;
        }
    }
}
