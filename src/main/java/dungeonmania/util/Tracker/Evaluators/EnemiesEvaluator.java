package dungeonmania.util.Tracker.Evaluators;

import org.json.JSONObject;

import dungeonmania.util.Tracker.GoalTypes;
import dungeonmania.util.Tracker.TrackerNode;

public class EnemiesEvaluator implements TrackerNode {
    private static final GoalTypes TYPE = GoalTypes.ENEMIES;

    private int enemyGoal;
    private int currEnemy = 0;

    public EnemiesEvaluator(JSONObject config) {
        enemyGoal = config.getInt("enemy_goal");
    }

    @Override
    public String getOriginalGoalString() {
        return TYPE.getGoalString();
    }

    @Override
    public String getEvaluationString() {
        if (currEnemy >= enemyGoal)
            return "";

        return TYPE.getGoalString();
    }

    @Override
    public void notifyTracker(GoalTypes type) {
        if (type == TYPE)
            currEnemy++;
    }

    @Override
    public void unnotifyTracker(GoalTypes type) {
        if (type == TYPE)
            currEnemy--;
    }
    
}
