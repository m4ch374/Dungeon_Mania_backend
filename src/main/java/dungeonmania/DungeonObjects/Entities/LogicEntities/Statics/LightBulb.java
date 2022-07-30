package dungeonmania.DungeonObjects.Entities.LogicEntities.Statics;

import dungeonmania.DungeonObjects.EntityTypes;
import dungeonmania.DungeonObjects.Entities.LogicEntities.LogicEntity;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.DungeonFactory.EntityStruct;

public class LightBulb extends LogicEntity {

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

    @Override
    public EntityResponse toEntityResponse() {
        if (isActive()) {
            return new EntityResponse(getId(), EntityTypes.LIGHT_BULB_ON.toString(), getMap().getEntityPos(this), false);
        }
        return new EntityResponse(getId(), EntityTypes.LIGHT_BULB_OFF.toString(), getMap().getEntityPos(this), false);
    }
}
