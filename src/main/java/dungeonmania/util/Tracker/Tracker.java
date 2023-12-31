package dungeonmania.util.Tracker;

import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.EntityTypes;
import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.DungeonObjects.Entities.Statics.Boulder;

// Its the last day so this one's gonna be very botched
public class Tracker {
    TrackerNode rootNode;

    public Tracker(JSONObject rawGoals, JSONObject config) throws Exception {
        rootNode = NodeBuilder.buildNode(rawGoals, config);
    }

    public void notifyTracker(GoalTypes types) {
        rootNode.notifyTracker(types);
    }

    public void unnotifyTracker(GoalTypes types) {
        rootNode.notifyTracker(types);
    }

    public String getUnfinishedGoals(DungeonMap map) {
        if (allBoulderOnSwitch(map))
            rootNode.notifyTracker(GoalTypes.BOULDERS);
        else
            rootNode.unnotifyTracker(GoalTypes.BOULDERS);

        if (!playerOnExit(map))
            rootNode.unnotifyTracker(GoalTypes.EXIT);

        return rootNode.getEvaluationString();
    }

    private boolean allBoulderOnSwitch(DungeonMap map) {
        List<Boulder> boulders = map.getAllEntities().stream()
                                    .filter(e -> e.getType().equals(EntityTypes.BOULDER.toString()))
                                    .map(e -> (Boulder) e)
                                    .collect(Collectors.toList());

        if (boulders.isEmpty())
            return false;

        for (Boulder boulder : boulders) {
            if (!boulder.overlappedWithSwitch())
                return false;
        }
        return true;
    }

    private boolean playerOnExit(DungeonMap map) {
        Entity player = map.getAllEntities().stream()
                    .filter(e -> e.getType().equals(EntityTypes.PLAYER.toString()))
                    .findFirst()
                    .orElse(null);

        if (player == null)
            return false;

        List<Entity> entitiesOverlapped = map.getEntitiesOverlapped(player);

        return entitiesOverlapped.stream().filter(e -> e.getType().equals(EntityTypes.EXIT.toString())).count() != 0;
    }
}
