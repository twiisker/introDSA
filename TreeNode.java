

import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;

/**
 * Simple class to store tree nodes. A tree nodes stores a key, and optionally a left and right child.
 * A node always needs to have a key.
 * @author Fabian Schmierer
 */
public final class TreeNode<TreeElementType extends Comparable<TreeElementType>> {
	
	private final Optional<TreeElementType> key;
	private Optional<TreeNode<TreeElementType>> leftChild = Optional.empty();
	private Optional<TreeNode<TreeElementType>> rightChild = Optional.empty();
	private Queue<TreeNode<TreeElementType>> queue = new ArrayDeque<TreeNode<TreeElementType>>();
	
	/**
	 * Creates a new tree node without a left and a right child.
	 * @param key Content of the tree node on which comparisons with other tree nodes are made.
	 */
	public TreeNode(final TreeElementType key) {
		if (key == null) {
			throw new IllegalArgumentException("Argument key is null");
		}
		if (key==null) {
			this.key = Optional.empty();
		}else {
			this.key = Optional.of(key);
		}
			
		queue.add(this);
	}
	
	/**
	 * Constructor to create a pseudo nodes in binary trees. Introduces more coupling between the classes
	 * but is necessary for the implementation.
	 */
	public TreeNode() {
		this.key = Optional.empty();
	}
	
	/** 
	 * Returns an Optional with a reference to the stored left child or an empty Optional if no such element
	 * exists.
	 * @return Left child of this node wrapped in an Optional
	 */
	public Optional<TreeNode<TreeElementType>> getLeftChild() {
		return leftChild;
	}
	
	/** 
	 * Returns an Optional with a reference to the stored right child or an empty Optional if no such element
	 * exists.
	 * @return Right child of this node wrapped in an Optional
	 */
	public Optional<TreeNode<TreeElementType>> getRightChild() {
		return rightChild;
	}
	
	/**
	 * Returns the key stored by this node. A key always needs to exist.
	 * @return Key stored by the node
	 */
	public Optional<TreeElementType> getKey() {
		return key;
	}
	
	/**
	 * Set the left child of this node. 
	 * @param newLeftChild new left child of this node.
	 * @throws IllegalArgumentException if argument newLeftChild is null
	 */
	public void setLeftChild(final TreeNode<TreeElementType> newLeftChild) {
		if (newLeftChild == null) {
			throw new IllegalArgumentException("Argument newLeftChild is null");
		}
		leftChild = Optional.of(newLeftChild);
	}
	
	/**
	 * Set the right child of this node. 
	 * @param newRightChild new right child of this node.
	 * @throws IllegalArgumentException if argument newRightChild is null
	 */
	public void setRightChild(final TreeNode<TreeElementType> newRightChild) {
		if (newRightChild == null) {
			throw new IllegalArgumentException("Argument newRightChild is null");
		}
		rightChild = Optional.of(newRightChild);
	}
	
	/**
	 * Does a recursive inorder traversal of this nodes subtree and prints all nodes in that
	 * order to the console.
	 */
	public void traverseInorder() {
		if (!leftChild.isEmpty()) {
			leftChild.get().traverseInorder();
		}
		System.out.print(this.toString() +" ");
		if (!rightChild.isEmpty()) {
			rightChild.get().traverseInorder();
		}
	}

	
	/**
	 * Does a recursive preorder traversal of this nodes subtree and prints all nodes in that
	 * order to the console.
	 */
	public void traversePreorder() {
		System.out.print(this.toString() +" ");
		if (!leftChild.isEmpty()) {
			leftChild.get().traversePreorder();
		}
		if (!rightChild.isEmpty()) {
			rightChild.get().traversePreorder();
		}
	}
	
	/**
	 * Does a recursive postorder traversal of this nodes subtree and prints all nodes in that
	 * order to the console.
	 */
	public void traversePostorder() {
		if (!leftChild.isEmpty()) {
			leftChild.get().traversePostorder();
		}
		if (!rightChild.isEmpty()) {
			rightChild.get().traversePostorder();
		}
		System.out.print(this.toString() +" ");
	}

	/**
	 * Does a recursive levelorder traversal of this nodes subtree and prints all nodes in that
	 * order to the console.
	 */
	public void traverseLevelorder() {
		while(!queue.isEmpty()) {
			TreeNode<TreeElementType> currentNode = queue.poll();
			if (!currentNode.getLeftChild().isEmpty()) {
				queue.offer(currentNode.getLeftChild().get());
			}
			if (!currentNode.getRightChild().isEmpty()) {
				queue.offer(currentNode.getRightChild().get());
			}
			System.out.print(currentNode.toString() +" ");
		}
	}
	
	/**
	 * Returns a textual representation of this node, which is in this case the textual
	 * representation of the key.
	 */
	@Override
	public String toString() {
		if (key.isEmpty()) {
			return "NONE";
		}
		return key.get().toString();
	}
	
	/**
	 * Compare the key of this node with the given key.
	 * @param otherNodeKey key of another mode
	 * @return -1 if this node has no key or this key is smaller, 0 if they have the same value and 1 if this key is bigger.
	 */
	public int compareKeyTo(TreeElementType otherNodeKey) {
		if (key.isEmpty()) {
			return -1;
		}
		return key.get().compareTo(otherNodeKey);
	}
}
