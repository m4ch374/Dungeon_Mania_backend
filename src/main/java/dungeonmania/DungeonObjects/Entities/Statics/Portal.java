package dungeonmania.DungeonObjects.Entities.Statics;

import java.util.List;
import java.util.ArrayList;
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

        // These following checks are for this instance of the portal and its pair (no succeding portals via recursion is included)
        // FIRST check if interactor is either Player or Merc, since only they can teleport (for now...)
        if (!(interactor instanceof Player || interactor instanceof Mercenary)) {throw new InvalidActionException("Only Player & Merc can teleport");}
        // SECOND determine which direction interactor must land to, from pair Portal, based on where Iteractor is coming from (N,E,S or W)
        Position interactorPos = super.getMap().getEntityPos(interactor);
        Direction destinationDir = determineDestinationDirection(interactorPos);
        // THIRD get destination position Interactor must teleport to (cardinally adjacent to pairPortal)
        Position destinationPos = getDestinations(destinationDir).get(0);

        // FINAL CHECKINGS # # # # # # # # # # # # #
        List<Entity> entitiesAtPos = super.getMap().getEntitiesAt(destinationPos);
        // FIRSTLY check for a wall at the to-teleport location, if so, do not teleport.
        if ((entitiesAtPos.stream().filter(e -> e.getType().equals(EntityTypes.WALL.toString())).count() > 0)) {throw new InvalidActionException("Entity cannot teleport onto Wall");}
        // SECONDLY if theres a boulder at to-teleport location...
        if ((entitiesAtPos.stream().filter(e -> e.getType().equals(EntityTypes.BOULDER.toString())).count() > 0)) {
            // 1) ...and IF entity is a Merc, then do not teleport, merc's cant push boulders,
            if (interactor instanceof Mercenary) {throw new InvalidActionException("Mercenary cannot push the boulder at teleport location");}
            // 2) ...and IF its a player, call boulder.interactBy(Player), and assess what it returns
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

        // IGNORE THIS NOTE, ITS WRONG
        // NOTE: this ihteractedBy() is used to pass Player.java's ableToMove() fnc.
        //      The multi-teleportation issue is apprehended in Player.java's move() fnc, which calls portal.getDestination(), 
        //      and the next portal in line is recursively updated each time, 
        //      whilst also going thru all the initial checkings (in this fnc), due to ableToMove() being called first in move()
    }
    
    // Get List of Destinations entity can teleport to (which are cardinally adjacent to the Pair Portal),
    // AND the first index is the intended destination, the others are used only if the intended is impossible
    public List<Position> getDestinations(Direction interactorDir) {
        Portal pairPortal = getPairPortal(this.colour);
        Position pairPortalPos = super.getMap().getEntityPos(pairPortal);
        // use "interactorDir" to add the intended destination to the first index of the list, which will be run by Player.java first
        Position intendedDestinationPos = pairPortalPos.translateBy(interactorDir);
        List<Position> interactorsDestinations = new ArrayList<Position>();
        interactorsDestinations.add(intendedDestinationPos);

        // Add all the Positions translated by Directions which are not the original intended destination 
        if (!interactorDir.equals(Direction.UP)) {interactorsDestinations.add(pairPortalPos.translateBy(Direction.UP));}
        if (!interactorDir.equals(Direction.RIGHT)) {interactorsDestinations.add(pairPortalPos.translateBy(Direction.RIGHT));}
        if (!interactorDir.equals(Direction.DOWN)) {interactorsDestinations.add(pairPortalPos.translateBy(Direction.DOWN));}
        if (!interactorDir.equals(Direction.LEFT)) {interactorsDestinations.add(pairPortalPos.translateBy(Direction.LEFT));}

        return interactorsDestinations;
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
