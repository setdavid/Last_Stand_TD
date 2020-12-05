import java.awt.Color;
import java.awt.Graphics;

public class Tile extends PFNode{
    private final int px;
    private final int py;
    
    private final int size;
    private final String type;
    private final Color color;
    
    private Tower tower = null;
    
//    private PFNode pfNode;
    
    public Tile (int px, int py, int row, int col, int size, String type) {
        super(row, col);
        
        this.px = px;
        this.py = py;
        
        this.size = size;
        this.type = type;
        
        if (type == "block") {
            this.color = Color.black;
        } else {
            this.color = Color.white;
        }
        
    }
    
    public int getPx() {
        return this.px;
    }
    
    public int getPy() {
        return this.py;
    }
    
    public int getSize() {
        return this.size;
    }

    public String getType() {
        return this.type;
    }
    
    public Tower getTower() {
        return this.tower;
    };
    
    public void setTower(Tower tower) {
        this.tower = tower;
    };
    
    public void removeTower() {
      this.tower = null; 
    };
    
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillRect(this.px, this.py, this.size, this.size);
    }
}
