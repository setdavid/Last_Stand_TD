import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

public class BasicEnemy extends Enemy {
    public static final int SPEED = 10;
    public static final int WIDTH = 400;
    public static final int HEIGHT = 400;
    public static final int HP = 10;
    
    public BasicEnemy(int mapSize, LinkedList<Tile> path) {
        super(SPEED, WIDTH, HEIGHT, HP, mapSize, path);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(this.getPx(), this.getPy(), WIDTH, HEIGHT);
    }

}
