

import java.util.Optional;

/**
 * Simple RBTree implementation. We do not base this implementation on the TreeNode class, because
 * the nodes do not offer a coloring option and the Optionals make implementation more difficult and are
 * not necessary (because null does not appear often due to the nullNode).
 * @author Fabian Schmierer
 */
public class RBTree<TreeElementType extends Comparable<TreeElementType>> {
	
	//Enum for the color of a node (replaces the boolean value)
	private enum Color {
		RED, BLACK
	}
	
	//Pseudonodes
	private RBNode head, nullNode;
	
	/**
	 * Construct a new tree with a headNode and a nullNode. The resultist the following tree:
	 * 			head
	 * 		   /    \
	 * 		   \	/
	 * 		  nullNode
	 */
	public RBTree() {
		nullNode = new RBNode();
		nullNode.setLeftChild(nullNode);
		nullNode.setRightChild(nullNode);
		head = new RBNode(null,nullNode,nullNode);
	}
	
	/**
	 * Class for nodes of the RB tree. The implementation is based on the node class for binary trees, however
	 * we do not make use of Optionals, because the pseudonodes ensure, that values for a child are never null.
	 * 
	 * Note that a node also has a color value, which can either be red oder black.
	 */
	private class RBNode {
		
		private TreeElementType key;
		private RBNode leftChild;
		private RBNode rightChild;
		private Color color;
		
		/**
		 * Constructor for all nodes with content in them. Creates a new node and gives them a key, a left
		 * child and a right child. In case, that the node has no child in a classical representation, then
		 * pass the null node as the child.
		 * @param element key that the node stores
		 * @param leftChild left child of this node (nullNode if the node does not have a child)
		 * @param rightChild right child of this node (nullNode if the node does not have a child)
		 */
		public RBNode(final TreeElementType element, final RBNode leftChild, final RBNode rightChild) {
			key = element;
			this.leftChild = leftChild;
			this.rightChild = rightChild;
			color = Color.BLACK;
		}
		
		/**
		 * Standard constructor that should only be used to generate the pseudonodes
		 */
		public RBNode() {}
		
		/**
		 * Get the color of this node. Is either black or red.
		 */
		public Color getColor() {
			return color; 
		}
		
		/**
		 * Set the color of this node. Note that this method does not check the trees coloring properties.
		 * This has to be done by the caller.
		 * @param newColor newColor of the node (can be the same as the old color)
		 * @throws IllegalArgumentException thrown if newColor is null
		 */
		public void setColor(final Color newColor) {
			if (newColor == null) {
				throw new IllegalArgumentException("newColor must not be null");
			}
			color = newColor;
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
		 * Get the left child of this node.
		 */
		public RBNode getLeftChild() {
			return leftChild;
		}
		
		/**
		 * Get the right child of this node.
		 */
		public RBNode getRightChild() {
			return rightChild;
		}
		
		/**
		 * Set the left child of this node.
		 * @throws IllegalArgumentException thrown if the newLeftChild argument is null
		 */
		public void setLeftChild(final RBNode newLeftChild) {
			if (newLeftChild == null) {
				throw new IllegalArgumentException("The new left child should not be null. Do you mean the null node?");
			}
			leftChild = newLeftChild;
		}
		
		/**
		 * Set the right child of this node.
		 * @throws IllegalArgumentException thrown if the newRightChild argument is null
		 */
		public void setRightChild(final RBNode newRightChild) {
			if (newRightChild == null) {
				throw new IllegalArgumentException("The new right child should not be null. Do you mean the null node?");
			}
			rightChild = newRightChild;
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

	/**
	 * Insert a newKey into the RB tree (create a new RBNode for it). All 4-nodes (in the 2-3-4-tree representation)
	 * will get split while traversing to the leafs of the tree.
	 * @param newKey key that get inserted
	 * @throws IllegalArgumentException thrown if the argument newKey is null
	 * @throws IllegalStateException thrown if the key already exists in the tree
	 */
	public void insert(TreeElementType newKey) {
		if (newKey == null) {
			throw new IllegalArgumentException("newKey must not be null!");
		}
		
		RBNode node, greatGrandParent, grandParent, parent;
		
		node = head;
		parent = head;
		grandParent = head;
		greatGrandParent = head;
		while (!node.equals(nullNode)) {
			greatGrandParent = grandParent;
			grandParent = parent;
			parent = node;
			int comparisonValue = node.compareKeyTo(newKey);
			if (comparisonValue == 0) {
				throw new IllegalStateException("Key is already in the RB tree.");
			}else {
				if (comparisonValue > 0) {
					node = node.getLeftChild();
				}else {
					node = node.getRightChild();
				}
			}
			
			if (node.getLeftChild().getColor()==Color.RED && node.getRightChild().getColor()==Color.RED) {
				split(newKey,node,parent,grandParent,greatGrandParent);
			}
		}
		
		node = new RBNode(newKey,nullNode,nullNode);
		if (parent.compareKeyTo(newKey)>0) {
			parent.setLeftChild(node);
		}else {
			parent.setRightChild(node);
		}
		split(newKey,node,parent,grandParent,greatGrandParent);

	}
	
	/**
	 * Perform a split operation. We distinguish between two cases:
	 * 1) If there is a 4-node connected to a 2-node (in the 2-3-4-tree representation), then we color node red
	 * and both its childs black.
	 * 2) If there is a 4-node connected to a 3-node than we recolor like in case 1), rotate the parent over the
	 * grandParent and recolor the parent black and the grandParent red.
	 */
	private void split(TreeElementType newKey, RBNode node, RBNode parent, RBNode grandParent, RBNode greatGrandParent) {
		assert newKey!=null;
		assert node!=null;
		assert parent!=null;
		assert grandParent!=null;
		assert greatGrandParent!=null;
		
		node.setColor(Color.RED);
		node.getLeftChild().setColor(Color.BLACK);
		node.getRightChild().setColor(Color.BLACK);
		if (parent.getColor()==Color.RED) {
			grandParent.setColor(Color.RED);
			if (grandParent.compareKeyTo(newKey) != parent.compareKeyTo(newKey)) {
				parent = rotate(newKey, grandParent);
			}
			node = rotate(newKey, greatGrandParent);
			node.setColor(Color.BLACK);
		}
		head.getRightChild().setColor(Color.BLACK);
	}
	
	/**
	 * Perform the rotation described in the comment of the split operation and return the parent node, which
	 * is not the new root of this subtree.
	 */
	private RBNode rotate(final TreeElementType newKey, final RBNode node) {
		RBNode child, grandChild;
		child = node.compareKeyTo(newKey) > 0 ? node.getLeftChild() : node.getRightChild();
		if (child.compareKeyTo(newKey) > 0) {
			grandChild = child.getLeftChild();
			child.setLeftChild(grandChild.getRightChild());
			grandChild.setRightChild(child);
		} else {
			grandChild = child.getRightChild();
			child.setRightChild(grandChild.getLeftChild());
			grandChild.setLeftChild(child);
		}
		if (node.compareKeyTo(newKey) > 0) {
			node.setLeftChild(grandChild);
		} else {
			node.setRightChild(grandChild);
		}
		return grandChild;
	}

	/**
	 * Traverses the the whole tree in an inorder fashion and prints every nodes key on the
	 * console.
	 */
	public void inorder() {
		head.getRightChild().inorder();
	}
}