package dungeonmania.DungeonObjects.DungeonMap;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.util.Position;

public class DungeonMap {
    List<List<DungeonCell>> map = new ArrayList<List<DungeonCell>>();

    public List<Entity> getEntitiesAt(Position pos) { return null; }

    public Position getEntityPos(Entity entity) { return null; }

    public void removeEntity(Entity entity) {}

    public void placeEntityAt(Entity entity, Position pos) {}

    public void moveEntityTo(Entity entity, Position pos) {}
}
