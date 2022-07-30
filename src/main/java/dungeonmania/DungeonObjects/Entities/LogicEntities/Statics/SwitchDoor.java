package dungeonmania.DungeonObjects.Entities.LogicEntities.Statics;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.DungeonObjects.Entities.Statics.Door;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.DungeonFactory.EntityStruct;

public class SwitchDoor extends Door {

    public SwitchDoor(EntityStruct metaData, int keyId, String logic) {
        super(metaData, keyId, logic);
    }

    @Override
    public boolean isActive() {
        return super.isActive();
    }

    @Override
    public String getLogic() {
        return super.getLogic();
    }

    @Override
    public void interactedBy(Entity interactor) throws InvalidActionException {
        if (isActive()) {
            return;
        }

        try {
            super.interactedBy(interactor);
        } catch (InvalidActionException e) {
            throw new InvalidActionException("ERROR: Cannot open this SwitchDoor");
        }
    }
}
