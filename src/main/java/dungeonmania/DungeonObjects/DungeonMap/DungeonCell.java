package dungeonmania.DungeonObjects.DungeonMap;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.DungeonObjects.EntityTypes;
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
        EntityTypes type = EntityTypes.lookupEnum(entity.getType());
        int entityLayer = type.getLayer();

        int insertIdx = 0;
        for (Entity e : cell) {
            EntityTypes currType = EntityTypes.lookupEnum(e.getType());
            int currTypeLayer = currType.getLayer();

            if (entityLayer <= currTypeLayer)
                break;

            insertIdx++;
        }

        cell.add(insertIdx, entity);
    }

    // Get all entities in ascending order of layer
    // i.e. top layer goes first
    public List<Entity> getAllEntitiesInCell() {
        return cell;
    }
}
