public class TrieApp {

	public static void main(String[] args) {
		//Construct a trie and insert some words into it (In this case: Words from Slide 46, Slideset 08)
		Trie trie = new Trie();
		trie.insert("database");
		trie.insert("datum");
		trie.insert("datenbankmodell");
		trie.insert("datenbanksprache");
		trie.insert("datenbanksystem");
		
		System.out.println(trie.find("datum"));
		System.out.println(trie.find("data"));
		System.out.println(trie.find("datenbanksystem"));
		
	}

}
