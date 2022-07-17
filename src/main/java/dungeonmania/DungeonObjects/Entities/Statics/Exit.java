package dungeonmania.DungeonObjects.Entities.Statics;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.IStaticInteractable;
import dungeonmania.util.Tracker;
import dungeonmania.util.DungeonFactory.EntityStruct;
import dungeonmania.exceptions.*;

public class Exit extends Entity implements IStaticInteractable {

    Tracker tracker;

    public Exit(EntityStruct metaData, Tracker tracker) {
        super(metaData);
        this.tracker = tracker;
    }

    @Override
    public void interactedBy(Entity interactor) throws InvalidActionException {
        tracker.notifyExits();
    }
    
}
