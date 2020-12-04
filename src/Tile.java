import java.awt.Color;
import java.awt.Graphics;

public class Tile {
    private final int px;
    private final int py;
    private final int row;
    private final int col;
    private final int size;
    private final String type;
    private final Color color;
    private Entity entity = null;
    
    private PFNode node;
    
    public Tile (int px, int py, int row, int col, int size, String type) {
        this.px = px;
        this.py = py;
        this.row = row;
        this.col = col;
        this.size = size;
        this.type = type;
        
        if (type == "block") {
            this.color = Color.black;
        } else {
            this.color = Color.white;
        }
        
        this.node = new PFNode(row, col);
    }

    public Entity getEntity() {
        return this.entity;
    };
    
    public void setEntity(Entity entity) {
        this.entity = entity;
    };
    
    public void removeEntity() {
      this.entity = null; 
    };
    
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillRect(this.px, this.py, this.size, this.size);
    }
}
