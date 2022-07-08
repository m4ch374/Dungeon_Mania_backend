package dungeonmania.util.DungeonFactory;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.DungeonObjects.Player;
import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.DungeonObjects.Entities.Characters.*;
import dungeonmania.DungeonObjects.Entities.Collectables.*;
import dungeonmania.DungeonObjects.Entities.Statics.*;
import dungeonmania.util.Position;

import static dungeonmania.DungeonObjects.EntityTypes.*;

public class DungeonMapParser {
    public static DungeonMap buildDungeonMap(JSONArray entities, JSONObject config) {
        DungeonMap map = new DungeonMap();
        Map<String, Integer> idMap = new HashMap<String, Integer>();

        for (int i = 0; i < entities.length(); i++) {
            JSONObject entityJson = entities.getJSONObject(i);
            Position entityPos = new Position(entityJson.getInt("x"), entityJson.getInt("y"));
            Entity entity = buildEntity(entityJson, idMap, map, config);
            map.placeEntityAt(entity, entityPos);
        }

        return map;
    }

    private static Entity buildEntity(JSONObject entityJson, Map<String, Integer> idMap, DungeonMap map, JSONObject config) {
        String entityType = entityJson.getString("type");
        String entityId = getEntityId(idMap, entityType);
        EntityStruct metaData = new EntityStruct(entityId, entityType, map);

        // This is some horendous yandere simulator shit
        // Would use switch case after migrating entity types to enums
        if (entityType.equals(PLAYER)) {
            return new Player(metaData, config);
        } else if (entityType.equals(WALL)) {
            return new Wall(metaData);
        } else if (entityType.equals(EXIT)) {
            return new Exit(metaData);
        } else if (entityType.equals(BOULDER)) {
            return new Boulder(metaData);
        } else if (entityType.equals(FLOOR_SWITCH)) {
            return new FloorSwitch(metaData);
        } else if (entityType.equals(DOOR)) {
            int pairedKey = entityJson.getInt("key");
            return new Door(metaData, pairedKey);
        } else if (entityType.equals(PORTAL)) {
            String portalColour = entityJson.getString("colour");
            return new Portal(metaData, portalColour);
        } else if (entityType.equals(ZOMBIE_TOAST_SPAWNER)) {
            return new ZombieToastSpawner(metaData, config);
        } else if (entityType.equals(SPIDER)) {
            return new Spider(metaData, config);
        } else if (entityType.equals(ZOMBIE_TOAST)) {
            return new ZombieToast(metaData, config);
        } else if (entityType.equals(MERCENARY)) {
            return new Mercenary(metaData, config);
        } else if (entityType.equals(TREASURE)) {
            return new Treasure(metaData);
        } else if (entityType.equals(KEY)) {
            int keyId = entityJson.getInt("key");
            return new Key(metaData, keyId);
        } else if (entityType.equals(INVINCIBILITY_POTION)) {
            return new InvincibilityPotion(metaData, config);
        } else if (entityType.equals(INVISIBILITY_POTION)) {
            return new InvisibilityPotion(metaData, config);
        } else if (entityType.equals(WOOD)) {
            return new Wood(metaData);
        } else if (entityType.equals(ARROWS)) {
            return new Arrow(metaData);
        } else if (entityType.equals(BOMB)) {
            return new Bomb(metaData, config);
        } else if (entityType.equals(SWORD)) {
            return new Sword(metaData, config);
        } else {
            return null;
        }
    }

    private static String getEntityId(Map<String, Integer> idMap, String type) {
        idMap.putIfAbsent(type, 0);
        int currId = idMap.get(type);
        String entityId = currId == 0 ? type : type + String.valueOf(currId);
        idMap.put(type, currId++);
        return entityId;
    }
}
