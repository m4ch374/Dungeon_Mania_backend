package dungeonmania.DungeonObjects.Entities.Statics;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.IStaticInteractable;
import dungeonmania.util.DungeonFactory.EntityStruct;

public class FloorSwitch extends Entity implements IStaticInteractable {

    private boolean active = false;

    public boolean isActive() {
        return this.active;
    }

    public FloorSwitch(EntityStruct metaData) {
        super(metaData);
    }

    @Override
    public void interactedBy(Entity interactor) {}
    
}
