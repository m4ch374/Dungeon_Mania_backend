package dungeonmania.DungeonObjects;

import java.util.HashMap;
import java.util.Map;

// A list of types
public enum EntityTypes {
    PLAYER("player"),
    WALL("wall"),
    EXIT("exit"),
    BOULDER("boulder"),
    FLOOR_SWITCH("switch"),
    DOOR("door"),
    PORTAL("portal"),
    ZOMBIE_TOAST_SPAWNER("zombie_toast_spawner"),
    SPIDER("spider"),
    ZOMBIE_TOAST("zombie_toast"),
    MERCENARY("mercenary"),
    TREASURE("treasure"),
    KEY("key"),
    INVINCIBILITY_POTION("invincibility_potion"),
    INVISIBILITY_POTION("invisibility_potion"),
    WOOD("wood"),
    ARROWS("arrow"),
    BOMB("bomb"),
    SWORD("sword"),
    BOW("bow"),
    SHIELD("shield"),

    // Player behavior definition
    PLAYERMOVE("player_move"),
    PLAYERMAKE("player_make"),
    PLAYERUSE("player_use");

    // Map of value to enum
    private static final Map<String, EntityTypes> map = new HashMap<String, EntityTypes>();
    static {
        for (EntityTypes e : values())
            map.put(e.TYPE, e);
    }

    private final String TYPE;

    private EntityTypes(String TYPE) {
        this.TYPE = TYPE;
    }

    public static EntityTypes lookupEnum(String type) {
        return map.get(type);
    }

    @Override
    public String toString() {
        return TYPE;
    }
}
