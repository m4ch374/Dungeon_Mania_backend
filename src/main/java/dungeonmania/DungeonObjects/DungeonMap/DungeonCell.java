package dungeonmania.DungeonObjects.DungeonMap;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.DungeonObjects.Entities.Entity;

public class DungeonCell {
    private List<Entity> cell = new ArrayList<Entity>();

    public boolean cellIsEmpty() {
        return cell.isEmpty();
    }

    // Precondition: entity is present in this cell
    public void removeEntity(Entity entity) {
        cell.remove(entity);
    }

    public void addToCell(Entity entity) {
        cell.add(0, entity);
    }

    // Get all entities in ascending order of layer
    // i.e. top layer goes first
    public List<Entity> getAllEntitiesInCell() {
        return cell;
    }
}
