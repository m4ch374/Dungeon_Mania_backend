package dungeonmania.DungeonObjects.Entities.Collectables;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.ICollectable;
import dungeonmania.Interfaces.IEquipment;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.DungeonFactory.EntityStruct;

public class Key extends Entity implements ICollectable, IEquipment {
    private int keyId;

    public Key(EntityStruct metaData, int keyId) {
        super(metaData);
        this.keyId = keyId;
    }

    @Override
    public ItemResponse toItemResponse() {return null;}

    @Override
    public void collectedBy(Entity collector) {}
    
}
