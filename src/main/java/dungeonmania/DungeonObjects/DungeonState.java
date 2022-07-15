package dungeonmania.DungeonObjects;

import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Tracker;
import dungeonmania.util.DungeonFactory.DungeonBuilder;

public class DungeonState {
    // Metadata
    private String dungeonId;
    private String dungeonName;

    private Player player;
    private DungeonMap map;
    private Tracker tracker;

    private JSONObject config;

    private int currTick = 0;

    public DungeonState(DungeonBuilder builder) {
        dungeonId       = builder.getDungeonId();
        dungeonName     = builder.getDungeonName();
        player          = builder.getPlayer();
        map             = builder.getMap();
        tracker         = builder.getTracker();
        config          = builder.getConfig();
    }
    
    // Would it move all the npc like tick(direction) does?
    //
    // Turns out it does lol
    public void tick(String itemUsedId) throws IllegalArgumentException, InvalidActionException {
        currTick++;
        player.tick(Constant.PLAYERUSE, null, itemUsedId);

        map.updateCharPos();
        map.spawnEntites(config, currTick);
    }

    public void tick(Direction movementDirection) {
        currTick++;

        try {
            player.tick(Constant.PLAYERMOVE, movementDirection, null);
        } catch (IllegalArgumentException e) {
            System.out.println("DungeonState: Player " + e.getMessage());
        } catch (InvalidActionException e) {
            System.out.println("DungeonState: Player " + e.getMessage());
        }

        map.updateCharPos();

        map.spawnEntites(config, currTick);

        // Structure would be something like this:
        //
        // player.updatePos() ---> update player position
        // player.interact() ---> includes boulders, doors, portals, exits
        
        // map.processOverlappables() ---> activation/deactivation of switches, collecting items

        // entitiesWithinRange.trigger() ---> includes destruction of zombie toast spawner, <Stage_2>[bribery, bomb explosion]

        // npc.updatePos() ---> update all enemy positions, <Stage_2>[update friendly pos]
        
        // spawner.spawnItems() ---> spawns zombies

        // player.initiateBattle() ---> initiates battle with all overlapped enemies
    }

    public void build(String buildable) throws IllegalArgumentException, InvalidActionException {
        player.tick(Constant.PLAYERMAKE, null, buildable);
    }

    public void interact(String entityId) throws IllegalArgumentException, InvalidActionException {}

    public DungeonResponse toDungeonResponse() {
        List<EntityResponse> entities = map.getAllEntities().stream().map(e -> e.toEntityResponse()).collect(Collectors.toList());
        return new DungeonResponse(dungeonId, dungeonName, entities, player.getPlayerItems(), null, player.getBuildables(), null);
    }
}
