

import java.util.Iterator;
import java.util.Optional;

public class BinarySearchTreeApp {

	public static void main(String[] args) {
		//Instantiation of binary trees with different Types (Types must extend Comparable)
		BinarySearchTree<Integer> intTree = new BinarySearchTree<>();
		BinarySearchTree<Double> doubleTree = new BinarySearchTree<>();
		BinarySearchTree<String> stringTree = new BinarySearchTree<>();
		
		//apply some operations to the trees:
		//1) Insert elements
		intTree.insert(6);
		intTree.insert(2);
		intTree.insert(8);
		intTree.insert(7);
		intTree.insert(9);
		intTree.insert(4);
		intTree.insert(3);
		intTree.insert(1);
		
		//2) Search for a given Element (recursively or iteratively)
		Optional<TreeNode<Integer>> foundElement = intTree.iterativeFindNode(3);
		if (foundElement.isEmpty()) {
			System.out.println("Element 3 is not in the tree.");
		}else {
			System.out.println("Element found in node "+foundElement.get());
		}
		
		//3) Remove a node from the tree if the node exists in it
		if (!intTree.recursiveFindNode(2).isEmpty()) {
			intTree.removeNode(2);
		}
		
		//4) See if the removed node can still be found in the tree
		foundElement = intTree.iterativeFindNode(2);
		if (foundElement.isEmpty()) {
			System.out.println("Element 2 is not in the tree.");
		}else {
			System.out.println("Element found in node "+foundElement.get());
		}
		
		//5) Use the iterator provided by the binary tree. By now only Preorder iteration is implemented
		intTree.setIterationOrder(IterationOrder.PREORDER);
		Iterator<Integer> binaryTreeIterator = intTree.iterator();
		
		while(binaryTreeIterator.hasNext()) {
			System.out.print(binaryTreeIterator.next()+"|");
		}
		
	}

}
