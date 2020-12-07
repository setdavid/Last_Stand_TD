
public abstract class Tower extends GameObj {
    private Tile homeTile;

    private int initialCost;
    private int upgradeCost;
    private int level;

    public Tower(int px, int py, int width, int height, int mapSize, Tile homeTile, int initialCost) {
        super(0, 0, px, py, width, height, mapSize);

        this.homeTile = homeTile;
        this.initialCost = initialCost;
        this.upgradeCost = (int) (1.5 * initialCost);
        level1();
    }

    public int getLevel() {
        return this.level;
    }
    
    public Tile getHomeTile() {
        return this.homeTile;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String upgrade(int payment) {
        if (level != 5) {
            if (payment == upgradeCost) {
                level++;

                if (level == 5) {
                    this.upgradeCost = -1;
                    level5();
                } else {
                    this.upgradeCost = (int) (this.upgradeCost * 1.5);

                    switch (level) {
                    case 2:
                        level2();
                        break;
                    case 3:
                        level3();
                        break;
                    case 4:
                        level4();
                        break;
                    default:
                        break;
                    }
                }

                return "SUCCESS";
            } else {
                return "INSUFFICIENT";
            }
        } else {
            return "MAX_LEVEL";
        }
    }

    public abstract void level1();

    public abstract void level2();

    public abstract void level3();

    public abstract void level4();

    public abstract void level5();

}
