package dungeonmania.DungeonObjects.Entities.LogicEntities.Statics;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.DungeonObjects.Entities.Statics.Boulder;
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
    public void interactedBy(Entity interactor) throws InvalidActionException {}
}
