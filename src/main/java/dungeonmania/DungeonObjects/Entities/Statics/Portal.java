package dungeonmania.DungeonObjects.Entities.Statics;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.IStaticInteractable;
import dungeonmania.util.DungeonFactory.EntityStruct;

public class Portal extends Entity implements IStaticInteractable {
    private String colour;
    public Portal pairPortal;

    public Portal(EntityStruct metaData, String colour) {
        super(metaData);
        this.colour = colour;
    }

    @Override
    public void interactedBy(Entity interactor) { }
    // NOTE: PLAY THE PORTALS_ADVANCED SETTING !!!!!!
}
