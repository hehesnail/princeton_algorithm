import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
	private Node<Item> first;
	private Node<Item> last;
	private int n;

	//private node class, Item differ from the Deque's Item
	private static class Node<Item> {
		private Item item;
		private Node<Item> next;
		private Node<Item> prev;
	}

	//Constructor
	public Deque() {
		first = null;
		last = null;
		n = 0;
	}

	//return true if the deque is empty
	public boolean isEmpty() {
		return first == null;
	}

	//return the size of the deque
	public int size() {
		return n;
	}
	
	//add the element in the front
	public void addFirst(Item item) {
		if (item == null) 
			throw new IllegalArgumentException("invalid argument");
		Node<Item> oldFirst = first;
		first = new Node<Item>();
		first.item = item;
		first.next = oldFirst;

		if (oldFirst == null) {
			last = first;
		} else {
			oldFirst.prev = first;
		}

		n++;
	}

	//add the element in the end
	public void addLast(Item item) {
		if (item == null)
			throw new IllegalArgumentException("invalid argument");
		Node<Item> oldLast = last;
		last = new Node<Item>();
		last.prev = oldLast;
		last.item = item;

		if (oldLast == null) 
			first = last;
		else
			oldLast.next = last;
		
		n++;
	}

	// removes and return the front element
	public Item removeFirst() {
		if (isEmpty())
			throw new NoSuchElementException("Deque empty");
		Item item = first.item;
		first = first.next;
		
		if (first == null) {
			last = null;
		} else {
			first.prev = null;
		}

		n--;
		return item;
	}

	//removes and return the last element
	public Item removeLast() {
		if (isEmpty())
			throw new NoSuchElementException("Deque empty");
		Item item = last.item;
		last = last.prev;

		if(last == null) {
			first = null;
		} else {
			last.next = null;
		}

		n--;
		return item;
	}
	
	public Iterator<Item> iterator() {
		return new DequeIterator<Item>(first);
	}

	private class DequeIterator<Item> implements Iterator<Item> {
		private Node<Item> current;

		public DequeIterator(Node<Item> first) {
			current = first;
		}

		public boolean hasNext() {
			return current != null;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public Item next() {
			if (!hasNext()) throw new NoSuchElementException();
			Item item = current.item;
			current = current.next;

			return item;
		}

	}

	/*
	public static void main(String[] args) {
		Deque<Integer> d = new Deque<Integer>();

		//test isEmpty(), size
		//StdOut.println("testing empty deque");
		//StdOut.println(d.isEmpty());
		//StdOut.println(d.size());

		//Add first
		//d.addFirst(1);
		//d.addFirst(2);
		//d.addFirst(3);
		//d.addFirst(4);
		//d.addLast(1);
		//StdOut.println(d.removeLast());
		d.addFirst(2);
		StdOut.println(d.removeLast());
		d.addFirst(3);
		d.addLast(4);
		StdOut.println(d.removeFirst());
		StdOut.println(d.removeLast());
	}
	*/
}