import java.awt.Color;
import java.awt.Graphics;

public class Bullet extends AttackProjectile {
    
    public Bullet(int px, int py, int mapSize, int damage, Enemy target, int accuracy) {
        super(px, py, 7, 7, mapSize, target, damage, accuracy, 1);
        
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.gray);
        g.fillOval(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }
}
