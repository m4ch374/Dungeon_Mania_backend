package dungeonmania.Interfaces;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.exceptions.InvalidActionException;

public interface ICollectable {
    public void collectedBy(Entity collector) throws InvalidActionException;
}
