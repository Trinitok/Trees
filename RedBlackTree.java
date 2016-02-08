package redblacktree;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * 
 * @author kellymr1
 * 
 * @param <T>
 *            - Makes the Red Black Tree a generic type
 * 
 *            This is the RedBlackTree class. It will insert an element in
 *            O(logn) time and perform swaps based on its colors.
 */
@SuppressWarnings("rawtypes")
public class RedBlackTree<T extends Comparable<? super T>> implements
		Iterable<RedBlackTree.BinaryNode> {
	public BinaryNode root;
	private int rotCount;
	public int modCont = 0;
	public int size = 0;

	public enum Color {
		RED, BLACK
	}

	/**
	 * No args constructor for a BinarySearchTree that will set the root to null
	 */
	public RedBlackTree() {
		root = null;
	}

	/**
	 * @param n
	 *            the root node
	 * 
	 *            Constructor for the RedBlackTree which will take in a
	 *            BinaryNode and make it the root
	 */
	public RedBlackTree(BinaryNode n) {
		root = n;
	}

	/**
	 * 
	 * @return int height of tree
	 * 
	 *         This method will recursively find the height of the tree. It will
	 *         return -1 if the root is null
	 */
	public int height() {
		if (root == null)
			return -1;
		return root.height();
	}

	/**
	 * @return int - the number of rotations
	 * 
	 *         Returns the number of rotations during the insertion and deletion
	 *         of the red black tree
	 */
	public int getRotationCount() {
		return rotCount;
	}

	/**
	 * @return int - The size of the tree
	 * 
	 *         This method will recursively traverse through the tree and then
	 *         return the size. If the root is equal to null, it will return 0
	 */
	public int size() {
		if (root == null)
			return 0;
		return root.size();
	}

	/**
	 * 
	 * @return true if root is null, false otherwise
	 * 
	 *         Tests to see if the root exists. If the root is null, it will
	 *         return true. If the root exists and is not null, it will return
	 *         false
	 */
	public boolean isEmpty() {
		return root == null;
	}

	public String toString() {
		return this.toArrayList().toString();
	}

	/**
	 * @return Iterator<RedBlackTree.BinaryNode> - The iterator for the tree
	 * 
	 *         This method will return the iterator for the Red Black Tree using
	 *         a pre order iterator from the AVL Tree class.
	 */
	public Iterator<RedBlackTree.BinaryNode> iterator() {

		return new PreOrderIterator(root);
	}

	/**
	 * Inserts elements into the tree
	 * 
	 * @param o
	 * @return
	 */
	public boolean insert(T element) {
		if (root == null) {
			root = new BinaryNode();
			root.color = Color.BLACK;
			root.element = element;
			return true;
		}

		myBool check = new myBool();
		root.colorFlip();
		root.insert(element, check, null, null, null);
		root.color = Color.BLACK;
		return check.isTrue();
	}

	/**
	 * Creates an arraylist of elements
	 * 
	 * @return
	 */
	public ArrayList<T> toArrayList() {
		if (isEmpty())
			return new ArrayList<T>();
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
		if (root == null)
			return new Array[0];
		return this.toArrayList().toArray();
	}

	public boolean remove(T el) {

		if (root == null) { // test to see if the size is 0
			return false;
		}

		boolean ret;
		if (root.checkBlackKids()) {
			root.color = Color.RED;
			if (root.element.compareTo(el) < 0) {
				if (root.rightChild == null) {
					root.color = Color.BLACK;
					return false;
				}

				ret = root.rightChild.remove2(el, root.leftChild, root, null);
				root.color = Color.BLACK;
				return ret;
			}

			if (root.element.compareTo(el) > 0) {
				if (root.leftChild == null) {
					root.color = Color.BLACK;
					return false;
				}

				ret = root.leftChild.remove2(el, root.rightChild, root, null);

				if (root != null)
					root.color = Color.BLACK;
				return ret;
			}

			ret = root.remove3(null, null, null);
			if (root != null) {
				root.color = Color.BLACK;
			}
			return ret;
		}

		ret = root.remove2B(el, null, null, null);

		if (root != null)
			root.color = Color.BLACK;

		return ret;

	}

	/**
	 * @author kellymr1
	 * 
	 *         The class for the binary nodes which make up the Binary Search
	 *         Tree
	 */
	/**
	 * @author kellymr1
	 * 
	 */
	public class BinaryNode {
		public T element;
		public BinaryNode leftChild;
		private BinaryNode rightChild;
		private Stack<T> stack = new Stack<T>();
		private Color color = Color.RED;

		/**
		 * @return The height of the tree
		 * 
		 *         This method will recursively traverse through the tree and
		 *         count up the height of each side. It will then return the
		 *         larger height
		 */
		public int height() {

			if (this.leftChild == null && this.rightChild == null) {
				return 0;
			}

			int leftHeight = 0;
			if (leftChild != null) {
				leftHeight = leftChild.height() + 1;
			}

			int rightHeight = 0;
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
		 * @param sibling
		 *            - The sibling to the current node
		 * @param parent
		 *            - The parent of the current node
		 * @param grand
		 *            - the grandparent of the current node
		 * @return true if the node is removed, false otherwise
		 */
		public boolean remove3(BinaryNode sibling, BinaryNode parent,
				BinaryNode grand) {
			if (this.leftChild == null && this.rightChild == null) {
				if (parent == null) {
					root = null;
					return true;
				}

				if (parent.leftChild == this) {
					parent.leftChild = null;
				} else {
					parent.rightChild = null;
				}

				return true;
			}

			if (this.leftChild == null) {
				this.rightChild.color = Color.BLACK;
				if (parent == null) {
					root = this.rightChild;
					return true;
				}
				if (this == parent.leftChild) {
					parent.leftChild = this.rightChild;
					return true;
				}

				parent.rightChild = this.rightChild;
				return true;
			}

			if (this.rightChild == null) {
				this.leftChild.color = Color.BLACK;
				if (parent == null) {
					root = this.leftChild;
					return true;
				}
				if (this == parent.leftChild) {
					parent.leftChild = this.leftChild;
					return true;
				}

				parent.rightChild = this.leftChild;
				return true;
			}

			T temp = this.leftChild.maxValue();

			if (this.color == Color.RED) {
				this.element = temp;
				return this.leftChild.remove2(temp, this.rightChild, this,
						parent);
			}

			this.remove2B(temp, sibling, parent, grand);
			this.element = temp;

			return true;
		}

		/**
		 * @param el
		 *            - The element to be removed
		 * @param sibling
		 *            - The sibling of the current element
		 * @param parent
		 *            - The parent of the current element
		 * @param grand
		 *            - The grandparent of the current element
		 * @return true if the element is removed, false otherwise
		 * 
		 *         this will remove the node. This method will take it to the
		 *         respective node
		 */
		public boolean remove2(T el, BinaryNode sibling, BinaryNode parent,
				BinaryNode grand) {
			if (sibling == null)
				System.out.println("sibling is null at " + this.element);
			if (this.checkBlackKids())
				return remove2A(el, sibling, parent, grand);
			return this.remove2B(el, sibling, parent, grand);

		}

		public boolean remove2B(T el, BinaryNode sibling, BinaryNode parent,
				BinaryNode grand) {

			if (this.element.compareTo(el) == 0) {
				return this.remove3(sibling, parent, grand);
			}

			if (this.element.compareTo(el) > 0) {
				if (this.leftChild == null)
					return false;

				if (this.leftChild.color == Color.BLACK) {
					return this.leftChild.remove2B2(el, this.rightChild, this,
							parent);
				}

				return this.leftChild.remove2B1(el, this.rightChild, this,
						parent);
			}

			if (this.rightChild == null)
				return false;

			if (this.rightChild.color == Color.BLACK) {
				return this.rightChild.remove2B2(el, this.leftChild, this,
						parent);
			}

			return this.rightChild.remove2B1(el, this.leftChild, this, parent);

		}

		private boolean remove2B2(T el, BinaryNode sibling, BinaryNode parent,
				BinaryNode grand) {

			parent.color = Color.RED;
			sibling.color = Color.BLACK;

			if (parent.leftChild == sibling) {
				if (grand == null) {
					root = parent.removeRotateRight();
				}

				else if (parent == grand.leftChild) {
					grand.leftChild = parent.removeRotateRight();
				} else {
					grand.rightChild = parent.removeRotateRight();
				}

				return this.remove2(el, parent.leftChild, parent, sibling);

			}
			if (grand == null) {
				root = parent.removeRotateLeft();
			}

			else if (parent == grand.leftChild) {
				grand.leftChild = parent.removeRotateLeft();
			} else {
				grand.rightChild = parent.removeRotateLeft();
			}

			return this.remove2(el, parent.rightChild, parent, sibling);

		}

		private boolean remove2B1(T el, BinaryNode sibling, BinaryNode parent,
				BinaryNode grand) {
			return this.traverse(el, sibling, parent, grand);
		}

		public boolean find(T el) {
			boolean left = false;
			boolean right = false;
			if (this.element.compareTo(el) == 0)
				return true;

			if (this.rightChild != null && this.element.compareTo(el) < 0) {
				left = this.rightChild.find(el);
			}
			if (this.leftChild != null && this.element.compareTo(el) > 0) {
				right = this.leftChild.find(el);
			}

			return right || left;
		}

		/**
		 * @return int - The size of the tree
		 * 
		 *         This method will recursively traverse through the tree and
		 *         then return the size
		 */
		public int size() {
			int sizevar = 1;
			if (rightChild != null) {
				sizevar += rightChild.size();
			}
			if (leftChild != null) {
				sizevar += leftChild.size();
			}
			return sizevar;
		}

		/**
		 * @return String The String version of the tree
		 * 
		 *         This method will traverse the tree and take each element and
		 *         make it part of a string
		 */
		public String toString() {
			if (leftChild == null && rightChild == null) {
				return this.element.toString();
			}

			else if (leftChild != null && rightChild == null) {
				return leftChild.toString() + ", " + this.element;
			}

			else if (leftChild == null && rightChild != null) {
				return this.element + ", " + rightChild.toString();
			}

			else if (this.leftChild != null && this.rightChild != null) {
				return leftChild.toString() + this.element
						+ rightChild.toString() + ", ";
			} else {
				return "";
			}
		}

		/**
		 * 
		 * @param i
		 *            - The element to be inserted
		 * @return
		 * 
		 *         This method will recursively traverse through the RED Black
		 *         Tree keeping track of the various positions such as the
		 *         parent, grandparent, and great grandparent. It will return
		 *         the myBool as true if the element is successfully inserted,
		 *         false otherwise. It will also balance the tree as needed
		 *         testing as it goes up and down the tree for all possible
		 *         cases
		 */
		public void insert(T i, myBool b, BinaryNode parent, BinaryNode grand,
				BinaryNode gg) {
			this.colorFlip();
			this.ConsecutiveReds(parent, grand, gg);

			if (i.compareTo(element) < 0) { // i < element
				if (leftChild != null) {
					leftChild.insert(i, b, this, parent, grand);
					return;
				}
				leftChild = new BinaryNode();
				leftChild.element = i;

				if (color == Color.RED) {
					if (parent != null) {
						if (grand != null) { // Check for possible root cases
												// with grand and parent
							if (grand.element.compareTo(parent.element) > 0) {
								// right-left
								if (parent.rightChild == this) {
									this.leftChild.swapColor(); // make sure to
																// swap the
																// colors before
																// rotating
									this.swapColor();
									parent.rightChild = this.rotateRight();
									grand.leftChild = parent.rotateLeft();

								}
								// right rotation
								else {
									grand.leftChild = parent.rotateRight();
								}
							} else {
								// right-left rotate
								if (parent.rightChild == this) {
									this.leftChild.swapColor();
									this.swapColor();
									parent.rightChild = this.rotateRight();
									grand.rightChild = parent.rotateLeft();

								}
								// right-right
								else {
									grand.rightChild = parent.rotateRight();
								}
							}
						}

						// Check root cases as the parent
						if (parent == root) {
							// double rotate first the pr then the root
							if (parent.rightChild == this) {
								this.swapColor();
								this.leftChild.swapColor();
								parent.rightChild = this.rotateRight();
								root = root.rotateLeft();
							}

							// rotate the root right
							else {
								root = root.rotateRight();
							}
						}
					}
				}

				b.setBool();
			}

			if (i.compareTo(element) > 0) { // i > element
				if (rightChild != null) {
					rightChild.insert(i, b, this, parent, grand);
					return;
				}
				rightChild = new BinaryNode();
				rightChild.element = i;

				if (isRed(this)) { // test to see if this is red
					if (parent != null) { // check to see if there is the parent
						if (grand != null) { // check to see if the grand exists
							if (grand.element.compareTo(parent.element) > 0) {
								// double rotation first left then rotate the
								// parent right
								if (parent.leftChild == this) {
									this.rightChild.swapColor();
									this.swapColor();
									parent.leftChild = this.rotateLeft();
									grand.leftChild = parent.rotateRight();

								}
								// single left rotation on parent
								else {
									grand.leftChild = parent.rotateLeft();
								}
							} else {
								if (parent.leftChild == this) {
									this.rightChild.swapColor();
									this.swapColor();
									parent.leftChild = this.rotateLeft();
									grand.rightChild = parent.rotateRight();

								}
								// single left rotate
								else {
									grand.rightChild = parent.rotateLeft();
								}
							}
						}

						if (parent == root) {
							// will double rotate the root to the left then
							// right
							if (parent.leftChild == this) {
								this.swapColor();
								this.rightChild.swapColor();
								parent.leftChild = this.rotateLeft();
								root = root.rotateRight();
							}

							// will rotate the root to the left
							else {
								root = root.rotateLeft();
							}
						}

					}
				}
				b.setBool();

			}

		}

		/**
		 * 
		 * @param parent
		 *            - The parent of the curent node
		 * @param grand
		 *            - The grand parent of the current node
		 * @param gg
		 *            - The great grandparent of the current node
		 * 
		 *            This method will test all of the consecutive red nodes. If
		 *            it finds some, it will then perform rotations based on
		 *            what if required
		 */
		private void ConsecutiveReds(BinaryNode parent, BinaryNode grand,
				BinaryNode gg) {
			if (parent != null) {
				if (this.color == Color.RED && parent.color == Color.RED) {

					// check to see if the great grand == null for root and
					// double rotates
					if (gg != null) {

						if (gg.leftChild == grand && grand.leftChild == parent) {

							// double rotate left and right
							if (parent.rightChild == this) {
								this.swapColor();
								parent.swapColor();
								grand.leftChild = parent.rotateLeft();
								gg.leftChild = grand.rotateRight();
								return;
							}
							// Rotate to the right
							else {
								gg.leftChild = grand.rotateRight();
								return;
							}
						}

						// Right child of the gg gets worked with
						if (gg.rightChild == grand
								&& grand.rightChild == parent) {

							// double rotate with a right and left
							if (parent.leftChild == this) {
								this.swapColor();
								parent.swapColor();
								grand.rightChild = parent.rotateRight();
								gg.rightChild = grand.rotateLeft();
								return;
							}
							// rotate the child of the great grand to the left
							else {
								gg.rightChild = grand.rotateLeft();
								return;
							}
						}

					}

					// This will check for root cases
					if (grand == root) {

						// handles root double rotations
						if (grand.leftChild == parent) {
							if (parent.rightChild == this) {
								this.swapColor();
								parent.swapColor();
								grand.leftChild = parent.rotateLeft();
								root = root.rotateRight();

								return;
							}
							root = root.rotateRight();
							return;
						}

						// grand left != parent
						else {
							// handles root double rotations
							if (parent.leftChild == this) {
								this.swapColor();
								parent.swapColor();
								grand.rightChild = parent.rotateRight();
								root = root.rotateLeft();
								return;
							}

							//
							root = root.rotateLeft();
							return;
						}
					}
				}
			}
		}

		/**
		 * @param n
		 *            The node you wish to see is red
		 * @return true if the node is red, false otherwise
		 * 
		 *         This method will simply test to see if the node is red or not
		 */
		public boolean isRed(BinaryNode n) {
			if (n == null)
				return false;

			return (n.color == Color.RED);
		}

		/**
		 * This method will make a black node red and a red node black
		 */
		private void swapColor() {
			if (this.color == Color.RED)
				this.color = Color.BLACK;
			else
				this.color = Color.RED;

		}

		/**
		 * @return true if the colors were successfully flipped, false otherwise
		 * 
		 *         This method will check to see if the right and left
		 *         children's colors are RED. If they are, it will perform a
		 *         color flip
		 */
		private boolean colorFlip() {
			if (this.leftChild != null && this.rightChild != null)
				if (this.leftChild.color == Color.RED
						&& this.rightChild.color == Color.RED) {
					this.leftChild.swapColor();
					this.rightChild.swapColor();
					this.swapColor();
					return true;
				}
			return false;

		}

		private BinaryNode removeRotateLeft() {
			rotCount++;
			BinaryNode temp = this;
			BinaryNode temp2 = this.rightChild;
			BinaryNode kid = temp2.leftChild;

			temp.rightChild = kid;

			temp2.leftChild = temp;

			return temp2;
		}

		private BinaryNode removeRotateRight() {
			rotCount++;
			BinaryNode temp = this;
			BinaryNode temp2 = this.leftChild;
			BinaryNode kid = temp2.rightChild;

			temp.leftChild = kid;

			temp2.rightChild = temp;

			return temp2;
		}

		private BinaryNode removeDoubleLeft() {
			this.rightChild = this.rightChild.removeRotateRight();
			return this.removeRotateLeft();
		}

		private BinaryNode removeDoubleRight() {
			this.rightChild = this.rightChild.removeRotateLeft();
			return this.removeRotateRight();
		}

		/**
		 * @return Binary Node - The new root of the shifted portion
		 * 
		 *         This will take several red nodes and rotate them to the left.
		 *         It will then return the Node that is the root of all the
		 *         nodes. It will also swap any colors and keep track of the
		 *         rotations
		 */
		private BinaryNode rotateLeft() {
			BinaryNode temp = rightChild;
			this.rightChild = temp.leftChild;
			this.rightChild = temp.leftChild;
			temp.leftChild = this;
			this.swapColor();
			temp.swapColor();
			rotCount++;
			return temp;

		}

		/**
		 * @return Binary Node - The new root of the shifted portion
		 * 
		 *         This will take several red nodes and rotate them to the left.
		 *         It will then return the Node that is the root of all the
		 *         nodes. It will also swap any colors and keep track of the
		 *         rotations
		 */
		private BinaryNode rotateRight() {
			BinaryNode temp = this.leftChild;
			this.leftChild = temp.rightChild;
			temp.rightChild = this;
			this.swapColor();
			temp.swapColor();

			rotCount++;

			return temp;
		}

		/**
		 * @param list
		 * @return ArrayList<T>
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
		 * @return Iterator<T> - The iterator of the elements
		 * 
		 *         Will look to see if there are children and if there are, it
		 *         will push them onto the stack and then return the iterator of
		 *         the stack
		 */
		public Iterator<T> Iterator() {

			stack.push(element);

			if (this.rightChild != null) {
				rightChild.Iterator();
			}

			if (this.leftChild != null) {
				leftChild.Iterator();
			}

			Iterator<T> ret = stack.iterator();

			return ret;
		}

		/**
		 * @return true if the kid is black, false otherwise
		 * 
		 *         This method will check to see if the children are black. If
		 *         they are red, then it will return false
		 */
		public boolean checkBlackKids() {
			if (this.leftChild == null && this.rightChild == null) {
				return true;
			}

			if (this.leftChild == null) {
				return rightChild.color == Color.BLACK;
			}

			if (this.rightChild == null) {
				return leftChild.color == Color.BLACK;
			}

			return rightChild.color == Color.BLACK
					&& leftChild.color == Color.BLACK;
		}

		/**
		 * @param sibling
		 *            - The sibling of the element
		 * @param parent
		 *            - The parent of the element
		 * @param grand
		 *            - The grandparent of the element
		 * @param element
		 *            The element that is to be removed
		 * @param root
		 *            Where the root is in the tree
		 * @return True if the value is removed; false otherwise
		 * 
		 *         This is the BinaryNode version of the remove method created
		 *         in the RedBlackTree class. It will recursively move through
		 *         the tree looking for element to be removed. It will then
		 *         adjust the tree accordingly if required. It will return true
		 *         if the specified element is removed; false otherwise.
		 */
		public boolean remove2A(T el, BinaryNode sibling, BinaryNode parent,
				BinaryNode grand) {

			// going to the left
			if (sibling.checkBlackKids()) {
				return this.remove2A1(el, sibling, parent, grand);
			}

			if (parent.leftChild == sibling) {
				if (sibling.leftChild != null
						&& sibling.leftChild.color == Color.RED) {
					return this.remove2A3(el, sibling, parent, grand);
				}

				return this.remove2A2(el, sibling, parent, grand);
			}

			if (sibling.rightChild != null
					&& sibling.rightChild.color == Color.RED) {
				return this.remove2A3(el, sibling, parent, grand);
			}

			return this.remove2A2(el, sibling, parent, grand);

		}

		private boolean remove2A3(T el, BinaryNode sibling, BinaryNode parent,
				BinaryNode grand) {
			if (parent.leftChild == sibling) {
				sibling.leftChild.color = Color.BLACK;
				if (grand == null) {
					root = parent.removeRotateRight();
				}

				else if (parent == grand.leftChild) {
					grand.leftChild = parent.removeRotateRight();
				} else {
					grand.rightChild = parent.removeRotateRight();
				}

			} 
			
			else {
				sibling.rightChild.color = Color.BLACK;
				if (grand == null) {
					root = parent.removeRotateLeft();
				}

				else if (parent == grand.leftChild) {
					grand.leftChild = parent.removeRotateLeft();
				} 
				
				else {
					grand.rightChild = parent.removeRotateLeft();
				}

			}

			this.color = Color.RED;
			parent.color = Color.BLACK;
			sibling.color = Color.RED;

			return this.traverse(el, sibling, parent, grand);
		}

		private boolean remove2A2(T el, BinaryNode sibling, BinaryNode parent,
				BinaryNode grand) {

			if (parent.leftChild == sibling) {
				if (grand == null) {
					root = parent.removeDoubleRight();
				}

				else if (parent == grand.leftChild) {
					grand.leftChild = parent.removeDoubleRight();
				} else {
					grand.rightChild = parent.removeDoubleRight();
				}

			} 
			
			else {
				if (grand == null) {
					root = parent.removeDoubleLeft();
				}

				else if (parent == grand.leftChild) {
					grand.leftChild = parent.removeDoubleLeft();
				} 
				
				else {
					grand.rightChild = parent.removeDoubleLeft();
				}

			}

			this.color = Color.RED;
			parent.color = Color.BLACK;

			return this.traverse(el, sibling, parent, grand);

		}

		private boolean remove2A1(T el, BinaryNode sibling, BinaryNode parent,
				BinaryNode grand) {

			parent.color = Color.BLACK;
			this.color = Color.RED;
			sibling.color = Color.RED;

			return traverse(el, sibling, parent, grand);

		}

		/**
		 * @param el - The current element to find
		 * @param sibling - The sibling of the current node
		 * @param parent - The parent of the current node
		 * @param grand - The grandparent of the current node
		 * @return - True or false based on where the node is sent
		 */
		public boolean traverse(T el, BinaryNode sibling, BinaryNode parent,
				BinaryNode grand) {
			if (this.element.compareTo(el) < 0) {
				if (this.rightChild == null) {
					return false;
				}

				return this.rightChild
						.remove2(el, this.leftChild, this, parent);
			}

			if (this.element.compareTo(el) > 0) {  // if this if greater than the element
				if (this.leftChild == null) {
					return false;
				}

				return this.leftChild
						.remove2(el, this.rightChild, this, parent);

			}

			return this.remove3(sibling, parent, grand);  // if it is the one to be deleted
		}

		/**
		 * @return T the max value in the tree
		 * 
		 *         This method will traverse the right side of the tree until it
		 *         finds the max value. It will then return that value
		 */
		private T maxValue() {
			return (rightChild == null) ? element : rightChild.maxValue();
		}

		/**
		 * This method will try to go through and reverse the order of the tree.
		 * It will make the elements on one side on the other by switching the
		 * nodes and recursing down
		 */
		public void reverse() {
			while (leftChild != null && rightChild != null) {
				BinaryNode temp = leftChild;

				leftChild = rightChild;
				rightChild = temp;
			}
		}

		/**
		 * @return an element
		 * 
		 *         Returns the current Node's element
		 */
		public T getElement() {
			return this.element;
		}

		/**
		 * @return Color - The color of the node
		 * 
		 *         Returns the current Node's color
		 */
		public Color getColor() {
			return this.color;
		}

		public BinaryNode getLeftChild() {
			// DONE Auto-generated method stub
			return this.leftChild;
		}

		public BinaryNode getRightChild() {
			// DONE Auto-generated method stub
			return this.rightChild;
		}

	}

	/**
	 * @author kellymr1
	 * 
	 *         This is the class for the preorder iterator. It is within the
	 *         BinarySearchTree class so it is hidden
	 */
	private class PreOrderIterator implements Iterator<RedBlackTree.BinaryNode> {

		private Stack<RedBlackTree.BinaryNode> s = new Stack<RedBlackTree.BinaryNode>();
		RedBlackTree.BinaryNode root;
		private int current = 0;
		private int myMod;
		private RedBlackTree.BinaryNode currNode;
		private boolean tempBool;

		/**
		 * @param root
		 *            The root of the tree that the iterator will start at
		 * 
		 *            Creates the iterator and adds the root to the stack
		 */
		public PreOrderIterator(BinaryNode root) {
			if (root != null) {
				this.root = root;
				this.s.push(root);
			}
			myMod = modCont;
		}

		public boolean hasNext() {
			return !s.empty();
		}

		public RedBlackTree.BinaryNode next() {
			if (myMod != modCont)
				throw new ConcurrentModificationException();

			if (hasNext()) {
				current++;
				RedBlackTree.BinaryNode temp = (RedBlackTree.BinaryNode) s
						.pop();
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
				return currNode;
			}

			else
				throw new NoSuchElementException();

		}

		@SuppressWarnings("unchecked")
		public void remove() {
			// if (currNode == null)
			// throw new IllegalStateException();
			// if (tempBool) {
			// tempBool = false;
			//
			// currNode.remove2A(currNode.element, null, null);
			// } else
			// throw new IllegalStateException();
			// return;
		}
		//
	}

	public class myBool {
		private boolean value;

		public myBool() {
			value = false;
		}

		public boolean isTrue() {
			if (value)
				return true;
			else
				return false;
		}

		public void setBool() {
			value = !value;
		}

	}

}
