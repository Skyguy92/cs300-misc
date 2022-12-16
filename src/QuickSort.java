/**
 * Quicksort algorithm holder
 */
public class QuickSort {
    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            Integer[] arr1 = new Integer[10];

            for (int j = 0; j < arr1.length; j++) {
                arr1[j] = (int) (Math.random() * 100);
            }

            quickSort(arr1, 0, arr1.length - 1);

            if (!validateSortedAscending(arr1)) {
                System.err.println("Error");
            };

//            System.out.println(Arrays.toString(arr1));
        }

        System.out.println("Tested a 10 element array 1000 times, if no error message, it's good");
    }


    /**
     * Validates that the array is sorted in ascending order.
     *
     * @param arr The array to validate.
     * @return True if the array is sorted in ascending order, false otherwise.
     * @param <T> must be comparable
     */
    public static <T extends Comparable<T>> boolean validateSortedAscending(T[] arr) {
        int i = 0; // leading index
        int j = 0; // trailing index

        while (i < arr.length - 1) {
            i++;

            // if this is negative it means that a further index must come before this one!
            if (arr[i].compareTo(arr[j]) < 0) {
                return false;
            }

            j++;
        }

        return true;
    }

    /**
     * Sorts the array using the quicksort algorithm.
     *
     * @param arr The array to sort.
     * @param left The left index to start sorting from.
     * @param right The right index to stop sorting at.
     * @param <T> must be comparable
     */
    public static <T extends Comparable<T>> void quickSort(T[] arr, int left, int right) {
        if ( left < right ) {
            int mid  = partition(arr, left, right);
            quickSort(arr, left, mid - 1);
            quickSort(arr, mid + 1, right);
        }
    }

    /**
     * Partitions the array into two parts, one side is less than the pivot, the other side is greater than the pivot.
     *
     * @param arr The array to partition.
     * @param left The left index to start partitioning from.
     * @param right The right index to stop partitioning at.
     * @return The index of the pivot.
     * @param <T> must be comparable
     */
    private static <T extends Comparable<T>> int partition(T[] arr, int left, int right) {
        int i = left; // the last swapped index
        T pivot = arr[right]; // the pivot value

        // loop through the array from left to right
        for (int j = left; j < right; j++) {

            // j is the current iterating index, when a conflict is found, such as
            // its value is less than the pivot, swap it with the current swap index
            // and increment the swap index
            if (arr[j].compareTo(pivot) < 0) {
                swap(arr, i, j);
                i++;
            }
        }

        swap(arr, i, right);
        return i;
    }

    /**
     * Swaps two elements in the array.
     *
     * @param arr The array to swap elements in.
     * @param i The index of the first element to swap.
     * @param j The index of the second element to swap.
     * @param <T> any type
     */
    private static <T> void swap(T[] arr, int i, int j) {
        T temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}