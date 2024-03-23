package application;

//rasha mansour-1210773
public class CANode {

	// attributes
	private String element;
	private int next;

	// constructor
	public CANode(String element, int next) {
		this.element = element;
		this.next = next;
	}

	// getters && setters
	public String getElement() {
		return element;
	}

	public void setElement(String element) {
		this.element = element;
	}

	public int getNext() {
		return next;
	}

	public void setNext(int next) {
		this.next = next;
	}

	// toString method
	@Override
	public String toString() {
		return "CANode{" + "element='" + element + '\'' + ", next=" + next + '}';
	}
}
