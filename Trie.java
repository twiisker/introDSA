import java.util.Optional;

/**
 * @author Fabian Schmierer
 */
public class Trie {
	
	private class TrieNode {
		private TrieNode[] children = new TrieNode['z'-'a'+1];
		private boolean isLeaf = false;
	
		/**
		 * Tries to find a given word in this node. If it cannot be found here, we proceed with our search
		 * in the child node (only one based on the letter we are currently at)
		 * @return returns true if the word was found in this node or any child node, false otherwise
		 */
		private boolean find(final String word) {
			if (word.length() == 0 && isLeaf) {
				return true; 
			} else if (word.length() == 0) {
				return false; 
			}
			char currentChar = word.charAt(0);
			int position = currentChar - 'a';
			if (children[position] != null) {
				return children[position].find(word.substring(1));
			}
			return false;
		}

		/**
		 * Insert a new wort into the trie.
		 * @param word word we want to insert
		 * @return returns a new root node if one was created wrapped inside an optional. If no new root was
		 * created an empty optional gets returned.
		 */
		private Optional<TrieNode> insert(final String word) {
			if (word.length() == 0) {
				TrieNode newTrieNode = new TrieNode(this);
				return Optional.of(newTrieNode); 
			}
			char current = word.charAt(0);
			int position = current - 'a';
			
			if (children[position] == null) {
				children[position] = new TrieNode();
			}
			Optional<TrieNode> result = children[position].insert(word.substring(1));
			if (!result.isEmpty()) {
				children[position] = result.get();
			}
			return Optional.empty(); 
		}

		/**
		 * Constructor for a non-leaf node.
		 */
		public TrieNode() {
			this.isLeaf = false;
		}
		
		/**
		 * Constructor for a leaf node.
		 */
		public TrieNode(TrieNode node) {
			this.children = node.children;
			this.isLeaf = true;
		}
	}
	
	/**private class LeafNode extends TrieNode {
		public LeafNode(TrieNode node) {
			this.children = node.children;
		}
	}**/
	
	private TrieNode root = new TrieNode();
	
	/**
	 * Insert a new word into the trie.
	 * @param word word we want to insert
	 */
	public void insert(final String word) {
		checkPreconditions(word);
		Optional<TrieNode> result = root.insert(word.toLowerCase());
		if (!result.isEmpty()) {
			root = result.get();
		}
	}
	
	/**
	 * Try to find a word in the whole trie.
	 */
	public boolean find(final String word) {
		checkPreconditions(word);
		return root.find(word);
	}
	
	/**
	 * Check that the word is not null and that only lowercase letters are used.
	 * @param word argument on which we check the preconditions
	 * @throws IllegalArgumentExceptions thrown if one of the preconditions is violated
	 */
	private void checkPreconditions(final String word) {
		if (word == null) {
			throw new IllegalArgumentException("word must not be null!");
		}
		for (int i=0;i<word.length();i++) {
			if (Character.isUpperCase(word.charAt(i))) {
				throw new IllegalArgumentException("Word must not contain uppercase letters!");
			}
		}
		if (!word.matches("[a-z]*")) {
			throw new IllegalArgumentException("Word does not match the regular expression [a-z]*!");
		}
	}
}
