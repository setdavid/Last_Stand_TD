//This class is to assist the PathFinder
public class PFNode {

    private int row;
    private int col;

    private double gScore;
    private double fScore;
    private Tile cameFrom;

    public PFNode(int row, int col) { 
        this.row = row;
        this.col = col;
        
        this.gScore = Integer.MAX_VALUE;
        this.fScore = Integer.MAX_VALUE;
        this.cameFrom = null;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    public double getGScore() {
        return this.gScore;
    }

    public double getFScore() {
        return this.fScore;
    }

    public Tile getCameFrom() {
        return this.cameFrom;
    }

    public void setGScore(double gScore) {
        this.gScore = gScore;
    }

    public void setFScore(double fScore) {
        this.fScore = fScore;
    }

    public void setCameFrom(Tile cameFrom) {
        this.cameFrom = cameFrom;
    }
    
    public void reset() {
        gScore = Integer.MAX_VALUE;
        fScore = Integer.MAX_VALUE;
        cameFrom = null;
    }
}