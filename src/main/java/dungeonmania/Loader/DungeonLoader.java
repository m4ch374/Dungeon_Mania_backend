package dungeonmania.Loader;

import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.DungeonObjects.Player;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.util.Tracker.Tracker;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;



public class DungeonLoader {

    private String dungeonId;
    private String dungeonName;
    private Player player;
    private DungeonMap map;
    private Tracker tracker;
    private List<BattleResponse> battles = new ArrayList<BattleResponse>();
    private boolean successfulLoad = false;
    private JSONObject config;
    private File gameFile;
    private int currTick = 0;

    public DungeonLoader(String name){
        if (! getAllGames().contains(name)){
            return;
        }
        gameFile = new File("/saves/" + name + ".json");
        this.successfulLoad = true;
    }

    public void loadDungeon(){
        











    }


    public boolean getSuccessfulLoad(){
        return this.successfulLoad;
    }

    public String getDungeonId() {
        return this.dungeonId;
    }

    public String getDungeonName() {
        return this.dungeonName;
    }

    public Player getPlayer() {
        return this.player;
    }

    public DungeonMap getMap() {
        return this.map;
    }

    public Tracker getTracker() {
        return this.tracker;
    }

    public List<BattleResponse> getBattles() {
        return this.battles;
    }

    public JSONObject getConfig() {
        return this.config;
    }

    public int getCurrTick() {
        return this.currTick;
    }

    public static List<String> getAllGames(){
        final File SAVE_DIR = new File("/saves/");
        List<String> games = new ArrayList<String>();
        for (final File game : SAVE_DIR.listFiles()){
            String game_file = game.getName();
            games.add(game_file.split("\\.")[0]);
        }

        return games;
    }
}
