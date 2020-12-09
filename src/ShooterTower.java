import java.awt.Color;
import java.awt.Graphics;

public class ShooterTower extends AttackTower {
    public static int INITIAL_COST = 150;
    private static int RANGE = 150;
    private static int FIRE_INTERVAL = 1000;

    private int damage;

    public ShooterTower(int mapSize, Tile homeTile) {
        super(mapSize, homeTile, INITIAL_COST, RANGE, FIRE_INTERVAL, 0);

        this.damage = 5;
    }

    @Override
    public Projectile makeProj() {
        int[] cc = GameObj.centerCoords(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
        return new Bullet(cc[0], cc[1], this.getMapSize(), this.damage, this.getTarget(), this.getAccuracy());
    }

    @Override
    public void level1() {
        this.setUpgradeMessage("UPGRADE FIRE RATE");
    }

    @Override
    public void level2() {
        this.setUpgradeMessage("UPGRADE RANGE");
        setFireInterval(500);
    }

    @Override
    public void level3() {
        this.setUpgradeMessage("UPGRADE DAMAGE");
        this.setRange(250);
    }

    @Override
    public void level4() {
        this.setUpgradeMessage("ULTIMATE UPGRADE");
        this.damage = 10;
    }

    @Override
    public void level5() {
        this.setUpgradeMessage("MAX LEVEL");
        this.damage = 15;
        this.setRange(400);
        this.setFireInterval(250);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillRect(this.getPx(), this.getPy(), this.getHomeTile().getSize(), this.getHomeTile().getSize());
    }

}
