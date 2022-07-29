package dungeonmania.DungeonObjects.Entities.LogicEntities;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.Entities.Entity;
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
        if (this.logic == null) {
            return false;
        }

        switch (this.logic){
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
