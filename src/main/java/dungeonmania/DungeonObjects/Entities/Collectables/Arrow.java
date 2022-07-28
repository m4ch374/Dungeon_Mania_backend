package dungeonmania.DungeonObjects.Entities.Collectables;

import dungeonmania.DungeonObjects.Player;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.ICollectable;
import dungeonmania.Interfaces.IEquipment;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.DungeonFactory.EntityStruct;

import static dungeonmania.DungeonObjects.EntityTypes.ARROWS;

public class Arrow extends Entity implements ICollectable, IEquipment {

    public Arrow(EntityStruct metaData) {
        super(metaData);
    }

    @Override
    public ItemResponse toItemResponse() {
        return new ItemResponse(getId(), ARROWS.toString());
    }

    @Override
    public void collectedBy(Entity collector) throws InvalidActionException {
        if (collector instanceof Player) {
            Player player = (Player) collector;
            player.collect(this);
            getMap().removeEntity(this);
        }
    }
}
