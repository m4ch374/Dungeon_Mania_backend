package dungeonmania.DungeonObjects.Entities.Collectables;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.Player;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.ICollectable;
import dungeonmania.Interfaces.IEquipment;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.DungeonFactory.EntityStruct;

import static dungeonmania.DungeonObjects.EntityTypes.BOMB;

public class Bomb extends Entity implements ICollectable, IEquipment {

    private int bombRadius;
    private boolean collectible = true;

    public Bomb(EntityStruct metaData, JSONObject config) {
        super(metaData);
        this.bombRadius = config.getInt("bomb_radius");
    }

    @Override
    public String getId() {
        return super.getId();
    }

    public int getBombRadius() {
        return this.bombRadius;
    }

    public void drop() {
        this.collectible = false;
    }

    public boolean isCollectible() {
        return this.collectible;
    }

    @Override
    public ItemResponse toItemResponse() {
        return new ItemResponse(getId(), BOMB.toString());
    }

    @Override
    public void collectedBy(Entity collector) {
        if (collector instanceof Player) {
            Player player = (Player) collector;
            try {
                player.collect(this);
                getMap().removeEntity(this);
            } catch (InvalidActionException e) {
                // should never happened
                // user's move already stop before this
                // refer to Player.java line 157 - ableToMove()
            }
        }
    }
}
