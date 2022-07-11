package dungeonmania.DungeonObjects.Entities.Characters;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.IMovable;
import dungeonmania.Interfaces.IMovingStrategy;
import dungeonmania.MovingStrategies.CircularMoveStrat;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Position;
import dungeonmania.util.DungeonFactory.EntityStruct;

public class Spider extends Entity implements IMovable {
    private int attackDamage;
    private int health;

    // Prolly not used in here
    // Imma still keep it tho, to remind myself that spider spawns
    private int spawnRate;
    
    IMovingStrategy moveStrat;

    public Spider(EntityStruct metaData, JSONObject config, Position initialPos) {
        super(metaData);
        this.attackDamage = config.getInt("spider_attack");
        this.health = config.getInt("spider_health");
        this.spawnRate = config.getInt("spider_spawn_rate");

        moveStrat = new CircularMoveStrat(this, super.getMap(), initialPos);
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
    
    // TODO: spawn spider
    public static void spawnSpider() {}
}
