package dungeonmania.Interfaces;

import org.json.JSONObject;

public interface ISpawnable {
    public void spawn(JSONObject config, int currTick);
}
