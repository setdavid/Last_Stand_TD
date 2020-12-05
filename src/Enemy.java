import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.LinkedList;

public abstract class Enemy extends GameObj {
    private int hp;
    private int speed;

    private Iterator<Tile> path;
    private Tile currTarget;
    private Direction direction;

    public Enemy(int speed, int width, int height, int hp, int mapSize, LinkedList<Tile> path) {
        super(0, 0, 0, 0, width, height, mapSize);

        this.hp = hp;
        this.speed = speed;

        this.path = path.listIterator();
        this.currTarget = this.path.next();

        this.setPx((int) (currTarget.getPx() + 0.5 * (currTarget.getSize() - width)));
        this.setPy((int) (currTarget.getPy() + 0.5 * (currTarget.getSize() - height)));

        advancePath();
    }

    public void checkAdvance() {
        int[] ccThis = GameObj.centerCoords(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
        int[] ccTarget = GameObj.centerCoords(currTarget.getPx(), currTarget.getPy(), currTarget.getSize(),
                currTarget.getSize());

        if (((direction == Direction.UP) && (ccThis[1] <= ccTarget[1]))
                || ((direction == Direction.DOWN) && (ccThis[1] >= ccTarget[1]))
                || ((direction == Direction.LEFT) && (ccThis[0] <= ccTarget[0]))
                || ((direction == Direction.RIGHT) && (ccThis[0] >= ccTarget[0]))) {
            advancePath();
        }
    }

    public void advancePath() {
        if (path.hasNext()) {
            Tile nextTarget = path.next();

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
        }
    }

    @Override
    public void move() {
        this.setPx(this.getPx() + this.getVx());
        this.setPy(this.getPy() + this.getVy());

        checkAdvance();

        clip();
    }

    public void takeDamage(Damage damage) {
        this.hp -= damage.getDamage();
    }
}
