package dungeonmania.DungeonObjects.Entities.Statics;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.IStaticInteractable;

public class Portal extends Entity implements IStaticInteractable {
    private String colour;

    @Override
    public void interactedBy(Entity interactor) { }
    
}
