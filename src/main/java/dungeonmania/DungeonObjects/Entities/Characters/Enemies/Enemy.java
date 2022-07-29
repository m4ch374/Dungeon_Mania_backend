package dungeonmania.DungeonObjects.Entities.Characters.Enemies;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.EntityTypes;
import dungeonmania.DungeonObjects.Player;
import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.DungeonObjects.Entities.Statics.SwampTile;
import dungeonmania.Interfaces.IEnemy;
import dungeonmania.util.DungeonFactory.EntityStruct;
import dungeonmania.util.Tracker.GoalTypes;
import dungeonmania.util.Tracker.Tracker;

public abstract class Enemy extends Entity implements IEnemy {
    protected static final String DEFAULT_OBSERVE_ID = "player";

    private double attackDamage;
    private double health;

    private Player observing = null;
    private DungeonMap map = super.getMap();

    private Tracker tracker;

    // An undefined enemy does not exist
    protected Enemy(EntityStruct metaData, Tracker tracker, JSONObject config, String attackStr, String healthStr) {
        super(metaData);

        this.tracker = tracker;
        this.attackDamage = config.getInt(attackStr);
        this.health = config.getInt(healthStr);
    }

    protected Player getObservingEntity() {
        if (observing == null)
            observing = map.getAllEntities().stream()
                        .filter(e -> e.getType().equals(EntityTypes.PLAYER.toString()))
                        .map(e -> (Player) e)
                        .findFirst()
                        .get();
        
        return observing;
    }

    protected boolean trappedBySwamp() {
        SwampTile tile = map.getEntitiesOverlapped(this).stream()
                            .filter(e -> e.getType().equals(EntityTypes.SWAMP_TILE.toString()))
                            .map(e -> (SwampTile) e)
                            .findFirst()
                            .orElse(null);

        if (tile == null)
            return false;

        return !tile.ableToMove(this);
    }

    @Override
    public double getAttackDamage() {
        return attackDamage;
    }

    @Override
    public double getHealth() {
        return health;
    }

    @Override
    public void death() {
        map.removeEntity(this);
        tracker.notifyTracker(GoalTypes.ENEMIES);
    }

    @Override
    public String getClasString() {
        return super.getType();
    }
}
