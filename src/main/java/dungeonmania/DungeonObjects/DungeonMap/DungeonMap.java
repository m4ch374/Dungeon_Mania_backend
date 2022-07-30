package dungeonmania.DungeonObjects.DungeonMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.Player;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.DungeonObjects.Entities.Characters.Enemies.Spider;
import dungeonmania.DungeonObjects.Entities.Characters.Enemies.ZombieToast;
import dungeonmania.DungeonObjects.Entities.LogicEntities.LogicPathFinder;
import dungeonmania.DungeonObjects.Entities.LogicEntities.Collectables.Bomb;
import dungeonmania.DungeonObjects.Entities.LogicEntities.Statics.FloorSwitch;
import dungeonmania.DungeonObjects.Entities.LogicEntities.Statics.Wire;
import dungeonmania.Interfaces.IMovable;
import dungeonmania.Interfaces.ISpawnable;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.util.Tracker.Tracker;

public class DungeonMap {
    // Assumes the map is not unlimited and surrounded by walls
    // Map would work even if these restrictions does not satisfy
    private final Position topLeftCorner;
    private final Position bottomRightCorner;

    private Map<Position, DungeonCell> map = new HashMap<Position, DungeonCell>();
    private Map<Entity, Position> lookup = new HashMap<Entity, Position>();

    public DungeonMap(Position topLeftCorner, Position bottomRightCorner) {
        this.topLeftCorner = topLeftCorner;
        this.bottomRightCorner = bottomRightCorner;
    }

    public boolean isEptyCell(Position pos) { 
        return !map.containsKey(pos); 
    }

    // Get all entities at a certain position
    public List<Entity> getEntitiesAt(Position pos) {
        if (isEptyCell(pos))
            return new ArrayList<Entity>();

        return map.get(pos).getAllEntitiesInCell();
    }

    // Get all overlapped entities of a given entity
    // Returned list excludes the given entity
    public List<Entity> getEntitiesOverlapped(Entity entity) {
        List<Entity> entities = getEntitiesAt(getEntityPos(entity));
        return entities.stream().filter(e -> !e.equals(entity)).collect(Collectors.toList());
    }

    public List<Entity> getAllEntities() { 
        return lookup.keySet().stream().collect(Collectors.toList());
    }

    public Position getEntityPos(Entity entity) { 
        return lookup.get(entity);
    }

    // Removes every entity at a position
    public void removeAllAtPos(Position pos) {
        if (!map.containsKey(pos))
            return;

        DungeonCell currCell = map.get(pos);
        List<Entity> cellEntities = currCell.getAllEntitiesInCell();

        map.remove(pos);

        cellEntities.forEach(e -> lookup.remove(e));
    }

    // Removes every entity at a posision except for Player
    public void removeAtPosExceptPlayer(Position pos) {
        if (!map.containsKey(pos))
            return;

        DungeonCell currCell = map.get(pos);
        List<Entity> celleEntities = new ArrayList<Entity>();
        celleEntities.addAll(currCell.getAllEntitiesInCell());

        celleEntities.stream()
                    .filter(e -> !(e instanceof Player))
                    .forEach(e -> {
                        currCell.removeEntity(e);
                        lookup.remove(e);
                    });
    }

    // Removes an entity form a map
    public void removeEntity(Entity entity) {
        Position entityPos = getEntityPos(entity);
        DungeonCell cell = map.get(entityPos);
        cell.removeEntity(entity);

        lookup.remove(entity);

        if (cell.cellIsEmpty())
            map.remove(entityPos);
    }

    // Place an entity to the map
    public void placeEntityAt(Entity entity, Position pos) {
        DungeonCell cell = map.get(pos);

        if (cell == null) {
            cell = new DungeonCell();
            map.put(pos, cell);
        }
        cell.addToCell(entity);

        lookup.put(entity, pos);
    }

    // Removes the entity and put it in a new position
    public void moveEntityTo(Entity entity, Position pos) {
        removeEntity(entity);
        placeEntityAt(entity, pos);
    }

    // Bound is based on initial position of all the entities
    // Entities out of bound is supported
    public int getLeftBound() {
        return topLeftCorner.getX();
    }

    public int getTopBound() {
        return topLeftCorner.getY();
    }

    public int getRightBound() {
        return bottomRightCorner.getX();
    }

    public int getBottomBound() {
        return bottomRightCorner.getY();
    }

    // Update all movable's position except for player
    public void updateCharPos() {
        // Ugly but works
        List<IMovable> characters = getAllEntities().stream()
                                    .filter(e -> e instanceof IMovable)
                                    .map(e -> (IMovable) e)
                                    .collect(Collectors.toList());

        characters.forEach(c -> c.move());
    }

    // Spawns all spawnable objects
    public void spawnEntites(JSONObject config, int currTick, Tracker tracker) {
        // Spawn spiders, unfortunately it has to be a bit more white box than usual
        Spider.spawnSpider(config, currTick, this, tracker);

        List<ISpawnable> spawnerEntities = getAllEntities().stream()
                                            .filter(e -> e instanceof ISpawnable)
                                            .map(e -> (ISpawnable) e)
                                            .collect(Collectors.toList());
        
        spawnerEntities.forEach(e -> e.spawn(config, currTick));
    }

    public Position getPlayerPos() {
        for (Entity entity : lookup.keySet()) {
            if (entity instanceof Player) {
                return lookup.get(entity);
            }
        }

        return null;
    }

    public boolean hasZombie() {
        for (Entity entity : lookup.keySet()) {
            if (entity instanceof ZombieToast) {
                return true;
            }
        }

        return false;
    }

    private int getListSzieOfSwitch(List<Entity> list) {
        return list.stream()
                    .filter(e -> (e instanceof FloorSwitch))
                    .collect(Collectors.toList())
                    .size();
    }

    private boolean hasSource(List<Entity> list) {
        int wires = list.stream()
                                .filter(e -> (e instanceof FloorSwitch))
                                .map(e -> (FloorSwitch) e)
                                .filter(e -> e.isActive())
                                .collect(Collectors.toList())
                                .size();

        return (wires > 0);
    }

    private boolean hasWire(List<Entity> list) {
        int wires = list.stream()
                                .filter(e -> (e instanceof Wire))
                                .collect(Collectors.toList())
                                .size();

        return (wires > 0);
    }

    private char[][] get2DMap(int x, int y) {
        char[][] matrix = new char[x][y];

        return matrix;
    }

    private boolean hasSwitchAt(Position pos) {
        List<Entity> list = getEntitiesAt(pos);
        boolean hasSwitch = (list.stream()
                            .filter(e -> (e instanceof FloorSwitch))
                            .collect(Collectors.toList())
                            .size() > 0);

        return hasSwitch;
    }

    private HashMap<String, Integer> getMapSize() {
        HashMap<String, Integer> mapSize = new HashMap<String, Integer>();
        int max_y = 0;
        int min_y = 0;
        int max_x = 0;
        int min_x = 0;

        List<Position> pos_list = new ArrayList<>(map.keySet());

        for (Position pos : pos_list) {
            int x = pos.getX();
            int y = pos.getY();

            if (x > max_x) {
                max_x = x;
            }
            if (x < min_x) {
                min_x = x;
            }
            if (y > max_y) {
                max_y = y;
            }
            if (y < min_y) {
                max_x = y;
            }
        }

        mapSize.put("max_y", max_y);
        mapSize.put("min_y", min_y);
        mapSize.put("max_x", max_x);
        mapSize.put("min_x", min_x);

        return mapSize;
    }

    /**
     * get all entities in four dirct, check their state
     * @param pos
     * @return
     */
    public JSONObject getAdjacentActive(Position pos) {
        int switch_num = 0;
        int active_switch_num = 0;
        int active_wire_num = 0;

        // four dirct
        Position up = pos.translateBy(Direction.UP);
        Position down = pos.translateBy(Direction.DOWN);
        Position left = pos.translateBy(Direction.LEFT);
        Position right = pos.translateBy(Direction.RIGHT);

        List<Position> dest_pos = new ArrayList<>();
        dest_pos.add(up);
        dest_pos.add(down);
        dest_pos.add(left);
        dest_pos.add(right);

        // entities at four dirct
        List<Entity> e_up = getEntitiesAt(up);
        List<Entity> e_down = getEntitiesAt(down);
        List<Entity> e_left = getEntitiesAt(left);
        List<Entity> e_right = getEntitiesAt(right);

        // count the number of switchs
        switch_num += getListSzieOfSwitch(e_up);
        switch_num += getListSzieOfSwitch(e_down);
        switch_num += getListSzieOfSwitch(e_left);
        switch_num += getListSzieOfSwitch(e_right);

        // position of all active switch
        List<FloorSwitch> active_switchs= lookup.keySet()
                                                .stream()
                                                .filter(e -> (e instanceof FloorSwitch))
                                                .map(e -> (FloorSwitch) e)
                                                .filter(e -> e.isActive())
                                                .collect(Collectors.toList());

        List<Position> active_switchs_pos = new ArrayList<>();
        active_switchs
                    .stream()
                    .forEach(e -> active_switchs_pos.add(lookup.get(e)));

        final int row_start = getMapSize().get("max_y");        // max y for map
        final int row_end = getMapSize().get("min_y");;         // min y for map
        final int col_start = getMapSize().get("max_x");;       // max x for map
        final int col_end = getMapSize().get("min_x");;         // min x for map
        final int rows = Math.abs(row_end - row_start);
        final int cols = Math.abs(col_end - col_start);

        // for each adjacent cell, there is at least one source connect to it
        for (Position destPos : dest_pos) {
            for (Position A_S_pos : active_switchs_pos) {

                // build up the map for current dest and source
                char[][] matrix = get2DMap(rows, cols);
                for (int i = row_start; i <= row_end; i++) {
                    for (int j = col_start; j <= col_end; j++) {
                        Position new_pos = new Position(j, i); // mirror
                        List<Entity> entities = getEntitiesAt(new_pos);

                        // the position of entities might be negative
                        // but not for 2D array :(
                        final int r = i - row_start;
                        final int c = j - col_start;

                        if (entities != null) { // might empty
                            if (new_pos.equals(pos)) {
                                matrix[r][c] = 'D'; // current dest
                            } else if (new_pos.equals(A_S_pos) && hasSource(entities)) {
                                matrix[r][c] = 'S'; // current source
                            } else if (hasWire(entities)) {
                                matrix[r][c] = '1'; // wire
                            } else {
                                matrix[r][c] = '0'; // consider as block
                            }
                        } else {
                            matrix[r][c] = '0'; // consider as block
                        }
                    }
                }

                if (LogicPathFinder.hasPath(matrix)) {
                    if (hasSwitchAt(destPos)) {
                        // adjacent active switch
                        active_switch_num++;
                    }
                    // adjacent active wire
                    active_wire_num++;
                    break; // only one active source path needed for each adjacent cell
                }
            }
        }

        JSONObject json = new JSONObject();

        // number of adjacent switch
        json.put("switch_num", switch_num);
        // is all adjacent switch number active?
        json.put("all_switch_is_avtive", active_switch_num == switch_num);
        // number of adjacent active entities
        json.put("active_num", active_wire_num);

        return json;
    }

    public void activeBombIfActive() {
        getAllEntities()
                    .stream()
                    .filter(e -> (e instanceof Bomb))
                    .map(e -> (Bomb) e)
                    .filter(e -> (e.isActive() && !e.isCollectible()))
                    .forEach(e -> e.activate(getEntityPos(e)));
    }
}
