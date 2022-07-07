package dungeonmania.DungeonObjects.Entities.Craftables;

import dungeonmania.Interfaces.IEquipment;
import dungeonmania.response.models.ItemResponse;

import static dungeonmania.DungeonObjects.EntityTypes.SHIELD;

public class Shield implements IEquipment {
    public static final String TYPE = SHIELD;

    private int durability;
    private int defense;

    @Override
    public ItemResponse toItemResponse() { return null; }
}
