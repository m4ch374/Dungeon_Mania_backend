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

    public DungeonState(DungeonBuilder builder) {
        dungeonId       = builder.getDungeonId();
        dungeonName     = builder.getDungeonName();
        player          = builder.getPlayer();
        map             = builder.getMap();
        tracker         = builder.getTracker();
        config          = builder.getConfig();
    }
    
    public void tick(String itemUsedId) throws IllegalArgumentException, InvalidActionException {
        player.useItem(itemUsedId);;
    }

    public void tick(Direction movementDirection) {
        try {
            player.move(movementDirection);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (InvalidActionException e) {
            System.out.println(e.getMessage());
        }

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
        player.make(buildable);
    }

    public void interact(String entityId) throws IllegalArgumentException, InvalidActionException {}

    public DungeonResponse toDungeonResponse() {
        List<EntityResponse> entities = map.getAllEntities().stream().map(e -> e.toEntityResponse()).collect(Collectors.toList());
        return new DungeonResponse(dungeonId, dungeonName, entities, player.getPlayerItems(), null, player.getBuildables(), null);
    }
}
