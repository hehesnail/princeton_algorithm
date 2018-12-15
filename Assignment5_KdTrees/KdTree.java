import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import java.util.ArrayList;

public class KdTree {

	private Node root;
	private ArrayList<Point2D> ranges;

	private static class Node {
		private Point2D p;
		private RectHV rect;
		private Node lb;
		private Node rt;
		private int size;

		public Node(Point2D p, RectHV rect, int size) {
			this.p = p;
			this.rect = rect;
			this.size = size;
		}

	}

	public KdTree() {
	}

	public boolean isEmpty() {
		return size() == 0;
	}


	public int size() {
		return size(root);
	}

	private int size(Node x) {
		if (x == null) return 0;
		else return x.size;
	}

	public void insert(Point2D p) {
		if (p == null) throw new java.lang.IllegalArgumentException("p null"); 

		//1 means vertical, compare pi.x
		root = insert(root, null, p, 1);
	}

	private Node insert(Node x, Node px, Point2D pi, int orientation) {
		if (x == null) {
			if (size() == 0) {
				RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
				return new Node(pi, rect, 1);
			} else {
				//Node x, and paranet px
				RectHV rec;
				if (orientation == -1) {
					if (pi.x() < px.p.x()) {
						double xmin = px.rect.xmin();
						double ymin = px.rect.ymin();
						double xmax = px.p.x();
						double ymax = px.rect.ymax();

						rec = new RectHV(xmin, ymin, xmax, ymax);
					} else {
						double xmin = px.p.x();
						double ymin = px.rect.ymin();
						double xmax = px.rect.xmax();
						double ymax = px.rect.ymax();

						rec = new RectHV(xmin, ymin, xmax, ymax);
					}
				} else {
					if (pi.y() < px.p.y()) {
						double xmin = px.rect.xmin();
						double ymin = px.rect.ymin();
						double xmax = px.rect.xmax();
						double ymax = px.p.y();

						rec = new RectHV(xmin, ymin, xmax, ymax);
					} else {
						double xmin = px.rect.xmin();
						double ymin = px.p.y();
						double xmax = px.rect.xmax();
						double ymax = px.rect.ymax();

						rec = new RectHV(xmin, ymin, xmax, ymax);
					}
				}

				return new Node(pi, rec, 1);
			}
		}

		if (pi.x() == x.p.x() && pi.y() == x.p.y())
			return x;

		if (orientation == 1) {
			//vertical, compare x

			if (pi.x() < x.p.x()) {
				x.lb = insert(x.lb, x, pi, -orientation);
			}
			else {
				x.rt = insert(x.rt, x, pi, -orientation); 
			} 
		} else if (orientation == -1) {
			//horizon, compare y

			if (pi.y() < x.p.y()) {
				x.lb = insert(x.lb, x, pi, -orientation);
			}
			else {
				x.rt = insert(x.rt, x, pi, -orientation);
			}
		}

		x.size = 1 + size(x.lb) + size(x.rt);
		return x;
	}

	public boolean contains(Point2D p) {
		if (p == null) throw new java.lang.IllegalArgumentException("p null");
		//1 means, vertical, compare pi.x
		return contains(root, p, 1);
	}

	private boolean contains(Node x, Point2D pi, int orientation) {
		while (x != null) {
			if (pi.equals(x.p)) return true;

			if (orientation == 1) {
				//vertical, compare x
				if (pi.x() < x.p.x()) 
					x = x.lb;
				else
					x = x.rt;
			} else if (orientation == -1) {
				//horizon, compare y
				if (pi.y() < x.p.y())
					x = x.lb;
				else
					x = x.rt;
			}

			orientation = -orientation;
		}

		return false;
	}
	
	public void draw() {
		draw(root, 1);
	} 

	private void draw(Node x, int orientation) {

		if (x == null) return;

		StdDraw.setPenColor(StdDraw.RED);
        //StdDraw.setPenRadius(0.02);

        Point2D p1;
        Point2D p2;
        double xmin=0, xmax=0, ymin=0, ymax=0;

		if (orientation == 1) {
			StdDraw.setPenColor(StdDraw.RED);
			xmin = x.p.x();
			xmax = x.p.x();
			ymin = x.rect.ymin();
			ymax = x.rect.ymax();
		}
		else if (orientation == -1){
			StdDraw.setPenColor(StdDraw.BLUE);
			ymin = x.p.y();
			ymax = x.p.y();
			xmin = x.rect.xmin();
			xmax = x.rect.xmax();
		}

		p1 = new Point2D(xmin, ymin);
		p2 = new Point2D(xmax, ymax);

		StdDraw.setPenRadius(0.005);
		p1.drawTo(p2);
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(0.02);
		x.p.draw();

		draw(x.lb, -orientation);
		draw(x.rt, -orientation);
	}

	public Iterable<Point2D> range(RectHV rect) {
		if (isEmpty()) return null;
		if (rect == null) throw new java.lang.IllegalArgumentException("invalid rect");
		if (root == null) throw new java.lang.IllegalArgumentException("null pointer");
		ranges = new ArrayList<Point2D>();
		range(root, rect, ranges);

		return ranges;
	}

	private void range(Node x, RectHV rect, ArrayList<Point2D> ranges) {
		if (x == null) return;
		if (!x.rect.intersects(rect)) return;
		else {
			if (rect.contains(x.p))
				ranges.add(x.p);
			range(x.lb, rect, ranges);
			range(x.rt, rect, ranges);
		}
	}



	public Point2D nearest(Point2D p) {
		if (isEmpty()) return null;
		if (root == null) throw new java.lang.IllegalArgumentException("null pointer");
		if (p == null) throw new java.lang.IllegalArgumentException("invalid point");
		
		return nearest(root, p, root, 1).p;
	}

	private Node nearest(Node x, Point2D p, Node y, int orientation) {
		if (x == null) return y;
		double nrDist = p.distanceSquaredTo(y.p);
		if (nrDist < x.rect.distanceSquaredTo(p)) return y;

		double kdDist = p.distanceSquaredTo(x.p);

		if (nrDist >= kdDist || nrDist >= x.rect.distanceSquaredTo(p)) {
			if (nrDist > kdDist) y = x;

			if (orientation == 1) {
				double cmpX = p.x() - x.p.x();
				if (cmpX < 0.0) {
					if (x.lb != null) y = nearest(x.lb, p, y, -orientation);
					if (x.rt != null) y = nearest(x.rt, p, y, -orientation);
				} else {
					if (x.rt != null) y = nearest(x.rt, p, y, -orientation);
					if (x.lb != null) y = nearest(x.lb, p, y, -orientation);
				}
			} else if (orientation == -1) {
				double cmpY = p.y() - x.p.y();
				if (cmpY < 0.0) {
					if (x.lb != null) y = nearest(x.lb, p, y, -orientation);
					if (x.rt != null) y = nearest(x.rt, p, y, -orientation);
				} else {
					if (x.rt != null) y = nearest(x.rt, p, y, -orientation);
					if (x.lb != null) y = nearest(x.lb, p, y, -orientation);
				}
			}
		}

		return y;
	}

	public static void main(String[] args) {
		In in = new In(args[0]);
		KdTree k = new KdTree();
		while (!in.isEmpty()) {
			double p1 = in.readDouble();
			double p2 = in.readDouble();
			Point2D p = new Point2D(p1, p2);
			StdOut.println(p);
			k.insert(p);
		}

		k.draw();

		in = new In(args[0]);
		while (!in.isEmpty()) {
			double p1 = in.readDouble();
			double p2 = in.readDouble();
			Point2D p = new Point2D(p1, p2);
			StdOut.println(k.contains(p));
		}
	}
}