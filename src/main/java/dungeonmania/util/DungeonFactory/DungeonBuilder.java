package dungeonmania.util.DungeonFactory;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.DungeonObjects.DungeonState;
import dungeonmania.DungeonObjects.EntityTypes;
import dungeonmania.DungeonObjects.Player;
import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.DungeonObjects.Entities.Statics.Exit;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;
import dungeonmania.util.DungeonFactory.DungeonMazeFactory.MazeGenMetadata;
import dungeonmania.util.DungeonFactory.DungeonMazeFactory.MazeGenerator;
import dungeonmania.util.Tracker.Tracker;

public class DungeonBuilder {
    private String dungeonId = "Stubbed";
    private String dungeonName;
    private Player player;
    private DungeonMap map;
    private Tracker tracker;

    private JSONArray dungeonEntities;
    private JSONObject dungeonGoals;
    private JSONObject configJson;

    public String getDungeonId() {
        return dungeonId;
    }

    public String getDungeonName() {
        return dungeonName;
    }

    public Player getPlayer() {
        return player;
    }

    public DungeonMap getMap() {
        return map;
    }

    public Tracker getTracker() {
        return tracker;
    }

    public JSONObject getConfig() {
        return configJson;
    }

    public DungeonBuilder useMap(String dungeonName) throws IOException {
        setDungeonJson(dungeonName);
        return this;
    }

    public DungeonBuilder withConfig(String configName) throws IOException {
        setConfigJson(configName);
        return this;
    }

    public DungeonState build() throws Exception {
        tracker = new Tracker(dungeonGoals, configJson);
        map = DungeonMapParser.buildDungeonMap(dungeonEntities, configJson, tracker);
        player = (Player) map.getAllEntities().stream().filter(e -> e instanceof Player).findFirst().get();

        return new DungeonState(this);
    }

    public DungeonState generateDungeon(MazeGenMetadata metadata) throws Exception {
        dungeonGoals = new JSONObject();
        dungeonGoals.put("goal", "exit");
        tracker = new Tracker(dungeonGoals, configJson);

        // Generate maze
        map = MazeGenerator.initGenerator(metadata).generateMazeMap();

        // Add player to maze
        EntityStruct playerMetaData = new EntityStruct("player", EntityTypes.PLAYER.toString(), map);
        player = new Player(playerMetaData, configJson, tracker);
        map.placeEntityAt(player, new Position(metadata.getxStart(), metadata.getyStart()));

        // Add exit to maze
        EntityStruct exitMetaData = new EntityStruct("exit", EntityTypes.EXIT.toString(), map);
        Exit exit = new Exit(exitMetaData, tracker);
        map.placeEntityAt(exit, new Position(metadata.getxEnd(), metadata.getyEnd()));

        return new DungeonState(this);
    }

    private void setConfigJson(String configName) throws IOException {
        if (this.dungeonName == null)
            this.dungeonName = configName;

        String configContent = FileLoader.loadResourceFile("/configs/" + configName + ".json");
        configJson = new JSONObject(configContent);
    }

    private void setDungeonJson(String dungeonName) throws IOException {
        this.dungeonName = dungeonName;

        String dungeonCotent = FileLoader.loadResourceFile("/dungeons/" + dungeonName + ".json");
        JSONObject dungeonJsonMap = new JSONObject(dungeonCotent);

        dungeonEntities = dungeonJsonMap.getJSONArray("entities");
        dungeonGoals = dungeonJsonMap.getJSONObject("goal-condition");
    }
    
    public static DungeonBuilder initializeBuilder() {
        return new DungeonBuilder();
    }
}
