package dungeonmania.MovingStrategies;

import java.util.List;

import dungeonmania.DungeonObjects.EntityTypes;
import dungeonmania.DungeonObjects.Player;
import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.DungeonObjects.Entities.Statics.Door;
import dungeonmania.Interfaces.IMovingStrategy;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.util.PathFinder.PathFinder;

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

        boolean hasWall = entities.stream().filter(e -> e.getType().equals(EntityTypes.WALL.toString())).count() > 0;
        boolean hasLockedDoors = entities.stream()
                                    .filter(e -> e.getType().equals(EntityTypes.DOOR.toString()))
                                    .map(e -> (Door) e)
                                    .filter(d -> !d.isOpen())
                                    .count() > 0;

        return hasWall || hasLockedDoors;
    }

    private boolean overlapsWithPlayer(Position nextPos) {
        return map.getEntitiesAt(nextPos).stream().filter(e -> e instanceof Player).count() > 0;
    }

    // Very botched code
    private boolean cannotMoveCloser(Position nextPos) {
        Player p = (Player) seekingEntity;

        Position playerPrevPos = p.getCurrentPosition();
        Position playerCurrPos = p.getPreviousPosition();

        return playerCurrPos.equals(playerPrevPos) && overlapsWithPlayer(nextPos);
    }

    private Position fallBackNextPos(Position moverPos, Position seekingPos) {
        Position relativeVect = Position.calculatePositionBetween(moverPos, seekingPos);

        double radian = Math.atan2(relativeVect.getY(), relativeVect.getX());
        Direction directionToGo = radiansToDirection(radian);

        if (!containsBlockable(moverPos.translateBy(directionToGo)))
            return moverPos.translateBy(directionToGo);
        
        return moverPos;
    }

    @Override
    public Position moveEntity() {
        // Load the entity to seek
        if (seekingEntity == null)
            seekingEntity = getSeekingEntity();

        Position moverPos = map.getEntityPos(mover);
        Position seekingPos = map.getEntityPos(seekingEntity);

        if (moverPos.equals(seekingPos))
            return moverPos;

        Position newPos = PathFinder.getOptimalNextPos(map, moverPos, seekingPos);

        // Use legacy pathfinding algo if there's no known path
        if (newPos == null)
            newPos = fallBackNextPos(moverPos, seekingPos);
        
        if (cannotMoveCloser(newPos))
            return moverPos;

        return newPos;
    }
}
