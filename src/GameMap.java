
import java.awt.*;
import java.awt.event.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.*;

@SuppressWarnings("serial")

public class GameMap extends JPanel {

    public static final int MAP_SIZE = 750;
    public static final double BLOCK_PROB = 0.3;
    public static final int ARRAY_SIZE = 30;

    private Tile[][] tileMap;
    private static final int TILE_SIZE = MAP_SIZE / ARRAY_SIZE;

    private Collection<LinkedList<Tile>> paths = null;

    public GameMap() {
        this.tileMap = new Tile[ARRAY_SIZE][ARRAY_SIZE];
        reset();
    }

    public void reset() {
        int re1 = randomEntrance();
        int re2 = randomEntrance();

        for (int r = 0; r < tileMap.length; r++) {
            for (int c = 0; c < tileMap[0].length; c++) {
                String type;
                if (Math.random() <= BLOCK_PROB) {
                    type = "block";
                } else {
                    type = "open";
                }

                if ((r == 0 && c == 0) || (r == ARRAY_SIZE - 1 && c == ARRAY_SIZE - 1) || (r == 0 && c == re1)
                        || (r == re2 && c == 0)) {
                    type = "open";
                }

                tileMap[r][c] = new Tile(c * TILE_SIZE, r * TILE_SIZE, r, c, TILE_SIZE, type);
            }
        }

        Tile[] startNodes = new Tile[3];
        startNodes[0] = tileMap[0][0];
        startNodes[1] = tileMap[0][re1];
        startNodes[2] = tileMap[re2][0];

        HashSet<LinkedList<Tile>> paths = findPaths(startNodes, tileMap[ARRAY_SIZE - 1][ARRAY_SIZE - 1]);

        if (paths == null) {
            reset();
        } else {
            this.paths = paths;
            repaint();
        }
    }

    public int randomEntrance() {
        return (int) ((ARRAY_SIZE - 2) * Math.random()) + 1;
    }

    public LinkedList<Tile> findPath(Tile startNode, Tile targetNode) {
        PathFinder pf = new PathFinder(tileMap, startNode, targetNode);
        pf.loop();
        LinkedList<Tile> path = pf.getResultingPath();
        pf.printPath();
        pf.reset();
        return path;
    }

    public HashSet<LinkedList<Tile>> findPaths(Tile[] startNodes, Tile targetNode) {
        HashSet<LinkedList<Tile>> paths = new HashSet<LinkedList<Tile>>();
        boolean nullList = false;

        for (int i = 0; i < startNodes.length; i++) {
            LinkedList<Tile> path = findPath(startNodes[i], targetNode);

            if (path != null) {
                paths.add(path);
            } else {
                nullList = true;
            }

        }

        if (nullList) {
            return null;
        } else {
            System.out.println("found paths!");
            return paths;
        }

    }

    public void drawTiles(Graphics g) {
        for (int r = 0; r < tileMap.length; r++) {
            for (int c = 0; c < tileMap[0].length; c++) {
                tileMap[r][c].draw(g);
            }
        }
    }

    public void drawPath(Graphics g, LinkedList<Tile> path) {
        g.setColor(Color.GREEN);
        Iterator<Tile> iterator = path.listIterator();

        while (iterator.hasNext()) {
            Tile t = iterator.next();
            g.fillRect(t.getPx(), t.getPy(), t.getSize() - 10, t.getSize() - 10);
        }

    }

    public void drawPaths(Graphics g, Collection<LinkedList<Tile>> paths) {
        for (LinkedList<Tile> path : paths) {
            drawPath(g, path);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawTiles(g);
        drawPaths(g, paths);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(MAP_SIZE, MAP_SIZE);
    }
}
