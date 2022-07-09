package dungeonmania.DungeonObjects.Entities;

import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.response.models.EntityResponse;
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

    public EntityResponse toEntityResponse() {
        return new EntityResponse(id, type, map.getEntityPos(this), false);
    }
}
