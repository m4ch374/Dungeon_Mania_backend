package dungeonmania.DungeonObjects.Entities.Characters.Enemies;

import dungeonmania.util.DungeonFactory.EntityStruct;
import dungeonmania.util.Tracker.Tracker;
import org.json.JSONObject;


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

    @Override
    public double attacked(double attack){
        boolean healthIncreases = Math.random() <= healthIncreaseRate;

        if (healthIncreases){
            this.setHealth((this.getHealth() + healthIncreaseAmt));
            if (this.getHealth() <= 0){
                this.death();
            }
            return healthIncreaseAmt;
        }

        this.setHealth(this.getHealth() - (attack / 5));

        return -(attack / 5);
    }
}
