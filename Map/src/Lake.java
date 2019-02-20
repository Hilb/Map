import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Lake {

	public Point[] points;
	public double min = Integer.MAX_VALUE;
	private Point fin;

	private void findShortestDist() {
		loadMap("map.txt");
		fin = findPointByName("finish");

		Point[] temp = new Point[points.length - 1];

		for (int i = 0; i < temp.length; i++)
			temp[i] = points[i];

		points = temp;

		ArrayList<Point> toUse = new ArrayList<Point>(points.length);

		recursive(toUse);
	}

	// recursivly finds the shortest distance between all points
	private void recursive(ArrayList<Point> paths) {
		// base case
		if (paths.size() == points.length) {
			double dist = findLength(paths);
			if (min >= dist) {
				min = dist;
				System.out.println("The distance was " + dist + " the path was: " + paths.toString());
			}
			return;
		}

		// add dogbone
		if (paths.size() % 2 != 0) {
			paths.add(paths.get(paths.size() - 1).next);
			if (min > findLength(paths))
				recursive(paths);
			// if current total is more don't enter the loop
			paths.remove(paths.size() - 1);
			return;
		}

		for (int i = 0; i < points.length; i++) {
			// input i
			// add a letter from points[] that is not in the path yet

			for (; i < points.length; i++)
				if (!paths.contains(points[i])) {
					paths.add(points[i]);

					if (min > findLength(paths))
						recursive(paths);

					paths.remove(paths.size() - 1);
					break;
				}
		}
	}

	// finds the length of the path
	private Double findLength(ArrayList<Point> paths) {
		Double toRet = calcDist(fin.x, fin.y, paths.get(0).x, paths.get(0).y);

		for (int i = 0; i < paths.size() - 1; i++)
			toRet += calcDist(paths.get(i).x, paths.get(i).y, paths.get(i + 1).x, paths.get(i + 1).y);

		toRet += calcDist(paths.get(paths.size() - 1).x, paths.get(paths.size() - 1).y, fin.x, fin.y);

		return toRet;
	}

	// calculates the distance between two points
	private static Double calcDist(int x1, int y1, int x2, int y2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}

	// finds the length of a path in string form
	private void testLength(String line) {
		line = line.substring(1);
		line = line.substring(0, line.length() - 1);
		String[] temp = line.split(",");

		for (int i = 1; i < temp.length; i++)
			temp[i] = temp[i].substring(1);

		ArrayList<Point> temp2 = new ArrayList<Point>(temp.length);

		for (int i = 0; i < temp.length; i++)
			temp2.add(findPointByName(temp[i]));

		System.out.println(findLength(temp2));
	}

	// loads the map based on a file name
	public void loadMap(String fname) {
		Scanner fileIn = null;

		try {
			fileIn = new Scanner(new File(fname));
		} catch (FileNotFoundException e) {
			System.out.println("That file was not found");
			throw new NoSuchElementException();
		}
		int size = 0;
		// gets the size of the array
		while (fileIn.hasNextLine()) {
			size++;
			fileIn.nextLine();
		}
		try {
			fileIn = new Scanner(new File(fname));
		} catch (FileNotFoundException e) {
			System.out.println("That file was not found");
			throw new NoSuchElementException();
		}
		points = new Point[size];
		String nextVals[] = new String[size];
		// sets the lines of the file equal to the arrayi
		for (int i = 0; i < size; i++) {
			String line = fileIn.nextLine();
			String[] temp = line.split(",");

			String name = temp[0];
			String xval = temp[1];
			String yval = temp[2];
			nextVals[i] = temp[3];

			points[i] = new Point(name, Integer.parseInt(xval), Integer.parseInt(yval), null);
		}

		// used to set the next point
		for (int i = 0; i < size; i++)
			points[i].next = findPointByName(nextVals[i]);
	}

	// finds the points in the array based on the name
	private Point findPointByName(String name) {
		for (int i = 0; i < points.length; i++)
			if (points[i].name.equals(name))
				return points[i];

		return null;
	}

	public static void main(String[] args) {
		Lake LakeAccotink = new Lake();
		LakeAccotink.findShortestDist();
	}

	// inner class represents a point
	class Point {
		String name;
		int x;
		int y;
		Point next;

		public Point() {

		}

		public Point(String n, int valx, int valy, Point ne) {
			name = n;
			x = valx;
			y = valy;
			next = ne;

		}

		public String toString() {
			return name;
		}
	}
}