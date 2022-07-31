package dungeonmania.DungeonObjects.Entities;

import org.json.JSONObject;

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

    protected DungeonMap getMap() {
        return this.map;
    }

    public String getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public EntityResponse toEntityResponse() {
        return new EntityResponse(id, type, map.getEntityPos(this), false);
    }

    public JSONObject encode(){
        JSONObject obj = new JSONObject();
        obj.put("x", map.getEntityPos(this).getX());
        obj.put("y", map.getEntityPos(this).getY());
        obj.put("layer", map.getEntityPos(this).getLayer());
        obj.put("type", this.getType());
        return obj;
    }

}
