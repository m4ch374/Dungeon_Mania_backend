package dungeonmania.DungeonObjects.Entities.LogicEntities.Collectables;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.Player;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.DungeonObjects.Entities.LogicEntities.LogicEntity;
import dungeonmania.Interfaces.ICollectable;
import dungeonmania.Interfaces.IEquipment;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;
import dungeonmania.util.DungeonFactory.EntityStruct;

import static dungeonmania.DungeonObjects.EntityTypes.BOMB;

public class Bomb extends LogicEntity implements ICollectable, IEquipment {

    private int bombRadius;
    private boolean collectible = true;

    public Bomb(EntityStruct metaData, JSONObject config) {
        super(metaData);
        this.bombRadius = config.getInt("bomb_radius");
    }

    public Bomb(EntityStruct metaData, JSONObject config, String logic) {
        super(metaData, logic);
        this.bombRadius = config.getInt("bomb_radius");
    }

    @Override
    public boolean isActive() {
        return super.isActive();
    }

    public int getBombRadius() {
        return this.bombRadius;
    }

    public void drop() {
        this.collectible = false;

        Position pos = getMap().getPlayerPos();
        if (isActive()) {
            activate(pos);
        } else {
            getMap().placeEntityAt(this, pos);
        }
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
        Position playerPos = getMap().getPlayerPos();

        int x = pos.getX();
        int y = pos.getY();

        for (int i = x - bombRadius; i <= x + bombRadius; i++) {
            for (int j = y - bombRadius; j <= y + bombRadius; j++) {
                Position p = new Position(i, j);
                if (p.equals(playerPos)) {
                    getMap().removeAtPosExceptPlayer(p);
                } else {
                    getMap().removeAllAtPos(p);
                }
            }
        }
    }
}
