package dungeonmania.DungeonObjects.Entities.Characters;

import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.EntityTypes;
import dungeonmania.DungeonObjects.Player;
import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.DungeonObjects.Entities.Statics.Portal;
import dungeonmania.Interfaces.IEnemy;
import dungeonmania.Interfaces.IMovingStrategy;
import dungeonmania.Interfaces.IPlayerInteractable;
import dungeonmania.MovingStrategies.ConfusedMoveStrat;
import dungeonmania.MovingStrategies.CowerMoveStrat;
import dungeonmania.MovingStrategies.SeekerMoveStrat;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;
import dungeonmania.util.DungeonFactory.EntityStruct;
import dungeonmania.util.Tracker.GoalTypes;
import dungeonmania.util.Tracker.Tracker;

// TODO: sketch, might refactor after finishing dikstra algo
public class Assassin extends Entity implements IEnemy, IPlayerInteractable {

    private static final String OBSERVING_ID = "player";

    private int bribeRadius;
    private int brinbeAmount;
    private double failRate;

    private double attackDamage;
    private double health;

    private int reconRadius;

    private Player observing = null;
    private DungeonMap map = super.getMap();
    private IMovingStrategy moveStrat = new SeekerMoveStrat(this, this.map, OBSERVING_ID);

    private Tracker tracker;

    public Assassin(EntityStruct metaData, JSONObject config, Tracker tracker) {
        super(metaData);
        this.bribeRadius = config.getInt("bribe_radius");
        this.brinbeAmount = config.getInt("assassin_bribe_amount");
        this.failRate = config.getDouble("assassin_bribe_fail_rate");
        this.attackDamage = config.getInt("assassin_attack");
        this.health = config.getInt("assassin_health");
        this.reconRadius = config.getInt("assassin_recon_radius");
        this.tracker = tracker;
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
        tracker.notifyTracker(GoalTypes.ENEMIES);
    }

    public String getClasString() {
        return "Mercenary";
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

        // Prolly need to refactor this
        // Check for portal cases (NOTE: multi-teleportation not included due to time constraints)
        Portal portal = getNewPosPortal(pos);
        if (portal != null) {
            try {
                portal.interactedBy(this);
                List<Position> portalDests = portal.getDestinations(portal.determineDestinationDirection(map.getEntityPos(this)));
                // if no error is thrown, good to move
                map.moveEntityTo(this, portalDests.get(0));
            } catch (InvalidActionException e) {
            }
        } else {
            // if portal doesnt exist, dw
            map.moveEntityTo(this, pos);
        }
    }
    

    // Get portal at the given position
    public Portal getNewPosPortal(Position newPos) {
        List<Entity> inCell = map.getEntitiesAt(newPos);
        List<Portal> portal = inCell
                                .stream()
                                .filter(e -> (e instanceof Portal))
                                .map(e -> (Portal) e)
                                .collect(Collectors.toList());

        if (portal.size() == 0) return null;
        return portal.get(0);
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

        if (Math.abs(distance.getX()) > bribeRadius || Math.abs(distance.getY()) > bribeRadius)
            throw new InvalidActionException("Error: out of radius");

        player.bribe(brinbeAmount);

        // Switch state to friendly merc
        EntityStruct struct = new EntityStruct(super.getId(), super.getType(), super.getMap());
        FriendlyCharacter friendlyMerc = new FriendlyCharacter(struct);
        map.removeEntity(this);
        map.placeEntityAt(friendlyMerc, currPos);
    }
    
    private void switchMoveStrat() {
        boolean invincible = observing.isInvincible();
        boolean invisible = observing.isInvisible();

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
