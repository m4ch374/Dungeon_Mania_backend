package dungeonmania.MovingStrategies;

import java.util.List;
import java.util.Random;

import dungeonmania.DungeonObjects.EntityTypes;
import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.DungeonObjects.Entities.Statics.Door;
import dungeonmania.Interfaces.IMovingStrategy;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ConfusedMoveStrat implements IMovingStrategy {

    private DungeonMap map;
    private Entity mover;

    public ConfusedMoveStrat(Entity mover, DungeonMap map) {
        this.mover = mover;
        this.map = map;
    }

    private boolean containsBlockable(Position pos) {
        List<Entity> entities = map.getEntitiesAt(pos);

        boolean hasWall = entities.stream().filter(e -> e.getType().equals(EntityTypes.WALL.toString())).count() > 0;
        boolean hasLockedDoors = entities.stream()
                                    .filter(e -> e.getType().equals(EntityTypes.DOOR.toString()))
                                    .map(e -> (Door) e)
                                    .filter(e -> !e.isOpen())
                                    .count() > 0;

        return hasWall || hasLockedDoors;
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
