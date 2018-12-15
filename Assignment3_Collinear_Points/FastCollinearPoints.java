import java.util.ArrayList;
import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stack;

public class FastCollinearPoints {

	private Point[] points1;
	private ArrayList<LineSegment> segs;

	public FastCollinearPoints(Point[] points) {

		if (points == null) 
			throw new IllegalArgumentException("null pointer");
		for (int i = 0; i < points.length; i++) {
			if (points[i] == null)
				throw new IllegalArgumentException("null point");
		}

		int N = points.length;
		points1 = new Point[N-1];

		Arrays.sort(points);
		for (int i = 1; i < N; i++)
			if (points[i-1].compareTo(points[i]) == 0) 
				throw new IllegalArgumentException("repeated points");

		segs = new ArrayList<LineSegment>();

		for (int i = 0; i < N; i++) {
			Point p  = points[i];

			for (int j = 0; j < N; j++) {
				if (j < i) points1[j] = points[j];
				if (j > i) points1[j-1] = points[j];
			}

			Arrays.sort(points1, p.slopeOrder());
			int count = 2;

			for (int j = 1; j < N-1; j++) {
				Point q1 = points1[j-1];
				Point q2 = points1[j];

				double k1 = q1.slopeTo(p);
				double k2 = q2.slopeTo(p);

				if (k1 == k2) {
					count ++;

					//To the last point, and this point is collinear to previous points
					if (j == N-2) {
						if (count >= 4 && p.compareTo(points1[j-count+2])==-1) {
							Point end = points1[j];
							LineSegment line = new LineSegment(p, end);
							segs.add(line);
						}
					}
				} else {
					if (count >=4 && p.compareTo(points1[j-count+1])==-1) {
						Point end = points1[j-1];
						LineSegment line = new LineSegment(p, end);
						segs.add(line);
					}
					count = 2;
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
    	FastCollinearPoints collinear = new FastCollinearPoints(points);
    	for (LineSegment segment : collinear.segments()) {
        	StdOut.println(segment);
        	segment.draw();
    	}
    	StdDraw.show();
	}
}