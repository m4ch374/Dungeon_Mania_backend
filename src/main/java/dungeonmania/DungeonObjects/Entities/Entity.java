package dungeonmania.DungeonObjects.Entities;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.DungeonObjects.EntityTypes;
import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.DungeonObjects.Entities.Statics.FloorSwitch;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;
import dungeonmania.util.DungeonFactory.EntityStruct;

public class Entity {
    private String id;
    private String type;

    private DungeonMap map;

    public Entity(EntityStruct metaData) {
        id = metaData.getId();
        type = metaData.getType();
        map = metaData.getMap();
    }

    protected String getId() {
        return this.id;
    }

    protected DungeonMap getMap() {
        return this.map;
    }

    protected String getType() {
        return this.type;
    }

    protected boolean userCloseActiveSwitch(int x, int y) {
        Position up = new Position(x - 1, y);
        Position down = new Position(x + 1, y);
        Position left = new Position(x, y - 1);
        Position right = new Position(x, y + 1);

        List<Entity> entityList = new ArrayList<Entity>();
        entityList.addAll(map.getEntitiesAt(up));
        entityList.addAll(map.getEntitiesAt(down));
        entityList.addAll(map.getEntitiesAt(left));
        entityList.addAll(map.getEntitiesAt(right));

        boolean activeSwitch = entityList
                                .stream()
                                .filter(e -> e.getType().equals(EntityTypes.FLOOR_SWITCH.toString()))
                                .map(e -> (FloorSwitch) e)
                                .anyMatch(e -> e.isActive());

        return activeSwitch;
    }

    public EntityResponse toEntityResponse() {
        return new EntityResponse(id, type, map.getEntityPos(this), false);
    }
}
