package dungeonmania.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.DungeonObjects.EntityTypes;
import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.DungeonObjects.Entities.Statics.Boulder;

// Its the last day so this one's gonna be very botched
public class Tracker {
    private static final String AND = "AND";
    private static final String OR = "OR";
    private static final List<String> superGoals = Arrays.asList(new String[] {AND, OR});

    private String currGoal;
    private int treasureGoal;
    private int enemyGoal;

    private int currTreasure = 0;
    private int currEnemy = 0;
    private boolean steppedOnExit = false;

    Tracker leftChild = null;
    Tracker rightChild = null;

    public Tracker(JSONObject rawGoals, JSONObject config) {
        treasureGoal = config.getInt("treasure_goal");
        enemyGoal = config.getInt("enemy_goal");

        // Starts to get ugly
        String goal = rawGoals.getString("goal");
        if (superGoals.contains(goal)) {
            currGoal = goal;

            JSONArray subgoals = rawGoals.getJSONArray("subgoals");
            leftChild = new Tracker(subgoals.getJSONObject(0), config);
            rightChild = new Tracker(subgoals.getJSONObject(1), config);
        } else {
            currGoal = ":" + goal;
        }
    }

    public void notifyTreasure() {
        currTreasure += 1;

        if (leftChild != null)
            leftChild.notifyTreasure();

        if (rightChild != null)
            rightChild.notifyTreasure();
    }

    public void notifyEnemy() {
        currEnemy += 1;

        if (leftChild != null)
            leftChild.notifyEnemy();

        if (rightChild != null)
            rightChild.notifyEnemy();
    }

    public void notifyExits() {
        steppedOnExit = true;

        if (leftChild != null)
            leftChild.notifyExits();

        if (rightChild != null)
            rightChild.notifyExits();
    }

    public String getUnfinishedGoals(DungeonMap map) {
        if (superGoals.contains(currGoal)) {
            boolean leftComplete = leftChild.goalFinished(map);
            boolean rightComplete = rightChild.goalFinished(map);

            if (currGoal.equals(AND)) {
                if ((leftComplete && rightComplete) || (leftChild.getUnfinishedGoals(map).equals("") && rightChild.getUnfinishedGoals(map).equals("")))
                    return "";

                if (leftComplete)
                    return rightChild.getUnfinishedGoals(map);

                if (rightComplete)
                    return leftChild.getUnfinishedGoals(map);

                return "(" + leftChild.getUnfinishedGoals(map) + " " + AND + " " + rightChild.getUnfinishedGoals(map) + ")";
            } else {
                if (leftComplete || rightComplete || leftChild.getUnfinishedGoals(map).equals("") || rightChild.getUnfinishedGoals(map).equals(""))
                    return "";

                    return "(" + leftChild.getUnfinishedGoals(map) + " " + OR + " " + rightChild.getUnfinishedGoals(map) + ")";
            }
        }

        if (goalFinished(map))
            return "";
        else
            return currGoal;
    }

    private boolean goalFinished(DungeonMap map) {
        switch (currGoal) {
            case ":treasure":
                return treasureAchieved();

            case ":enemies":
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
