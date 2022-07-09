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

    private int quantity;

    public Arrow(EntityStruct metaData) {
        super(metaData);
        this.quantity = 1;
    }

    public Arrow(EntityStruct metaData, int quantity) {
        super(metaData);
        this.quantity = quantity;
    }

    public int getQuantity() {
        return this.quantity;
    }

    @Override
    public ItemResponse toItemResponse() {
        return new ItemResponse(getId(), ARROWS.toString());
    }

    @Override
    public void collectedBy(Entity collector) {
        if (collector instanceof Player) {
            Player player = (Player) collector;
            try {
                player.collect(this);
                getMap().removeEntity(this);
            } catch (InvalidActionException e) {
                // do nothing
                // should never fail
            }
        }
    }
}
