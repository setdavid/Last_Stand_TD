//This class is to assist the PathFinder
public class PFNode {

    private int row;
    private int col;
    
    private int gScore;
    private int h;
    private int fScore;
    private PFNode cameFrom;
    
    public PFNode(int row, int col) {
        this.row = row;
        this.col = col;
        
        gScore = Integer.MAX_VALUE;
        h = Integer.MAX_VALUE;
        fScore = Integer.MAX_VALUE;
        cameFrom = null;
    }
    
    public int getRow() {
        return this.row;
    }
    
    public int getCol() {
        return this.col;
    }
    
    public int getGScore() {
        return this.gScore;
    }
    
    public int getFScore() {
        return this.fScore;
    }
    
    public int getH() {
        return this.h;
    }

    public PFNode getCameFrom() {
        return this.cameFrom;
    }
    
    public int setGScore() {
        return this.gScore;
    }
    
    public int getFScore() {
        return this.fScore;
    }
    
    public int getH() {
        return this.h;
    }

    public PFNode getCameFrom() {
        return this.cameFrom;
    }
}
