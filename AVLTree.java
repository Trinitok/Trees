
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class AVLTree<T extends Comparable<? super T>> implements Iterable<T> {
	public BinaryNode root;
	public int modCont = 0;
	public int size = 0;
	private int rotateCount;

	/**
	 * An empty constructor for the class that sets the root node to null and is
	 * an empty tree
	 */
	public AVLTree() {
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
			return root.height(root);
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
			return new PreOrderIterator(root);
		}
	}

	/**
	 * @author kellymr1
	 * 
	 *         The class for the binary nodes which make up the Binary Search
	 *         Tree
	 */
	class BinaryNode {
		public T element;
		public BinaryNode leftChild;
		public BinaryNode rightChild;
		public int leftHeight = 0;
		public int rightHeight = 0;
		private Stack<Object> stack = new Stack<Object>();
		public int hpos;

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
			this.getHeight();
		}

		/**
		 * @return The height of the tree
		 * 
		 *         This method will recursively traverse through the tree and
		 *         count up the height of each side. It will then return the
		 *         larger height
		 */
		public int height(BinaryNode a) {
			if (a == null)
				return -1;
			else {
				return Math.max(height(a.leftChild), height(a.rightChild)) + 1;
			}
		}

		/**
		 * @return Returns an array list of the tree in order
		 * 
		 *         Returns an array list of the elements from the Binary Search
		 *         Tree in order
		 */
		public ArrayList<T> toArrayList(ArrayList<T> list) {
			
//			list.

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
		 * @param o
		 *            The element that will be inserted into the tree
		 * @return true if it is successfully inserted
		 * 
		 *         This method will insert an element into a tree and check the
		 *         nodes to see if it should go on the left or right side of the
		 *         tree. It will then return true if the node is successfully
		 *         placed; false otherwise.
		 */
		public BinaryNode insert(T i) {

            if (i.compareTo(element) > 0) {
                  if (rightChild != null) {
                         rightChild = rightChild.insert(i);  // inserts recursively into the tree
                         this.hpos = this.getHeight();  // gives this a height
                     
                         return this.adjust();  // adjust if height is imbalanced
                         
                  }
                  
                  // if right = null
                  rightChild = new BinaryNode(i);  // sets new right child
                  rightChild.hpos = 0;
                  this.hpos = this.getHeight();
                  modCont++;
                  return this;
            }
            
            if (i.compareTo(element) < 0) {
                  if (leftChild != null) {
                         leftChild = leftChild.insert(i);
                         this.hpos = this.getHeight();
                         return this.adjust();
                  }
                  
                  leftChild = new BinaryNode(i);
                  leftChild.hpos = 0;
                  this.hpos = this.getHeight();
                  modCont++;
                  return this;
            }
            
            return this;

     }


		 /**
		 * @return A binary node that has been adjusted
		 * 
		 * This method will check the position of the nodes and see if there is a height imbalance.  It will then make any corrections that are necessary.
		 */
		private BinaryNode adjust() {
			 	 int rightHeight = -1;
            	 int leftHeight = -1;
            	 
            	 if(leftChild != null){
            		 leftHeight = leftChild.hpos;
            	 }
            	 if(rightChild != null) {
            		 rightHeight = rightChild.hpos;
            	 }
            	 if(leftHeight - rightHeight < -1){
            		 int lh = -1;
            		 int rh = -1;
            		 if(rightChild.leftChild != null){
            			 lh = rightChild.leftChild.hpos;
            		 }
            		 
            		 if(rightChild.rightChild != null){
            			 rh = rightChild.rightChild.hpos;
            		 }
            		 
            		 if(lh > rh){
            			 rightChild = rightChild.rotateRight();
            		 }
            		 
            		 return rotateLeft();
            	 }
            	 if(rightHeight - leftHeight < -1){
            		 int lh = -1;
            		 int rh = -1;
            		 if(leftChild.leftChild != null){
            			 lh = leftChild.leftChild.hpos;
            		 }
            		 
            		 if(leftChild.rightChild != null){
            			 rh = leftChild.rightChild.hpos;
            		 }
            		 
            		 if(lh < rh){
            			 leftChild = leftChild.rotateLeft();
            		 }
            		 
            		 return rotateRight();
            	 }

			return this;
      }


		/**
		 * @return the height of the node
		 * 
		 * This method will get the height of the node that calls the method
		 */
		private int getHeight() {
			int lh = -1;
			int rh = -1;
			
			if(leftChild != null){
				lh = leftChild.hpos;
			}
			
			if(rightChild != null){
				rh = rightChild.hpos;
			}
			
			return Math.max(lh, rh) + 1;
			
		}

		/**
		 * @return A rotated binary node
		 * 
		 * This will return a binary node that has been rotated so that it is the new root.  It will also redo any height values
		 */
		private BinaryNode rotateLeft() {
			BinaryNode temp = new BinaryNode(element);
			temp = this.rightChild;
			this.rightChild = temp.leftChild;
			temp.leftChild = this;
			this.hpos = this.getHeight();
			temp.hpos = temp.getHeight();
			rotateCount++;
			return temp;
		}

		/**
		 * @return A rotated binary node
		 * 
		 * This will return a binary node that has been rotated so that it is the new root.  It will also redo any height values
		 */
		private BinaryNode rotateRight() {
			BinaryNode temp = this.leftChild;
			this.leftChild = temp.rightChild;
			temp.rightChild = this;
			this.hpos = this.getHeight();
			temp.hpos = temp.getHeight();
			rotateCount++;
			return temp;
		}

		/**
		 * @param element
		 *            The element that is to be removed
		 * @param root
		 *            Where the root is in the tree
		 * @return True if the value is removed; false otherwise
		 * 
		 *         This is the BinaryNode version of the remove method created
		 *         in the BinarySearchTree class. It will recursively move
		 *         through the tree looking for element to be removed. It will
		 *         then adjust the tree accordingly if required. It will return
		 *         true if the specified element is removed; false otherwise.
		 */
		public BinaryNode remove(T element, bool b) {
			this.hpos = this.getHeight();
			if (element.compareTo(this.element) < 0){
				if(leftChild != null){
					leftChild = leftChild.remove(element, b);
					this.hpos = this.getHeight();
					return this.adjust();
				}
				else {
					b.setBool();
					return null;
				}
			}

			else if (element.compareTo(this.element) > 0) {
				if(rightChild != null){
					rightChild = rightChild.remove(element, b);
					this.hpos = this.getHeight();
					return this.adjust();
				}
				else {
					b.setBool();
					return null;
				}
			}

			else {

				if (leftChild != null && rightChild != null) {
					this.element = leftChild.maxValue();
					leftChild = leftChild.remove(this.element, b);
					return this;
				}

				else if(rightChild == null && leftChild == null) return null;
				else if(rightChild == null && leftChild != null) return leftChild;
				else if(leftChild == null && rightChild != null) return rightChild;

				return this.adjust();
			}

		}

		private T maxValue() {
			return (rightChild == null) ? element : rightChild.maxValue();
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
		private int current = 0;
		private int myMod;
		private BinaryNode currNode;
		private boolean tempBool;

		/**
		 * @param root
		 *            The root of the tree that the iterator will start at
		 * 
		 *            Creates the iterator and adds the root to the stack
		 */
		public PreOrderIterator(BinaryNode root) {
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
				if (currNode == null)
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
			if (tempBool) {
				tempBool = false;
				bool newBool = new bool();
				currNode.remove(currNode.element, newBool);
			} else
				throw new IllegalStateException();
			return;
		}

	}

	/**
	 * @param i
	 *            The element that you will be inserting into the tree
	 * @return true if it was inserted; false otherwise. Will throw an exception
	 *         if there is nothing passed as the argument
	 * 
	 *         This method will insert an element into the tree. It will also
	 *         increase the size paramter
	 */
	public boolean insert(T i) {
		if (i == null)
			throw new IllegalArgumentException();

		else {
			if (isEmpty()) {
				this.root = new BinaryNode(i);
				root.hpos = 0;
				size++;
				modCont++;
				return true;
			} else{
				root = root.insert(i);
				return root.insert(i) != null;
			}

		}

	}

	/**
	 * @param i
	 *            The element that you want removed
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
			bool newBool = new bool();
			root = root.remove(i, newBool);
			return newBool.getbool();
		}
	}

	/**
	 * @return The rotation count
	 * 
	 * This method will count how many times the tree has been rotated
	 */
	public Object getRotationCount() {
		return rotateCount;
	}
	
	/**
	 * @author kellymr1
	 *
	 *	This is the boolean class for insert and remove that will return true if there is something other than null being returned
	 */
	public class bool{
		private boolean bool = true;
		/**
		 * A blank constructor for bool
		 */
		private bool(){
			
		}
		
		/**
		 * @return the bool value
		 * 
		 * Will return whatever value bool is currently set to
		 */
		boolean getbool(){
			return bool;
		}
		
		/**
		 * Will switch the value of bool to the opposite of its current value
		 */
		void setBool(){
			bool = !bool;
		}
	}

}
