package dungeonmania.DungeonObjects.Entities.Characters;

import dungeonmania.Interfaces.IMovable;
import dungeonmania.Interfaces.IMovingStrategy;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.RoundResponse;

import dungeonmania.DungeonObjects.Entities.Entity;

public class Mercenary extends Entity implements IMovable {

    private int bribeRadius;
    private int brinbeAmount;

    private int attackDamage;
    private int health;

    private IMovingStrategy moveStrat;

    @Override
    public void move() {}

    @Override
    public RoundResponse battleWith(Entity opponent) { return null; }

    @Override
    public EntityResponse toEntityResponse() { return null; }
    
}
