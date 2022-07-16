package dungeonmania.DungeonObjects.Entities.Characters;

import java.util.Random;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.EntityTypes;
import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.IMovable;
import dungeonmania.Interfaces.IMovingStrategy;
import dungeonmania.MovingStrategies.CircularMoveStrat;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Position;
import dungeonmania.util.DungeonFactory.EntityStruct;

public class Spider extends Entity implements IMovable {
    private static int spawnId = 0;

    private int attackDamage;
    private int health;
    
    IMovingStrategy moveStrat;

    public Spider(EntityStruct metaData, JSONObject config) {
        super(metaData);
        this.attackDamage = config.getInt("spider_attack");
        this.health = config.getInt("spider_health");

        moveStrat = new CircularMoveStrat(this, super.getMap());
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public int getHealth() {
        return health;
    }

    public void death() {
        getMap().removeEntity(this);
        return;
    }

    public String getClasString() {
        return "Spider";
    }

    @Override
    public void move() {
        moveStrat.moveEntity();
    }

    @Override
    public RoundResponse battleWith(Entity opponent) { return null; }
    
    
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

        EntityStruct struct = new EntityStruct("spawned_spider" + spawnId, EntityTypes.SPIDER.toString(), map);
        Spider spiderSpawned = new Spider(struct, config);
        map.placeEntityAt(spiderSpawned, new Position(entityX, entityY));
        spawnId++;
    }
}
