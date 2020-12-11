import java.awt.Color;
import java.awt.Graphics;

public class SniperTower extends AttackTower {
    public static final int INITIAL_COST = 200;
    public static final int INITIAL_RANGE = 100000;
    public static final int INITIAL_FIRE_INTERVAL = 10000;

    private int damage;

    public SniperTower(GameMap gameMap, int mapSize, Tile homeTile) {
        super(gameMap, mapSize, homeTile, INITIAL_COST, INITIAL_RANGE, INITIAL_FIRE_INTERVAL, 0);

        this.damage = 7;
    }

    @Override
    public Projectile makeProj() {
        int[] cc = GameObj.centerCoords(this.getPx(), this.getPy(), this.getWidth(),
                this.getHeight());
        return new Bullet(cc[0], cc[1], this.getMapSize(), this.damage, this.getTarget(),
                this.getAccuracy());
    }

    @Override
    public void level1() {
        this.setUpgradeMessage("UPGRADE FIRE RATE");
    }

    @Override
    public void level2() {
        this.setUpgradeMessage("UPGRADE ACCURACY");
        setFireInterval(8000);
    }

    @Override
    public void level3() {
        this.setUpgradeMessage("UPGRADE DAMAGE");
        this.setAccuracy(10);
    }

    @Override
    public void level4() {
        this.setUpgradeMessage("ULTIMATE UPGRADE");
        this.damage = 15;
    }

    @Override
    public void level5() {
        this.setUpgradeMessage("MAX LEVEL");
        this.damage = 50;
        this.setAccuracy(10000);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(62, 135, 82));
        g.fillRect(this.getPx(), this.getPy(), this.getHomeTile().getSize(),
                this.getHomeTile().getSize());
    }

}
