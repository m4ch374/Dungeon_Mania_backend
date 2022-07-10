package dungeonmania.MovingStrategies;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dungeonmania.DungeonObjects.EntityTypes;
import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.IMovingStrategy;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieMoveStrat implements IMovingStrategy {

    private DungeonMap map;
    private Entity mover;

    public ZombieMoveStrat(Entity mover, DungeonMap map) {
        this.mover = mover;
        this.map = map;
    }

    private boolean containsBlockable(Position pos) {
        List<Entity> entities = map.getEntitiesAt(pos);
        return !(entities.stream().filter(e -> e.getType().equals(EntityTypes.WALL.toString())).count() == 0);
    }

    @Override
    public void moveEntity() {
        Position currPos = map.getEntityPos(mover);

        Random random = new Random();
        int randIdx = random.nextInt(Direction.values().length);
        Direction newDir = Direction.values()[randIdx];

        Position newPos = currPos.translateBy(newDir);
        if (!containsBlockable(newPos))
            map.moveEntityTo(mover, newPos);
    }
    
}
