package dungeonmania.DungeonObjects.Entities.LogicEntities;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.DungeonObjects.Entities.LogicEntities.Statics.FloorSwitch;
import dungeonmania.DungeonObjects.Entities.LogicEntities.Statics.LightBulb;
import dungeonmania.DungeonObjects.Entities.LogicEntities.Statics.SwitchDoor;
import dungeonmania.DungeonObjects.Entities.LogicEntities.Statics.Wire;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public abstract class logicProcessor {
    private static int getListSzieOfSwitch(List<Entity> list) {
        return list.stream()
                    .filter(e -> (e instanceof FloorSwitch))
                    .collect(Collectors.toList())
                    .size();
    }

    private static boolean hasSource(List<Entity> list) {
        int wires = list.stream()
                                .filter(e -> (e instanceof FloorSwitch))
                                .map(e -> (FloorSwitch) e)
                                .filter(e -> e.isActive())
                                .collect(Collectors.toList())
                                .size();

        return (wires > 0);
    }

    private static boolean hasWire(List<Entity> list) {
        int wires = list.stream()
                                .filter(e -> (e instanceof Wire))
                                .collect(Collectors.toList())
                                .size();

        return (wires > 0);
    }

    private static String[][] get2DMap(int x, int y) {
        String[][] matrix = new String[x][y];

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                matrix[i][j] = "0";
            }
        }

        return matrix;
    }

    private static boolean hasSwitchAt(Position pos, DungeonMap map) {
        List<Entity> list = map.getEntitiesAt(pos);
        boolean hasSwitch = (list.stream()
                            .filter(e -> (e instanceof FloorSwitch))
                            .collect(Collectors.toList())
                            .size() > 0);

        return hasSwitch;
    }

    private static boolean hasLogicEntityAt(Position pos, DungeonMap map) {
        List<Entity> list = map.getEntitiesAt(pos);
        boolean hasSwitch = (list.stream()
                            .filter(e -> ((e instanceof FloorSwitch) || (e instanceof Wire) || (e instanceof LightBulb) || (e instanceof SwitchDoor)))
                            .collect(Collectors.toList())
                            .size() > 0);

        return hasSwitch;
    }

    /**
     * get all entities in four dirct, check their state
     * @param pos
     * @return
     */
    public static JSONObject getAdjacentActive(Position pos, DungeonMap map) {
        int switch_num = 0;
        int active_switch_num = 0;
        int active_entity_num = 0;

        // four dirct
        Position up = pos.translateBy(Direction.UP);
        Position down = pos.translateBy(Direction.DOWN);
        Position left = pos.translateBy(Direction.LEFT);
        Position right = pos.translateBy(Direction.RIGHT);

        ArrayList<Position> dest_pos = new ArrayList<Position>();
        ArrayList<Entity> e_4_dirct = new ArrayList<Entity>();

        if (hasLogicEntityAt(up, map)) {
            dest_pos.add(up);
            e_4_dirct.addAll(map.getEntitiesAt(up));
        }
        if (hasLogicEntityAt(down, map)) {
            dest_pos.add(down);
            e_4_dirct.addAll(map.getEntitiesAt(down));
        }
        if (hasLogicEntityAt(left, map)) {
            dest_pos.add(left);
            e_4_dirct.addAll(map.getEntitiesAt(left));
        }
        if (hasLogicEntityAt(right, map)) {
            dest_pos.add(right);
            e_4_dirct.addAll(map.getEntitiesAt(right));
        }

        // count the number of switchs
        switch_num += getListSzieOfSwitch(e_4_dirct);

        // position of all active switch
        List<FloorSwitch> active_switchs= map.getLookup().keySet()
                                                .stream()
                                                .filter(e -> (e instanceof FloorSwitch))
                                                .map(e -> (FloorSwitch) e)
                                                .filter(e -> e.isActive())
                                                .collect(Collectors.toList());

        List<Position> active_switchs_pos = new ArrayList<>();
        active_switchs
                    .stream()
                    .forEach(e -> active_switchs_pos.add(map.getLookup().get(e)));

        final int row_start = map.getTopBound();                    // max y for map
        final int row_end = map.getBottomBound();                   // min y for map
        final int col_start = map.getLeftBound();                   // max x for map
        final int col_end = map.getRightBound();                    // min x for map
        final int rows = Math.abs(row_end - row_start) + 10;
        final int cols = Math.abs(col_end - col_start) + 10;

        // for each adjacent cell, there is at least one source connect to it
        for (Position destPos : dest_pos) {
            for (Position A_S_pos : active_switchs_pos) {

                // build up the map for current dest and source
                String[][] matrix = get2DMap(rows, cols);
                for (int i = row_start; i <= row_end; i++) {
                    for (int j = col_start; j <= col_end; j++) {
                        Position new_pos = new Position(j, i); // mirror
                        List<Entity> entities = map.getEntitiesAt(new_pos);

                        // the position of entities might be negative
                        // but not for 2D array :(
                        final int r = i - row_start;
                        final int c = j - col_start;

                        if (entities != null) { // might empty
                            if (new_pos.equals(destPos)) {
                                matrix[r][c] = "D"; // current dest
                            } else if (new_pos.equals(A_S_pos) && hasSource(entities)) {
                                matrix[r][c] = "S"; // current source
                            } else if (hasWire(entities)) {
                                matrix[r][c] = "1"; // wire
                            } else {
                                matrix[r][c] = "0"; // consider as block
                            }
                        } else {
                            matrix[r][c] = "0"; // consider as block
                        }
                    }
                }

                // for (int i = 0; i < rows; i++) {
                //     for (int j = 0; j < cols; j++) {
                //         System.out.printf("%s ", matrix[i][j]);
                //     }
                //     System.out.println(".");
                // }

                if (LogicPathFinder.hasPath(matrix, rows, cols)) {
                    if (hasSwitchAt(destPos, map)) {
                        // adjacent active switch
                        active_switch_num++;
                    }

                    // adjacent active entity
                    active_entity_num++;
                    break; // only one active path from source is needed for each adjacent cell
                }
            }
        }

        JSONObject json = new JSONObject();

        // number of adjacent switch
        json.put("switch_num", switch_num);
        // is all adjacent switch number active?
        json.put("all_switch_is_avtive", active_switch_num == switch_num);
        // number of adjacent active entities
        json.put("active_num", active_entity_num);

        return json;
    }
}
