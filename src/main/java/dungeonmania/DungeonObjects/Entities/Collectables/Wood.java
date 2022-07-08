package dungeonmania.DungeonObjects.Entities.Collectables;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.ICollectable;
import dungeonmania.Interfaces.IEquipment;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.DungeonFactory.EntityStruct;

public class Wood extends Entity implements ICollectable, IEquipment {

    public Wood(EntityStruct metaData) {
        super(metaData);
    }

    @Override
    public ItemResponse toItemResponse() {return null;}

    @Override
    public void collectedBy(Entity collector) {}
    
}
