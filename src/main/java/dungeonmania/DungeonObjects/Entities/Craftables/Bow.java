package dungeonmania.DungeonObjects.Entities.Craftables;

import dungeonmania.Interfaces.IEquipment;
import dungeonmania.response.models.ItemResponse;

import static dungeonmania.DungeonObjects.EntityTypes.BOW;

import org.json.JSONObject;

public class Bow implements IEquipment {
    public static final String TYPE = BOW;

    private int durability;

    public Bow(JSONObject config) {
        this.durability = config.getInt("bow_durability");
    }

    @Override
    public ItemResponse toItemResponse() { return null; }
}
