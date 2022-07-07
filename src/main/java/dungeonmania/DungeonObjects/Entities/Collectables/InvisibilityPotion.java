package dungeonmania.DungeonObjects.Entities.Collectables;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.ICollectable;
import dungeonmania.Interfaces.IEquipment;
import dungeonmania.response.models.ItemResponse;

public class InvisibilityPotion extends Entity implements ICollectable, IEquipment {
    private int duration;

    @Override
    public void collectedBy(Entity collector) { }

    @Override
    public ItemResponse toItemResponse() { return null; }
}
