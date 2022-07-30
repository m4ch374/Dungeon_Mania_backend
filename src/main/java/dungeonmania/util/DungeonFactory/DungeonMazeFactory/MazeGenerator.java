package dungeonmania.util.DungeonFactory.DungeonMazeFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import dungeonmania.DungeonObjects.EntityTypes;
import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.DungeonObjects.Entities.Statics.Wall;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.util.DungeonFactory.EntityStruct;

public class MazeGenerator {
    private static final boolean EMPTY = true;
    private static final boolean WALL = false;
    
    private final int minX;
    private final int maxX;
    private final int minY;
    private final int maxY;

    private DungeonMap map;

    public MazeGenerator(MazeGenMetadata metadata) {
        minX = metadata.getxStart();
        maxX = metadata.getxEnd();
        minY = metadata.getyStart();
        maxY = metadata.getyEnd();
        map = new DungeonMap(new Position(minX - 1, minY - 1), new Position(maxX + 1, maxY + 1));
    }

    public DungeonMap generateMazeMap() {
        Map<Position, Boolean> mazeData = getMazeData();

        int wallID = 0;
        for (int i = minX; i <= maxX; i++) {
            for (int j = minY; j <= maxY; j++) {
                Position currPos = new Position(i, j);

                if (mazeData.get(currPos) == WALL) {
                    map.placeEntityAt(generateWall(wallID), currPos);
                    wallID++;
                }
            }
        }

        // Surround border with walls
        for (int i = map.getLeftBound(); i <= map.getRightBound(); i++) {
            Position topPos = new Position(i, map.getTopBound());
            Position bottomPos = new Position(i, map.getBottomBound());

            map.placeEntityAt(generateWall(wallID), topPos);
            wallID++;

            map.placeEntityAt(generateWall(wallID), bottomPos);
            wallID++;
        }

        for (int i = minY; i <= maxY; i++) {
            Position leftPos = new Position(map.getLeftBound(), i);
            Position rightPos = new Position(map.getRightBound(), i);

            map.placeEntityAt(generateWall(wallID), leftPos);
            wallID++;

            map.placeEntityAt(generateWall(wallID), rightPos);
            wallID++;
        }

        return map;
    }

    private Map<Position, Boolean> getMazeData() {
        Map<Position, Boolean> mazeData = getInitialMazeData();

        Position startPos = new Position(minX, minY);
        mazeData.replace(startPos, EMPTY);

        Random random = new Random();
        List<Position> options = getWallNeighbours(startPos, mazeData);
        while(!options.isEmpty()) {
            Position currPos = options.remove(random.nextInt(options.size()));
            List<Position> neighbours = getEmptyNeighbours(currPos, mazeData);
            if (!neighbours.isEmpty()) {
                Position neighbour = neighbours.get(random.nextInt(neighbours.size()));
                mazeData.replace(currPos, EMPTY);

                Position midPoint = new Position((currPos.getX() + neighbour.getX()) / 2, (currPos.getY() + neighbour.getY()) / 2);
                mazeData.replace(midPoint, EMPTY);

                mazeData.replace(neighbour, EMPTY);
            }
            
            options.addAll(getWallNeighbours(currPos, mazeData));
        }

        Position endPos = new Position(maxX, maxY);
        if (mazeData.get(endPos) == WALL) {
            mazeData.replace(endPos, EMPTY);

            List<Position> neighbours = getCardinalNeighbour(endPos, mazeData);
            if (neighbours.stream().filter(n -> mazeData.get(n) == EMPTY).count() == 0)
                mazeData.replace(neighbours.get(random.nextInt(neighbours.size())), EMPTY);
        }

        return mazeData;
    }

    private Map<Position, Boolean> getInitialMazeData() {
        Map<Position, Boolean> mazeData = new HashMap<Position, Boolean>();

        for (int i = minX; i <= maxX; i++) {
            for (int j = minY; j <= maxY; j++) {
                mazeData.put(new Position(i, j), WALL);
            }
        }
        return mazeData;
    }

    private List<Position> getWallNeighbours(Position currPos, Map<Position, Boolean> mazeData) {
        return getLegalNeighbours(currPos, mazeData).stream().filter(n -> mazeData.get(n) == WALL).collect(Collectors.toList());
    }

    private List<Position> getEmptyNeighbours(Position currPos, Map<Position, Boolean> mazeData) {
        return getLegalNeighbours(currPos, mazeData).stream().filter(n -> mazeData.get(n) == EMPTY).collect(Collectors.toList());
    }

    private List<Position> getLegalNeighbours(Position currPos, Map<Position, Boolean> mazeData) {
        List<Position> translatePos = getTranslatePos();
        List<Position> legalNeighbours = new ArrayList<Position>();

        for (Position direction : translatePos) {
            Position possiblePos = currPos.translateBy(direction);
            if (mazeData.containsKey(possiblePos))
                legalNeighbours.add(possiblePos);
        }

        return legalNeighbours;
    }

    private List<Position> getTranslatePos() {
        return new ArrayList<Position>(
            Arrays.asList(
                new Position(-2, 0),
                new Position(2, 0),
                new Position(0, -2),
                new Position(0, 2)
            )
        );
    }

    private List<Position> getCardinalNeighbour(Position currPos, Map<Position, Boolean> mazeData) {
        List<Position> neighbour = new ArrayList<Position>();

        for (Direction direction : Direction.values()) {
            Position possiblePos = currPos.translateBy(direction);
            if (mazeData.containsKey(possiblePos))
                neighbour.add(possiblePos);
        }

        return neighbour;
    }

    private Entity generateWall(int wallID) {
        String id = wallID == 0 ? EntityTypes.WALL.toString() : EntityTypes.WALL.toString() + wallID;
        EntityStruct metaData = new EntityStruct(id, EntityTypes.WALL.toString(), map);
        Entity wall = new Wall(metaData);

        return wall;
    }

    public static MazeGenerator initGenerator(MazeGenMetadata metadata) {
        return new MazeGenerator(metadata);
    }
}
