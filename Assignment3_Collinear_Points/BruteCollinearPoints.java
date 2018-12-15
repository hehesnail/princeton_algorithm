import java.util.ArrayList;
import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
	private ArrayList<LineSegment> segs;
 
	public BruteCollinearPoints(Point[] points) {
		if (points == null) 
			throw new IllegalArgumentException("null pointer");
		for (int i = 0; i < points.length; i++) {
			if (points[i] == null)
				throw new IllegalArgumentException("null point");
		}

		int N = points.length;
		Arrays.sort(points);
		for (int i = 1; i < N; i++)
			if (points[i-1].compareTo(points[i]) == 0) 
				throw new IllegalArgumentException("repeated points");

		segs = new ArrayList<LineSegment>();

		for (int i = 0; i < N-3; i++) {
			Point p = points[i];
			for (int j = i+1; j < N-2; j++) {
				Point q = points[j];
				double pq = p.slopeTo(q);
				if (pq == Double.NEGATIVE_INFINITY) continue;
				for (int k = j+1; k < N-1; k++) {
					Point s = points[k];
					double ps = p.slopeTo(s);
					if (ps == Double.NEGATIVE_INFINITY) continue;
					for (int l = k+1; l < N; l++) {
						Point r = points[l];
						double pr = p.slopeTo(r);
						if (pr == Double.NEGATIVE_INFINITY) continue;
						if (pq == ps && pq == pr) {
							LineSegment line = new LineSegment(p, r);
							segs.add(line);
						}
					}
				}
			}
		}
	}

	public int numberOfSegments() {
		return segs.size();
	}

	public LineSegment[] segments() {
		LineSegment[] lsegs = new LineSegment[segs.size()];
		int i = 0;
		for (LineSegment s : segs) {
			lsegs[i++] = s;
		}

		return lsegs;
	}

	public static void main(String[] args) {

    	// read the n points from a file
    	In in = new In(args[0]);
    	int n = in.readInt();
    	Point[] points = new Point[n];
    	for (int i = 0; i < n; i++) {
        	int x = in.readInt();
        	int y = in.readInt();
        	points[i] = new Point(x, y);
    	}

    	// draw the points
    	StdDraw.enableDoubleBuffering();
    	StdDraw.setXscale(0, 32768);
    	StdDraw.setYscale(0, 32768);
    	for (Point p : points) {
        	p.draw();
    	}
    	StdDraw.show();

    	// print and draw the line segments
    	BruteCollinearPoints collinear = new BruteCollinearPoints(points);
    	for (LineSegment segment : collinear.segments()) {
        	StdOut.println(segment);
        	segment.draw();
    	}
    	StdDraw.show();
	}
}