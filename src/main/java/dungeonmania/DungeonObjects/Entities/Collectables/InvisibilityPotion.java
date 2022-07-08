package dungeonmania.DungeonObjects.Entities.Collectables;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.ICollectable;
import dungeonmania.Interfaces.IEquipment;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.DungeonFactory.EntityStruct;

public class InvisibilityPotion extends Entity implements ICollectable, IEquipment {
    private int duration;

    public InvisibilityPotion(EntityStruct metaData, JSONObject config) {
        super(metaData);
        this.duration = config.getInt("invisibility_potion_duration");
    }

    @Override
    public void collectedBy(Entity collector) { }

    @Override
    public ItemResponse toItemResponse() { return null; }
}
