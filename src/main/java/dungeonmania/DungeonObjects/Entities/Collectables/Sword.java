package dungeonmania.DungeonObjects.Entities.Collectables;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.Player;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.ICollectable;
import dungeonmania.Interfaces.IEquipment;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.DungeonFactory.EntityStruct;

import static dungeonmania.DungeonObjects.EntityTypes.SWORD;

public class Sword extends Entity implements ICollectable, IEquipment {

    private int durability;

    public Sword(EntityStruct metaData, JSONObject config) {
        super(metaData);
        this.durability = config.getInt("sword_durability");
    }

    public int getDurability() {
        return this.durability;
    }

    public void reduceDurability(int times) {
        this.durability -= times;
    }

    public String getId() {
        return super.getId();
    }

    @Override
    public ItemResponse toItemResponse() {
        return new ItemResponse(getId(), SWORD.toString());
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
