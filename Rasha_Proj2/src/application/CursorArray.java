package application;

//rasha mansour-1210773
public class CursorArray {

	// attributes
	private CANode[] cursorArray;
	private int maxSize;

	// constructor
	public CursorArray(int size) {
		cursorArray = new CANode[size];
		initialize();
	}

	// methods:

	// method to initialize the cursorArray with CANodes
	private void initialize() {
		for (int i = 0; i < cursorArray.length - 1; i++) {
			cursorArray[i] = new CANode(null, i + 1);
		}
		cursorArray[cursorArray.length - 1] = new CANode(null, 0);
		maxSize = 0;
	}

	// method to allocate memory for a new node in the cursorArray
	private int malloc() {
		int p = cursorArray[0].getNext();
		if (p != 0) {
			cursorArray[0].setNext(cursorArray[p].getNext());
		}
		return p;
	}

	// method to deallocate memory for a new node in the cursorArray
	private void free(int p) {
		if (p != 0) {
			cursorArray[p] = new CANode(null, cursorArray[0].getNext());
			cursorArray[0].setNext(p);
		}
	}

	// method to check if a given index in the cursorArray is null
	public boolean isNull(int l) {
		return cursorArray[l] == null;
	}

	// method to check if a given index in the cursorArray is empty
	public boolean isEmpty(int l) {
		return cursorArray[l].getNext() == 0;
	}

	// method to check if a given index in the cursorArray is the last node
	public boolean isLast(int p) {
		return cursorArray[p].getNext() == 0;
	}

	// method to create a new list and return its index
	public int createList() {
		int l = malloc();
		if (l == 0) {
			System.out.println("Error: Out of space!!!");
		} else {
			cursorArray[l] = new CANode(null, 0);
		}
		return l;
	}

	// method to insert a new node with the given element at the head of the list
	public void insertAtHead(String element, int l) {
		if (isNull(l)) {
			return;
		}
		int p = malloc();

		if (p != 0) {
			cursorArray[p] = new CANode(element, cursorArray[l].getNext());
			cursorArray[l].setNext(p);
			maxSize++;
		} else {
			System.out.println("Error: Out of space!!!");
		}
	}

	// method to traverse and print the elements
	public void traverseList(int l) {
		System.out.print("list_" + l + "-->");
		while (!isNull(l) && !isEmpty(l)) {
			l = cursorArray[l].getNext();
			System.out.print(cursorArray[l] + "-->");
		}
		System.out.println("null");
	}

	// method to find the index of the node with the given element
	public int find(String element, int l) {
		while (!isNull(l) && !isEmpty(l)) {
			l = cursorArray[l].getNext();
			if (cursorArray[l].getElement().equals(element))
				return l;
		}
		return -1; // not found
	}

	// method to find the index of the node with the previous element to the given
	// element in the list
	public int findPrevious(String element, int l) {
		while (!isNull(l) && !isEmpty(l)) {
			if (cursorArray[cursorArray[l].getNext()].getElement().equals(element))
				return l;
			l = cursorArray[l].getNext();
		}
		return -1; // not found
	}

	// method to delete the node with the given element
	public CANode delete(String element, int l) {
		int p = findPrevious(element, l);
		CANode result = null;
		if (p != -1) {
			int c = cursorArray[p].getNext();
			CANode temp = cursorArray[c];
			cursorArray[p].setNext(temp.getNext());
			maxSize--;
			result = cursorArray[c];
			free(c);
		}

		return result;
	}

	// method to Clear all nodes in the list
	public void clearList(int l) {
		int t = cursorArray[l].getNext();
		for (int i = 0; i < maxSize; i++) {
			free(t);
			t = cursorArray[t].getNext();
		}

	}

	// method to delete the head node from the list
	public CANode deleteHead(int l) {
		int c = cursorArray[l].getNext();
		CANode result = cursorArray[c];
		cursorArray[l].setNext(result.getNext());
		maxSize--;
		free(c);
		return result;
	}

	// Getter method for the cursorArray
	public CANode[] getCursorArray() {
		return cursorArray;
	}

	// method to find the index of the node that points to the head of the list
	public int findPreviousListHead(int currentListHead) {
		int l = cursorArray[0].getNext();
		while (cursorArray[l].getNext() != 0 && cursorArray[l].getNext() != currentListHead) {
			l = cursorArray[l].getNext();
		}
		return cursorArray[l].getNext() == currentListHead ? l : -1;
	}

}