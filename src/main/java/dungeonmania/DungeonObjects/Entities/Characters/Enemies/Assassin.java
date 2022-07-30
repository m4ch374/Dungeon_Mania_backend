package dungeonmania.DungeonObjects.Entities.Characters.Enemies;

import org.json.JSONObject;

import dungeonmania.util.DungeonFactory.EntityStruct;
import dungeonmania.util.Tracker.Tracker;

// TODO: sketch, might refactor after finishing dikstra algo
public class Assassin extends Mercenary {
    private static final String ATK_STR = "assassin_attack";
    private static final String HEALTH_STR = "assassin_health";
    private static final String RADIUS_STR = "bribe_radius";
    private static final String AMT_STR = "assassin_bribe_amount";

    private double bribeFailRate;

    private int reconRadius;

    public Assassin(EntityStruct metaData, JSONObject config, Tracker tracker) {
        super(metaData, tracker, config, ATK_STR, HEALTH_STR, RADIUS_STR, AMT_STR);
        this.bribeFailRate = config.getDouble("assassin_bribe_fail_rate");
        this.reconRadius = config.getInt("assassin_recon_radius");
    }
    
}
