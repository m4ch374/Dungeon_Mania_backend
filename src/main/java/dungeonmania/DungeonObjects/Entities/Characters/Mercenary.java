package dungeonmania.DungeonObjects.Entities.Characters;

import dungeonmania.Interfaces.IMovable;
import dungeonmania.Interfaces.IMovingStrategy;
import dungeonmania.MovingStrategies.SeekerMoveStrat;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.DungeonFactory.EntityStruct;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.DungeonObjects.Entities.Entity;

public class Mercenary extends Entity implements IMovable {

    private int bribeRadius;
    private int brinbeAmount;

    private int attackDamage;
    private int health;

    private IMovingStrategy moveStrat = new SeekerMoveStrat(this, super.getMap(), "player");

    public Mercenary(EntityStruct metaData, JSONObject config) {
        super(metaData);
        this.bribeRadius = config.getInt("bribe_radius");
        this.brinbeAmount = config.getInt("bribe_amount");
        this.attackDamage = config.getInt("mercenary_attack");
        this.health = config.getInt("mercenary_health");
    }

    public int getBribeRadius() {
        return bribeRadius;
    }

    public int getBribeAmount() {
        return brinbeAmount;
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

    @Override
    public EntityResponse toEntityResponse() {
        DungeonMap map = super.getMap();
        return new EntityResponse(super.getId(), super.getType(), map.getEntityPos(this), true);
    }
    
}
