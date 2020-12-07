import java.awt.Color;
import java.awt.Graphics;

public class ShooterTower extends AttackTower {
    public static int INITIAL_COST = 100;
    public static int RANGE = 600;
    public static int FIRE_INTERVAL = 100;

    private int damage;
    private int accuracy;

    public ShooterTower(Tile homeTile, int mapSize) {
        super(homeTile.getPx(), homeTile.getPy(), homeTile.getSize(), homeTile.getSize(), mapSize, homeTile,
                INITIAL_COST, RANGE, FIRE_INTERVAL);

        this.damage = 2;
        this.accuracy = 25;
    }

    @Override
    public Projectile makeProj() {
        int[] cc = GameObj.centerCoords(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
        return new Bullet(cc[0], cc[1], this.getMapSize(), this.damage, this.getTarget(), this.accuracy);
    }

    @Override
    public void level1() {

    }

    @Override
    public void level2() {
        this.damage = 20;
    }

    @Override
    public void level3() {
        this.setRange(60);
    }

    @Override
    public void level4() {
        this.setFireInterval(1500);
    }

    @Override
    public void level5() {
        this.damage = 30;
        this.setRange(90);
        this.setFireInterval(500);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillRect(this.getPx(), this.getPy(), this.getHomeTile().getSize(), this.getHomeTile().getSize());
    }

}
