import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.LinkedList;

public class Enemy extends Entity {
    
    private Iterator<Tile> path;
    private Tile currTile;
    
    public Enemy(int vx, int vy, int px, int py, int width, int height, 
            int mapSize, int hp, int damage, int range, LinkedList<Tile> path) {
        super(vx, vy, px, py, width, height, mapSize, hp, damage, range);
        this.path = path.listIterator(0);
        this.currTile = this.path.next();
    }

    @Override
    public void draw(Graphics g) {
//        g.setColor(COLOR);
        g.fillOval(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }

    @Override
    public boolean detectEnemy(Entity e) {
        return false;
    }
    
//    public void moveNextTile() {
//        if (path.hasNext()) {
//            Tile nextTile = path.next();
//            if () {
//                
//            }
//            this.setPx(px);
//        }
//    }

}
