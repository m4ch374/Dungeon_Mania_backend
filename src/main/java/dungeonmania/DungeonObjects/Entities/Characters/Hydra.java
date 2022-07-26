package dungeonmania.DungeonObjects.Entities.Characters;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.EntityTypes;
import dungeonmania.DungeonObjects.Player;
import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.IEnemy;
import dungeonmania.Interfaces.IMovingStrategy;
import dungeonmania.MovingStrategies.ConfusedMoveStrat;
import dungeonmania.MovingStrategies.CowerMoveStrat;
import dungeonmania.util.Position;
import dungeonmania.util.DungeonFactory.EntityStruct;
import dungeonmania.util.Tracker.GoalTypes;
import dungeonmania.util.Tracker.Tracker;

// Might extend it to zombies idk
public class Hydra extends Entity implements IEnemy {

    private static final String OBSERVING_ID = "player";

    private double attackDamage;
    private double health;
    private double healthIncreaseRate;
    private double healthIncreaseAmt;

    private Player observing = null;
    private DungeonMap map = super.getMap();
    private IMovingStrategy moveStrat = new ConfusedMoveStrat(this, map);

    Tracker tracker;

    public Hydra(EntityStruct metaData, JSONObject config, Tracker tracker) {
        super(metaData);
        this.attackDamage = config.getInt("hydra_attack");
        this.health = config.getInt("hydra_health");
        this.healthIncreaseRate = config.getDouble("hydra_health_increase_rate");
        this.healthIncreaseAmt = config.getDouble("hydra_health_increase_amount");
        this.tracker = tracker;
    }

    public double getAttackDamage() {
        return attackDamage;
    }

    public double getHealth() {
        return health;
    }

    public double getHealthIncreaseRate() {
        return healthIncreaseRate;
    }

    public double getHealthIncreaseAmt() {
        return healthIncreaseAmt;
    }

    public void death() {
        getMap().removeEntity(this);
        tracker.notifyTracker(GoalTypes.ENEMIES);
    }

    public String getClasString() {
        return super.getType();
    }


    @Override
    public void move() {
        if (observing == null)
            observing = map.getAllEntities().stream()
                        .filter(e -> e.getType().equals(EntityTypes.PLAYER.toString()))
                        .map(e -> (Player) e)
                        .findFirst()
                        .get();

        switchMoveStrat();
        Position pos = moveStrat.moveEntity();
        map.moveEntityTo(this, pos);
    }

    private void switchMoveStrat() {
        boolean invincible = observing.isInvincible();

        if (invincible && !(moveStrat instanceof CowerMoveStrat)) {
            moveStrat = new CowerMoveStrat(this, map, OBSERVING_ID);
            return;
        }

        if (!invincible && !(moveStrat instanceof ConfusedMoveStrat)) {
            moveStrat = new ConfusedMoveStrat(this, map);
        }
    }
    
}
