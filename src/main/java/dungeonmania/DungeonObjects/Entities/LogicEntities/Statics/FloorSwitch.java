package dungeonmania.DungeonObjects.Entities.LogicEntities.Statics;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.DungeonObjects.Entities.Statics.Boulder;
import dungeonmania.Interfaces.IStaticInteractable;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.util.DungeonFactory.EntityStruct;
import dungeonmania.exceptions.*;

public class FloorSwitch extends Entity implements IStaticInteractable {

    public FloorSwitch(EntityStruct metaData) {
        super(metaData);
    }

    public boolean isActive() {
        Position pos = getMap().getEntityPos(this);

        boolean isActive = getMap().getEntitiesAt(pos)
                                            .stream()
                                            .anyMatch(e -> (e instanceof Boulder));

        return isActive;
    }

    public void active() {
        Position pos = getMap().getEntityPos(this);

        ArrayList<Entity> enties = new ArrayList<Entity>();

        enties.addAll(getMap().getEntitiesAt(pos.translateBy(Direction.UP)));
        enties.addAll(getMap().getEntitiesAt(pos.translateBy(Direction.DOWN)));
        enties.addAll(getMap().getEntitiesAt(pos.translateBy(Direction.LEFT)));
        enties.addAll(getMap().getEntitiesAt(pos.translateBy(Direction.RIGHT)));

        enties
            .stream()
            .filter(e -> (e instanceof Bomb))
            .map(e -> (Bomb) e)
            .forEach(e -> e.activate(getMap().getEntityPos(e)));
    }

    @Override
    public void interactedBy(Entity interactor) throws InvalidActionException {}
}
