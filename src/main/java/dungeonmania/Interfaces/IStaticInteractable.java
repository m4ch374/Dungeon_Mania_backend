package dungeonmania.Interfaces;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.exceptions.*;

public interface IStaticInteractable {
    public void interactedBy(Entity interactor) throws InvalidActionException;
}
