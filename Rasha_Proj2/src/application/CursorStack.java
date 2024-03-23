package application;

//rasha mansour-1210773
public class CursorStack {

	// attributes
	private CursorArray cursor;
	private int listHead;
	private int stackSize;

	// Constructor
	public CursorStack(int size) {
		cursor = new CursorArray(size);
		this.listHead = cursor.createList();
		stackSize = 0;
	}

	// methods:

	// Pushes an element onto the stack
	public void push(String data) {
		cursor.insertAtHead(data, listHead);
		stackSize++;
	}

	// Pops an element from the stack
	public CANode pop() {
		stackSize--;
		return cursor.deleteHead(listHead);
	}

	// Peeks at the top element of the stack
	public String peek() {
		return cursor.getCursorArray()[cursor.getCursorArray()[listHead].getNext()].getElement();
	}

	// Checks if the stack is empty
	public boolean isEmpty() {
		return cursor.isEmpty(listHead);
	}

	// Clears the stack
	public void clear() {
		cursor.clearList(listHead);
		System.out.println("Stack emptied");
	}

	// Getter for CursorArray
	public CursorArray getCursor() {
		return cursor;
	}

	// Getter for listHead
	public int getListHead() {
		return listHead;
	}

	// Getter for stackSize
	public int getStackSize() {
		return stackSize;
	}

	// String representation of the stack
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("CursorStack{");
		CANode[] cursorArray = cursor.getCursorArray();
		for (int i = 0; i < cursorArray.length; i++) {
			result.append("[").append(cursorArray[i]).append("], ");
		}
		result.append("}");
		return result.toString();
	}

	// Returns the size of the stack
	public int size() {
		return stackSize;
	}
}