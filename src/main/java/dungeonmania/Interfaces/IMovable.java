package dungeonmania.Interfaces;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.response.models.RoundResponse;

public interface IMovable {
    public void move();

    public RoundResponse battleWith(Entity opponent);

    public int getAttackDamage();

    public int getHealth();

    public void death();

    public String getClasString();
}
