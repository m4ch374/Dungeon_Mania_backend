package dungeonmania.DungeonObjects.Entities.Craftables;

import dungeonmania.Interfaces.IEquipment;
import dungeonmania.response.models.ItemResponse;

import static dungeonmania.DungeonObjects.EntityTypes.SHIELD;

public class Shield implements IEquipment {

    private int durability;
    private final String id;

    public Shield(String id, int durability) {
        this.id = id;
        this.durability = durability;
    }

    public int getDurability() {
        return this.durability;
    }

    public void reduceDurability(int times) {
        this.durability -= times;
    }

    @Override
    public ItemResponse toItemResponse() {
        return new ItemResponse(this.id, SHIELD.toString());
    }

    @Override
    public String getId() {
        return this.id;
    }
}
