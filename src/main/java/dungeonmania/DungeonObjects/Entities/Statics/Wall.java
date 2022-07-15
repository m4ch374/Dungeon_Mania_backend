package dungeonmania.DungeonObjects.Entities.Statics;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.IStaticInteractable;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.DungeonFactory.EntityStruct;

public class Wall extends Entity implements IStaticInteractable {
    public Wall(EntityStruct metaData) {
        super(metaData);
    }

    @Override
    public void interactedBy(Entity interactor) throws InvalidActionException {
        throw new InvalidActionException("ERROR: Cannot cross a wall");
    }
}
