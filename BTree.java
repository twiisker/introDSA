import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the B-Tree from the lecture. Note that this implementation needs to be extended in order
 * to be usable (there is no way of getting data into the B-Tree by now).
 * @author Fabian Schmierer
 *
 * @param <KeyType> Type of the elements that are stored in the B-Tree. They need to be comparable to each other.
 */
public class BTree<KeyType extends Comparable<KeyType>> {
	
	/**
	 * Inner node class for the B-Tree. The number of elements in a node is determined by the order
	 * of the tree. Note that this class does not check integrity for this cases: So, a malicious caller
	 * could also construct nodes with too many elements in it. Integrity checks like these, need to be
	 * done on the caller side.
	 */
	private class BTreeNode {
		private final List<Optional<KeyType>> keys;
		private final List<Optional<BTreeNode>> children;
		private boolean isLeaf;
		
		private BTreeNode() {
			keys = new ArrayList<>(2*order);
			children = new ArrayList<>(2*order+1);
		}
		
		private boolean isLeaf() {
			return isLeaf;
		}
		
		private void setIsLeaf(final boolean newLeafStatus) {
			isLeaf = newLeafStatus;
		}
		
		/**
		 * Try to find a given key in this node. If this node does not contain the given key, then we 
		 * recursively search in the child nodes of this node.
		 * @param searchKey key to search in this nodes subtree
		 * @return true if the key is contained in this node or a child node, false otherwise
		 */
		private boolean find(final KeyType searchKey) {
			for (int i=0;i<children.size();i++) {
				if (i==keys.size() || keys.get(i).isEmpty()) {
					return children.get(i).get().find(searchKey);
				}
				int comparisonValue = keys.get(i).get().compareTo(searchKey);
				if (comparisonValue == 0) {
					return true;
				}else if (comparisonValue > 0 && !children.get(i).isEmpty()) {
					return children.get(i).get().find(searchKey);
				}else if (comparisonValue > 0 && isLeaf()) {
					return false;
				}
			}
			return false;
		}
		
	}
	
	private final int order;
	private final BTreeNode root;
	
	/**
	 * Constructs a new B-Tree without any content. Creates a root node with no elements in it.
	 * @param order order of the B-Tree. It should hold for each node (except for the root):
	 * m <= #elements in node <= 2*m 
	 */
	public BTree(final int order) {
		if (order <= 0) {
			throw new IllegalArgumentException("Order needs to be larger than 0.");
		}
		this.order = order;
		root = new BTreeNode();
	}
	
	/**
	 * Try to find a given key in this B-Tree. The search is performed recursively starting with the root node.
	 * @param searchKey key to search in this B-Tree
	 * @return true if the key is contained in this node or a child node, false otherwise
	 */
	private boolean containsKey(final KeyType searchKey) {
		return root.find(searchKey);
	}
}
