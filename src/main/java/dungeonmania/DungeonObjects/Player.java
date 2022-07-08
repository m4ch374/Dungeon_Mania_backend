package dungeonmania.DungeonObjects;

import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.DungeonFactory.EntityStruct;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.IMovingStrategy;

public class Player extends Entity {

    private int health;
    private int baseDamage;

    private int allyAttackBonous;
    private int allyDefenceBonous;

    private IMovingStrategy moveStrat;

    public Player(EntityStruct metaData, JSONObject config) {
        super(metaData);
        this.health = config.getInt("player_health");
        this.baseDamage = config.getInt("player_attack");

        // ally attack & defence not found in config file
        this.allyAttackBonous = config.has("ally_attack") ? config.getInt("ally_attack") : 0;
        this.allyDefenceBonous = config.has("ally_defence") ? config.getInt("ally_defence") : 0;
    }

    public void collect() {}

    public void move(Direction direction) {}

    public void initiateBattle() {}
}
