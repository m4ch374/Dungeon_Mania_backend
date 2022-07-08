package dungeonmania.DungeonObjects.Entities.Characters;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.IMovable;
import dungeonmania.Interfaces.ISpawnable;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.DungeonFactory.EntityStruct;

public class Spider extends Entity implements IMovable, ISpawnable {
    private int attackDamage;
    private int health;

    private int spawnRate;

    public Spider(EntityStruct metaData, JSONObject config) {
        super(metaData);
        this.attackDamage = config.getInt("spider_attack");
        this.health = config.getInt("spider_health");
        this.spawnRate = config.getInt("spider_spawn_rate");
    }

    @Override
    public void move() {}

    @Override
    public RoundResponse battleWith(Entity opponent) { return null; }

    @Override
    public void spawn() {}
    
}
