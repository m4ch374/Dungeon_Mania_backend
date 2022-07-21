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
import dungeonmania.util.Tracker.GoalTypes;
import dungeonmania.util.Tracker.Tracker;

public class Spider extends Entity implements IEnemy {
    private static int spawnId = 0;

    private double attackDamage;
    private double health;
    
    IMovingStrategy moveStrat;

    Tracker tracker;

    public Spider(EntityStruct metaData, JSONObject config, Tracker tracker) {
        super(metaData);
        this.attackDamage = config.getInt("spider_attack");
        this.health = config.getInt("spider_health");

        moveStrat = new CircularMoveStrat(this, super.getMap());

        this.tracker = tracker;
    }

    public double getAttackDamage() {
        return attackDamage;
    }

    public double getHealth() {
        return health;
    }

    public void death() {
        getMap().removeEntity(this);
        tracker.notifyTracker(GoalTypes.ENEMIES);
    }

    public String getClasString() {
        return super.getType();
    }

    @Override
    public void move() {
        Position posToMove = moveStrat.moveEntity();
        super.getMap().moveEntityTo(this, posToMove);
    }
    
    public static void spawnSpider(JSONObject config, int currTick, DungeonMap map, Tracker tracker) {
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
        Spider spiderSpawned = new Spider(struct, config, tracker);
        map.placeEntityAt(spiderSpawned, new Position(entityX, entityY));
        spawnId++;
    }
}
