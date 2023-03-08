import java.util.Arrays;
import java.util.Scanner;

public class Sorting {
    public static int[] insertionSort(int[] A, int n) {
        for (int i=1; i<n; i++) {
            int key = A[i];
            int j = i-1;
            while (j>=0 && A[j]>key) {
                A[j+1] = A[j];
                j--;
            }
            A[j+1] = key;
        }
        return A;
    }

    public static int[] bubbleSort(int[] A, int n) {
        for (int i=0; i<n; i++) {
            for (int j=0; j<n-1; j++) {
                if (A[j] > A[j+1]) {
                    int temp = A[j+1];
                    A[j+1] = A[j];
                    A[j] = temp;
                }
            }
        }
        return A;
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter the size of array: ");
        int arraySize = scan.nextInt();
        int[] array = new int[arraySize];

        int min = 0;
        int max = 9;
        for (int i=0; i<array.length; i++) {
            int numberToAdd = (int)(Math.random() * (max-min+1) + min);
            array[i] = numberToAdd;
        }

        System.out.println("Before sorting: " + Arrays.toString(array));
        insertionSort(array, arraySize);
        System.out.println("After sorting: " + Arrays.toString(array));

        System.out.println("Before sorting: " + Arrays.toString(array));
        bubbleSort(array, arraySize);
        System.out.println("After sorting: " + Arrays.toString(array));
    }
}