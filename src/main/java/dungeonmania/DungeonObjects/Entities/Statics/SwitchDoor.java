package dungeonmania.DungeonObjects.Entities.Statics;

import java.util.Arrays;
import java.util.List;

import dungeonmania.util.DungeonFactory.EntityStruct;

// TODO: stubbed
//
// Note:
// idk, prolly extends door or some shit, Amith / Hanqi, implementation its up to you
public class SwitchDoor extends Door {
    private static final List<String> LOGIC_TYPES = Arrays.asList(new String[] {"and", "or", "xor", "co_and"});

    public SwitchDoor(EntityStruct metaData, int keyId, String logic) {
        super(metaData, keyId);
        //TODO Auto-generated constructor stub
    }
    
}
