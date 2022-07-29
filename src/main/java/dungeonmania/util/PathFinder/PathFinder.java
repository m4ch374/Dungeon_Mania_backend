package dungeonmania.util.PathFinder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.util.Position;

public class PathFinder {
    private static final int INF = -1;

    // ========================================
    // The BDSM algorithm thing
    //
    // B - Be
    // D - Da
    // S - Smart
    // M - Man
    //
    // Note:
    // Assumes that map is surrounded by walls such that both source and destination will not 
    // go out of bounds
    // ========================================
    public static Position getOptimalNextPos(DungeonMap map, Position source, Position destination) {
        Grid grid = Grid.buildGrid(map);

        if (outOfBounds(grid, source) || outOfBounds(grid, destination)) 
            return null;

        Map<Position, List<Position>> gridMap = grid.getGridMap();
        Map<Edge, Integer> edgeWeightMap = grid.getEdgeWeightMap();

        Map<Position, Integer> distance = getInitialDisance(gridMap.keySet(), source);
        Map<Position, Position> prevPos = getInitialPrevPos(gridMap.keySet(), source);

        Queue<Position> queue = new LinkedList<Position>(Arrays.asList(source));
        Set<Position> visitedPos = new HashSet<Position>();
        while(!queue.isEmpty()) {
            Position currPos = queue.poll();
            visitedPos.add(currPos);

            List<Position> adjacentPositions = gridMap.get(currPos);
            for (Position adjacentPos : adjacentPositions) {
                if (visitedPos.contains(adjacentPos))
                    continue;

                queue.add(adjacentPos);

                int edgeWeight = edgeWeightMap.get(new Edge(currPos, adjacentPos));

                int currDist = distance.get(currPos);
                int adjDist = distance.get(adjacentPos);

                if (adjDist == INF || currDist + edgeWeight < adjDist) {
                    distance.replace(adjacentPos, currDist + edgeWeight);
                    prevPos.replace(adjacentPos, currPos);
                }
            }
        }

        return getBacktrackedPos(prevPos, source, destination);
    }

    private static Map<Position, Integer> getInitialDisance(Set<Position> allPosition, Position source) {
        Map<Position, Integer> initialDistance = new HashMap<Position, Integer>();

        for (Position position: allPosition) {
            if (position.equals(source))
                initialDistance.put(position, 0);
            else
                initialDistance.put(position, INF);
        }

        return initialDistance;
    }

    private static Map<Position, Position> getInitialPrevPos(Set<Position> allPositions, Position source) {
        Map<Position, Position> prevPos = new HashMap<Position, Position>();

        for (Position pos: allPositions) {
            if (pos.equals(source))
                prevPos.put(pos, pos);
            else
                prevPos.put(pos, null);
        }

        return prevPos;
    }

    private static Position getBacktrackedPos(Map<Position, Position> prevPos, Position source, Position currPos) {
        if (currPos == null)
            return null;

        Position backtrackedPos = prevPos.get(currPos);

        if (backtrackedPos == source)
            return currPos;

        return getBacktrackedPos(prevPos, source, backtrackedPos);
    }

    private static boolean outOfBounds(Grid grid, Position pos) {
        boolean outOfHorizontalBounds = pos.getX() < grid.getLeftBound() || pos.getX() > grid.getRightBound();
        boolean outOfVerticalBounds = pos.getY() < grid.getTopBound()  || pos.getY() > grid.getBottomBound();

        return outOfHorizontalBounds || outOfVerticalBounds;
    }
}
