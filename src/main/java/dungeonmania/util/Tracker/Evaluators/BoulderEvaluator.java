package dungeonmania.util.Tracker.Evaluators;

import dungeonmania.util.Tracker.GoalTypes;
import dungeonmania.util.Tracker.TrackerNode;

public class BoulderEvaluator implements TrackerNode {
    private static final GoalTypes TYPE = GoalTypes.BOULDERS;

    private boolean allBoulderOnSwitch = false;

    @Override
    public String getOriginalGoalString() {
        return TYPE.getGoalString();
    }

    @Override
    public String getEvaluationString() {
        if (allBoulderOnSwitch)
            return "";

        return TYPE.getGoalString();
    }

    @Override
    public void notifyTracker(GoalTypes type) {
        if (type == TYPE)
            allBoulderOnSwitch = true;
    }

    @Override
    public void unnotifyTracker(GoalTypes type) {
        if (type == TYPE)
            allBoulderOnSwitch = false;
    }
}
