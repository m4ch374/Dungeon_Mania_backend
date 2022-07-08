package dungeonmania.DungeonObjects.Entities.Characters;

import dungeonmania.Interfaces.IMovable;
import dungeonmania.Interfaces.IMovingStrategy;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.DungeonFactory.EntityStruct;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.Entities.Entity;

public class Mercenary extends Entity implements IMovable {

    private int bribeRadius;
    private int brinbeAmount;

    private int attackDamage;
    private int health;

    private IMovingStrategy moveStrat;

    public Mercenary(EntityStruct metaData, JSONObject config) {
        super(metaData);
        this.bribeRadius = config.getInt("bribe_radius");
        this.brinbeAmount = config.getInt("bribe_amount");
        this.attackDamage = config.getInt("mercenary_attack");
        this.health = config.getInt("mercenary_health");
    }

    @Override
    public void move() {}

    @Override
    public RoundResponse battleWith(Entity opponent) { return null; }

    @Override
    public EntityResponse toEntityResponse() { return null; }
    
}
