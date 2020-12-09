import java.util.Iterator;
import java.util.LinkedList;

public abstract class Enemy extends GameObj implements Comparable<Enemy> {
    private int hp;
    private int speed;

    private LinkedList<Tile> path;
    private Iterator<Tile> iterator;
    
    private Tile currTarget;
    private Direction direction;
    private double progress;
    private double progressIncr;

    public Enemy(int speed, int width, int height, int hp, int mapSize, LinkedList<Tile> path) {
        super(0, 0, 0, 0, width, height, mapSize);

        this.hp = hp;
        this.speed = speed;

        this.path = path;
        this.iterator = path.listIterator();
        this.currTarget = this.iterator.next();

        this.progress = 0;
        this.progressIncr = 1.0 / path.size();
        incrementProgress();

        GameObj.positionToCenter(this, this.currTarget);
    }

    private void incrementProgress() {
        this.progress += this.progressIncr;
    }

    public double getProgress() {
        return this.progress;
    }

    public int getSpeed() {
        return this.speed;
    }

    public LinkedList<Tile> getPath() {
        return this.path;
    }

    public void checkAdvance() {
        int[] ccThis = GameObj.centerCoords(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
        int[] ccTarget = GameObj.centerCoords(currTarget.getPx(), currTarget.getPy(), currTarget.getSize(),
                currTarget.getSize());
        boolean allowAdvance = false;
        if ((direction == Direction.UP) && (ccThis[1] <= ccTarget[1])) {
            allowAdvance = true;
            this.setPy(this.getPy() + (ccTarget[1] - ccThis[1]));
        } else if ((direction == Direction.DOWN) && (ccThis[1] >= ccTarget[1])) {
            allowAdvance = true;
            this.setPy(this.getPy() - (ccThis[1] - ccTarget[1]));
        } else if ((direction == Direction.LEFT) && (ccThis[0] <= ccTarget[0])) {
            allowAdvance = true;
            this.setPx(this.getPx() + (ccTarget[0] - ccThis[0]));
        } else if ((direction == Direction.RIGHT) && (ccThis[0] >= ccTarget[0])) {
            allowAdvance = true;
            this.setPx(this.getPx() - (ccThis[0] - ccTarget[0]));
        }

        if (allowAdvance) {
            advancePath();
        }

    }

    public void advancePath() {
        if (this.iterator.hasNext()) {
            Tile nextTarget = this.iterator.next();

            if (nextTarget.getRow() == currTarget.getRow()) {
                if (nextTarget.getCol() > currTarget.getCol()) {
                    this.direction = Direction.RIGHT;
                    this.setVy(0);
                    this.setVx(speed);
                } else {
                    this.direction = Direction.LEFT;
                    this.setVy(0);
                    this.setVx(-speed);
                }
            } else if (nextTarget.getCol() == currTarget.getCol()) {
                if (nextTarget.getRow() > currTarget.getRow()) {
                    this.direction = Direction.DOWN;
                    this.setVx(0);
                    this.setVy(speed);
                } else {
                    this.direction = Direction.UP;
                    this.setVx(0);
                    this.setVy(-speed);
                }
            }

            currTarget = nextTarget;
            incrementProgress();
        }
    }

    @Override
    public void move() {
        this.setPx(this.getPx() + this.getVx());
        this.setPy(this.getPy() + this.getVy());

        checkAdvance();

        clip();
    }

    @Override
    public int compareTo(Enemy e2) {
        double e1P = this.progress;
        double e2P = e2.getProgress();
        
        if (e1P == e2P) {
            return 0;
        } else if (e1P > e2P) {
            return 1;
        } else {
            return -1;
        }
    }

    public void takeDamage(int damage) {
        System.out.println("Speed: " + this.speed + ", " + "HP: " + this.hp);
        this.hp -= damage;
    }

    public boolean isDead() {
        return this.hp <= 0;
    }
}
