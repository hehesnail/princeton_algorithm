import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
	private Item[] a;
	private int n;

	public RandomizedQueue() {
		a = (Item[]) new Object[2];
		n = 0;
	}

	public boolean isEmpty() {
		return n == 0;
	}
	
	public int size() {
		return n;
	}

	private void resize(int capacity) {
		assert capacity >= n;

		Item[] temp = (Item[]) new Object[capacity];
		for (int i = 0; i < n; i++) {
			temp[i] = a[i];
		}
		a = temp;
	}

	public void enqueue(Item item) {
		if (item == null) 
			throw new IllegalArgumentException("invalid argument");
		if (n == a.length) resize(2*a.length);
		a[n++] = item;
	}

	public Item dequeue() {
		if (isEmpty())
			throw new NoSuchElementException();
		int i = StdRandom.uniform(0, n);
		Item temp = a[i];
		a[i] = a[n-1];
		a[n-1] = temp;

		Item item = a[n-1];
		a[n-1] = null;
		n--;
		if (n > 0 && n == a.length/4) resize(a.length/2);

		return item;
	}

	public Item sample() {
		if (isEmpty())
			throw new NoSuchElementException();
		int i = StdRandom.uniform(0, n);
		return a[i];
	}

	public Iterator<Item> iterator() {
		return new RandomizedQueueIterator();
	}

	private class RandomizedQueueIterator implements Iterator<Item> {
		private int i;

		public RandomizedQueueIterator() {
			i = n-1;
		}

		public boolean hasNext() {
			return i >= 0;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public Item next() {
			if (!hasNext()) 
				throw new NoSuchElementException();
			int j = StdRandom.uniform(i+1);
			//StdOut.println(i);
			//StdOut.println(a[i]);
			//StdOut.println(a[j]);
			Item temp = a[j];
			a[j] = a[i]; 
			a[i] = temp;
			//StdOut.println(a[i]);
			//StdOut.println(a[j]);

			return a[i--];
		}
	}

	/*
	public static void main(String[] args) {
		RandomizedQueue<String> s = new RandomizedQueue<String>();
		s.enqueue("shall");
		s.enqueue("I");
		s.enqueue("compare");
		s.enqueue("thee");
		s.enqueue("to");
		s.enqueue("a");
		s.enqueue("summer's");
		s.enqueue("day");

		for (int i = 0 ; i < 8; i++) {
			StdOut.println(s.dequeue());
		}
	}
	*/
}