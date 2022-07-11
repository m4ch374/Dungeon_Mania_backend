package dungeonmania.DungeonObjects.Entities.Characters;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.IMovable;
import dungeonmania.Interfaces.IMovingStrategy;
import dungeonmania.MovingStrategies.ConfusedMoveStrat;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.DungeonFactory.EntityStruct;

public class ZombieToast extends Entity implements IMovable {
    private int attackDamage;
    private int health;

    private IMovingStrategy moveStrat = new ConfusedMoveStrat(this, super.getMap());

    public ZombieToast(EntityStruct metaData, JSONObject config) {
        super(metaData);
        this.attackDamage = config.getInt("zombie_attack");
        this.health = config.getInt("zombie_health");
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public int getHealth() {
        return health;
    }

    @Override
    public void move() {
        moveStrat.moveEntity();
    }

    @Override
    public RoundResponse battleWith(Entity opponent) { return null; }
}
