package dungeonmania.DungeonObjects.Entities.Collectables;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.ICollectable;
import dungeonmania.Interfaces.IEquipment;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.DungeonFactory.EntityStruct;

public class InvincibilityPotion extends Entity implements ICollectable, IEquipment {
    private int duration;

    public InvincibilityPotion(EntityStruct metaData, JSONObject config) {
        super(metaData);
        this.duration = config.getInt("invincibility_potion_duration");
    }

    @Override
    public void collectedBy(Entity collector) { }

    @Override
    public ItemResponse toItemResponse() { return null; }
}
