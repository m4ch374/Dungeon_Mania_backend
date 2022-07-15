package dungeonmania.MovingStrategies;

import java.util.List;

import dungeonmania.DungeonObjects.EntityTypes;
import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.IMovingStrategy;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class SeekerMoveStrat implements IMovingStrategy {

    private String seekingEntityId;

    private Entity mover;
    private Entity seekingEntity;

    private DungeonMap map;

    public SeekerMoveStrat(Entity mover, DungeonMap map, String seekForId) {
        this.mover = mover;
        this.map = map;
        this.seekingEntityId = seekForId;
    }

    private Entity getSeekingEntity() {
        return map.getAllEntities().stream().filter(e -> e.getId().equals(seekingEntityId)).findFirst().get();
    }

    private double rotateRadianBy45Degrees(double radian) {
        double newRad = radian - Math.PI / 4;
        if (newRad <= -Math.PI)
            newRad = 2 * Math.PI + newRad;

        return newRad;
    }

    private Direction radiansToDirection(double radian) {
        double convertedRad = rotateRadianBy45Degrees(radian);

        if (convertedRad > 0 && convertedRad <= Math.PI / 2) {
            return Direction.DOWN;
        } else if (convertedRad > Math.PI / 2 && convertedRad <= Math.PI) {
            return Direction.LEFT;
        } else if (convertedRad > -Math.PI / 2 && convertedRad <= 0) {
            return Direction.RIGHT;
        } else {
            return Direction.UP;
        }
    }

    private boolean containsBlockable(Position pos) {
        List<Entity> entities = map.getEntitiesAt(pos);

        return entities.stream().filter(e -> e.getType().equals(EntityTypes.WALL.toString())).count() > 0;
    }

    @Override
    public void moveEntity() {
        // Load the entity to seek
        if (seekingEntity == null)
            seekingEntity = getSeekingEntity();

        Position moverPos = map.getEntityPos(mover);
        Position seekingPos = map.getEntityPos(seekingEntity);

        if (moverPos.equals(seekingPos))
            return;

        Position relativeVect = Position.calculatePositionBetween(moverPos, seekingPos);

        double radian = Math.atan2(relativeVect.getY(), relativeVect.getX());
        Direction directionToGo = radiansToDirection(radian);

        Position newPos = moverPos.translateBy(directionToGo);
        if (!containsBlockable(newPos))
            map.moveEntityTo(mover, newPos);
    }
}
