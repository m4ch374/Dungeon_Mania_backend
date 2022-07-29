package dungeonmania.util.PathFinder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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

        Map<Position, Set<Edge>> gridMap = grid.getGridMap();

        Map<Position, Integer> distance = getInitialDisance(gridMap.keySet(), source);
        Map<Position, Position> prevPos = getInitialPrevPos(gridMap.keySet(), source);

        Set<Position> allPos = new HashSet<Position>(gridMap.keySet());

        while(!allPos.isEmpty()) {
            Position currFrom = getMinDistVertex(allPos, distance);
            if (currFrom == null)
                return getBacktrackedPos(prevPos, source, destination);

            allPos.remove(currFrom);

            Set<Edge> connectedEdges = gridMap.get(currFrom);

            for (Edge connectedEdge : connectedEdges) {
                Position to = connectedEdge.getTo();

                int edgeWeight = connectedEdge.getWeight();

                int currDist = distance.get(currFrom);
                int adjDist = distance.get(to);

                if (adjDist == INF || currDist + edgeWeight < adjDist) {
                    distance.replace(to, currDist + edgeWeight);
                    prevPos.replace(to, currFrom);
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

        if (backtrackedPos == null)
            return null;

        if (backtrackedPos.equals(source))
            return currPos;

        return getBacktrackedPos(prevPos, source, backtrackedPos);
    }

    private static boolean outOfBounds(Grid grid, Position pos) {
        boolean outOfHorizontalBounds = pos.getX() < grid.getLeftBound() || pos.getX() > grid.getRightBound();
        boolean outOfVerticalBounds = pos.getY() < grid.getTopBound()  || pos.getY() > grid.getBottomBound();

        return outOfHorizontalBounds || outOfVerticalBounds;
    }

    private static Position getMinDistVertex(Set<Position> allPos, Map<Position, Integer> dist) {
        int minDist = Integer.MAX_VALUE;
        Position minPos = null;

        for (Position pos : allPos) {
            int distance = dist.get(pos);

            if (distance != INF && distance < minDist) {
                minDist = distance;
                minPos = pos;
            }
        }
        return minPos;
    }
}
