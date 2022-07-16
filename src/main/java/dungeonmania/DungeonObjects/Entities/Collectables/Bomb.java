package dungeonmania.DungeonObjects.Entities.Collectables;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.Player;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.ICollectable;
import dungeonmania.Interfaces.IEquipment;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;
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
    public void collectedBy(Entity collector) throws InvalidActionException {
        if (collector instanceof Player) {
            Player player = (Player) collector;
            player.collect(this);
            getMap().removeEntity(this);
        }
    }

    // Destroy entities within range but not player
    public void activate(Position pos) {
        int x = pos.getX();
        int y = pos.getY();

        for (int i = x - bombRadius; i <= x + bombRadius; i++) {
            for (int j = y - bombRadius; j <= y + bombRadius; j++) {
                Position p = new Position(i, j);
                if (i == x && j == y) {
                    getMap().removeAtPosExceptPlayer(p);
                } else {
                    getMap().removeAllAtPos(p);
                }
            }
        }
    }
}
