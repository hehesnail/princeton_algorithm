import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {

	private final int x;
	private final int y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void draw() {
		StdDraw.point(x, y);
	}

	public void drawTo(Point that) {
		StdDraw.line(this.x, this.y, that.x, that.y);
	}

	public double slopeTo(Point that) {
		if (this.x == that.x) {
			if (this.y == that.y) {
				/*equal*/
				return Double.NEGATIVE_INFINITY;
			} else {
				/*vertical*/
				return Double.POSITIVE_INFINITY;
			}
		}

		/*horizontal*/
		if (this.y == that.y) {
			return 0.0;
		}

		return (double)(that.y-this.y) / (double)(that.x-this.x);
	}

	public int compareTo(Point that) {
		if (this.y < that.y ) return -1;
		if (this.y == that.y && this.x < that.x) return -1;
		if (this.y == that.y && this.x == that.x) return 0;
		return +1;
	}

	public Comparator<Point> slopeOrder() {
		return new SlopeOrder();
	}

	private class SlopeOrder implements Comparator<Point> {
		public int compare(Point p1, Point p2) {
			if (slopeTo(p1) < slopeTo(p2)) return -1;
			else if (slopeTo(p1) > slopeTo(p2)) return +1;
			else return 0;
		}
	}

	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}