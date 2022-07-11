package dungeonmania.DungeonObjects.Entities.Statics;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.IStaticInteractable;
import dungeonmania.util.DungeonFactory.EntityStruct;

public class Portal extends Entity implements IStaticInteractable {
    private String colour;
    private Portal pairPortal;

    public Portal(EntityStruct metaData, String colour) {
        super(metaData);
        this.colour = colour;
        // this.pairPortal = new Portal(metaData, colour)
        // OR 
        //    feed it this portal also, so constructor becomes Portal(EntityStruct, String, Portal);
        //    and this portal will be fed "null", whereas the pairPortal will be fed "this".
    }

    @Override
    public void interactedBy(Entity interactor) {
        // NOTE: PLAY THE PORTALS_ADVANCED SETTING !!!!!!

        // Run through the series of checks (can only teleport Player for now)
        // then teleport them adjacent cardinal direction of pair portal
        // i.e if interactor is of left, then teleport them to pairPortal's right

    }
}
