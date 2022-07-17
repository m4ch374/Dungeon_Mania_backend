package dungeonmania.DungeonObjects.Entities.Characters;

import dungeonmania.Interfaces.IEnemy;
import dungeonmania.Interfaces.IMovingStrategy;
import dungeonmania.Interfaces.IPlayerInteractable;
import dungeonmania.Interfaces.IEnemy;
import dungeonmania.MovingStrategies.ConfusedMoveStrat;
import dungeonmania.MovingStrategies.CowerMoveStrat;
import dungeonmania.MovingStrategies.SeekerMoveStrat;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;
import dungeonmania.util.DungeonFactory.EntityStruct;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.EntityTypes;
import dungeonmania.DungeonObjects.Player;
import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.DungeonObjects.Entities.Characters.*;;

public class Mercenary extends Entity implements IPlayerInteractable, IEnemy {
    private static final String OBSERVING_ID = "player";

    private int bribeRadius;
    private int brinbeAmount;

    private double attackDamage;
    private double health;

    private Player observing = null;
    private DungeonMap map = super.getMap();
    private IMovingStrategy moveStrat = new SeekerMoveStrat(this, this.map, OBSERVING_ID);

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

    public double getAttackDamage() {
        return attackDamage;
    }

    public double getHealth() {
        return health;
    }

    public void death() {
        getMap().removeEntity(this);
        return;
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

    @Override
    public EntityResponse toEntityResponse() {
        DungeonMap map = super.getMap();
        return new EntityResponse(super.getId(), super.getType(), map.getEntityPos(this), true);
    }

    @Override
    public void interactedByPlayer(Player player) throws InvalidActionException {
        Position currPos = map.getEntityPos(this);
        Position playerPos = map.getEntityPos(player);

        Position distance = Position.calculatePositionBetween(currPos, playerPos);

        if (distance.getX() > bribeRadius || distance.getY() > bribeRadius)
            throw new InvalidActionException("Error: out of radius");

        player.bribe(brinbeAmount);

        // Switch state to friendly merc
        EntityStruct struct = new EntityStruct(super.getId(), super.getType(), super.getMap());
        FriendlyCharacter friendlyMerc = new FriendlyCharacter(struct);
        map.removeEntity(this);
        map.placeEntityAt(friendlyMerc, currPos);
    }
    
    private void switchMoveStrat() {
        boolean invincible = (boolean) observing.getState().get("invincible");
        boolean invisible = (boolean) observing.getState().get("invisible");

        if (invincible && !(moveStrat instanceof CowerMoveStrat)) {
            moveStrat = new CowerMoveStrat(this, map, OBSERVING_ID);
            return;
        }

        if (invisible && !(moveStrat instanceof ConfusedMoveStrat)) {
            moveStrat = new ConfusedMoveStrat(this, map);
            return;
        }

        if (!invincible && !invisible && !(moveStrat instanceof SeekerMoveStrat)) {
            moveStrat = new SeekerMoveStrat(this, map, OBSERVING_ID);
            return;
        }
    }
}
