
public class RBTreeApp {

	public static void main(String[] args) {
		//Create a RB-Tree and insert some values into it.
		RBTree<Integer> tree = new RBTree();
		tree.insert(5);
		tree.insert(2);
		tree.insert(3);
		tree.insert(4);
		tree.insert(7);
		tree.insert(9);
		tree.inorder();
	}

}
