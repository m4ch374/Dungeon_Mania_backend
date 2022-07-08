package dungeonmania.util.DungeonFactory;

import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;

public class EntityStruct {
    private final String id;
    private final String type;
    private final DungeonMap map;

    public EntityStruct(String id, String type, DungeonMap map) {
        this.id = id;
        this.type = type;
        this.map = map;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public DungeonMap getMap() {
        return map;
    }
}
