

import java.util.Iterator;
import java.util.Optional;
import java.util.Stack;

/**
 * Class for a binary search tree. By now we can insert new elements into the tree, remove them afterwards or
 * try to find them using an iterative or recursive approach. The implementation is based on pseudo nodes. We use
 * a head node and the first node with real content will be the right child of this head. All nodes, where a child
 * is missing (for example leaf nodes) will be connected to the special nullNode. This eases the implementation
 * of the tree. Note that this is the reason, why we can always securely call get() on all returned Optionals.
 * 
 * @author Fabian Schmierer
 *
 * @param <TreeElementType> Type of the elements stored in this trees node. The elements need to be Comparable
 * like Integers oder Strings
 */
public class BinarySearchTree<TreeElementType extends Comparable<TreeElementType>>
		implements Iterable<TreeElementType> {

	//Pseudonodes
	private TreeNode<TreeElementType> head;
	private TreeNode<TreeElementType> nullNode;

	private IterationOrder iterationOrder;
	
	/**
	 * Create a new binary tree. The pseudonodes head and nullNode are created and get connected. Left and
	 * right child of the head is now the nullNode.
	 */
	public BinarySearchTree() {
		head = new TreeNode<TreeElementType>();
		nullNode = new TreeNode<TreeElementType>();
		nullNode.setLeftChild(nullNode);
		nullNode.setRightChild(nullNode);
		head.setRightChild(nullNode);
		iterationOrder = iterationOrder.PREORDER;
	}
	
	/**
	 * Try to find the given key in the tree by using an iterative strategy and return an Optional of the
	 * node it is contained in. The given key must not be null.
	 * @param keyToFind key we want to search for in the tree
	 * @return Return an Optional with the node that the given key is contained in. In case that the key is
	 * not present in the tree, an empty Optional will be returned
	 * @throws IllegalArgumentException thrown if keyToFind is null
	 */
	public Optional<TreeNode<TreeElementType>> iterativeFindNode(final TreeElementType keyToFind) {
		if (keyToFind == null) {
			throw new IllegalArgumentException("keyToFind must not be null!");
		}
		
		TreeNode<TreeElementType> node = head.getRightChild().get(); 
		while (node != nullNode) {
			int comparisonValue = node.compareKeyTo(keyToFind);
			if (comparisonValue == 0)
				return Optional.of(node);
			else
				if (comparisonValue > 0) {
					node = node.getLeftChild().get();
				}else {
					node = node.getRightChild().get();
				}
		}
		return Optional.empty();
	}
	
	/**
	 * Try to find the given key in the tree by using an recursive strategy and return an Optional of the
	 * node it is contained in. The given key must not be null.
	 * @param keyToFind key we want to search for in the tree
	 * @return Return an Optional with the node that the given key is contained in. In case that the key is
	 * not present in the tree, an empty Optional will be returned
	 * @throws IllegalArgumentException thrown if keyToFind is null
	 */
	public Optional<TreeNode<TreeElementType>> recursiveFindNode(TreeElementType keyToFind) {
		if (keyToFind == null) {
			throw new IllegalArgumentException("keyToFind must not be null!");
		}
		
		if (head.getRightChild().get()==nullNode) {
			return Optional.empty();
		}
		return recursiveFindNode(head.getRightChild().get(), keyToFind);
	}
	
	private Optional<TreeNode<TreeElementType>> recursiveFindNode(TreeNode<TreeElementType> currentSubtreeRoot,TreeElementType keyToFind) {
		assert currentSubtreeRoot != null;
		assert keyToFind != null;
		TreeNode<TreeElementType> currentNode = currentSubtreeRoot; 
		if (currentNode != nullNode) {
			int comparisonValue = currentNode.compareKeyTo(keyToFind);
			if (comparisonValue == 0) {
				return Optional.of(currentNode);
			} else if (comparisonValue > 0) {
				return recursiveFindNode(currentNode.getLeftChild().get(), keyToFind);
			} else {
				return recursiveFindNode(currentNode.getRightChild().get(), keyToFind);
			}
		} else {
			return Optional.empty();
		}
	}
	
	/**
	 * Try to find the smallest element stored in the tree (based on the order the type TreeElementType implies).
	 * This is done by taking the left-most-path until we reach a leaf node.
	 * 
	 * @return The smallest element stored in the tree.
	 * @throws IllegalStateException thrown if the tree is empty. In this case a minimal element cannot exist
	 */
	public TreeElementType findMinElement() {
		if (head.getRightChild().equals(nullNode)) {
			throw new IllegalStateException(
					"The tree is empty. Therefore we cannot find the element with smallest key.");
		}
		TreeNode<TreeElementType> currentNodeInTraversal = head.getRightChild().get();
		while (!currentNodeInTraversal.getLeftChild().get().equals(nullNode)) {
			currentNodeInTraversal = currentNodeInTraversal.getLeftChild().get();
		}
		return currentNodeInTraversal.getKey().get();
	}

	/**
	 * Try to find the biggest element stored in the tree (based on the order the type TreeElementType implies).
	 * This is done by taking the right-most-path until we reach a leaf node.
	 * 
	 * @return The biggest element stored in the tree.
	 * @throws IllegalStateException thrown if the tree is empty. In this case a maximum element cannot exist
	 */
	public TreeElementType findMaxElement() {
		if (head.getRightChild().equals(nullNode)) {
			throw new IllegalStateException(
					"The tree is empty. Therefore we cannot find the element with smallest key.");
		}
		TreeNode<TreeElementType> currentNodeInTraversal = head.getRightChild().get();
		while (!currentNodeInTraversal.getLeftChild().get().equals(nullNode)) {
			currentNodeInTraversal = currentNodeInTraversal.getLeftChild().get();
		}
		return currentNodeInTraversal.getKey().get();
	}

	
	/**
	 * Insert a new value to the tree. This is done by traversing to the current leaf node, where the node
	 * needs to be linked to and then perform the linking process.
	 * @param keyToInsert key that should be added to the tree
	 * @throws IllegalArgumentException the key we want to insert must not be null
	 * @throws IllegalStateExceptin thrown if the key was already inserted into the tree
	 */
	public void insert(TreeElementType keyToInsert) {
		if (keyToInsert == null) {
			throw new IllegalArgumentException("Key to be inserted should not be null.");
		}
		
		TreeNode<TreeElementType> parentNode = head;
		TreeNode<TreeElementType> childNode = head.getRightChild().get();

		while (!childNode.equals(nullNode)) {
			parentNode = childNode;
			int comparisonValue = childNode.compareKeyTo(keyToInsert);
			if (comparisonValue == 0) {
				throw new IllegalStateException("The key already existed in the binary search tree.");
			} else if (childNode.compareKeyTo(keyToInsert) > 0) {
				childNode = childNode.getLeftChild().get();
			} else {
				childNode = childNode.getRightChild().get();
			}
		}

		TreeNode<TreeElementType> nodeToInsert = new TreeNode<TreeElementType>(keyToInsert);
		nodeToInsert.setLeftChild(nullNode);
		nodeToInsert.setRightChild(nullNode);

		if (parentNode.compareKeyTo(keyToInsert) > 0) {
			parentNode.setLeftChild(nodeToInsert);
		} else {
			parentNode.setRightChild(nodeToInsert);
		}
	}

	/**
	 * The key, given as argument, will be removed from the tree. If the node is not a leaf node,
	 * it will be replaced by its inorder successor.
	 * @param keyToRemove Key that gets removed from the tree
	 * @throws IllegalArgumentException thrown if the given arguemnt is null
	 * @throws IllegalStateException thrown if argument key is not stored in this tree.
	 */
	public void removeNode(TreeElementType keyToRemove) {
		if (keyToRemove == null) {
			throw new IllegalArgumentException("The argument key must not be null!");
		}
		
		TreeNode<TreeElementType> parent = head;
		TreeNode<TreeElementType> node = head.getRightChild().get();
		TreeNode<TreeElementType> child = null;
		TreeNode<TreeElementType> tmp = null;

		while (!node.equals(nullNode)) {
			int comparisonValue = node.compareKeyTo(keyToRemove);
			if (comparisonValue == 0) {
				break;
			} else {
				parent = node;
				if (comparisonValue > 0) {
					node = node.getLeftChild().get();
				}else {
					node = node.getRightChild().get();
				}
			}
		}
		if (node.equals(nullNode)) {
			throw new IllegalStateException("The given key was not in the the search tree.");
		}
		
		if (node.getLeftChild().get().equals(nullNode) && node.getRightChild().get().equals(nullNode)) // Fall 2.1
			child = nullNode;
		else if (node.getRightChild().get().equals(nullNode)) 
			child = node.getLeftChild().get();
		else if (node.getLeftChild().get().equals(nullNode)) 
			child = node.getRightChild().get();
		else { 
			child = node.getRightChild().get();
			tmp = node;
			while (!child.getLeftChild().get().equals(nullNode)) {
				tmp = child;
				child = child.getLeftChild().get();
			}
			child.setLeftChild(node.getLeftChild().get());
			if (!tmp.equals(node)) {
				tmp.setLeftChild(child.getRightChild().get());
				child.setRightChild(node.getRightChild().get());
			}
		}
		
		if (parent.getLeftChild().get().equals(node)) {
			parent.setLeftChild(child);
		}else {
			parent.setRightChild(child);
		}	
	}
	
	
	/**
	 * Traverse the tree in preorder style and print every node, visited while traversing the tree, on
	 * the console.
	 */
	public void printPreorder() {
		if (head.getRightChild().isEmpty()) {
			System.out.println("There is nothing to see here in this tree.");
		}

		Stack<TreeNode<TreeElementType>> stack = new Stack<TreeNode<TreeElementType>>();
		stack.push(head.getRightChild().get());
		while (!stack.isEmpty()) {
			TreeNode<TreeElementType> currentNode = stack.pop();
			if (!currentNode.equals(nullNode)) {
				stack.push(currentNode.getRightChild().get());
				stack.push(currentNode.getLeftChild().get());
				System.out.println(currentNode.getKey().get());
			}
		}
	}
	
	/**
	 * Setter for the current iteration order. Should be called before retrieving the iterator with the
	 * iterator() operation.
	 */
	public void setIterationOrder (final IterationOrder newIterationType) {
		if (newIterationType == null) {
			throw new IllegalArgumentException("newIterationType should not be null!");
		}
		iterationOrder = newIterationType;
	}
	
	/**
	 * Returns an iterator object based on the selected iterationOrder. By now, only the PreorderIterator
	 * is implemented. The rest will be done by you in the exercises.
	 */
	@Override
	public Iterator<TreeElementType> iterator() {
		switch (iterationOrder) {
			case PREORDER:
				return new PreorderIterator<TreeElementType>();
			case INORDER:
				throw new UnsupportedOperationException("IterationType has to be implemented first.");
		case LEVELORDER:
				throw new UnsupportedOperationException("IterationType has to be implemented first.");
			case POSTORDER:
				throw new UnsupportedOperationException("IterationType has to be implemented first.");
			default:
				break;
		}
		return new PreorderIterator<TreeElementType>();
	}
	
	/**
	 * Implementation of a preorder iterator using a stack to simulate recursion. Traverse the tree 
	 * using the hasNext() and next() operations.
	 */
	private class PreorderIterator<TreeElementType extends Comparable<TreeElementType>>
			implements Iterator<TreeElementType> {

		Stack<TreeNode<TreeElementType>> stack = new Stack<TreeNode<TreeElementType>>();

		public PreorderIterator() {
			if (!head.getRightChild().get().equals(nullNode)) {
				stack.push((TreeNode<TreeElementType>) head.getRightChild().get());
			}
		}

		@Override
		public boolean hasNext() {
			return !stack.isEmpty();
		}

		@Override
		public TreeElementType next() {
			TreeNode<TreeElementType> node = stack.pop();
			TreeElementType nodeElement = node.getKey().get();
			if (!node.getRightChild().get().equals(nullNode)) {
				stack.push(node.getRightChild().get());
			}
			if (!node.getLeftChild().get().equals(nullNode)) {
				stack.push(node.getLeftChild().get());
			}
			return nodeElement;
		}

	}
	
	
	
	
	
}

	
