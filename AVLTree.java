
/**
 * Simple AVLTree implementation. We do not base this implementation on the TreeNode class, because
 * the nodes do not offer a balance value and the Optionals make implementation more difficult and are
 * not necessary (because null does not appear often due to the nullNode).
 * @author Fabian Schmierer
 */
public class AVLTree<TreeElementType extends Comparable<TreeElementType>> {
	
	/**
	 * Class for nodes of the AVL tree. The implementation is based on the node class for binary trees, however
	 * we do not make use of Optionals, because the pseudonodes ensure, that values for a child are never null.
	 * 
	 * Note that a node also has a balance value, which should be either 0, -1 or 1.
	 */
	class AVLNode {
		private int balance; 
		private TreeElementType key;
		private AVLNode leftChild;
		private AVLNode	rightChild;

		/**
		 * Constructor for all nodes with content in them. Creates a new node and gives them a key, a left
		 * child and a right child. In case, that the node has no child in a classical representation, then
		 * pass the null node as the child. 
		 * @param element key that the node stores
		 * @param leftChild left child of this node (nullNode if the node does not have a child)
		 * @param rightChild right child of this node (nullNode if the node does not have a child)
		 */
		public AVLNode(final TreeElementType element, final AVLNode leftChild, final AVLNode rightChild) {
			key = element;
			this.leftChild = leftChild;
			this.rightChild = rightChild;
			balance = 0;
		}
		
		/**
		 * Standard constructor that should only be used to generate the pseudonodes
		 */
		public AVLNode() {}

		/**
		 * Get the AVL balance of this node. Is -1, 0 or 1.
		 */
		public int getBalance() {
			return balance;
		}

		/**
		 * Set the AVL balance of this node. Note that this method does not check the trees balancing properties.
		 * This has to be done by the caller.
		 * @param newBalance new balance of the node (can be the same as the old balance)
		 * @throws IllegalArgumentException thrown if the new balance is not -1, 0 or 1
		 */
		public void setBalance(final int newBalance) {
			if (newBalance > 1 || newBalance < -1) {
				throw new IllegalArgumentException("An AVL-Balance should be -1, 0 or 1");
			}
			balance = newBalance;
		}
		
		/**
		 * Get the left child of this node.
		 */
		public AVLNode getLeftChild() {
			return leftChild;
		}
		
		/**
		 * Get the right child of this node.
		 */
		public AVLNode getRightChild() {
			return rightChild;
		}
		
		/**
		 * Set the left child of this node.
		 * @throws IllegalArgumentException thrown if the newLeftChild argument is null
		 */
		public void setLeftChild(final AVLNode newLeftChild) {
			if (newLeftChild == null) {
				throw new IllegalArgumentException("The new left child should not be null. Do you mean the null node?");
			}
			leftChild = newLeftChild;
		}
		
		/**
		 * Set the right child of this node.
		 * @throws IllegalArgumentException thrown if the newRightChild argument is null
		 */
		public void setRightChild(final AVLNode newRightChild) {
			if (newRightChild == null) {
				throw new IllegalArgumentException("The new left child should not be null. Do you mean the null node?");
			}
			rightChild = newRightChild;
		}
		
		public void setKey(final TreeElementType newKey) {
			if (newKey == null) {
				throw new IllegalArgumentException("newKey must not be null");
			}
			this.key = newKey;
		}
		
		public TreeElementType getKey() {
			return this.key;
		}
		
		/**
		 * Compare the key of this node with the given key.
		 * @param otherNodeKey key of another mode
		 * @return -1 if this node has no key or this key is smaller, 0 if they have the same value and 1 if this key is bigger.
		 */
		public int compareKeyTo(TreeElementType otherNodeKey) {
			if (key == null) {
				return -1;
			}
			return key.compareTo(otherNodeKey);
		}
		
		/**
		 * Traverses the the node and its subtree in an inorder fashion and prints every nodes key on the
		 * console.
		 */
		public void inorder() {
			if (!leftChild.equals(nullNode)) {
				leftChild.inorder();
			}
			System.out.println(key);
			if (!rightChild.equals(nullNode)) {
				rightChild.inorder();
			}
		}
		
	}
	
	//Pseudonodes
	private AVLNode head, nullNode;
	
	//Global variable: Signals if the tree needs to be rebalanced
	private boolean rebalance = false;

	/**
	 * Construct a new tree with a headNode and a nullNode. The resultist the following tree:
	 * 			head
	 * 		   /    \
	 * 		   \	/
	 * 		  nullNode
	 */
	public AVLTree() {
		nullNode = new AVLNode();
		nullNode.setRightChild(nullNode);
		nullNode.setLeftChild(nullNode);
		head = new AVLNode(null,nullNode,nullNode);
	}
	
	/**
	 * Insert a new key into the tree. Note that this method is not fully implemented yet, because we can
	 * only insert elements into right subtrees.
	 * This operation provides the interface to calling classes and handles the first insertion (special case).
	 * @param newKey key we want to insert. Should not be in the AVL tree yet.
	 * @throws IllegalArgumentException Thrown if argument newKey is null
	 */
	public void insert(final TreeElementType newKey) {
		if (newKey == null) {
			throw new IllegalArgumentException("newKey must not be null.");
		}
		
		if (this.head.getRightChild().equals(nullNode)) {
			this.head.setRightChild(new AVLNode(newKey,nullNode,nullNode));
		} else {
			this.head.setRightChild(insertNode(this.head.getRightChild(), newKey));
		}
	}
	
	/**
	 * This operation performs the real work of inserting the nodes. 
	 * It mainly consists of three cases:
	 * 1) Key already exists (if-case)
	 * 2) Insert the key into the right subtree (else-if-case): Performs a left rotation or a right-then-left-rotation
	 * if necessary.
	 * 
	 * @param currentNode root of the subtree the new key should be inserted to
	 * @param newKey Key we want to insert (should not be in the tree)
	 */
	private AVLNode insertNode(final AVLNode currentNode, final TreeElementType newKey) {
		assert currentNode != null;
		assert newKey != null;
		
		AVLNode newSubtreeRoot = null;
		if (currentNode.compareKeyTo(newKey) == 0) {
			rebalance = false;
			return currentNode;
		} else if (currentNode.compareKeyTo(newKey) < 0) { 
			if (currentNode.getRightChild() != nullNode) {
				currentNode.setRightChild(insertNode(currentNode.getRightChild(), newKey));
				if (currentNode != head && rebalance) {
					switch (currentNode.getBalance()) {
					case -1:
						if (currentNode.getRightChild().getBalance() == -1) {
							newSubtreeRoot = rotateLeft(currentNode);
							newSubtreeRoot.getLeftChild().setBalance(0);
						} else {
							int currentNodeBalance = currentNode.getRightChild().getLeftChild().getBalance();
							currentNode.setRightChild(rotateRight(currentNode.getRightChild()));
							newSubtreeRoot = rotateLeft(currentNode);
							newSubtreeRoot.getRightChild().setBalance((currentNodeBalance == 1) ? -1 : 0);
							newSubtreeRoot.getLeftChild().setBalance((currentNodeBalance == -1) ? 1 : 0);
						}
						newSubtreeRoot.setBalance(0);
						rebalance = false;
						return newSubtreeRoot;
					case 0:
						currentNode.setBalance(-1);
						return currentNode; 
					case 1: 
						currentNode.setBalance (0);
						rebalance = false;
						return currentNode;
					}
				} else {
					return currentNode;
				}
			} else { 
				AVLNode newLeafNode = new AVLNode(newKey,nullNode,nullNode);
				currentNode.setRightChild(newLeafNode);
				currentNode.setBalance(currentNode.getBalance() - 1);
				rebalance = (currentNode.getBalance() <= -1);
				return currentNode;
			}
		} else { 
			
			//TODO: This case is not implemented yet. This will be the task in one of your exercises.
			
			currentNode.setBalance(currentNode.getBalance() + 1);
			rebalance = (currentNode.getBalance() >= 1);
		}
		
		return newSubtreeRoot;
	}
	
	/**
	 * Perform a left rotation on the argument node and it's subtree. 
	 * 
	 * 				3						4
	 * Example: 	 \					   / \
	 * 				  4 		=> 		  3   5
	 * 				   \
	 * 					5
	 * 
	 * @param node Node with balance -2. The child of this node is rotated above it
	 * @return Return the subtrees new root (which was the right child of the input node)
	 */
	private AVLNode rotateLeft(final AVLNode node) {
		assert node!=null;
		System.out.println("Perform left rotation with: "+node.getKey());
		AVLNode newRoot = node.getRightChild();
		node.setRightChild(node.getRightChild().getLeftChild());
		newRoot.setLeftChild(node);
		return newRoot;
	}
	
	/**
	 * Perform a right rotation on the argument node and it's subtree. 
	 * 
	 * 				 3						2
	 * Example: 	/					   / \
	 * 			   2   		=> 		  	  1   3
	 * 			  /   
	 * 			 1		
	 * 
	 * @param node Node with balance +2. The child of this node is rotated above it
	 * @return Return the subtrees new root (which was the left child of the input node)
	 */
	private AVLNode rotateRight(final AVLNode node) {
		assert node!=null;
		System.out.println("Perform right rotation with: "+node.getKey());
		AVLNode newRoot = node.getLeftChild();
		node.setLeftChild(node.getLeftChild().getRightChild());
		newRoot.setRightChild(node);
		return newRoot;
	}
	
	/**
	 * Traverses the the whole tree in an inorder fashion and prints every nodes key on the
	 * console.
	 */
	public void inorder() {
		head.getRightChild().inorder();
	}
	
}
