package dungeonmania.DungeonObjects.Entities.Statics;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.DungeonObjects.Player;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.IStaticInteractable;
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

        // Run through the series of checks (can only teleport Player for now)
        // then teleport them adjacent cardinal direction of pair portal
        // i.e if interactor is of left, then teleport them to pairPortal's right

        // FIRST check if interactor instanceof Player, since only player can teleport (for now...)
        if (!(interactor instanceof Player)) {throw new InvalidActionException("Only Player can move boulder");}
        // SECOND determine where interactor is coming from (N,E,S or W)
        Position interactorPos = super.getMap().getEntityPos(interactor);
        Direction relInteractionDir = determineRelativeInteractionDirection(interactorPos);
        // THIRD get pair portal
        Portal pairPortal = getPairPortal(this.colour);
        // Then check for a wall at the next location, if so, do not teleport.
        // NOTE: If it's a Portal, then teleport IN A LOOP and do further checkings until
        //       the destination does not have a portal (RECURSION, w/ ender being if theres a wall, or )
        //       IFF theres a wall, return type "wall"
        //       IFF theres a boulder, return type "boulder", which the portal then does boulder.interactBy(interactor)
    }

    public Direction determineRelativeInteractionDirection(Position interactorPos) {
        // Determine relative direction of interaction (left, right, above, below)
        // and return which direction of the portal, the interactor must land.
        // For e.g. if relative interaction Direction is from the LEFT, then must teleport RIGHT of the pair portal.
        Position portalPos = super.getMap().getEntityPos(this);
        if (portalPos.getX() + 1 == interactorPos.getX()) {
            // Interacting from Right of portal, so teleport to Left
            return Direction.LEFT;

        }  else if (portalPos.getX() - 1 == interactorPos.getX()) {
            // Interacting from Left of Portal, so teleport to Right
            return Direction.RIGHT;

        } else if (portalPos.getY() - 1 == interactorPos.getY()) {
            // Interacting from Above of Portal, so teleport to Above
            return Direction.UP;
            
        } else {
            // Interacting from Below of Portal, so teleport Below
            return Direction.DOWN;
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


    // For cases of 
    // 1) multiple teleportation across many portal pairs, within one Player movement
    // 2) player teleportation destination has a boulder on it, 
    //                              which is then returned and EITHER handled by this class or call entity.boulderInteraction() fnc in Player.java (then refactoring file is needed)
    // 3) player teleportation destination has a wall, which is then returned, and detected, and UnInteractable is thrown by interactedBy()
    // IF none of these, then returns null, which is caught, 
    public Entity recursiveCheckingIfNewPosHasEntity(Entity destEntity, Direction dirToLandTo) {
        // FIRST get position to land to, via direction
        // SECOND get Entity at that position
        return null;
    }
}
