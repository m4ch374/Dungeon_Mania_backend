package dungeonmania.util;

import org.json.JSONObject;

public class Tracker {
    private String goalsToBeCompleted;
    private JSONObject rawGoals;
    private int treasureGoal;
    private int enemyGoal;

    public Tracker(JSONObject rawGoals, JSONObject config) {
        this.rawGoals = rawGoals;
        treasureGoal = config.getInt("treasure_goal");
        enemyGoal = config.getInt("enemy_goal");
    }

    public String getUnfinishedGoals() {
        return goalsToBeCompleted;
    }
}
