package dungeonmania.MovingStrategies;

import java.util.List;

import dungeonmania.DungeonObjects.EntityTypes;
import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.DungeonObjects.Entities.Statics.Door;
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

        boolean hasWall = entities.stream().filter(e -> e.getType().equals(EntityTypes.WALL.toString())).count() > 0;
        boolean hasLockedDoors = entities.stream()
                                    .filter(e -> e.getType().equals(EntityTypes.DOOR.toString()))
                                    .map(e -> (Door) e)
                                    .filter(d -> !d.isOpen())
                                    .count() > 0;

        return hasWall || hasLockedDoors;
    }

    // Logic for going around walls
    // Would only go around "simple" walls, otherwise, stay at the current position
    // Where "simple" means a non "enclosed" wall as defined in the forums
    // Link: https://edstem.org/au/courses/8675/discussion/933616?comment=2098362
    //
    // I better hope this algo is correct cuz "simple wall" is ambiguous asf
    private Position resolveBlocked(Position currPos, Direction originalDirection) {
        RelativePosition relativeCurrPos = new RelativePosition(currPos, originalDirection);
        RelativePosition posToCheck = relativeCurrPos.translateBy(Direction.UP, originalDirection);

        // Go left and right to search for the nearest opening
        int leftUntilOpen = getOpenings(relativeCurrPos, posToCheck, originalDirection, Direction.LEFT);
        int rightUntilOpen = getOpenings(relativeCurrPos, posToCheck, originalDirection, Direction.RIGHT);

        if (leftUntilOpen == -1 && rightUntilOpen == -1)
            return currPos;
        
        Position leftPos = relativeCurrPos.translateBy(Direction.LEFT, originalDirection).getOriginalPos();
        Position rightPos = relativeCurrPos.translateBy(Direction.RIGHT, originalDirection).getOriginalPos();

        if (rightUntilOpen == -1)
            return leftPos;

        if (leftUntilOpen == -1)
            return rightPos;

        if (leftUntilOpen < rightUntilOpen)
            return leftPos;

        if (rightUntilOpen < leftUntilOpen)
            return rightPos;

        // If both have the same number of steps until opening, choose the one with shorter distance to player
        Position seekingPos = map.getEntityPos(seekingEntity);
        double leftPosDistance = Math.sqrt(Math.pow((leftPos.getX() - seekingPos.getX()), 2) + Math.pow((leftPos.getY() - seekingPos.getY()), 2));
        double rightPosDistance = Math.sqrt(Math.pow((rightPos.getX() - seekingPos.getX()), 2) + Math.pow((rightPos.getY() - seekingPos.getY()), 2));

        if (leftPosDistance <= rightPosDistance) 
            return leftPos;

        return rightPos;
    }

    private boolean hasOpening(RelativePosition relativePos, RelativePosition otherRelativePos) {
        Position originalPos = relativePos.getOriginalPos();
        Position otherOriginalPos = otherRelativePos.getOriginalPos();

        return !containsBlockable(originalPos) && !containsBlockable(otherOriginalPos);
    }

    private int getOpenings(RelativePosition relativePos, RelativePosition otherRelativePos, Direction transposeDir, Direction heading) {
        int leftOpening = 0;

        while (true) {
            if (containsBlockable(relativePos.getOriginalPos()))
                return -1;

            if (hasOpening(relativePos, otherRelativePos))
                break;

            relativePos = relativePos.translateBy(heading, transposeDir);
            otherRelativePos = otherRelativePos.translateBy(heading, transposeDir);
            leftOpening++;
        }
        return leftOpening;
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
        if (containsBlockable(newPos))
            newPos = resolveBlocked(moverPos, directionToGo);
        
        map.moveEntityTo(mover, newPos);
    }
}

class RelativePosition {
    Position originalPos;
    Position relativePos;

    public RelativePosition(Position originalPos, Direction direction) {
        this.originalPos = originalPos;
        relativePos = toRelativePos(originalPos, direction);
    }

    public Position getOriginalPos() {
        return originalPos;
    }

    public Position getRelativePos() {
        return relativePos;
    }

    public RelativePosition translateBy(Direction heading, Direction direction) {
        Position originalPos = toOriginalPos(relativePos.translateBy(heading), direction);
        return new RelativePosition(originalPos, direction);
    }

    private Position toRelativePos(Position pos, Direction direction) {
        // Does not do anything
        if (direction == Direction.UP)
            return pos;

        int posX = pos.getX();
        int posY = pos.getY();

        // Rotate 180 degree
        if (direction == Direction.DOWN) 
            return new Position(-posX, -posY);

        // Rotate 90 degrees clockwise
        if (direction == Direction.RIGHT) 
            return new Position(posY, -posX);

        // Rotate 90 degrees anticlockwise
        return new Position(-posY, posX);
    }

    private Position toOriginalPos(Position relativePos, Direction direction) {
        // Does not do anything
        if (direction == Direction.UP)
            return relativePos;

        int posX = relativePos.getX();
        int posY = relativePos.getY();

        // Rotate 180 degree
        if (direction == Direction.DOWN) 
            return new Position(-posX, -posY);

        // Rotate 90 degrees clockwise
        if (direction == Direction.LEFT) 
            return new Position(posY, -posX);

        // Rotate 90 degrees anticlockwise
        return new Position(-posY, posX);
    }
}
