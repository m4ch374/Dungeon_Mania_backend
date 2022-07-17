package dungeonmania.DungeonObjects.Entities.Characters;

import java.util.Random;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.EntityTypes;
import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.IEnemy;
import dungeonmania.Interfaces.IMovingStrategy;
import dungeonmania.MovingStrategies.CircularMoveStrat;
import dungeonmania.util.Position;
import dungeonmania.util.DungeonFactory.EntityStruct;

public class Spider extends Entity implements IEnemy {
    private static int spawnId = 0;

    private double attackDamage;
    private double health;
    
    IMovingStrategy moveStrat;

    public Spider(EntityStruct metaData, JSONObject config) {
        super(metaData);
        this.attackDamage = config.getInt("spider_attack");
        this.health = config.getInt("spider_health");

        moveStrat = new CircularMoveStrat(this, super.getMap());
    }

    public double getAttackDamage() {
        return attackDamage;
    }

    public double getHealth() {
        return health;
    }

    public void death() {
        getMap().removeEntity(this);
        return;
    }

    public String getClasString() {
        return super.getType();
    }

    @Override
    public void move() {
        moveStrat.moveEntity();
    }
    
    public static void spawnSpider(JSONObject config, int currTick, DungeonMap map) {
        int spawnRate = config.getInt("spider_spawn_rate");

        if (spawnRate == 0)
            return;

        if (currTick % spawnRate != 0 || currTick == 0)
            return;

        int minX = map.getLeftBound();
        int maxX = map.getRightBound() + 1;
        int minY = map.getTopBound();
        int maxY = map.getBottomBound() + 1;

        Random random = new Random();
        int entityX = random.nextInt(maxX - minX) + minX;
        int entityY = random.nextInt(maxY - minY) + minY;

        // minimum 5 x 5 grid
        entityX = entityX < 5 ? 5 : entityX;
        entityY = entityY < 5 ? 5 : entityY;

        EntityStruct struct = new EntityStruct("spawned_spider" + spawnId, EntityTypes.SPIDER.toString(), map);
        Spider spiderSpawned = new Spider(struct, config);
        map.placeEntityAt(spiderSpawned, new Position(entityX, entityY));
        spawnId++;
    }
}
