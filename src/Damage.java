
public abstract class Damage {
    private int damage;
    
    public Damage(int damage) {
        this.damage = damage;
    }
    
    public int getDamage() {
        return this.damage;
    }
    
    public void damageToEnemy(Enemy e) {
        e.takeDamage(this);
    }
}
