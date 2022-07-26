package dungeonmania.DungeonObjects.Entities.Characters;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.IEnemy;
import dungeonmania.util.DungeonFactory.EntityStruct;
import dungeonmania.util.Tracker.Tracker;

// TODO: stubbed
public class Hydra extends Entity implements IEnemy {

    public Hydra(EntityStruct metaData, JSONObject config, Tracker tracker) {
        super(metaData);
        
    }

    @Override
    public void move() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public double getAttackDamage() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getHealth() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void death() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String getClasString() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
