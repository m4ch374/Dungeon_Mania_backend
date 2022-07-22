package dungeonmania.util.Tracker;

public interface TrackerNode {
    public String getOriginalGoalString();

    public String getEvaluationString();

    public void notifyTracker(GoalTypes type);

    public void unnotifyTracker(GoalTypes type);
}
