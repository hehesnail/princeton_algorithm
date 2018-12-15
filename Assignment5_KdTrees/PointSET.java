import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.SET;
import java.util.ArrayList;

public class PointSET {
	private SET<Point2D> points;
	private ArrayList<Point2D> ranges;
	
	public PointSET() {
		points = new SET<Point2D>();
	} 

	public boolean isEmpty() {
		return points.isEmpty();
	}

	public int size() {
		return points.size();
	}

	public void insert(Point2D p) {
		if (p == null) throw new java.lang.IllegalArgumentException("p null"); 
		if (points.contains(p)) return;
		points.add(p);
	}

	public boolean contains(Point2D p) {
		if (p == null) throw new java.lang.IllegalArgumentException("p null");
		return points.contains(p);
	}

	public void draw() {
		for (Point2D p : points) {
			p.draw();
		}
	}

	public Iterable<Point2D> range(RectHV rect) {
		if (isEmpty()) return null;
		if (rect == null) throw new java.lang.IllegalArgumentException("rect null");
		ranges = new ArrayList<Point2D>();
		for (Point2D p: points) {
			if (rect.contains(p)) {
				ranges.add(p);
			}
		}

		return ranges;
	}

	public Point2D nearest(Point2D p) {
		if (isEmpty()) return null;
		if (p == null) throw new java.lang.IllegalArgumentException("p null");
		Point2D current = new Point2D(0.0, 0.0);
		for (Point2D pi: points) {
			current = pi;
			break;
		}

		for (Point2D pi : points) {
			if (pi.distanceSquaredTo(p) < current.distanceSquaredTo(p)) {
				current = pi;
			}
		}

		return current;
	}

	public static void main(String[] args) {

	}
}