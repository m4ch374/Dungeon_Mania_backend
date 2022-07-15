package dungeonmania.Interfaces;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.exceptions.InvalidActionException;

public interface IStaticInteractable {
    public void interactedBy(Entity interactor) throws InvalidActionException ;
}
