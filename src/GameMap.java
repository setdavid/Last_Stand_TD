
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")

public class GameMap extends JPanel {

    public static final int MAP_SIZE = 750;
    public static final double BLOCK_PROB = 0.3;
    public static final int ARRAY_SIZE = 30;

    private Tile[][] tileMap;
    private static final int TILE_SIZE = MAP_SIZE / ARRAY_SIZE;

    public GameMap() {
        this.tileMap = new Tile[ARRAY_SIZE][ARRAY_SIZE];
        reset();
    }

    public void reset() {
        for (int r = 0; r < tileMap.length; r++) {
            for (int c = 0; c < tileMap[0].length; c++) {
                String type;
                if (Math.random() <= BLOCK_PROB) {
                    type = "block";
                } else {
                    type = "open";
                }

                tileMap[r][c] = new Tile(c * TILE_SIZE, r * TILE_SIZE, r, c, TILE_SIZE, type);
            }
        }
        
        repaint();
    }
    
    public void drawTiles(Graphics g) {
        for (int r = 0; r < tileMap.length; r++) {
            for (int c = 0; c < tileMap[0].length; c++) {
                tileMap[r][c].draw(g);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawTiles(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(MAP_SIZE, MAP_SIZE);
    }
}
