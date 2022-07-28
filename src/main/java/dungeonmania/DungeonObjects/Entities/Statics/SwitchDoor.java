package dungeonmania.DungeonObjects.Entities.Statics;

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
}
