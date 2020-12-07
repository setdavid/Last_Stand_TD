
public abstract class Projectile extends GameObj {
    private Enemy target;
    private final int SPEED = 15;

    private int accuracy;
    private int reAdjustFreqTracker = 1;

    public Projectile(int px, int py, int width, int height, int mapSize, Enemy target, int accuracy) {
        super(0, 0, px, py, width, height, mapSize);

        this.target = target;
        this.accuracy = accuracy;
        findTrajectory();
    }

    public Enemy getTarget() {
        return this.target;
    }

    public void findTrajectory() {
        int xDirection;
        if (this.getPx() - target.getPx() > 0) {
            xDirection = -1;
        } else {
            xDirection = 1;
        }
        int yDirection;
        if (this.getPy() - target.getPy() > 0) {
            yDirection = -1;
        } else {
            yDirection = 1;
        }

        double dx = Math.abs(this.getPx() - target.getPx());
        double dy = Math.abs(this.getPy() - target.getPy());
        double euclD = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        double sin = dy / euclD;
        double cos = dx / euclD;
        this.setVx((int) (xDirection * SPEED * cos));
        this.setVy((int) (yDirection * SPEED * sin));
    }

    public boolean hitEnemy(Enemy enemy) {
        if (this.willIntersect(enemy)) {
            affectEnemy(enemy);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void move() {
        if (!this.target.isDead() && this.reAdjustFreqTracker <= this.accuracy) {
            findTrajectory();
            this.reAdjustFreqTracker++;
        }

        this.setPx(this.getPx() + this.getVx());
        this.setPy(this.getPy() + this.getVy());
    }

    public boolean outOfBounds() {
        return ((this.getPx() < 0) || (this.getPx() > this.getMaxX()) || (this.getPy() < 0)
                || (this.getPy() > this.getMaxY()));
    }

    public abstract void affectEnemy(Enemy enemy);
}