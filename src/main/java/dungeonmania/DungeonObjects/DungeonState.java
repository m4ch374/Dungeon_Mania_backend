package dungeonmania.DungeonObjects;

import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.DungeonBuilder;
import dungeonmania.util.Tracker;

public class DungeonState {
    // Metadata
    private String dungeonId;
    private String dungeonName;

    private Player player = new Player();
    private DungeonMap map = new DungeonMap();
    private Tracker tracker = new Tracker();

    public DungeonState(DungeonBuilder builder) {}
    
    public void tick(String itemUsedId) throws IllegalArgumentException, InvalidActionException {}

    public void tick(Direction movementDirection) {}

    public void build(String buildable) throws IllegalArgumentException, InvalidActionException {}

    public void interact(String entityId) throws IllegalArgumentException, InvalidActionException {}

    public DungeonResponse toDungeonResponse() {
        return null;
    }
}
