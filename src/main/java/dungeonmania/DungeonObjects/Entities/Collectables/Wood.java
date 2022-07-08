package dungeonmania.DungeonObjects.Entities.Collectables;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.ICollectable;
import dungeonmania.Interfaces.IEquipment;
import dungeonmania.response.models.ItemResponse;

public class Wood extends Entity implements ICollectable, IEquipment {

    @Override
    public ItemResponse toItemResponse() {return null;}

    @Override
    public void collectedBy(Entity collector) {}
    
}
