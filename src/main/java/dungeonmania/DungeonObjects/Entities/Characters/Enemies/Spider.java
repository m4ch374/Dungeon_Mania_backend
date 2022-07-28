package dungeonmania.DungeonObjects.Entities.Characters.Enemies;

import java.util.Random;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.EntityTypes;
import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.Interfaces.IMovingStrategy;
import dungeonmania.MovingStrategies.CircularMoveStrat;
import dungeonmania.util.Position;
import dungeonmania.util.DungeonFactory.EntityStruct;
import dungeonmania.util.Tracker.Tracker;

public class Spider extends Enemy {
    private static final String ATK_STR = "spider_attack";
    private static final String HEALTH_STR = "spider_health";

    private static int spawnId = 0;
    
    IMovingStrategy moveStrat;

    public Spider(EntityStruct metaData, JSONObject config, Tracker tracker) {
        super(metaData, tracker, config, ATK_STR, HEALTH_STR);

        moveStrat = new CircularMoveStrat(this, super.getMap());
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
