package dungeonmania.util.DungeonFactory.DungeonMazeFactory;

public final class MazeGenMetadata {
    private final int xStart;
    private final int yStart;
    private final int xEnd;
    private final int yEnd;

    public MazeGenMetadata(int xStart, int yStart, int xEnd, int yEnd) {
        this.xStart = xStart;
        this.yStart = yStart;
        this.xEnd = xEnd;
        this.yEnd = yEnd;
    }

    public final int getxStart() {
        return xStart;
    }

    public final int getyStart() {
        return yStart;
    }

    public final int getxEnd() {
        return xEnd;
    }

    public final int getyEnd() {
        return yEnd;
    }
}
