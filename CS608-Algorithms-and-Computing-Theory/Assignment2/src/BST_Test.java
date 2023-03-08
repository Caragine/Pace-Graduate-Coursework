/************************************************************************
 *
 *  Pace University
 *  Fall 2020
 *  Algorithms and Computing Theory
 *
 *  Course: CS 608
 *  Team members: Rodrigo Martins, Adam Caragine
 *  Other collaborators: None
 *  References:
 *
 *
 *  Assignment: #2
 *  Problem: Binary Search Tree (BST)
 *  Description: In this problem we have to measure and compare the running time of a skewed BST and a balanced BST by adding values from 1 to x (skewed) and random numbers without repetition (balanced).
 *
 *  Input: x -> The number of nodes in the tree
 *  Output:
 *      - boolean value: true, if tree contains the number searched, false otherwise
 *      - running time of searching a number in each type of tree
 *
 *
 *  Visible data fields:
 *  COPY DECLARATION OF VISIBLE DATA FIELDS HERE
 *
 *  Visible methods:
 *  COPY SIGNATURE OF VISIBLE METHODS HERE
 *
 *  public static int getNumberToBeSearched(int x);
 *  public static int getRandomNumber(int min, int max);
 *
 *   Remarks
 *   -------
 *
 *   PUT ALL NON-CODING ANSWERS HERE
 *
 *   1) If the number searched is the root, it will run in constant time, O(1). The same answer if the tree is empty, and in that case we could also use the operation isEmpty().
 *      However, if the number is not the root, in the skewed BST it will run in the worst case in linear time, O(n), because since the numbers are always greater than the root, all numbers will be added to the right side of the root. So, in order to find a number in this type of tree, we would have to use the method contain(number) which would go through all the numbers in the tree to find the number unless the number is smaller than the leaf.
 *      On the other hand, if the tree is balanced (it has, for each node, about the same number of nodes in its left subtree as in its right subtree), it will run in the worst case in log n times, O(log n), since it will compare the number to the root and if the number is smaller it will cut the tree in half and look only in the left side, or if the number is higher it will look only the right side, and it will repeat this operation recursively until it finds (or does not) the number.
 *
 *   2) Running times measured:
 *
 *                      n=10        n=10^2      n=10^3      n=10^4
 *   ---------------------------------------------------------------------------
 *   Skewed BST:        8340000     9413400     8554300     8613500
 *   Balanced BST:      365800      285200      308500      331400
 *
 *   3) For the Skewed BST, we observe in row 1 columns 1 to 3 that when the input size grows by 1 digit the running time doesn't grow a digit but it grows in the running time. Sometimes it grows by a digit when n is 10^4 but not often. So we can say it matches in digits of the running time.
 *   For the Balanced BST, we observe in row 2 that the input size grows by 1 digit and the running time doesn't grow at all. However we can see a small amount of growth in the running time for columns 2 to 4 when we use larger input sizes, and surprisingly the amount of time for n=10 is sometimes greater than the others factors of n as you can see in the row 2, column 1.
 *
 *   4) Running time measured for TreeMap:
 *
 *                  n=10        n=10^2      n=10^3      n=10^4
 *   ----------------------------------------------------------------
 *   TreeMap:       170000      154500      163100      167300
 *
 *   For the TreeMap, it's the same as Balanced BST, even when the input size grows by a digit, the running time is still approximately the same, and surprisingly when we use n=10 the amount of time is greater than the others input sizes. As we can see in the row of TreeMap, in columns 2 to 4 that the amount of growth is insignificant even when we use input size with 3 more digits. Comparing to Skewed Tree, the amount of time one digit less for all n sizes, as we can see the columns 1 to 4 in each of rows, so we can see a huge difference in the result running time but when comparing to Balanced Tree, we can see that the number of digits in running time is the same for all n sizes, yet TreeMap is still faster for all n sizes.
 *
 *************************************************************************/

import java.util.Scanner;
import java.util.TreeMap;

public class BST_Test {

    public static int getNumberToBeSearched(int x) {
        int num;
        if (x <= 10) {
            num = 11;
        } else if (x <= 100) {
            num = 101;
        } else if (x <= 1000) {
            num = 1001;
        } else if (x <= 10000) {
            num = 10001;
        } else if (x <= 100000) {
            num = 100001;
        } else {
            num = 1000001;
        }
        return num;
    }

    public static int getRandomNumber(int min, int max) {
        return (int)(Math.random() * (max-min+1) + min);
    }

    public static void main(String[] args) {
        // Getting input number
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter the number of nodes: ");
        int x = scan.nextInt();

        // Creating skewed BST
        BinarySearchTree<Integer> BST_S = new BinarySearchTree<>();
        for (int i=1; i<=x; i++) {
            BST_S.insert(i);
        }

        // Creating balanced BST and TreeMap with non-repeating random numbers
        BinarySearchTree<Integer> BST_R = new BinarySearchTree<>();
        TreeMap<Integer, Integer> TREE_MAP = new TreeMap<>();
        for (int i=0; i<x; i++) {
            int number = getRandomNumber(1, x);
            boolean containBST = BST_R.contains(number);
            boolean containTM = TREE_MAP.containsKey(number);
            if (!containBST && !containTM) {
                BST_R.insert(number);
                TREE_MAP.put(number, 1);
            } else {
                // If tree contains random number, generate another number that multiplies the previous number by another random number in a range(143,1267) and add the new number to another random number in a range(13,2312268)
                // This way it's almost impossible that we get repeated numbers
                BST_R.insert(number * getRandomNumber(143, 1267) + getRandomNumber(13, 2312268));
                TREE_MAP.put(number * getRandomNumber(143, 1267) + getRandomNumber(13, 2312268), 1);
                // Numbers of the range were chosen randomly without any reference
            }
        }

        // Getting running time of each algorithm
        long startTime = System.nanoTime();
        System.out.println("===========================");
        System.out.println(getNumberToBeSearched(x) + " in BST_S? " + BST_S.contains(getNumberToBeSearched(x)));
        System.out.println("Time = " + (System.nanoTime() - startTime) + " nanosecs");

        int randomNumberToBeSearched = getRandomNumber(1, x);
        startTime = System.nanoTime();
        System.out.println("===========================");
        System.out.println(randomNumberToBeSearched + " in BST_R? " + BST_R.contains(randomNumberToBeSearched));
        System.out.println("Time = " + (System.nanoTime() - startTime) + " nanosecs");

        startTime = System.nanoTime();
        System.out.println("===========================");
        System.out.println(randomNumberToBeSearched + " in TREE_MAP? " + TREE_MAP.containsKey(randomNumberToBeSearched));
        System.out.println("Time = " + (System.nanoTime() - startTime) + " nanosecs");
    }
}
