

public class TreeNodeApp {
	
	public static void main (String[] args) {
		//Create some tree node objects and give the key a value
		TreeNode<Integer> head = new TreeNode<>(4);
		TreeNode<Integer> leftChild = new TreeNode<>(2);
		TreeNode<Integer> rightChild = new TreeNode<>(5);
		TreeNode<Integer> leftLeftChild = new TreeNode<>(1);
		TreeNode<Integer> leftRightChild = new TreeNode<>(3);
		
		//Link all the nodes together into the resulting tree (with no pseudonodes)
		//		4
		//	   / \
		//    2   5
		//   / \  
		//  1   3
		head.setLeftChild(leftChild);
		head.setRightChild(rightChild);
		leftChild.setLeftChild(leftLeftChild);
		leftChild.setRightChild(leftRightChild);
		
		//Get the nodes over the linkings
		System.out.println("Head left: "+head.getLeftChild().get().toString());
		System.out.println("Head right: "+head.getRightChild().get().toString());
		
		//Traverse the whole tree starting at the root
		System.out.print("Inorder: ");
		head.traverseInorder();
		System.out.print("\nPostorder: ");
		head.traversePostorder();
		System.out.print("\nPreorder: ");
		head.traversePreorder();
		System.out.print("\nLevelorder: ");
		head.traverseLevelorder();
	}
	
}
