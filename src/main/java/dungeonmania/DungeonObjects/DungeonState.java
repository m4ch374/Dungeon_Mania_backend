package dungeonmania.DungeonObjects;

import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.IPlayerInteractable;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.DungeonFactory.DungeonBuilder;
import dungeonmania.util.Tracker.Tracker;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.json.JSONObject;



public class DungeonState {
    // Metadata
    private String dungeonId;
    private String dungeonName;

    private Player player;
    private DungeonMap map;
    private Tracker tracker;
    private List<BattleResponse> battles = new ArrayList<BattleResponse>();

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
        player.tick(EntityTypes.PLAYERUSE.toString(), null, itemUsedId);

        map.updateCharPos();
        map.spawnEntites(config, currTick, tracker);
    }

    public void tick(Direction movementDirection) {
        currTick++;

        try {
            player.tick(EntityTypes.PLAYERMOVE.toString(), movementDirection, null);
        } catch (IllegalArgumentException e) {
            System.out.println("DungeonState: Player " + e.getMessage());
        } catch (InvalidActionException e) {
            System.out.println("DungeonState: Player " + e.getMessage());
        }

        List<BattleResponse> playerMoveBattle = player.initiateBattle();
        if ( playerMoveBattle != null) {
            playerMoveBattle.stream().forEach(b -> battles.add(b));
        }

        if (player.isDead()){
            return;
        }

        map.updateCharPos();

        map.spawnEntites(config, currTick, tracker);

        if (!player.isDead()){
            List<BattleResponse> enemyMoveBattle = player.initiateBattle();
            if ( enemyMoveBattle != null) {
                enemyMoveBattle.stream().forEach(b -> battles.add(b));
            }
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
        player.tick(EntityTypes.PLAYERMAKE.toString(), null, buildable);
    }

    public void interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        Entity entityToInteract = map.getAllEntities().stream().filter(e -> e.getId().equals(entityId)).findFirst().orElse(null);

        if (entityToInteract == null)
            throw new IllegalArgumentException("Error: " + entityId + " not found");

        // Do the interaction
        ((IPlayerInteractable) entityToInteract).interactedByPlayer(player);
    }

    public void saveGame(String name) {
        // TODO: stubbed
    }

    public void loadGame(String name) {
        // TODO: stubbed
    }

    public List<String> getAllGames() {
        return null;
    }

    public DungeonResponse toDungeonResponse() {
        List<EntityResponse> entities = map.getAllEntities().stream().map(e -> e.toEntityResponse()).collect(Collectors.toList());
        return new DungeonResponse(dungeonId, dungeonName, entities, player.getPlayerItems(), battles, player.getBuildables(), tracker.getUnfinishedGoals(map));
    }
}
