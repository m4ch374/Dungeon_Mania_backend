package dungeonmania.util.Tracker;

import java.util.HashMap;
import java.util.Map;

public enum GoalTypes {
    TREASURE("treasure"),
    ENEMIES("enemies"),
    BOULDERS("boulders"),
    EXIT("exit"),
    AND("AND"),
    OR("OR");

    private static final Map<String, GoalTypes> map = new HashMap<String, GoalTypes>();
    static {
        for (GoalTypes e : values())
            map.put(e.TYPE, e);
    }

    private final String TYPE;

    private GoalTypes(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getType() {
        return TYPE;
    }

    public String getGoalString() {
        return ":" + TYPE;
    }

    public static GoalTypes lookup(String type) {
        return map.get(type);
    }
}
