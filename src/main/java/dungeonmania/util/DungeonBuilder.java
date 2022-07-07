package dungeonmania.util;

import dungeonmania.DungeonObjects.DungeonState;

public class DungeonBuilder {
    public static DungeonState buildDungeon(String dungeonName, String configName) {
        return new DungeonState(new DungeonBuilder());
    }
}
