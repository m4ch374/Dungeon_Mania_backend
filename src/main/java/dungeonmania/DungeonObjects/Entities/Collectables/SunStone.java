package dungeonmania.DungeonObjects.Entities.Collectables;

import dungeonmania.DungeonObjects.EntityTypes;
import dungeonmania.DungeonObjects.Player;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.ICollectable;
import dungeonmania.Interfaces.IEquipment;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.DungeonFactory.EntityStruct;

public class SunStone extends Entity implements ICollectable, IEquipment {

    public SunStone(EntityStruct metaData) {
        super(metaData);
    }

    @Override
    public String getId() {
        return super.getId();
    }

    @Override
    public void collectedBy(Entity collector) throws InvalidActionException {
        if (collector instanceof Player) {
            Player player = (Player) collector;
            player.collect(this);
            getMap().removeEntity(this);
        }
    }

    @Override
    public ItemResponse toItemResponse() {
        return new ItemResponse(getId(), EntityTypes.SUNSTONE.toString());
    }
}
