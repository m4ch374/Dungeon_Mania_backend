package dungeonmania.Interfaces;

public interface IEnemy extends IMovable{
    public double getAttackDamage();

    public double getHealth();

    public void death();

    public String getClasString();
}
