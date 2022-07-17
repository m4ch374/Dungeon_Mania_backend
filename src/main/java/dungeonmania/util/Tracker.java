package dungeonmania.util;

import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.EntityTypes;
import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.DungeonObjects.Entities.Statics.Boulder;

// Its the last day so this one's gonna be very botched
public class Tracker {
    private String goalsToBeCompleted;
    private int treasureGoal;
    private int enemyGoal;

    private int currTreasure = 0;
    private int currEnemy = 0;
    private boolean steppedOnExit = false;

    public Tracker(JSONObject rawGoals, JSONObject config) {
        treasureGoal = config.getInt("treasure_goal");
        enemyGoal = config.getInt("enemy_goal");

        goalsToBeCompleted = ":" + rawGoals.getString("goal");
    }

    public void notifyTreasure() {
        currTreasure += 1;
    }

    public void notifyEnemy() {
        currEnemy += 1;
    }

    public void notifyExits() {
        steppedOnExit = true;
    }

    public String getUnfinishedGoals(DungeonMap map) {
        if (goalFinished(map))
            return "";
        else
            return goalsToBeCompleted;
    }

    private boolean goalFinished(DungeonMap map) {
        switch (goalsToBeCompleted) {
            case ":treasure":
                return treasureAchieved();

            case ":enemy":
                return enemyAchieved();

            case ":boulders":
                List<Boulder> boulders = map.getAllEntities().stream()
                                            .filter(e -> e.getType().equals(EntityTypes.BOULDER.toString()))
                                            .map(e -> (Boulder) e)
                                            .collect(Collectors.toList());

                for (Boulder b : boulders) {
                    if (!b.overlappedWithSwitch())
                        return false;
                }
                return true;

            case ":exit":
                return steppedOnExit;

            default:
                return false;
        }
    }

    private boolean treasureAchieved() {
        return currTreasure >= treasureGoal;
    }

    private boolean enemyAchieved() {
        return currEnemy >= enemyGoal;
    }
}
