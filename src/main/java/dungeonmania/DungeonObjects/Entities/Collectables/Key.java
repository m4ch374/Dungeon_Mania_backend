package dungeonmania.DungeonObjects.Entities.Collectables;

import dungeonmania.DungeonObjects.Player;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.ICollectable;
import dungeonmania.Interfaces.IEquipment;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.DungeonFactory.EntityStruct;

import static dungeonmania.DungeonObjects.EntityTypes.KEY;

import org.json.JSONObject;

public class Key extends Entity implements ICollectable, IEquipment {
    private final int keyId;

    public Key(EntityStruct metaData, int keyId) {
        super(metaData);
        this.keyId = keyId;
    }

    public int getKey() {
        return this.keyId;
    }

    @Override
    public ItemResponse toItemResponse() {
        return new ItemResponse(getId(), KEY.toString());
    }

    @Override
    public void collectedBy(Entity collector) throws InvalidActionException {
        if (collector instanceof Player) {
            Player player = (Player) collector;
            player.collect(this);
            getMap().removeEntity(this);
        }
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();

        obj.put("id", getId());
        obj.put("keyId", keyId);

        return obj;
    }
}
