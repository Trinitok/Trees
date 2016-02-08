import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * @author kellymr1
 * 
 * @param <T>
 * 
 *            The BinarySearchTree class which contains all the methods for the
 *            tree and can be iterated through. It can be converted to a string,
 *            arraylist, and array. One can also obtain the size and height of
 *            the tree.
 */
public class BinarySearchTree<T extends Comparable<? super T>> implements
		Iterable<T> {
	public BinaryNode root;
	public int modCont = 0;

	public int size = 0;

	/**
	 * An empty constructor for the class that sets the root node to null and is
	 * an empty tree
	 */
	public BinarySearchTree() {
		root = null;
	}

	/**
	 * @return the height of the tree
	 * 
	 *         Moves through the tree and finds the height of the highest side
	 */
	public int height() {
		if (isEmpty()) {
			return -1;
		}

		else
			return root.height();
	}

	/**
	 * @return size of the tree
	 * 
	 *         Counts how many elements are in the tree and returns the size of
	 *         the tree
	 */
	public int size() {
		return size;
	}

	/**
	 * @return true if empty; false if not
	 * 
	 *         Determines whether the root node is null or has an element. Will
	 *         return true if there is an element, else false
	 */
	public boolean isEmpty() {
		return root == null;
	}

	public String toString() {
		return toArrayList().toString();
	}

	/**
	 * @return An arraylist that contains the elements inorder of the tree. Will
	 *         return an empty arraylist if the tree is empty itself
	 * 
	 *         Converts the elements in order of the tree into an arraylist.
	 */
	public ArrayList<T> toArrayList() {
		if (isEmpty())
			return new ArrayList<T>();
		else
			return root.toArrayList(new ArrayList<T>());

	}

	/**
	 * @return An array of the elements of the tree in order. Returns an array
	 *         of size 0 if the tree is empty
	 * 
	 *         Uses the toArray() method of arrayList to convert the arraylist
	 *         returned in toArrayList() into an array.
	 */
	public Object[] toArray() {
		if (isEmpty())
			return new Object[0];
		else
			return this.toArrayList().toArray();
	}

	/**
	 * @return an iterator of the tree
	 * 
	 *         Converts the tree into a stack which it will then iterate through
	 */
	@SuppressWarnings("unchecked")
	public Iterator<T> preOrderIterator() {
		Stack<Object> stack = new Stack<Object>();
		if (isEmpty())
			return (Iterator<T>) stack.iterator();

		else {
			return new PreOrderIterator(root);
		}

	}

	@SuppressWarnings("unchecked")
	public Iterator<T> iterator() {
		Stack<Object> stack = new Stack<Object>();
		if (isEmpty())
			return (Iterator<T>) stack.iterator();

		else {
			if (!root.Iterator().hasNext()) {
				throw (new NoSuchElementException());
			}
			return new InOrderIterator(root);
		}
	}

	/**
	 * @author kellymr1
	 * 
	 *         The class for the binary nodes which make up the Binary Search
	 *         Tree
	 */
	public class BinaryNode {
		public T element;
		public BinaryNode leftChild;
		public BinaryNode rightChild;
		private int height = 0;
		private int leftHeight = 0;
		private int rightHeight = 0;
		private Stack<Object> stack = new Stack<Object>();

		/**
		 * @param element
		 *            The element contained within the node
		 * 
		 *            Constructor for a node that will set the element of the
		 *            node to the parameter that is passed to it. The right and
		 *            left children will still be set to null
		 */
		public BinaryNode(T element) {
			this.element = element;
			this.leftChild = null;
			this.rightChild = null;
		}

		/**
		 * @return The height of the tree
		 * 
		 *         This method will recursively traverse through the tree and
		 *         count up the height of each side. It will then return the
		 *         larger height
		 */
		public int height() {

			if (this.leftChild == null && this.rightChild == null) {
				return height;
			}

			if (leftChild != null) {
				leftHeight = leftChild.height() + 1;
			}

			if (rightChild != null) {
				rightHeight = rightChild.height() + 1;
			}

			if (rightHeight > leftHeight)
				return rightHeight;

			else {
				return leftHeight;

			}
		}

		/**
		 * @return Returns an array list of the tree in order
		 * 
		 *         Returns an array list of the elements from the Binary Search
		 *         Tree in order
		 */
		public ArrayList<T> toArrayList(ArrayList<T> list) {

			if (this.leftChild != null) {
				this.leftChild.toArrayList(list);

			}

			list.add(this.element);
			if (this.rightChild != null) {
				this.rightChild.toArrayList(list);
			}
			return list;

		}

		/**
		 * @return Returns an iterator
		 */
		public Iterator<Object> Iterator() {
			stack.push(element);

			if (this.rightChild != null) {
				rightChild.Iterator();
			}

			if (this.leftChild != null) {
				leftChild.Iterator();
			}

			Iterator<Object> it = stack.iterator();

			return it;
		}

		/**
		 * @author kellymr1
		 * @param o The element that will be inserted into the tree
		 * @return true if it is successfully inserted
		 * 
		 *         This method will insert an element into a tree and check the
		 *         nodes to see if it should go on the left or right side of the
		 *         tree. It will then return true if the node is successfully
		 *         placed; false otherwise.
		 */
		public Boolean insert(T o) {

			if (o.compareTo(element) > 0) {
				if (rightChild != null) {
					return rightChild.insert(o);
				} else {

					rightChild = new BinaryNode(o);
					size++;
					modCont++;
					return true;
				}

			}

			if (o.compareTo(element) < 0) {

				if (leftChild != null) {
					return leftChild.insert(o);
				} else {

					leftChild = new BinaryNode(o);
					size++;
					modCont++;
					return true;
				}

			}

			return false;

		}

		/**
		 * @param element The element that is to be removed
		 * @param root Where the root is in the tree
		 * @return True if the value is removed; false otherwise
		 * 
		 *         This is the BinaryNode version of the remove method created
		 *         in the BinarySearchTree class. It will recursively move
		 *         through the tree looking for element to be removed. It will
		 *         then adjust the tree accordingly if required. It will return
		 *         true if the specified element is removed; false otherwise.
		 */
		public boolean remove(T element, BinaryNode root) {

			if (element.compareTo(this.element) < 0)
				return (leftChild != null) ? leftChild.remove(element, this)
						: false; // no ternary with recursion

			else if (element.compareTo(this.element) > 0) {
				return (rightChild != null) ? rightChild.remove(element, this)
						: false;
			}

			else {

				if (leftChild != null && rightChild != null) {
					this.element = leftChild.maxValue();
					leftChild.remove(this.element, this);
				}

				else if (root.leftChild == this) {
					root.leftChild = (leftChild != null) ? leftChild
							: rightChild;
					modCont++;
					size--;
					return true;
				}

				else if (root.rightChild == this) {
					root.rightChild = (leftChild != null) ? leftChild
							: rightChild;
					modCont++;
					size--;
					return true;
				}

				return true;
			}

		}

		private T maxValue() {
			return (rightChild == null) ? element : rightChild.maxValue();
		}

		
		private boolean leftV() {
			while(leftChild != null){
				if(rightChild != null) //check to see if left has right
					return false;
				else return leftChild.leftV();
			}
			
			return true;
				
		}
		
		private boolean rightV() {
			while(rightChild != null){
				if(leftChild != null) //check to see if right has left
					return false;
				else return rightChild.leftV();
			}
			
			return true;
				
		}

		public void reverse() {
			while(leftChild != null && rightChild != null){
				BinaryNode temp = leftChild;
				
				leftChild = rightChild;
				rightChild = temp;
			}
		}

	}

	/**
	 * @author kellymr1
	 * 
	 *         This is the class for the preorder iterator. It is within the
	 *         BinarySearchTree class so it is hidden
	 */
	private class PreOrderIterator implements Iterator<T> {

		private Stack<BinaryNode> s = new Stack<BinaryNode>();
		BinaryNode root;
		private int current = 0;
		private int myMod;
		private BinaryNode currNode;
		private boolean tempBool;

		/**
		 * @param root The root of the tree that the iterator will start at
		 * 
		 *            Creates the iterator and adds the root to the stack
		 */
		public PreOrderIterator(BinaryNode root) {
			this.root = root;
			this.s.push(root);
			myMod = modCont;
		}

		public boolean hasNext() {
			return !s.empty();
		}

		public T next() {
			if (myMod != modCont)
				throw new ConcurrentModificationException();

			if (hasNext()) {
				current++;
				BinaryNode temp = (BinaryNode) s.pop();
				currNode = temp;
				if(currNode == null)
					throw new IllegalStateException();
				if (temp.rightChild != null) {
					s.push(temp.rightChild);
				}

				if (temp.leftChild != null) {
					s.push(temp.leftChild);
				}
				tempBool = true;
				return temp.element;
			}

			else
				throw new NoSuchElementException();

		}

		public void remove() {
			if (currNode == null)
				throw new IllegalStateException();
			if(tempBool){
				tempBool = false;
				currNode.remove(currNode.element, root);
			}
			else throw new IllegalStateException();
			return;
		}

	}

	/**
	 * @author kellymr1
	 * 
	 *         The class for the inorder iterator. It is hidden inside the
	 *         BinarySearchTree class and cannot be accessed because of it
	 */
	private class InOrderIterator implements Iterator<T> {
		Stack<BinaryNode> s = new Stack<BinaryNode>();
		private BinaryNode temp;
		private BinaryNode currNode;
		private boolean tempBool;

		/**
		 * @param root The root of the tree where the iterator will be starting
		 *            at
		 * 
		 *            This is the constructor for the inorder iterator that will
		 *            look for the left child and then allow you to iterate
		 *            through the tree with the elements in order
		 */
		public InOrderIterator(BinaryNode root) {
			s.push(root);
			if (root != null) {
				temp = root.leftChild;

				while (temp != null) {
					this.s.push(temp);
					temp = temp.leftChild;
				}
			}
		}

		public boolean hasNext() {
			return !s.empty();
		}

		public T next() {
			if (s.isEmpty())
				throw new NoSuchElementException();
			temp = s.pop();
			if (temp.rightChild != null) {
				if (temp.rightChild.leftChild != null) {
					BinaryNode curr = temp.rightChild;

					while (curr != null) {
						s.push(curr);
						curr = curr.leftChild;
					}

					currNode = temp.rightChild;
					tempBool = true;
					return temp.element;
				} else {
					s.push(temp.rightChild);
					currNode = temp;
					tempBool = true;
					return temp.element;
				}
			}

			currNode = temp;
			tempBool = true;
			return temp.element;

		}

		public void remove() {
			if (currNode == null)
				throw new IllegalStateException();
			if(tempBool){
				tempBool = false;
				currNode.remove(currNode.element, root);
			}
			else throw new IllegalStateException();
		}

	}

	/**
	 * @param o The element that you will be inserting into the tree
	 * @return true if it was inserted; false otherwise. Will throw an exception
	 *         if there is nothing passed as the argument
	 * 
	 *         This method will insert an element into the tree. It will also
	 *         increase the size paramter
	 */
	public boolean insert(T o) {
		if (o == null)
			throw new IllegalArgumentException();

		else {
			if (isEmpty()) {
				this.root = new BinaryNode(o);
				size++;
				modCont++;
				return true;
			} else
				return root.insert(o);

		}

	}

	/**
	 * @param i The element that you want removed
	 * @return true if it is successfully removed; false otherwise
	 * 
	 *         This method will take in an element that the user is trying to
	 *         remove from the tree. It will go through and look for the element
	 *         and adjust the tree accordingly if required. If the tree is
	 *         empty, it will return false. If it cannot find the element
	 *         specified, it will return false. It will also throw an
	 *         IllegalArgumentException if null is passed as the paramter
	 */
	public boolean remove(T i) {
		if (i == null)
			throw new IllegalArgumentException();

		if (isEmpty())
			return false;

		if (root.element == i && size == 1) {
			root = null;
			size--;
			modCont++;
			return true;
		} else {
			return root.remove(i, root);
		}
	}

	public void reverse() {
		
		if(isEmpty()) return;
		
		System.out.println("past first if");
		
		BinaryNode temp = root.leftChild;
		
		root.leftChild = root.rightChild;
		root.rightChild = temp;
		
		
		System.out.println("switched roots nodes");
		root.leftChild.reverse();
		root.rightChild.reverse();
		
		
	}
	
	public boolean formasAV(){
		
		if(root == null) return true;  // tests if there is a tree
		
		if(root.leftChild == null && root.rightChild == null) return true;  // tests if root is only one here
		
		root.height();  // sets left and right height variables
		
		if(root.leftHeight != root.rightHeight) return false;  // see if heights are equal to eachother
		
		if(root.leftChild != null && root.rightChild != null){  // goes through the left and right side
			
			boolean left = root.leftChild.leftV();
			
			boolean right = root.rightChild.rightV();
			
			
			if(left && right)
				return true;
		}
		
		return false;
	}
	
	public void addAll(Collection c){
		if(c == null || c.isEmpty()){
			
		}
		else{
			Iterator i = c.iterator();
			while(i.hasNext()){
				this.insert((T) c);  // You can't make a collection T because it is not in the lang.  Good try though!  In the exam this was taken from, I believe this was a Comparable type, not T
			}
		}
	}

}
