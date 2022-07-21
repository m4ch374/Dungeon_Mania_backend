package dungeonmania.util.Tracker.Evaluators;

import dungeonmania.util.Tracker.GoalTypes;
import dungeonmania.util.Tracker.TrackerNode;

public class ExitEvaluator implements TrackerNode {
    private static final GoalTypes TYPE = GoalTypes.EXIT;

    private boolean steppedOnExit = false;

    @Override
    public String getOriginalGoalString() {
        return TYPE.getGoalString();
    }

    @Override
    public String getEvaluationString() {
        if (steppedOnExit)
            return "";

        return TYPE.getGoalString();
    }

    @Override
    public void notifyTracker(GoalTypes type) {
        if (type == TYPE)
            steppedOnExit = true;
    }

    @Override
    public void unnotifyTracker(GoalTypes type) {
        if (type == TYPE)
            steppedOnExit = false;
    }
}
