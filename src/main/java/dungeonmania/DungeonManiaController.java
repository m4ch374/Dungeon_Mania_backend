package dungeonmania;

import dungeonmania.DungeonObjects.DungeonState;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.DungeonFactory.DungeonBuilder;
import dungeonmania.util.DungeonFactory.DungeonMazeFactory.MazeGenMetadata;

import java.util.List;

public class DungeonManiaController {
    private DungeonState dungeonState;

    public String getSkin() {
        return "default";
    }

    public String getLocalisation() {
        return "en_US";
    }

    /**
     * /dungeons
     */
    public static List<String> dungeons() {
        return FileLoader.listFileNamesInResourceDirectory("dungeons");
    }

    /**
     * /configs
     */
    public static List<String> configs() {
        return FileLoader.listFileNamesInResourceDirectory("configs");
    }

    /**
     * /game/new
     */
    public DungeonResponse newGame(String dungeonName, String configName) throws IllegalArgumentException {
        try {
            dungeonState = DungeonBuilder.initializeBuilder().useMap(dungeonName).withConfig(configName).build();
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        
        return dungeonState.toDungeonResponse();
    }

    /**
     * /game/dungeonResponseModel
     */
    public DungeonResponse getDungeonResponseModel() {
        return dungeonState.toDungeonResponse();
    }

    /**
     * /game/tick/item
     */
    public DungeonResponse tick(String itemUsedId) throws IllegalArgumentException, InvalidActionException {
        dungeonState.tick(itemUsedId);
        return dungeonState.toDungeonResponse();
    }

    /**
     * /game/tick/movement
     */
    public DungeonResponse tick(Direction movementDirection) {
        dungeonState.tick(movementDirection);
        return dungeonState.toDungeonResponse();
    }

    /**
     * /game/build
     */
    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        dungeonState.build(buildable);
        return dungeonState.toDungeonResponse();
    }

    /**
     * /game/interact
     */
    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        dungeonState.interact(entityId);
        return dungeonState.toDungeonResponse();
    }

    /**
     * /game/save
     */
    public DungeonResponse saveGame(String name) throws IllegalArgumentException {
        dungeonState.saveGame(name);
        return dungeonState.toDungeonResponse();
    }

    /**
     * /game/load
     */
    public DungeonResponse loadGame(String name) throws IllegalArgumentException {
        dungeonState.loadGame(name);
        return dungeonState.toDungeonResponse();
    }

    /**
     * /games/all
     */
    public List<String> allGames() {
        return dungeonState.getAllGames();
    }

    // Extension 2
    public DungeonResponse generateDungeon(int xStart, int yStart, int xEnd, int yEnd, String configName) throws IllegalArgumentException {
        MazeGenMetadata metadata = new MazeGenMetadata(xStart, yStart, xEnd, yEnd);

        try {
            dungeonState = DungeonBuilder.initializeBuilder().withConfig(configName).generateDungeon(metadata);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        return dungeonState.toDungeonResponse();
    }
}
