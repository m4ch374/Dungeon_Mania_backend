package dungeonmania.DungeonObjects;

import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.IMovingStrategy;

public class Player extends Entity {

    private int health;
    private int baseDamage;

    private int allyAttackBonous;
    private int allyDefenceBonous;

    private IMovingStrategy moveStrat;

    public void collect() {}

    public void move(Direction direction) {}

    public void initiateBattle() {}

    @Override
    public EntityResponse toEntityResponse() {
        return null;
    }
}
