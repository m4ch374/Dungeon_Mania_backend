package dungeonmania.DungeonObjects.Entities.Collectables;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.ICollectable;
import dungeonmania.Interfaces.IEquipment;
import dungeonmania.response.models.ItemResponse;

public class Key extends Entity implements ICollectable, IEquipment {
    private String keyId;

    @Override
    public ItemResponse toItemResponse() {return null;}

    @Override
    public void collectedBy(Entity collector) {}
    
}
