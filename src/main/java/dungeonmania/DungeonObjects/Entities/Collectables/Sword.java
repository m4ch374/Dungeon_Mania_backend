package dungeonmania.DungeonObjects.Entities.Collectables;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.ICollectable;
import dungeonmania.Interfaces.IEquipment;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.DungeonFactory.EntityStruct;

public class Sword extends Entity implements ICollectable, IEquipment {
    private int attackDamage;
    private int durability;

    public Sword(EntityStruct metaData, JSONObject config) {
        super(metaData);
        this.attackDamage = config.getInt("sword_attack");
        this.durability = config.getInt("sword_durability");
    }

    @Override
    public ItemResponse toItemResponse() { return null; }

    @Override
    public void collectedBy(Entity collector) {}
    
}
