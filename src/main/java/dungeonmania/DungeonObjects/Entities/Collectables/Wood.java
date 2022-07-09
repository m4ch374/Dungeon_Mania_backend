package dungeonmania.DungeonObjects.Entities.Collectables;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.ICollectable;
import dungeonmania.Interfaces.IEquipment;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.DungeonFactory.EntityStruct;

import static dungeonmania.DungeonObjects.EntityTypes.WOOD;

import dungeonmania.DungeonObjects.Player;

public class Wood extends Entity implements ICollectable, IEquipment {

    private int quantity;

    public Wood(EntityStruct metaData) {
        super(metaData);
        this.quantity = 1;
    }

    public Wood(EntityStruct metaData, int quantity) {
        super(metaData);
        this.quantity = quantity;
    }

    @Override
    public String getId() {
        return super.getId();
    }

    public int getQuantity() {
        return this.quantity;
    }

    @Override
    public ItemResponse toItemResponse() {
        return new ItemResponse(getId(), WOOD.toString());
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
