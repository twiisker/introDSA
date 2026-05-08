import java.util.ArrayList;
import java.util.List;

public class HeapSortApp {

	public static void main(String[] args) {
		//Create a list of Integers and add some arbitrary numbers to it
		List<Integer> list = new ArrayList<Integer>();
		list.add(3);
		list.add(2);
		list.add(5);
		list.add(4);
		list.add(7);
		list.add(4);
		list.add(-1);
		
		/*Create a new sorter object and pass the list we want to sort: At construction time the heap sort
		algorithm will be called on this list */
		Sorter<Integer> sorter = new Sorter<Integer>(list);
		
		//Get the result and print it on the console
		list = sorter.getResult();
		for (int i : list) {
			System.out.print(i+"|");
		}

	}

}
