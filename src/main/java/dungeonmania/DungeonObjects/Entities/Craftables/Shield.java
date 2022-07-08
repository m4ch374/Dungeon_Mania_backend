package dungeonmania.DungeonObjects.Entities.Craftables;

import dungeonmania.Interfaces.IEquipment;
import dungeonmania.response.models.ItemResponse;

import static dungeonmania.DungeonObjects.EntityTypes.SHIELD;

import org.json.JSONObject;

public class Shield implements IEquipment {
    public static final String TYPE = SHIELD;

    private int durability;
    private int defense;

    public Shield(JSONObject config) {
        this.durability = config.getInt("shield_durability");
        this.defense = config.getInt("shield_defence");
    }

    @Override
    public ItemResponse toItemResponse() { return null; }
}
