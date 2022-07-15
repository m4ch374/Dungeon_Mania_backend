package dungeonmania.DungeonObjects;

import java.util.HashMap;
import java.util.Map;

// A list of types with their layer values
// Assumes BOW and SHIELD does not get added to the map
public enum EntityTypes {
    PLAYER("player", 0),
    WALL("wall", 4),
    EXIT("exit", 4),
    BOULDER("boulder", 3),
    FLOOR_SWITCH("switch", 4),
    DOOR("door", 3),
    PORTAL("portal", 4),
    ZOMBIE_TOAST_SPAWNER("zombie_toast_spawner", 3),
    SPIDER("spider", 1),
    ZOMBIE_TOAST("zombie_toast", 1),
    MERCENARY("mercenary", 1),
    TREASURE("treasure", 2),
    KEY("key", 2),
    INVINCIBILITY_POTION("invincibility_potion", 2),
    INVISIBILITY_POTION("invisibility_potion", 2),
    WOOD("wood", 2),
    ARROWS("arrow", 2),
    BOMB("bomb", 2),
    SWORD("sword", 2),
    BOW("bow", -1),
    SHIELD("shield", -1);

    // Map of value to enum
    private static final Map<String, EntityTypes> map = new HashMap<String, EntityTypes>();
    static {
        for (EntityTypes e : values())
            map.put(e.TYPE, e);
    }

    private final String TYPE;
    private final int LAYER;

    private EntityTypes(String TYPE, int LAYER) {
        this.TYPE = TYPE;
        this.LAYER = LAYER;
    }

    public static EntityTypes lookupEnum(String type) {
        return map.get(type);
    }

    public int getLayer() {
        return LAYER;
    }

    @Override
    public String toString() {
        return TYPE;
    }
}
