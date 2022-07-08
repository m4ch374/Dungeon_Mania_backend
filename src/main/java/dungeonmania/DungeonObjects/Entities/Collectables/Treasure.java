package dungeonmania.DungeonObjects.Entities.Collectables;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.ICollectable;
import dungeonmania.Interfaces.IEquipment;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.DungeonFactory.EntityStruct;

public class Treasure extends Entity implements ICollectable, IEquipment {

    public Treasure(EntityStruct metaData) {
        super(metaData);
    }

    @Override
    public ItemResponse toItemResponse() { return null; }

    @Override
    public void collectedBy(Entity collector) {}
    
}
