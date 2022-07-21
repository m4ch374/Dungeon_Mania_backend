package dungeonmania.DungeonObjects.Entities.Collectables;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.ICollectable;
import dungeonmania.Interfaces.IEquipment;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.DungeonFactory.EntityStruct;

import static dungeonmania.DungeonObjects.EntityTypes.TREASURE;

import dungeonmania.DungeonObjects.Player;

public class Treasure extends Entity implements ICollectable, IEquipment {

    public Treasure(EntityStruct metaData) {
        super(metaData);
    }

    @Override
    public String getId() {
        return super.getId();
    }

    @Override
    public ItemResponse toItemResponse() {
        return new ItemResponse(getId(), TREASURE.toString());
    }

    @Override
    public void collectedBy(Entity collector) throws InvalidActionException {
        if (collector instanceof Player) {
            Player player = (Player) collector;
            player.collect(this);
            player.notifyTrackerCollectedTreasure();
            getMap().removeEntity(this);
        }
    }
}
