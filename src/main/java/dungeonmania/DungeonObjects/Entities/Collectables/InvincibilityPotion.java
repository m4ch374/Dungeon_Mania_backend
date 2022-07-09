package dungeonmania.DungeonObjects.Entities.Collectables;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.Player;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.ICollectable;
import dungeonmania.Interfaces.IEquipment;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.DungeonFactory.EntityStruct;

import static dungeonmania.DungeonObjects.EntityTypes.INVINCIBILITY_POTION;

public class InvincibilityPotion extends Entity implements ICollectable, IEquipment {

    public InvincibilityPotion(EntityStruct metaData, JSONObject config) {
        super(metaData);
    }

    @Override
    public String getId() {
        return super.getId();
    }

    @Override
    public ItemResponse toItemResponse() {
        return new ItemResponse(getId(), INVINCIBILITY_POTION.toString());
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
