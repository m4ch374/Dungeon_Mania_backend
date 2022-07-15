package dungeonmania.DungeonObjects.Entities.Statics;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.ISpawnable;
import dungeonmania.Interfaces.IStaticInteractable;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.DungeonFactory.EntityStruct;
import dungeonmania.exceptions.*;

public class ZombieToastSpawner extends Entity implements IStaticInteractable, ISpawnable {
    int zombieSpawnRate;

    public ZombieToastSpawner(EntityStruct metaData, JSONObject config) {
        super(metaData);
        this.zombieSpawnRate = config.getInt("zombie_spawn_rate");
    }

    @Override
    public EntityResponse toEntityResponse() { return null; }

    @Override
    public void spawn() {}

    @Override
    public void interactedBy(Entity interactor) throws InvalidActionException {
        // Gets destroyed if player has a weapon
        // but does weapon wear & tear??
    }
    
}
