package dungeonmania.DungeonObjects.Entities.Characters;

import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.DungeonObjects.EntityTypes;
import dungeonmania.DungeonObjects.Player;
import dungeonmania.Interfaces.IEnemy;
import dungeonmania.Interfaces.IMovingStrategy;
import dungeonmania.MovingStrategies.ConfusedMoveStrat;
import dungeonmania.MovingStrategies.CowerMoveStrat;
import dungeonmania.util.Tracker;
import dungeonmania.util.DungeonFactory.EntityStruct;
import org.json.JSONObject;


public class ZombieToast extends Entity implements IEnemy {
    private static final String OBSERVING_ID = "player";

    private double attackDamage;
    private double health;

    private Player observing = null;
    private DungeonMap map = super.getMap();
    private IMovingStrategy moveStrat = new ConfusedMoveStrat(this, map);

    Tracker tracker;

    public ZombieToast(EntityStruct metaData, JSONObject config, Tracker tracker) {
        super(metaData);
        this.attackDamage = config.getInt("zombie_attack");
        this.health = config.getInt("zombie_health");
        this.tracker = tracker;
    }

    public double getAttackDamage() {
        return attackDamage;
    }

    public double getHealth() {
        return health;
    }

    public void death() {
        getMap().removeEntity(this);
        tracker.notifyEnemy();
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
        moveStrat.moveEntity();
    }

    private void switchMoveStrat() {
        boolean invincible = (boolean) observing.getState().get("invincible");

        if (invincible && !(moveStrat instanceof CowerMoveStrat)) {
            moveStrat = new CowerMoveStrat(this, map, OBSERVING_ID);
            return;
        }

        if (!invincible && !(moveStrat instanceof ConfusedMoveStrat)) {
            moveStrat = new ConfusedMoveStrat(this, map);
        }
    }
}
