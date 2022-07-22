package dungeonmania.util.Tracker.Evaluators;

import org.json.JSONObject;

import dungeonmania.util.Tracker.GoalTypes;
import dungeonmania.util.Tracker.TrackerNode;

public class TreasureEvaluator implements TrackerNode {
    private static final GoalTypes TYPE = GoalTypes.TREASURE;

    private int treasureGoal;
    private int currTreasure = 0;

    public TreasureEvaluator(JSONObject config) {
        treasureGoal = config.getInt("treasure_goal");
    }

    @Override
    public String getOriginalGoalString() {
        return TYPE.getGoalString();
    }

    @Override
    public String getEvaluationString() {
        if (currTreasure >= treasureGoal)
            return "";
        
        return TYPE.getGoalString();
    }

    @Override
    public void notifyTracker(GoalTypes type) {
        if (type == TYPE)
            currTreasure++;
    }

    // If player drops a treasure or something idk
    @Override
    public void unnotifyTracker(GoalTypes type) {
        if (type == TYPE)
            currTreasure--;
    }
    
}
