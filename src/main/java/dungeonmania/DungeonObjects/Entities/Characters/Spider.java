package dungeonmania.DungeonObjects.Entities.Characters;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.IMovable;
import dungeonmania.Interfaces.ISpawnable;
import dungeonmania.response.models.RoundResponse;

public class Spider extends Entity implements IMovable, ISpawnable {
    private int attackDamage;
    private int health;

    private int spawnRate;

    @Override
    public void move() {}

    @Override
    public RoundResponse battleWith(Entity opponent) { return null; }

    @Override
    public void spawn() {}
    
}
