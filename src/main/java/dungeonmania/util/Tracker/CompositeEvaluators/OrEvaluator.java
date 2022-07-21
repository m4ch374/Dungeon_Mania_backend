package dungeonmania.util.Tracker.CompositeEvaluators;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.util.Tracker.GoalTypes;
import dungeonmania.util.Tracker.NodeBuilder;
import dungeonmania.util.Tracker.TrackerNode;

public class OrEvaluator implements TrackerNode {
    private static final GoalTypes TYPE = GoalTypes.OR;

    TrackerNode leftChild;
    TrackerNode rightChild;

    public OrEvaluator(JSONObject goalJson, JSONObject config) throws Exception {
        JSONArray subgoals = goalJson.getJSONArray("subgoals");

        leftChild = NodeBuilder.buildNode(subgoals.getJSONObject(0), config);
        rightChild = NodeBuilder.buildNode(subgoals.getJSONObject(1), config);
    }

    @Override
    public String getOriginalGoalString() {
        return "( " + leftChild.getEvaluationString() + " " + TYPE.getType() + " " + rightChild.getEvaluationString() + " )"; 
    }

    @Override
    public String getEvaluationString() {
        if (leftChild.getEvaluationString().equals("") || rightChild.getEvaluationString().equals(""))
            return "";

        return getOriginalGoalString();
    }

    @Override
    public void notifyTracker(GoalTypes type) {
        leftChild.notifyTracker(type);
        rightChild.notifyTracker(type);
    }

    @Override
    public void unnotifyTracker(GoalTypes type) {
        leftChild.unnotifyTracker(type);
        rightChild.unnotifyTracker(type);
    }
    
}
