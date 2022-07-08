package dungeonmania.DungeonObjects.Entities.Statics;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.IStaticInteractable;
import dungeonmania.util.DungeonFactory.EntityStruct;

public class FloorSwitch extends Entity implements IStaticInteractable {

    public FloorSwitch(EntityStruct metaData) {
        super(metaData);
    }

    @Override
    public void interactedBy(Entity interactor) {}
    
}
