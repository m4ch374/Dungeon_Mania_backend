package dungeonmania.DungeonObjects.Entities.Collectables;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.Player;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.ICollectable;
import dungeonmania.Interfaces.IEquipment;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.DungeonFactory.EntityStruct;

import static dungeonmania.DungeonObjects.EntityTypes.INVISIBILITY_POTION;;

public class InvisibilityPotion extends Entity implements ICollectable, IEquipment {
    private int duration;

    public InvisibilityPotion(EntityStruct metaData, JSONObject config) {
        super(metaData);
        this.duration = config.getInt("invisibility_potion_duration");
    }

    @Override
    public ItemResponse toItemResponse() {
        return new ItemResponse(getId(), INVISIBILITY_POTION.toString());
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
