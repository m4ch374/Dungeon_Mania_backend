package dungeonmania.MovingStrategies;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.DungeonObjects.EntityTypes;
import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.IMovingStrategy;
import dungeonmania.util.Position;

public class CircularMoveStrat implements IMovingStrategy {
    private static final int CLOCKWISE = 1;
    private static final int ANTI_CLOCKWISE = -1;

    private Entity mover;
    private DungeonMap map;

    private int movePosIdx = 0;
    private int direction = CLOCKWISE;
    private boolean startCirculate = false;
    private List<Position> positionMap = new ArrayList<Position>();

    public CircularMoveStrat(Entity mover, DungeonMap map) {
        this.mover = mover;
        this.map = map;
    }

    // Populate in circular order
    private void populatePosMap(Position startPos) {
        positionMap.add(startPos.translateBy(new Position(0, -1)));
        positionMap.add(startPos.translateBy(new Position(1, -1)));
        positionMap.add(startPos.translateBy(new Position(1, 0)));
        positionMap.add(startPos.translateBy(new Position(1, 1)));
        positionMap.add(startPos.translateBy(new Position(0, 1)));
        positionMap.add(startPos.translateBy(new Position(-1, 1)));
        positionMap.add(startPos.translateBy(new Position(-1, 0)));
        positionMap.add(startPos.translateBy(new Position(-1, -1)));
    }

    private boolean hasBlockable(Position nextPos) {
        List<Entity> entities = map.getEntitiesAt(nextPos);

        return entities.stream().filter(e -> e.getType().equals(EntityTypes.BOULDER.toString())).count() > 0;
    }

    private int changeDirection() {
        if (direction == CLOCKWISE)
            return ANTI_CLOCKWISE;
        else
            return CLOCKWISE;
    }

    private int getNextMovePos() {
        int nextMovePos = (movePosIdx + direction) % positionMap.size();
        return nextMovePos < 0 ? nextMovePos + positionMap.size() : nextMovePos;
    }

    @Override
    public void moveEntity() {
        if (positionMap.isEmpty())
            populatePosMap(map.getEntityPos(mover));

        // Ignores boulder in the first move
        // i.e. heading to the "circle"
        if (!startCirculate) {
            map.moveEntityTo(mover, positionMap.get(0));
            startCirculate = true;
            return;
        }

        int nextMovePos = getNextMovePos();
        if (hasBlockable(positionMap.get(nextMovePos))) {
            direction = changeDirection();
            nextMovePos = getNextMovePos();
        }

        movePosIdx = nextMovePos;
        Position newPos = positionMap.get(movePosIdx);

        map.moveEntityTo(mover, newPos);
    }
    
}
