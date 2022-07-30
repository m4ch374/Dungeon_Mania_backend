package dungeonmania.DungeonObjects.Entities.LogicEntities;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class LogicPathFinder {

	public static boolean hasPath(String[][] matrix, int x, int y) {
		return (pathExists(matrix, x, y) > 0);
	}

	private static Node getSource(String[][] matrix, int x, int y) {
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				if (matrix[i][j].equals("S")) {
					return new Node(i, j, 0);
				}
			}
		}

		return null;
	}

	private static int pathExists(String[][] matrix, int x, int y) {

		Node source = getSource(matrix, x, y);
		if (source == null) { return -1; }

		Queue<Node> queue = new LinkedList<Node>();

		queue.add(source);

		while(!queue.isEmpty()) {
			Node poped = queue.poll();

			if(matrix[poped.x][poped.y].equals("D")) {
				return poped.distanceFromSource;
			}
			else {
				matrix[poped.x][poped.y]="0";

				List<Node> neighbourList = addNeighbours(poped, matrix);
				queue.addAll(neighbourList);
			}
		}
		return -1;
	}

	private static List<Node> addNeighbours(Node poped, String[][] matrix) {

		List<Node> list = new LinkedList<Node>();

		if((poped.x-1 >= 0 && poped.x-1 < matrix.length) && !matrix[poped.x-1][poped.y].equals("0")) {
			list.add(new Node(poped.x-1, poped.y, poped.distanceFromSource+1));
		}
		if((poped.x+1 >= 0 && poped.x+1 < matrix.length) && !matrix[poped.x+1][poped.y].equals("0")) {
			list.add(new Node(poped.x+1, poped.y, poped.distanceFromSource+1));
		}
		if((poped.y-1 >= 0 && poped.y-1 < matrix.length) && !matrix[poped.x][poped.y-1].equals("0")) {
			list.add(new Node(poped.x, poped.y-1, poped.distanceFromSource+1));
		}
		if((poped.y+1 >= 0 && poped.y+1 < matrix.length) && !matrix[poped.x][poped.y+1].equals("0")) {
			list.add(new Node(poped.x, poped.y+1, poped.distanceFromSource+1));
		}
		return list;
	}
}

class Node {
    int x;
    int y;
    int distanceFromSource;

    Node(int x, int y, int dis) {
        this.x = x;
        this.y = y;
        this.distanceFromSource = dis;
    }
}
