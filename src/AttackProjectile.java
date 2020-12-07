
public abstract class AttackProjectile extends Projectile {
    private int damage;

    public AttackProjectile(int px, int py, int width, int height, int mapSize, Enemy target, int damage, int accuracy) {
        super(px, py, width, height, mapSize, target, accuracy);

        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    @Override
    public void affectEnemy(Enemy enemy) {
        enemy.takeDamage(damage);
    }
}
