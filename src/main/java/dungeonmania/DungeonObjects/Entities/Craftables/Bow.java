package dungeonmania.DungeonObjects.Entities.Craftables;

import dungeonmania.Interfaces.IEquipment;
import dungeonmania.response.models.ItemResponse;

import static dungeonmania.DungeonObjects.EntityTypes.BOW;

public class Bow implements IEquipment {
    public static final String TYPE = BOW;

    private int durability;

    @Override
    public ItemResponse toItemResponse() { return null; }
}
