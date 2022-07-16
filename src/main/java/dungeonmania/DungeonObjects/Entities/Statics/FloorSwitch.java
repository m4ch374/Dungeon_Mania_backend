package dungeonmania.DungeonObjects.Entities.Statics;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.IStaticInteractable;
import dungeonmania.util.Position;
import dungeonmania.util.DungeonFactory.EntityStruct;
import dungeonmania.exceptions.*;

public class FloorSwitch extends Entity implements IStaticInteractable {

    public FloorSwitch(EntityStruct metaData) {
        super(metaData);
    }

    public boolean isActive() {
        Position pos = getMap().getEntityPos(this);

        boolean isActive = getMap().getEntitiesAt(pos)
                                            .stream()
                                            .anyMatch(e -> (e instanceof Boulder));

        return isActive;
    }

    @Override
<<<<<<< HEAD
    public void interactedBy(Entity interactor) throws InvalidActionException {}
    
=======
    public void interactedBy(Entity interactor) {}
>>>>>>> master
}
