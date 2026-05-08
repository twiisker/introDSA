
/**
 * Simple class that shows how to work with the AVLTree-Class. Note that the AVLTree-Class is not fully
 * implemented yet. You need to write the code to insert nodes into the left subtree (mirror-inverted to
 * inserting a node into the left subtree). Then you can test your class in here.
 */
public class AVLTreeApp {

	public static void main(String[] args) {
		//Create a new AVL tree and insert elements into it (Example is from slide set 07, slide 29/30
		AVLTree<Integer> tree = new AVLTree<Integer>();
		tree.insert(3);
		tree.insert(2);
		tree.insert(1);
		//Perform rotation: R(2,3)
		
		tree.insert(4);
		tree.insert(5);
		//Perform rotation: L(4,3)
		
		tree.insert(6);
		//Perform rotation: L(4,2)
		
		tree.insert(7);
		//Perform rotation: L(6,5)
		
		tree.insert(16);
		tree.insert(15);
		//Perform rotation: DR(7,15,16)
		
		//Do an inorder traversal and output every nodes' key on the console.
		tree.inorder(); 
		
	}

}
