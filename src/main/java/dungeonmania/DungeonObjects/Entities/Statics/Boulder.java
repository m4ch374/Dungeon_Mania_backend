package dungeonmania.DungeonObjects.Entities.Statics;

import dungeonmania.DungeonObjects.Player;
import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.DungeonObjects.Entities.Collectables.Bomb;
import dungeonmania.Interfaces.IStaticInteractable;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.DungeonFactory.EntityStruct;
import dungeonmania.DungeonObjects.EntityTypes;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.util.List;
import java.util.stream.Collectors;

public class Boulder extends Entity implements IStaticInteractable {

    public Boulder(EntityStruct metaData) {
        super(metaData);
    }

    @Override
    public void interactedBy(Entity interactor) throws InvalidActionException {
        // FIRST check if interactor instanceof Player, since only player can push boulder (for now...)
        if (!(interactor instanceof Player)) {throw new InvalidActionException("Only Player can move boulder");}
        // SECOND determine new position, relative to interactor's
        Position newPos = determineNewPosition((Player) interactor);
        // THIRD check if the moveTo position contains a boulder OR Wall
        if (!canMoveIntoNewPositionCell(newPos)) {
            throw new InvalidActionException("ERROR: Can not move the boulder to " + newPos);
        }
        // Finally, move the boulder
        move(newPos);
    }

    public Position determineNewPosition(Player player) {
        Direction direction = player.getDirection();
        Position boulderPos = super.getMap().getEntityPos(this);
        int x = boulderPos.getX();
        int y = boulderPos.getY();

        switch (direction) {
            case UP:
                y -= 1;
                break;
            case DOWN:
                y += 1;
                break;
            case LEFT:
                x -= 1;
                break;
            case RIGHT:
                x += 1;
                break;
        }

        Position position = new Position(x, y);

        return position;
    }

    public boolean canMoveIntoNewPositionCell(Position newPos) {
        List<Entity> inCell = getMap().getEntitiesAt(newPos);

        // check if a fixed bomb exist
        Boolean hasFixedBomb = inCell
                                .stream()
                                .filter(e -> (e instanceof Bomb))
                                .map(e -> (Bomb) e)
                                .anyMatch(e -> !e.isCollectible());

        if (hasFixedBomb) return false;

        List<IStaticInteractable> staticEntity = inCell
                                                .stream()
                                                .filter(e -> (e instanceof IStaticInteractable))
                                                .map(e -> (IStaticInteractable) e)
                                                .collect(Collectors.toList());

        // check if a static entity blocking the boulder
        for (IStaticInteractable entity : staticEntity) {
            if (entity instanceof Wall || entity instanceof Boulder) {
                return false;
            } else if (entity instanceof Door) {
                Door door = (Door) entity;
                if (!door.isOpen()) {
                    return false;
                }
            }
        }

        return true;
    }

    public void move(Position newPos) {
       super.getMap().moveEntityTo(this, newPos);
    }

    public boolean overlappedWithSwitch() {
        DungeonMap map = super.getMap();

        List<Entity> entitiesOverlapped = map.getEntitiesOverlapped(this);

        return entitiesOverlapped.stream().filter(e -> e.getType().equals(EntityTypes.FLOOR_SWITCH.toString())).count() > 0;
    }
}
