import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

public class BasicEnemy extends Enemy {
    public static final int WIDTH = 23;
    public static final int HEIGHT = 23;
    private Color color;
//    public static final int SPEED = 2;
//    public static final int HP = 10;
    
    public BasicEnemy(int speed, int hp, Color color, LinkedList<Tile> path, int mapSize) {
        super(speed, WIDTH, HEIGHT, hp, mapSize, path);
        this.color = color;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillOval(this.getPx(), this.getPy(), WIDTH, HEIGHT);
    }

}
