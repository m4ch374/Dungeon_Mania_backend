package dungeonmania.util.Tracker;

import org.json.JSONObject;

import dungeonmania.util.Tracker.CompositeEvaluators.AndEvaluator;
import dungeonmania.util.Tracker.CompositeEvaluators.OrEvaluator;
import dungeonmania.util.Tracker.Evaluators.BoulderEvaluator;
import dungeonmania.util.Tracker.Evaluators.EnemiesEvaluator;
import dungeonmania.util.Tracker.Evaluators.ExitEvaluator;
import dungeonmania.util.Tracker.Evaluators.TreasureEvaluator;

public class NodeBuilder {
    public static TrackerNode buildNode(JSONObject rawGoal, JSONObject config) throws Exception {
        String goal = rawGoal.getString("goal");

        switch (GoalTypes.lookup(goal)) {
            case TREASURE:
                return new TreasureEvaluator(config);
            case ENEMIES:
                return new EnemiesEvaluator(config);
            case BOULDERS:
                return new BoulderEvaluator();
            case EXIT:
                return new ExitEvaluator();
            case AND:
                return new AndEvaluator(rawGoal, config);
            case OR:
                return new OrEvaluator(rawGoal, config);
            default:
                throw new Exception("Something went wrong");
        }
    }
}
