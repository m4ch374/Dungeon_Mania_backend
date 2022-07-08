package dungeonmania.DungeonObjects.Entities.Collectables;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.ICollectable;
import dungeonmania.Interfaces.IEquipment;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.DungeonFactory.EntityStruct;

public class Bomb extends Entity implements ICollectable, IEquipment {

    private int bombRadius;

    public Bomb(EntityStruct metaData, JSONObject config) {
        super(metaData);
        this.bombRadius = config.getInt("bomb_radius");
    }

    @Override
    public void collectedBy(Entity collector) {}

    @Override
    public ItemResponse toItemResponse() { return null; }
}
