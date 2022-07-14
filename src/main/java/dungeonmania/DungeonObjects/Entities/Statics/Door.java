package dungeonmania.DungeonObjects.Entities.Statics;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.IStaticInteractable;
import dungeonmania.util.DungeonFactory.EntityStruct;
import dungeonmania.exceptions.*;

public class Door extends Entity implements IStaticInteractable {
    private int keyId;

    public Door(EntityStruct metaData, int keyId) {
        super(metaData);
        this.keyId = keyId;
    }

    @Override
    public void interactedBy(Entity interactor) throws InvalidActionException { }
    
}
