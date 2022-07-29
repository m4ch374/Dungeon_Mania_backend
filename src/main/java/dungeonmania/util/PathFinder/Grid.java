package dungeonmania.util.PathFinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dungeonmania.DungeonObjects.EntityTypes;
import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.DungeonObjects.Entities.Statics.Door;
import dungeonmania.DungeonObjects.Entities.Statics.Portal;
import dungeonmania.DungeonObjects.Entities.Statics.SwampTile;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Grid {
    // Bounds
    private int minX;
    private int maxX;

    private int minY;
    private int maxY;

    private Map<Position, Set<Edge>> gridMap = new HashMap<Position, Set<Edge>>();

    private Grid(DungeonMap map) {
        minX = map.getLeftBound() - 1;
        maxX = map.getRightBound() + 1;

        minY = map.getTopBound() - 1;
        maxY = map.getBottomBound() + 1;
    }

    public Map<Position, Set<Edge>> getGridMap() {
        return gridMap;
    }

    public int getLeftBound() {
        return minX;
    }

    public int getRightBound() {
        return maxX;
    }

    public int getTopBound() {
        return minY;
    }

    public int getBottomBound() {
        return maxY;
    }

    public static Grid buildGrid(DungeonMap map) {
        Grid grid = new Grid(map);
        setGridElements(grid, map);

        return grid;
    }

    // Returns an adjaceny list
    private static void setGridElements(Grid grid, DungeonMap map) {
        for (int currX = grid.minX; currX <= grid.maxX; currX++) {
            for (int currY = grid.minY; currY <= grid.maxY; currY++) {
                Position currPos = new Position(currX, currY);

                if (!cellIsBlocked(map, currPos))
                    processCurrentCell(grid, map, currPos);
            }
        }
    }

    private static void processCurrentCell(Grid grid, DungeonMap map, Position currCell) {
        List<Position> adjacentCells = getAdjacentCells(grid, map, currCell);

        SwampTile tile = getSwampTile(map, currCell);
        int edgeWeight = (tile == null) ? 1 : tile.getMovementFactor() + 1;

        Set<Edge> connectedEdges = new HashSet<Edge>();
        for (Position cell: adjacentCells) {
            connectedEdges.add(new Edge(currCell, cell, edgeWeight));
        }
        
        grid.gridMap.put(currCell, connectedEdges);
    }

    private static List<Position> getAdjacentCells(Grid grid, DungeonMap map, Position currCell) {
        List<Position> adjacentCells = new ArrayList<Position>();
        
        List<Direction> possibleDirections = getPossibleDirections(grid, currCell);
        for (Direction direction : possibleDirections) {
            Position adjacentCell = currCell.translateBy(direction);
            
            if (cellIsBlocked(map, adjacentCell))
                continue;
            
            adjacentCells.add(adjacentCell);

            // Would want a better way to do this but hey...
            // Im not the person who code up portal ¯\_(ツ)_/¯
            Portal adjacentPortal = getPortal(map, adjacentCell);
            if (adjacentPortal == null)
                continue;

            Position portalDestination = adjacentPortal.getDestinations(adjacentPortal.determineDestinationDirection(currCell)).get(0);
            if (!cellIsBlocked(map, portalDestination) && getPortal(map, portalDestination) == null)
                adjacentCells.add(portalDestination);
        }

        return adjacentCells;
    }

    private static List<Direction> getPossibleDirections(Grid grid, Position currCell) {
        List<Direction> possibleDirections = new ArrayList<>(Arrays.asList(Direction.values()));

        if (currCell.getX() == grid.minX)
            possibleDirections.remove(Direction.LEFT);

        if (currCell.getX() == grid.maxX)
            possibleDirections.remove(Direction.RIGHT);

        if (currCell.getY() == grid.minY)
            possibleDirections.remove(Direction.UP);

        if (currCell.getY() == grid.maxY)
            possibleDirections.remove(Direction.DOWN);

        return possibleDirections;
    }

    private static boolean cellIsBlocked(DungeonMap map, Position cell) {
        List<Entity> entities = map.getEntitiesAt(cell);

        boolean hasWall = entities.stream().filter(e -> e.getType().equals(EntityTypes.WALL.toString())).count() > 0;
        boolean hasLockedDoors = entities.stream()
                                    .filter(e -> e.getType().equals(EntityTypes.DOOR.toString()))
                                    .map(e -> (Door) e)
                                    .filter(d -> !d.isOpen())
                                    .count() > 0;

        return hasWall || hasLockedDoors;
    }

    private static Portal getPortal(DungeonMap map, Position pos) {
        Entity portal = map.getEntitiesAt(pos).stream().filter(e -> e.getType().equals(EntityTypes.PORTAL.toString())).findFirst().orElse(null);

        if (portal == null)
            return null;

        return (Portal) portal;
    }

    private static SwampTile getSwampTile(DungeonMap map, Position pos) {
        return map.getEntitiesAt(pos).stream()
                .filter(e -> e.getType().equals(EntityTypes.SWAMP_TILE.toString()))
                .map(e -> (SwampTile) e)
                .findFirst()
                .orElse(null);
    }
}
