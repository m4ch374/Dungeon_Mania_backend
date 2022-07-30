package dungeonmania.DungeonObjects.Entities.LogicEntities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.DungeonObjects.Entities.LogicEntities.Collectables.Bomb;
import dungeonmania.DungeonObjects.Entities.LogicEntities.Statics.FloorSwitch;
import dungeonmania.util.Position;
import dungeonmania.util.DungeonFactory.EntityStruct;

public abstract class LogicEntity extends Entity {

    private static final String and = "and";
    private static final String or = "or";
    private static final String xor = "xor";
    private static final String co_anc = "co_and";

    private final String logic;

    public LogicEntity(EntityStruct metaData) {
        super(metaData);
        this.logic = null;
    }

    public LogicEntity(EntityStruct metaData, String logic) {
        super(metaData);
        this.logic = logic;
    }

    public String getLogic() {
        return this.logic;
    }

    public boolean isActive() {
        if (this.logic == null) {   // as non-logic entity
            if (this instanceof Bomb) {
                return playerCloseActiveSwitch();
            }
            return false;
        }

        switch (this.logic){        // as logic entity
            case and:
                return isActiveAnd();
            case or:
                return isActiveOr();
            case xor:
                return isActiveXor();
            case co_anc:
                return isActiveCoAnd();
            default:
                return false;
        }
    }

    private boolean playerCloseActiveSwitch() {
        Position pos = getMap().getPlayerPos();

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

    private boolean isActiveAnd() {
        Position pos = getMap().getEntityPos(this);
        JSONObject josn = getMap().getAdjacentActive(pos);

        if (josn.getInt("switch_num") > 2 && josn.getBoolean("all_switch_is_avtive") && josn.getInt("active_num") >= 2) {
            return true;
        } else if (josn.getInt("switch_num") <= 2 && josn.getInt("active_num") >= 2) {
            return true;
        }

        return false;
    }

    private boolean isActiveOr() {
        Position pos = getMap().getEntityPos(this);
        JSONObject josn = getMap().getAdjacentActive(pos);

        if (josn.getInt("active_num") >= 1) {
            return true;
        }

        return false;
    }

    private boolean isActiveXor() {
        Position pos = getMap().getEntityPos(this);
        JSONObject josn = getMap().getAdjacentActive(pos);

        if (josn.getInt("active_num") == 1) {
            return true;
        }

        return false;
    }

    private boolean isActiveCoAnd() {
        Position pos = getMap().getEntityPos(this);
        JSONObject josn = getMap().getAdjacentActive(pos);

        if (josn.getInt("active_num") >= 1) {
            return true;
        }

        return false;
    }

}
