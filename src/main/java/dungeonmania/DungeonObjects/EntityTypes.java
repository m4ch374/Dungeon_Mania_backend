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
    SHIELD("shield", -1),

    // Additional objects in milestone 3
    //
    // Note:
    // LIGHT_BULB_ON and OLDER_PLAYER may or may not be an entity depending on our implementation
    // Added just in case, compatibility-wise.
    ASSASSIN("assassin", 1),
    HYDRA("hydra", 1),
    SWAMP_TILE("swamp_tile", 5),
    SUNSTONE("sun_stone", 2),
    SCEPTRE("sceptre", -1),
    MIDNIGHTARMOUR("midnight_armour", -1),
    TIMETURNER("time_turner", 2),
    TIME_TRAVELLING_PORTAL("time_travelling_portal", 4),
    LIGHT_BULB_OFF("light_bulb_off", 3),
    LIGHT_BULB_ON("light_bulb_on", 3),
    WIRE("wire", 3),
    SWITCH_DOOR("switch_door", 3),
    OLDER_PLAYER("older_player", 0),

    // behavior definition
    PLAYERMOVE("player_move", -5),
    PLAYERMAKE("player_make", -5),
    PLAYERUSE("player_use", -5);

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
