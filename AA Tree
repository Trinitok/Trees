package AATree;

import java.util.ArrayList;

/**
 * Self balancing tree with only two possible rotations (no symmetric cases)
 *
 * @author goistjt.
 *         Created Feb 10, 2015.
 * @param <T>
 */
public class AATree<T extends Comparable<? super T>> {
	BinaryNode root;
	int size;
	int rotationCount = 0;

	/**
	 * Constructs a new AATree with a null root
	 *
	 */
	public AATree() {
		this.root = null;
	}

	/**
	 * Returns the number of elements in the tree
	 *
	 * @return
	 */
	public int size() {
		return this.size;
	}

	/**
	 * Returns the number of rotations performed during the existence of the tree
	 *
	 * @return
	 */
	public int rotationCount() {
		return this.rotationCount;
	}

	/**
	 * Returns a pre-order representation of the tree
	 *
	 * @return
	 */
	public ArrayList<Object> toArrayList() {
		ArrayList<Object> a = new ArrayList<>();
		if (this.root == null) {
			return a;
		}
		this.root.toArrayList(a);
		return a;
	}

	/**
	 * Inserts a comparable object into the tree, bottom-up
	 *
	 * @param o
	 * @return
	 */
	public boolean insert(T o) {
		if (o == null) {
			throw new IllegalArgumentException();
		}
		if (this.root == null) {
			this.root = new BinaryNode(o);
			this.size++;
			return true;
		}
		BooleanWrapper b = new BooleanWrapper();
		this.root = this.root.insert(o, b);
		return b.getValue();
	}

	/**
	 * Removes an object from the tree, bottom-up
	 *
	 * @param o
	 * @return
	 */
	public boolean remove(T o) {
		if (o == null) {
			throw new IllegalArgumentException();
		}
		if (this.root == null) {
			return false;
		}
		if (this.root.element == o && this.size == 1) {
			this.root = null;
			this.size--;
			return true;
		}
		BooleanWrapper b = new BooleanWrapper();
		this.root = this.root.remove(o, b);
		return b.getValue();
	}

	@Override
	public String toString() {
		return toArrayList().toString();
	}

	/**
	 * Class for storing data inside the tree and doing all the recursive work
	 *
	 * @author goistjt.
	 *         Created Feb 10, 2015.
	 */
	public class BinaryNode {
		T element;
		BinaryNode leftChild;
		BinaryNode rightChild;
		int level = 1;

		BinaryNode(T o) {
			this.element = o;
		}

		BinaryNode insert(T o, BooleanWrapper b) {
			int compare = o.compareTo(this.element);
			if (compare < 0) {
				if (this.leftChild == null) {
					this.leftChild = new BinaryNode(o);
					AATree.this.size++;
				} else {
					this.leftChild = this.leftChild.insert(o, b);
				}
			} else if (compare > 0) {
				if (this.rightChild == null) {
					this.rightChild = new BinaryNode(o);
					AATree.this.size++;
				} else {
					this.rightChild = this.rightChild.insert(o, b);
				}
			} else {
				b.setFalse();
			}
			BinaryNode temp = this.skew();
			return temp.split();
		}

		BinaryNode remove(T o, BooleanWrapper b) {
			int comp = o.compareTo(this.element);
			if (comp < 0) {
				if (this.leftChild == null) {
					b.setFalse();
				} else {
					this.leftChild = this.leftChild.remove(o, b);
				}
				return this.balance();
			}
			if (comp > 0) {
				if (this.rightChild == null) {
					b.setFalse();
				} else {
					this.rightChild = this.rightChild.remove(o, b);
				}
				return this.balance();
			}
			if (this.leftChild != null && this.rightChild != null) {
				this.element = this.leftChild.maxRight();
				this.leftChild = this.leftChild.remove(this.element, b);
				return this.balance();
			}
			AATree.this.size--;
			return (this.leftChild == null) ? this.rightChild : this.leftChild;

		}

		private T maxRight() {
			if (this.rightChild == null) {
				return this.element;
			}
			return this.rightChild.maxRight();
		}

		private BinaryNode balance() {
			if (this.leftChild == null && this.rightChild != null) {
				this.level--;
				if (this.rightChild.level > this.level) {
					this.rightChild.level = this.level;
				}
				return this.subBalance();
			}
			if (this.rightChild == null && this.leftChild != null) {
				this.level--;
				return this.subBalance();
			}
			if ((this.leftChild != null && this.leftChild.level < this.level - 1)
					|| (this.rightChild != null && this.rightChild.level < this.level - 1)) {
				this.level--;
				if (this.rightChild.level > this.level) {
					this.rightChild.level = this.level;
				}
				return this.subBalance();
			}
			return this;
		}
		
		private BinaryNode subBalance() {
			BinaryNode temp = this.skew();
			if (temp.rightChild != null) {
				temp.rightChild = temp.rightChild.skew();
				if (temp.rightChild.rightChild != null) {
					temp.rightChild.rightChild = temp.rightChild.rightChild
							.skew();
				}
			}
			temp = temp.split();
			if (temp.rightChild != null) {
				temp.rightChild = temp.rightChild.split();
			}
			return temp;
		}

		private BinaryNode skew() {
			if (this.leftChild != null && this.leftChild.level == this.level) {
				return this.rightRotation();
			}
			return this;
		}

		private BinaryNode split() {
			if (this.rightChild != null && this.rightChild.rightChild != null) {
				if (this.level == this.rightChild.level
						&& this.rightChild.level == this.rightChild.rightChild.level) {
					return this.leftRotation();
				}
			}
			return this;
		}

		private BinaryNode rightRotation() {
			AATree.this.rotationCount++;
			BinaryNode x = this;
			BinaryNode p = this.leftChild;
			BinaryNode b = p.rightChild;

			p.rightChild = x;
			x.leftChild = b;

			return p;
		}

		private BinaryNode leftRotation() {
			AATree.this.rotationCount++;
			BinaryNode x = this;
			BinaryNode r = this.rightChild;
			BinaryNode b = r.leftChild;

			r.leftChild = x;
			x.rightChild = b;
			r.level++;

			return r;
		}

		void toArrayList(ArrayList<Object> temp) {
			temp.add(this);
			if (this.leftChild != null) {
				this.leftChild.toArrayList(temp);
			}
			if (this.rightChild != null) {
				this.rightChild.toArrayList(temp);
			}
		}

		/**
		 * Returns the element of this node
		 *
		 * @return
		 */
		public T getElement() {
			return this.element;
		}

		/**
		 * Returns the level of this node
		 *
		 * @return
		 */
		public int getLevel() {
			return this.level;
		}

	}

	private class BooleanWrapper {
		boolean b;

		public BooleanWrapper() {
			this.b = true;
		}

		public boolean getValue() {
			return this.b;
		}

		public void setFalse() {
			this.b = false;
		}
	}

}
