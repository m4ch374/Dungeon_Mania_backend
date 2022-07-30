package dungeonmania.DungeonObjects.Entities.Characters.Enemies;

import dungeonmania.Interfaces.IMovingStrategy;
import dungeonmania.Interfaces.IPlayerInteractable;
import dungeonmania.MovingStrategies.ConfusedMoveStrat;
import dungeonmania.MovingStrategies.CowerMoveStrat;
import dungeonmania.MovingStrategies.SeekerMoveStrat;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;
import dungeonmania.util.DungeonFactory.EntityStruct;
import dungeonmania.util.Tracker.Tracker;

import org.json.JSONObject;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.DungeonObjects.Player;
import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.DungeonObjects.Entities.Characters.Friendlies.FriendlyCharacter;
import dungeonmania.DungeonObjects.Entities.Characters.Friendlies.MindControlledCharacter;
import dungeonmania.DungeonObjects.Entities.Statics.Portal;

public class Mercenary extends Enemy implements IPlayerInteractable {
    private static final String ATK_STR = "mercenary_attack";
    private static final String HEALTH_STR = "mercenary_health";

    private int bribeRadius;
    private int brinbeAmount;

    private JSONObject config;
    private DungeonMap map = super.getMap();
    private IMovingStrategy moveStrat = new SeekerMoveStrat(this, this.map, DEFAULT_OBSERVE_ID);

    public Mercenary(EntityStruct metaData, JSONObject config, Tracker tracker) {
        super(metaData, tracker, config, ATK_STR, HEALTH_STR);
        this.bribeRadius = config.getInt("bribe_radius");
        this.brinbeAmount = config.getInt("bribe_amount");
        this.config = config;
    }

    // For mercenary-like entities
    public Mercenary(EntityStruct metaData, Tracker tracker, JSONObject config, String attackStr, String healthStr, String radiusStr, String amtStr) {
        super(metaData, tracker, config, attackStr, healthStr);
        this.bribeRadius = config.getInt(radiusStr);
        this.brinbeAmount = config.getInt(amtStr);
        this.config = config;
    }

    public int getBribeAmount() {
        return brinbeAmount;
    }

    @Override
    public void move() {
        if (super.trappedBySwamp())
            return;

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
        if (player.holdingSceptre())
            mindControlsByPlayer(player);
        else
            bribedByPlayer(player);
    }
    
    private void switchMoveStrat() {
        Player observing = getObservingEntity();

        boolean invincible = observing.isInvincible();
        boolean invisible = observing.isInvisible();

        if (invincible && !(moveStrat instanceof CowerMoveStrat)) {
            moveStrat = new CowerMoveStrat(this, map, observing.getId());
            return;
        }

        if (invisible && !(moveStrat instanceof ConfusedMoveStrat)) {
            moveStrat = new ConfusedMoveStrat(this, map);
            return;
        }

        if (!invincible && !invisible && !(moveStrat instanceof SeekerMoveStrat)) {
            moveStrat = new SeekerMoveStrat(this, map, observing.getId());
            return;
        }
    }

    private void mindControlsByPlayer(Player player) {
        Position currPos = map.getEntityPos(this);
        
        EntityStruct metaData = new EntityStruct(super.getId(), super.getType(), map);
        MindControlledCharacter character = new MindControlledCharacter(metaData, config, this, map, player);

        map.removeEntity(this);
        map.placeEntityAt(character, currPos);
        player.notifyAllyIncrease();
    }

    protected void bribedByPlayer(Player player) throws InvalidActionException {
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
}
