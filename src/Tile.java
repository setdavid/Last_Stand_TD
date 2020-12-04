import java.awt.Color;
import java.awt.Graphics;

public class Tile {
    private final int px;
    private final int py;
    private final int size;
    private final String type;
    private final Color color;
    private Entity entity = null;
    
    public Tile (int px, int py, int row, int col, int size, String type) {
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

    public String getType() {
        return this.type;
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
