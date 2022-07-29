package dungeonmania.DungeonObjects.Entities.LogicEntities;

import dungeonmania.DungeonObjects.Entities.Entity;
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
        return logic;
    }

    public boolean isActive() {
        if (logic == null) {
            return false;
        }

        switch (logic){
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
        return false;
    }

    private boolean isActiveOr() {
        return false;
    }

    private boolean isActiveXor() {
        return false;
    }

    private boolean isActiveCoAnd() {
        return false;
    }

}
