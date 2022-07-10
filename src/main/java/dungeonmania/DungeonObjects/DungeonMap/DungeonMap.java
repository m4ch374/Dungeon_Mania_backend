package dungeonmania.DungeonObjects.DungeonMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dungeonmania.DungeonObjects.Player;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.IMovable;
import dungeonmania.util.Position;

public class DungeonMap {
    private Map<Position, DungeonCell> map = new HashMap<Position, DungeonCell>();
    private Map<Entity, Position> lookup = new HashMap<Entity, Position>();

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
        List<Entity> celleEntities = currCell.getAllEntitiesInCell();

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

    // Update all movable's position except for player
    public void updateCharPos() {
        // Ugly but works
        List<IMovable> characters = getAllEntities().stream()
                                    .filter(e -> e instanceof IMovable)
                                    .map(e -> (IMovable) e)
                                    .collect(Collectors.toList());

        characters.forEach(c -> c.move());
    }
}
