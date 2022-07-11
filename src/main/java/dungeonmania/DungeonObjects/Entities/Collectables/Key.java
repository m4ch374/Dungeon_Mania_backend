package dungeonmania.DungeonObjects.Entities.Collectables;

import dungeonmania.DungeonObjects.Player;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.ICollectable;
import dungeonmania.Interfaces.IEquipment;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.DungeonFactory.EntityStruct;

import static dungeonmania.DungeonObjects.EntityTypes.KEY;

public class Key extends Entity implements ICollectable, IEquipment {
    private final int keyId;

    public Key(EntityStruct metaData, int keyId) {
        super(metaData);
        this.keyId = keyId;
    }

    @Override
    public String getId() {
        return super.getId();
    }

    public int getKey() {
        return this.keyId;
    }

    @Override
    public ItemResponse toItemResponse() {
        return new ItemResponse(getId(), KEY.toString());
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
            }
        }
    }
}
