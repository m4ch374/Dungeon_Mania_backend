package dungeonmania.DungeonObjects.Entities.Characters;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.IMovable;
import dungeonmania.Interfaces.IMovingStrategy;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.DungeonFactory.EntityStruct;

public class ZombieToast extends Entity implements IMovable {
    private int attackDamage;
    private int health;

    private IMovingStrategy moveStrat;

    public ZombieToast(EntityStruct metaData, JSONObject config) {
        super(metaData);
        this.attackDamage = config.getInt("zombie_attack");
        this.health = config.getInt("zombie_health");
    }

    @Override
    public void move() {}

    @Override
    public RoundResponse battleWith(Entity opponent) { return null; }
}
