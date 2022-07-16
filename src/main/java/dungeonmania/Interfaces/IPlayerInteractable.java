package dungeonmania.Interfaces;

import dungeonmania.DungeonObjects.Player;
import dungeonmania.exceptions.InvalidActionException;

public interface IPlayerInteractable {
    public void interactedByPlayer(Player player) throws InvalidActionException;
}
