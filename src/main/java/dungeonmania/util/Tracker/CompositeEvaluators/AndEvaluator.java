package dungeonmania.util.Tracker.CompositeEvaluators;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.util.Tracker.GoalTypes;
import dungeonmania.util.Tracker.NodeBuilder;
import dungeonmania.util.Tracker.TrackerNode;

public class AndEvaluator implements TrackerNode {
    private static final GoalTypes TYPE = GoalTypes.AND;

    TrackerNode leftChild;
    TrackerNode rightChild;

    public AndEvaluator(JSONObject goalJson, JSONObject config) throws Exception {
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
        if (leftChild.getEvaluationString().equals("") && rightChild.getEvaluationString().equals(""))
            return "";

        if (leftChild.getEvaluationString().equals(""))
            return rightChild.getEvaluationString();

        if (rightChild.getEvaluationString().equals(""))
            return leftChild.getEvaluationString();

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
