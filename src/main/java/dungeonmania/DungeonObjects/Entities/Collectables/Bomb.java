package dungeonmania.DungeonObjects.Entities.Collectables;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.Player;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.DungeonObjects.Entities.Statics.FloorSwitch;
import dungeonmania.Interfaces.ICollectable;
import dungeonmania.Interfaces.IEquipment;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;
import dungeonmania.util.DungeonFactory.EntityStruct;

import static dungeonmania.DungeonObjects.EntityTypes.BOMB;

import java.util.ArrayList;
import java.util.List;

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

        Position pos = getMap().getPlayerPos();
        if (playerCloseActiveSwitch(pos)) {
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

    private boolean playerCloseActiveSwitch(Position pos) {
        int x = pos.getX();
        int y = pos.getY();

        Position up = new Position(x - 1, y);
        Position down = new Position(x + 1, y);
        Position left = new Position(x, y - 1);
        Position right = new Position(x, y + 1);

        List<Entity> entityList = new ArrayList<Entity>();
        entityList.addAll(getMap().getEntitiesAt(up));
        entityList.addAll(getMap().getEntitiesAt(down));
        entityList.addAll(getMap().getEntitiesAt(left));
        entityList.addAll(getMap().getEntitiesAt(right));

        boolean activeSwitch = entityList
                                .stream()
                                .filter(e -> (e instanceof FloorSwitch))
                                .map(e -> (FloorSwitch) e)
                                .anyMatch(e -> e.isActive());

        return activeSwitch;
    }
}
