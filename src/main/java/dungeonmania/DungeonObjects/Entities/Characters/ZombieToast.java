package dungeonmania.DungeonObjects.Entities.Characters;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.IMovable;
import dungeonmania.Interfaces.IMovingStrategy;
import dungeonmania.response.models.RoundResponse;

public class ZombieToast extends Entity implements IMovable {
    private int attackDamage;
    private int health;

    private IMovingStrategy moveStrat;

    @Override
    public void move() { }

    @Override
    public RoundResponse battleWith(Entity opponent) { return null; }
}
