package dungeonmania.DungeonObjects.Entities.Characters;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.EntityTypes;
import dungeonmania.DungeonObjects.Player;
import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.IMovable;
import dungeonmania.Interfaces.IMovingStrategy;
import dungeonmania.MovingStrategies.ConfusedMoveStrat;
import dungeonmania.MovingStrategies.CowerMoveStrat;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.DungeonFactory.EntityStruct;

public class ZombieToast extends Entity implements IMovable {
    private static final String OBSERVING_ID = "player";

    private int attackDamage;
    private int health;

    private Player observing = null;
    private DungeonMap map = super.getMap();
    private IMovingStrategy moveStrat = new ConfusedMoveStrat(this, map);

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

    public void death() {
        getMap().removeEntity(this);
        return;
    }

    public String getClasString() {
        return "Zombie Toast";
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

    @Override
    public RoundResponse battleWith(Entity opponent) { return null; }

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
