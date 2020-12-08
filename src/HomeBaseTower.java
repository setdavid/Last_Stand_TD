import java.awt.Color;
import java.awt.Graphics;

public class HomeBaseTower extends Tower {
    private Color color;
    private int r;
    private int b;
    private int g;

    public HomeBaseTower(int mapSize, Tile homeTile) {
        super(mapSize, homeTile, 0);
        this.color = Color.red;

        this.r = 255;
        this.b = 0;
        this.g = 0;
    }

    @Override
    public void level1() {

    }

    @Override
    public void level2() {

    }

    @Override
    public void level3() {

    }

    @Override
    public void level4() {

    }

    @Override
    public void level5() {

    }

    public void changeColors() {
        this.color = new Color((int) (255.0 * Math.random()), (int) (255.0 * Math.random()), (int) (255.0 * Math.random()));
    }

    public boolean enemyWin(Enemy enemy) {
        return this.getHomeTile().objWithinTile(enemy);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillRect(this.getPx(), this.getPy(), this.getHomeTile().getSize(), this.getHomeTile().getSize());
    }

}
