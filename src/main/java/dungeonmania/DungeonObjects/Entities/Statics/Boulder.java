package dungeonmania.DungeonObjects.Entities.Statics;

import dungeonmania.DungeonObjects.Player;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.IStaticInteractable;
import dungeonmania.util.DungeonFactory.EntityStruct;
import dungeonmania.DungeonObjects.EntityTypes;
import dungeonmania.util.Position;
import dungeonmania.exceptions.*;

import java.util.List;

public class Boulder extends Entity implements IStaticInteractable {

    public Boulder(EntityStruct metaData) {
        super(metaData);
    }

    @Override
    public void interactedBy(Entity interactor) throws InvalidActionException{ 
        // FIRST check if interactor instanceof Player, since only player can push boulder (for now...)
        if (!(interactor instanceof Player)) {throw new InvalidActionException("Only Player can move boulder");}
        // SECOND determine new position, relative to interactor's
        Position newPos = determineNewPosition(interactor);
        // THIRD check if the moveTo position contains a boulder OR Wall
        if (!canMoveIntoNewPositionCell(newPos)) {throw new InvalidActionException("Player cannot move two boulders at once");}
        // Finally, move the boulder
        move(newPos);
    }

    public Position determineNewPosition(Entity interactor) {
        Position interactorPos = super.getMap().getEntityPos(interactor);
        Position boulderPos = super.getMap().getEntityPos(this);
        // Use a 5x5 grid to help visualise this
        if (boulderPos.getX() == interactorPos.getX() - 1) {
            // Interactor pushing from Right of boulder, so move to Left
            return new Position(boulderPos.getX() - 1, boulderPos.getY());

        } else if (boulderPos.getX() == interactorPos.getX() + 1) {
            // Interactor pushing from Left of boulder, so move to Right
            return new Position(boulderPos.getX() + 1, boulderPos.getY());

        } else if (boulderPos.getY() == interactorPos.getY() - 1) {
            // Interactor pushing from Below boulder, so move Above
            return new Position(boulderPos.getX(), boulderPos.getY() - 1);
            
        } else {
            // Interactor pushing from Above boulder, so move Below
            return new Position(boulderPos.getX(), boulderPos.getY() + 1);
        }
    }

    public boolean canMoveIntoNewPositionCell(Position newPos) {
        // Confirm there are no Boulders OR Walls at new position
        List<Entity> entities = super.getMap().getEntitiesAt(newPos);
        // Returns true if their are 0 items in the given postion of the given type
        boolean noWall = (entities.stream().filter(e -> e.getType().equals(EntityTypes.WALL.toString())).count() == 0);
        boolean noBoulder = (entities.stream().filter(e -> e.getType().equals(EntityTypes.BOULDER.toString())).count() == 0);
        
        // System.out.println("canMove? " + !(noWall && noBoulder) + (noWall && noBoulder) + noWall + noBoulder); ///////////////////////////////////////////////////////
        
        return (noWall && noBoulder);

    }

    public void move(Position newPos) {
       super.getMap().moveEntityTo(this, newPos);
    }
    
}
