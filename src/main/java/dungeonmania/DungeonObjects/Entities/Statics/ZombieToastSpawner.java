package dungeonmania.DungeonObjects.Entities.Statics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.EntityTypes;
import dungeonmania.DungeonObjects.Player;
import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.DungeonObjects.Entities.Characters.ZombieToast;
import dungeonmania.Interfaces.IPlayerInteractable;
import dungeonmania.Interfaces.ISpawnable;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.util.DungeonFactory.EntityStruct;
import dungeonmania.util.Tracker.Tracker;
import dungeonmania.exceptions.*;

public class ZombieToastSpawner extends Entity implements IPlayerInteractable, ISpawnable {
    private static int spawnId = 0;

    private int zombieSpawnRate;
    private DungeonMap map;

    private Tracker tracker;

    public ZombieToastSpawner(EntityStruct metaData, JSONObject config, Tracker tracker) {
        super(metaData);
        this.zombieSpawnRate = config.getInt("zombie_spawn_rate");
        map = super.getMap();
    }

    @Override
    public EntityResponse toEntityResponse() {
        return new EntityResponse(super.getId(), super.getType(), map.getEntityPos(this), true);
    }

    @Override
    public void spawn(JSONObject config, int currTick) {
        if (zombieSpawnRate == 0)
            return;

        if (currTick == 0 || currTick % zombieSpawnRate != 0)
            return;

        Position currPos = map.getEntityPos(this);
        List<Position> possiblePos = getPotentialSpawnPos(currPos);

        if (possiblePos.isEmpty()) 
            return;

        Random random = new Random();
        int randIdx = random.nextInt(possiblePos.size());
        Position newPos = possiblePos.get(randIdx);

        EntityStruct struct = new EntityStruct("spawned_zombie" + spawnId, EntityTypes.ZOMBIE_TOAST.toString(), map);
        ZombieToast zombieSpawned = new ZombieToast(struct, config, tracker);
        map.placeEntityAt(zombieSpawned, newPos);

        spawnId++;
    }

    @Override
    public void interactedByPlayer(Player player) throws InvalidActionException {
        if (!(playerInRange(player) && playerHasWeapon(player)))
            throw new InvalidActionException("Error: player not in range or does not have a weapon");

        map.removeEntity(this);
    }
    

    private List<Position> getPotentialSpawnPos(Position currPos) {
        List<Position> possiblePos = new ArrayList<Position>();

        for (Direction d : Direction.values()) {
            Position dPos = currPos.translateBy(d);

            if (!posContainBlockable(dPos))
                possiblePos.add(dPos);
        }
        return possiblePos;
    }

    private boolean posContainBlockable(Position pos) {
        List<Entity> entities = map.getEntitiesAt(pos);

        for (Entity e : entities) {
            if (e.getType().equals(EntityTypes.WALL.toString()))
                return true;

            if (e.getType().equals(EntityTypes.DOOR.toString())) {
                Door d = (Door) e;
                if (!d.isOpen())
                    return true;
            }
        }

        return false;
    }

    private boolean playerInRange(Player player) {
        Position currPos = map.getEntityPos(this);
        Position playerPos = map.getEntityPos(player);

        List<Position> allowedPos = Arrays.asList(Direction.values()).stream().map(d -> currPos.translateBy(d)).collect(Collectors.toList());

        return allowedPos.contains(playerPos);
    }

    private boolean playerHasWeapon(Player player) {
        return (player.holdingSword() || player.holdingBow() );
    }
}
