package dungeonmania.DungeonObjects.Entities.Statics;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.DungeonObjects.Player;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.DungeonObjects.Entities.Characters.Mercenary;
import dungeonmania.Interfaces.IStaticInteractable;
import dungeonmania.util.Position;
import dungeonmania.util.DungeonFactory.EntityStruct;
import dungeonmania.DungeonObjects.EntityTypes;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.exceptions.*;

public class Portal extends Entity implements IStaticInteractable {
    private String colour;
    // private Portal pairPortal;
    
    // #################################################################################
    /* NOTE !!!!!!!!
    * Read section 4.1.1 --> portals are linked via colours!! never will be 2 pairs of same colour !!
    */ 
    // #################################################################################

    public Portal(EntityStruct metaData, String colour) {
        super(metaData);
        this.colour = colour;
    }
    
    @Override
    public void interactedBy(Entity interactor) throws InvalidActionException {
        // NOTE: PLAY THE PORTALS_ADVANCED SETTING !!!!!!

        // FIRST check if interactor is either Player or Merc, since only they can teleport (for now...)
        if (!(interactor instanceof Player || interactor instanceof Mercenary)) {throw new InvalidActionException("Only Player & Merc can teleport");}
        // SECOND determine where interactor must land from pair Portal, based on where Iteractor is coming from (N,E,S or W)
        Position interactorPos = super.getMap().getEntityPos(interactor);
        Direction destinationDir = determineDestinationDirection(interactorPos);
        // THIRD get pair portal
        Portal pairPortal = getPairPortal(this.colour);
        Position pairPortalPos = super.getMap().getEntityPos(pairPortal);
        Position destinationPos = pairPortalPos.translateBy(destinationDir);
        // FINAL CHECKINGS # # # # # # # # # # # # #
        List<Entity> entitiesAtPos = super.getMap().getEntitiesAt(destinationPos);
        // FIRSTLY check for a wall at the next location, if so, do not teleport.
        if ((entitiesAtPos.stream().filter(e -> e.getType().equals(EntityTypes.WALL.toString())).count() > 0)) {throw new InvalidActionException("Entity cannot teleport onto Wall");}
        // SECONDLY if theres a boulder at next location...
        if ((entitiesAtPos.stream().filter(e -> e.getType().equals(EntityTypes.BOULDER.toString())).count() > 0)) {
            // ...and IF entity is a Merc, then do not teleport, merc's cant push boulders,
            if (interactor instanceof Mercenary) {throw new InvalidActionException("Mercenary cannot push the boulder at teleport location");}
            // ...and IF its a player, call boulder.interactBy(Player), and assess what it returns
            try {
                List<Boulder> boulders = entitiesAtPos.stream().filter(e -> e instanceof Boulder).map(e -> (Boulder) e).collect(Collectors. toList());
                // Can only be one boulder in a cell
                Boulder boulder = boulders.get(0);
                // If boulder interaction fails here (obstructed via wall or another boulder etc.), then it will throw an exception
                boulder.interactedBy(interactor);
                // DO NOT call "return;" here, let the Portal If statement pass, in cases of when the Boulder is overlapping a Portal
            } catch (InvalidActionException e) {
                // Now we know boulder cannot be moved, therefore throw exception for Player.java to catch
                throw new InvalidActionException("Player cannot push the boulder at teleport location");
            }
        }
        // THIRDLY, if theres another portal at the end, finds its next location, and go thru all these checks again. 
        if ((entitiesAtPos.stream().filter(e -> e.getType().equals(EntityTypes.PORTAL.toString())).count() > 0)) {
            // NOTE: keep MERCs in mind (dont hardcode for player)
            try {
                List<Portal> portals = entitiesAtPos.stream().filter(e -> e instanceof Portal).map(e -> (Portal) e).collect(Collectors. toList());
                Portal portalAtNewPos = portals.get(0);
                portalAtNewPos.interactedBy(interactor);
            } catch (InvalidActionException e) {
                // NOTE: if at any point in the recursion such Exception is thrown, it'll be caught, and added to this new exception to be thrown
                throw new InvalidActionException("The final portal's adjacent location is unteleportable; " + e.toString());
            }
        }
        // FINALLY, Portal is good to teleport player
        // note: even tho the last recorsion call, calls this fnc, every other previous iteration will also call this, so i need a way to detect if 
        // interactor has been moved sometime inside the recursion inception (maybe catch another exception??). 
        super.getMap().moveEntityTo(interactor, destinationPos);
        // // IGNORE: Sorta need this to indicate to Player.java's 'ableToMove()', that it doesnt need to move.
        // throw new InvalidActionException("Interactor already teleported");
    }
    

    public Position getDestination() {
        return getMap().getEntityPos(this.pairPortal);
    }


    // Determine direction to destination, relative to interaction (left, right, above, below)
    // which is the direction (from the pair portal) which the interactor must land onto.
    // For e.g. if relative interaction Direction is from the LEFT, then destination direction is RIGHT of the pair portal.
    public Direction determineDestinationDirection(Position interactorPos) {
        Position portalPos = super.getMap().getEntityPos(this);
        if (portalPos.getX() + 1 == interactorPos.getX()) {
            // Interacting from Right of portal, so teleport to Left of next portal
            return Direction.LEFT;

        }  else if (portalPos.getX() - 1 == interactorPos.getX()) {
            // Interacting from Left of Portal, so teleport to Right of next portal
            return Direction.RIGHT;

        } else if (portalPos.getY() - 1 == interactorPos.getY()) {
            // Interacting from Above of Portal, so teleport to Below next portal
            return Direction.DOWN;
            
        } else {
            // Interacting from Below of Portal, so teleport to Above next portal
            return Direction.UP;
        }
    }


    // Get the Pair Portal via map
    public Portal getPairPortal(String colour) {
        List<Portal> bothPortals = getAllPortals().stream().filter(e -> e.colour.equals(this.colour)).collect(Collectors.toList());
        Position thisPortalPos = super.getMap().getEntityPos(this);
        if (super.getMap().getEntityPos(bothPortals.get(0)).equals(thisPortalPos)) {
            // index 0 is this. portal, so return index 1
            return bothPortals.get(1);
        } else {
            return bothPortals.get(0);
        }
    }

    public List<Portal> getAllPortals() {
        return super.getMap().getAllEntities().stream()
                                                .filter(e -> e instanceof Portal)
                                                .map(e -> (Portal) e)
                                                .collect(Collectors. toList());
    }


}
