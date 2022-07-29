package dungeonmania.util.PathFinder;

import java.util.Objects;

import dungeonmania.util.Position;

public class Edge {
    private Position from;
    private Position to;
    private int edgeWeight;

    public Edge(Position from, Position to) {
        this.from = from;
        this.to = to;
    }

    public Edge(Position from, Position to, int edgeWeight) {
        this.from = from;
        this.to = to;
        this.edgeWeight = edgeWeight;
    }

    public int getWeight() {
        return edgeWeight;
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(from.hashCode(), to.hashCode());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (o == null)
            return false;

        if (!(o instanceof Edge))
            return false;

        Edge edgeToCompare = (Edge) o;

        return this.from.equals(edgeToCompare.from) && this.to.equals(edgeToCompare.to);
    }

    @Override
    public String toString() {
        return "From: " + from.toString() + " | To: " + to.toString();
    }
}
