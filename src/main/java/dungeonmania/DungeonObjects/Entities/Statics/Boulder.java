package dungeonmania.DungeonObjects.Entities.Statics;

import dungeonmania.DungeonObjects.Player;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.DungeonObjects.Entities.Collectables.Bomb;
import dungeonmania.Interfaces.IStaticInteractable;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.DungeonFactory.EntityStruct;
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
        if (!(interactor instanceof Player)) {return;}
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

        Position position = getMap().getEntityPos(this).translateBy(direction);

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
}
