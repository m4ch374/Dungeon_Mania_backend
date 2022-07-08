package dungeonmania.DungeonObjects.Entities.Statics;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.IStaticInteractable;

public class Door extends Entity implements IStaticInteractable {
    private String keyId;

    @Override
    public void interactedBy(Entity interactor) { }
    
}
