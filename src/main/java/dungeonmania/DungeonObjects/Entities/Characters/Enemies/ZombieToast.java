package dungeonmania.DungeonObjects.Entities.Characters.Enemies;

import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.DungeonObjects.Player;
import dungeonmania.Interfaces.IMovingStrategy;
import dungeonmania.MovingStrategies.ConfusedMoveStrat;
import dungeonmania.MovingStrategies.CowerMoveStrat;
import dungeonmania.util.Position;
import dungeonmania.util.DungeonFactory.EntityStruct;
import dungeonmania.util.Tracker.Tracker;

import org.json.JSONObject;

public class ZombieToast extends Enemy {
    private static final String ATK_STR = "zombie_attack";
    private static final String HEALTH_STR = "zombie_health";

    DungeonMap map = super.getMap();
    IMovingStrategy moveStrat = new ConfusedMoveStrat(this, map);

    public ZombieToast(EntityStruct metaData, JSONObject config, Tracker tracker) {
        super(metaData, tracker, config, ATK_STR, HEALTH_STR);
    }

    // For zombie-like entities
    protected ZombieToast(EntityStruct metaData, Tracker tracker, JSONObject config, String attackStr, String healthStr) {
        super(metaData, tracker, config, attackStr, healthStr);
    }

    @Override
    public void move() {
        switchMoveStrat();
        Position pos = moveStrat.moveEntity();
        map.moveEntityTo(this, pos);
    }

    private void switchMoveStrat() {
        Player observing = getObservingEntity();

        boolean invincible = observing.isInvincible();

        if (invincible && !(moveStrat instanceof CowerMoveStrat)) {
            moveStrat = new CowerMoveStrat(this, map, observing.getId());
            return;
        }

        if (!invincible && !(moveStrat instanceof ConfusedMoveStrat)) {
            moveStrat = new ConfusedMoveStrat(this, map);
        }
    }
}
