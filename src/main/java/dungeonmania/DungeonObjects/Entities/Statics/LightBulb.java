package dungeonmania.DungeonObjects.Entities.Statics;

import dungeonmania.DungeonObjects.Entities.LogicEntity;
import dungeonmania.util.DungeonFactory.EntityStruct;

public class LightBulb extends LogicEntity {

    public LightBulb(EntityStruct metaData) {
        super(metaData);
    }

    public LightBulb(EntityStruct metaData, String logic) {
        super(metaData, logic);
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
