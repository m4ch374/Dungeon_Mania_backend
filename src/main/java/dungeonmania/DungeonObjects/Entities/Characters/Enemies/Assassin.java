package dungeonmania.DungeonObjects.Entities.Characters.Enemies;

import java.util.Random;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.Player;
import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.Interfaces.IMovingStrategy;
import dungeonmania.MovingStrategies.SeekerMoveStrat;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Position;
import dungeonmania.util.DungeonFactory.EntityStruct;
import dungeonmania.util.Tracker.Tracker;

public class Assassin extends Mercenary {
    private static final String ATK_STR = "assassin_attack";
    private static final String HEALTH_STR = "assassin_health";
    private static final String RADIUS_STR = "bribe_radius";
    private static final String AMT_STR = "assassin_bribe_amount";

    private double bribeFailRate;

    private int reconRadius;

    public Assassin(EntityStruct metaData, JSONObject config, Tracker tracker) {
        super(metaData, tracker, config, ATK_STR, HEALTH_STR, RADIUS_STR, AMT_STR);
        this.bribeFailRate = config.getDouble("assassin_bribe_fail_rate");
        this.reconRadius = config.getInt("assassin_recon_radius");
    }

    @Override
    protected void bribedByPlayer(Player player) throws InvalidActionException {
        Random random = new Random();
        double rand = random.nextDouble();
        System.out.println(rand < bribeFailRate);
        if (rand < bribeFailRate)
            player.tryBribe(super.getBribeAmount());
        else
            super.bribedByPlayer(player);
    }

    @Override
    protected void switchMoveStrat() {
        super.switchMoveStrat();

        DungeonMap map = super.getMap();
        Player observing = super.getObservingEntity();

        if (!observing.isInvisible())
            return;
        
        Position observingPos = map.getEntityPos(observing);
        Position currPos = map.getEntityPos(this);

        Position distance = Position.calculatePositionBetween(currPos, observingPos);

        if (Math.abs(distance.getX()) > reconRadius || Math.abs(distance.getY()) > reconRadius)
            return;

        IMovingStrategy currMoveStrat = super.getMoveStrat();
        if (!(currMoveStrat instanceof SeekerMoveStrat))
            setMoveStrat(new SeekerMoveStrat(this, map, observing.getId()));
    }
}
