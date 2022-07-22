package dungeonmania.MovingStrategies;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.DungeonObjects.Player;
import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.DungeonObjects.Entities.Collectables.Bomb;
import dungeonmania.DungeonObjects.Entities.Statics.Boulder;
import dungeonmania.DungeonObjects.Entities.Statics.Door;
import dungeonmania.DungeonObjects.Entities.Statics.Exit;
import dungeonmania.DungeonObjects.Entities.Statics.Portal;
import dungeonmania.DungeonObjects.Entities.Statics.Wall;
import dungeonmania.Interfaces.ICollectable;
import dungeonmania.Interfaces.IStaticInteractable;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class PlayerMovement {
    private final Player player;
    private final DungeonMap map;
    private Position previousPosition = null;
    private Direction direction = null;


    public PlayerMovement(Player player, DungeonMap map) {
        this.player = player;
        this.map = map;
    }

    public void setDirection(Direction dirt) {
        this.direction = dirt;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public Position getCurrentPosition() {
        return map.getEntityPos(player);
    }

    public Position getPreviousPosition() {
        return this.previousPosition;
    }

    private boolean ableToMove(Position destination) {
        List<Entity> inCell = map.getEntitiesAt(destination);

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

        // check if a static entity blocking the player, include door interaction
        for (IStaticInteractable entity : staticEntity) {
            try {
                if (entity instanceof Wall) {
                    return false;
                } else if (entity instanceof Door) {
                    Door door = (Door) entity;
                    if (!door.isOpen()) {
                        door.interactedBy(player);
                    }
                }
            } catch (InvalidActionException e) {
                System.out.println(e.getMessage());
                return false;
            }
        }

        // if nothing else block player, check if player can push the boulder
        for (IStaticInteractable entity : staticEntity) {
            try {
                if (entity instanceof Boulder) {
                    entity.interactedBy(player);
                    // LOGIC: if interactedBy() does NOT throw the Exception, then the boulder has moved
                    // thus its safe for player to also move. 
                    // DO NOT "return;" here, since we want to interact with other overlapping entities
                }
            } catch (InvalidActionException e) {
                System.out.println(e.getMessage());
                return false;
            }
        }

        return true;
    }


    private void interactWithOverlapCollections(Position destination) throws InvalidActionException {
        List<Entity> inCell = map.getEntitiesAt(destination);

        List<ICollectable> collections = inCell
                                        .stream()
                                        .filter(e -> (e instanceof ICollectable))
                                        .map(e -> (ICollectable) e)
                                        .collect(Collectors.toList());

        // interaction with collections
        for (ICollectable collection : collections) {
            try {
                collection.collectedBy(player);
            } catch (InvalidActionException e) {
                System.out.println(e.getMessage());

                // Inactive bombs will block players as a wall
                if (collection instanceof Bomb) {
                    throw new InvalidActionException("ERROR: Unpickable bomb, should never happened");
                }
            }
        }
    }

    // Variable used to help prevent recursive backtracking in multi-teleportaion cases (also works with multiple seperate teleportations)
    // MUST be declared outside scope of the fnc for obvious reasons
    private boolean haveFoundFinalDest = false;

    public void move(Position destination) throws InvalidActionException {
        if (previousPosition == null)
            previousPosition = getCurrentPosition();

        // reset it to False for a new teleportation event.
        this.haveFoundFinalDest = false;
        // Check if something is blocking the player
        if (ableToMove(destination)) {
            this.previousPosition = getCurrentPosition();

            // move the player
            map.moveEntityTo(player, destination);

            // deal with interaction of collections
            interactWithOverlapCollections(destination);

            // deal with Exit if its at new Pos
            interactWithExit(destination);

            // deal with interaction of overlapped portal
            Portal portal = getOverlapPortal();
            if (portal != null) {
                // Get destinationS of the current Portal, player can jump into (0th index is the original intended destination)
                List<Position> destinationList = portal.getDestinations(getDirection());
                for (Position destinationPos : destinationList) {
                    try {
                        // Call recursively on each new destination, IFF theres another Portal there
                        if (this.haveFoundFinalDest == true) {return;}
                        move(destinationPos);
                        // If exception not thrown, it is Safe to move into current Position in loop 
                        if (this.haveFoundFinalDest == true) {return;}
                        map.moveEntityTo(player, destinationPos); this.haveFoundFinalDest = true;//throw new InvalidActionException("Success multi-teleportaion");
                        // // throw exception here? so the final portal's destination down the line doesnt get overriden via backtracing in recursion
                        // System.out.println("Player new pos" + getPos() + ", in move()");
                        // break;
                    } catch (InvalidActionException e) {
                        // nothing here, just let the player overlap with portal without teleport
                        // this structure will allow player go in portal as much as possible
                        // and player will stop at the portal which he cannot goes in
                    }
                }
            }
        }
    }

    private Portal getOverlapPortal() {
        List<Entity> inCell = map.getEntitiesAt(getCurrentPosition());
        List<Portal> portal = inCell
                                .stream()
                                .filter(e -> (e instanceof Portal))
                                .map(e -> (Portal) e)
                                .collect(Collectors.toList());

        if (portal.size() == 0) return null;

        // If multiple portals overlap, always take the first one, it may be random, but it doesn't matter
        return portal.get(0);
    }

    private void interactWithExit(Position currPos) {
        List<Entity> inCell = map.getEntitiesAt(currPos);

        List<IStaticInteractable> interactables = inCell
                                        .stream()
                                        .filter(e -> (e instanceof IStaticInteractable))
                                        .map(e -> (IStaticInteractable) e)
                                        .collect(Collectors.toList());
        for (IStaticInteractable inter : interactables) {
            if (inter instanceof Exit) {
                Exit exit = (Exit) inter;
                try {
                    exit.interactedBy(player);
                } catch (InvalidActionException e) {
                    // do nothing
                }
            }
        }
    }
}
