import java.util.List;

/**
 * Wrapper class for the heap sort algorithm in the lecture. This class is based on the array representation
 * of a heap. Tree terminology is used in comments to better understand the code, however all tree concepts are
 * mapped to an internal array representation.
 * @author Fabian Schmierer
 *
 * @param <ElementType> type of the elements in the list we want to sort. The elements need to be comparable
 * in order to get them into a predefined order
 */
public class Sorter<ElementType extends Comparable<ElementType>> {
	
	private final List<ElementType> list;
	
	/**
	 * Construct a new sorter object and initiate the sorting process
	 * @param list list we want to sort
	 */
	public Sorter (final List<ElementType> list) {
		if (list == null) {
			throw new IllegalArgumentException("Please give this class a valid list to sort");
		}
		this.list = list;
		heapSort();
	}
	
	/**
	 * Returns the sorted list back to the client class.
	 * @return sorted list
	 */
	public List<ElementType> getResult() {
		return list;
	}
	
	/**
	 * Sort the given list using heap sort. Traverse backwards from the middle of the list to the beginning and
	 * let all elements percolate into the heap. This will establish the heap property. 
	 * Then we iterate over all heap elements (starting at the end of the list), swap the current element
	 * with the root and let the current element percolate into the heap again.
	 */
	private void heapSort() {
		for (int i=list.size()/2;i>=0;i--) {
			percolate(i,list.size()-1);
		}
		
		for (int i=list.size()-1;i>0;i--) {
			swap(0,i);
			percolate(0,i-1);
		}
	}
	
	/**
	 * Let the element at position currentElementPosition sink into the heap. The element gets swapped with
	 * the biggest child as long as there are child nodes, which are bigger than the sinking element. If there
	 * is no such child node, the element is at the correct position in the heap.
	 * 
	 * @param currentElementPosition position of the element that should sink into the heap
	 * @param percolationBorder position the element should not exceed after percolation
	 */
	private void percolate(final int currentElementPosition, final int percolationBorder) {
		if (currentElementPosition < 0 || percolationBorder < 0 || currentElementPosition > percolationBorder) {
			throw new IllegalArgumentException("Invalid input parameters for percolation.");
		}
		
		int parentPosition = currentElementPosition;
		int childPosition;
		
		while((2*parentPosition)+1 <= percolationBorder) {
			childPosition = (2*parentPosition)+1;
			if (childPosition+1 <= percolationBorder) {
				if (list.get(childPosition).compareTo(list.get(childPosition+1)) > 0) {
					childPosition++;
				}
			}
			
			if (list.get(parentPosition).compareTo(list.get(childPosition)) > 0) {
				swap(parentPosition,childPosition);
				parentPosition = childPosition;
			}else {
				break;
			}
		}
	}
	
	/**
	 * Swaps the two elements at positions index1 and index2 in the global list "list". This is just
	 * a helper method for the heap sort algorithm.
	 * 
	 * @param index1 index of the first element in the list we want to swap
	 * @param index2 index of the second element in the list we want to swap
	 */
	private void swap(final int index1, final int index2) {
		ElementType buffer = list.get(index1);
		list.set(index1, list.get(index2));
		list.set(index2, buffer);
	}
}
