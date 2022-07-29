package dungeonmania.DungeonObjects.Entities.Characters.Enemies;

import org.json.JSONObject;

import dungeonmania.util.DungeonFactory.EntityStruct;
import dungeonmania.util.Tracker.Tracker;

// Might extend it to zombies idk
public class Hydra extends ZombieToast {
    private static final String ATK_STR = "hydra_attack";
    private static final String HEALTH_STR = "hydra_health";

    private double healthIncreaseRate;
    private double healthIncreaseAmt;

    public Hydra(EntityStruct metaData, JSONObject config, Tracker tracker) {
        super(metaData, tracker, config, ATK_STR, HEALTH_STR);
        this.healthIncreaseRate = config.getDouble("hydra_health_increase_rate");
        this.healthIncreaseAmt = config.getDouble("hydra_health_increase_amount");
    }

    public double getHealthIncreaseRate() {
        return healthIncreaseRate;
    }

    public double getHealthIncreaseAmt() {
        return healthIncreaseAmt;
    }
}
