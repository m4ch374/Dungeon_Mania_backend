package dungeonmania.util.DungeonFactory;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.DungeonObjects.DungeonState;
import dungeonmania.DungeonObjects.Player;
import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Tracker;

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

    public DungeonState build() {
        tracker = new Tracker(dungeonGoals, configJson);
        map = DungeonMapParser.buildDungeonMap(dungeonEntities, configJson);
        player = (Player) map.getAllEntities().stream().filter(e -> e instanceof Player).findFirst().get();

        return new DungeonState(this);
    }

    private void setJsonObj(String dungeonName, String configName) throws IOException {
        String dungeonCotent = FileLoader.loadResourceFile("/dungeons/" + dungeonName + ".json");
        String configContent = FileLoader.loadResourceFile("/configs/" + configName + ".json");

        JSONObject dungeonJsonMap = new JSONObject(dungeonCotent);

        dungeonEntities = dungeonJsonMap.getJSONArray("entities");
        dungeonGoals = dungeonJsonMap.getJSONObject("goal-condition");
        configJson = new JSONObject(configContent);
    }

    public static DungeonBuilder setConfig(String dungeonName, String configName) throws IOException {
        DungeonBuilder builder = new DungeonBuilder();
        builder.setJsonObj(dungeonName, configName);
        builder.dungeonName = dungeonName;

        return builder;
    }
}
