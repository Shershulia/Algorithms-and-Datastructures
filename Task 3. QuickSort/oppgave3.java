import java.util.Arrays;

import java.util.Random;

public class oppgave3 {
    public static void main(String[] args) {
        test1();
        test2();
        test3();
        test4();
        test5();
        test6();

    }

    /**
     * Quick sort algorithm with one pivot element
     * @param t array
     * @param v first index
     * @param h last index
     */
    public static void quicksort(int []t, int v, int h) {
        if (h - v > 2) {
            int delepos = splitt(t, v, h);
            quicksort(t, v, delepos - 1);
            quicksort(t, delepos + 1, h);
        } else median3sort(t, v, h);
    }

    /**
     * algorithm for sorting 3 values (first,middle,last)
     * @param t array
     * @param v first index
     * @param h last index
     * @return index of median
     */
    private static int median3sort(int []t, int v, int h) {
        int m = (v + h) / 2;
        if (t[v] > t[m]) swap(t, v, m);
        if (t[m] > t[h]) {
            swap(t, m, h);
            if (t[v] > t[m]) swap(t, v, m);
        }
        return m;
    }

    /**
     * Method splitting for quick sort with one pivot-element in which elements less than median placed before it
     * and those greater of it placed after it
     * @param t array
     * @param v first index
     * @param h last index
     * @return splitting index
     */
    private static int splitt(int []t, int v, int h) {
        int iv, ih;
        int m = median3sort(t, v, h);
        int dv = t[m];
        swap(t, m, h - 1);
        for (iv = v, ih = h - 1;;) {
            while (t[++iv] < dv) ;
            while (t[--ih] > dv) ;
            if (iv >= ih) break;
            swap(t, iv, ih);
        }
        swap(t, iv, h-1);
        return iv;
    }

    /**
     * The method which swaps array`s value according index
     * @param t array
     * @param first first index
     * @param second second index
     */
    private static void swap(int []t,int first,int second){
        int temp = t[first];
        t[first]=t[second];
        t[second]=temp;
    }

    /**
     * Quick sort with double pivot
     * @param arr array we needed to sort
     * @param low first index
     * @param high second index
     */
    private static void dualPivotQuickSort(int[] arr,
                                   int low, int high)
    {
        if (low < high)
        {

            // piv[] stores left pivot and right pivot.
            // piv[0] means left pivot and
            // piv[1] means right pivot
            int[] piv;
            piv = partition(arr, low, high);

            dualPivotQuickSort(arr, low, piv[0] - 1);
            dualPivotQuickSort(arr, piv[0] + 1, piv[1] - 1);
            dualPivotQuickSort(arr, piv[1] + 1, high);
        }
    }

    /**
     * Splitting algorithm for quick sort with 2 elements
     * @param arr array
     * @param low first index
     * @param high second index
     * @return indicates of pivot elements
     */
    static int[] partition(int[] arr, int low, int high)
    {
        swap(arr, low,low+(high-low)/3);
        swap(arr, high,high-(high-low)/3);

        if (arr[low] > arr[high])
            swap(arr, low, high);

        // p is the left pivot, and q
        // is the right pivot.
        int j = low + 1;
        int g = high - 1, k = low + 1,
                p = arr[low], q = arr[high];

        while (k <= g)
        {

            // If elements are less than the left pivot
            if (arr[k] < p)
            {
                swap(arr, k, j);
                j++;
            }

            // If elements are greater than or equal
            // to the right pivot
            else if (arr[k] >= q)
            {
                while (arr[g] > q && k < g)
                    g--;

                swap(arr, k, g);
                g--;

                if (arr[k] < p)
                {
                    swap(arr, k, j);
                    j++;
                }
            }
            k++;
        }
        j--;
        g++;

        // Bring pivots to their appropriate positions.
        swap(arr, low, j);
        swap(arr, high, g);

        // Returning the indices of the pivots
        // because we cannot return two elements
        // from a function, we do that using an array.
        return new int[] { j, g };
    }

    /**
     * Method which generate random array
     * @param number number of elements
     * @param bound limit for interval
     * @return array med random n-elements in interval [0,bound)
     */
    private static int[] generateRandomArray(int number,int bound){
        int[] array = new int[number];
        Random rd = new Random();
        for (int i = 0; i<number; i++){
            array[i] = rd.nextInt(bound);
        }
        return array;
    }

    /**
     * Method which checks that array is sorted
     * @param array array
     * @return boolean sorted or not
     */
    private static boolean sortIsValid(int[] array){
        for(int i= 0; i< array.length-2; i++){
            if (array[i]>array[i+1]){
                return false;
            }
        }return true;
    }

    /**
     * Test which checks sum of all elements in array
     * @param array array
     * @return sum of all elements in array
     */
    private static double checkSumTest(int[] array){
        double sum=0;
        for (int i: array){
            sum+=i;
        }return sum;
    }

    /**
     * Method which generates array in reversed order
     * @param number number of elements
     * @param bound limit for random
     * @return reverse sorted array
     */
    private static int[] reverseArray(int number,int bound){
        int[] array = generateRandomArray(number,bound);
        quicksort(array,0, array.length-1);
        int[] returnArray = new int[number];
        int counter=0;
        for (int i = array.length-1 ; i>=0;i--){
            returnArray[counter] = array[i];
            counter++;
        }return returnArray;
    }


    /**
     * Test one array med random number
     */
    private static void test1(){
        System.out.println("Test 1 with random number in array");
        int[] randomArray = generateRandomArray(10000000,100000);
        int[] copyRandomArray = Arrays.copyOf(randomArray,randomArray.length);
        double sum = checkSumTest(randomArray);
        long startTime = System.currentTimeMillis();
        quicksort(randomArray,0,randomArray.length-1);
        long endTime = System.currentTimeMillis();
        long time = (endTime-startTime);
        System.out.println("\n-=Single Pivot Quick Sort=-\nTotal execution time: " + time +
                "ms\nNumber elements: 10 millions in interval from 0 to 100000\nSum test:" + (checkSumTest(randomArray)==sum) +
                "Valid :" + sortIsValid(randomArray));

        startTime = System.currentTimeMillis();
        dualPivotQuickSort(copyRandomArray,0,randomArray.length-1);
        endTime = System.currentTimeMillis();
        time = (endTime-startTime);
        System.out.println("\n-=Double Pivot Quick Sort=-\nTotal execution time: " + time +
                "ms\nNumber elements: 10 millions in interval from 0 to 100000\nSum test:" + (checkSumTest(copyRandomArray)==sum) +
                " Valid :" + sortIsValid(copyRandomArray));
    }
    /**
     * Test 2 array med random number
     */
    private static void test2(){
        System.out.println("\nTest 2 with random number in array");
        int[] randomArray = generateRandomArray(100000000,1000000);
        int[] copyRandomArray = Arrays.copyOf(randomArray,randomArray.length);
        double sum = checkSumTest(randomArray);
        long startTime = System.currentTimeMillis();
        quicksort(randomArray,0,randomArray.length-1);
        long endTime = System.currentTimeMillis();
        long time = (endTime-startTime);
        System.out.println("\n-=Single Pivot Quick Sort=-\nTotal execution time: " + time +
                "ms\nNumber elements: 100 millions in interval from 0 to 1 million\nSum test:" + (checkSumTest(randomArray)==sum) +
                " Valid :" + sortIsValid(randomArray));

        startTime = System.currentTimeMillis();
        dualPivotQuickSort(copyRandomArray,0,randomArray.length-1);
        endTime = System.currentTimeMillis();
        time = (endTime-startTime);
        System.out.println("\n-=Double Pivot Quick Sort=-\nTotal execution time: " + time +
                "ms\nNumber elements:100 millions in interval from 0 to 1 million\nSum test:" + (checkSumTest(copyRandomArray)==sum) +
                " Valid :" + sortIsValid(copyRandomArray));
    }

    /**
     * Test 3 array med random number but with a lots of duplicates
     * if we have a lot of numbers in array , but bound is low it means that
     * we have a lot of duplicates
     */
    private static void test3(){
        System.out.println("\nTest 3 with random number in array but with a lot of duplicates");
        int[] randomArray = generateRandomArray(10000000,1000);
        int[] copyRandomArray = Arrays.copyOf(randomArray,randomArray.length);
        double sum = checkSumTest(randomArray);
        long startTime = System.currentTimeMillis();
        quicksort(randomArray,0,randomArray.length-1);
        long endTime = System.currentTimeMillis();
        long time = (endTime-startTime);
        System.out.println("\n-=Single Pivot Quick Sort=-\nTotal execution time: " + time +
                "ms\nNumber elements: 10 millions in interval from 0 to 1000\nSum test:" + (checkSumTest(randomArray)==sum) +
                " Valid :" + sortIsValid(randomArray));

        startTime = System.currentTimeMillis();
        dualPivotQuickSort(copyRandomArray,0,randomArray.length-1);
        endTime = System.currentTimeMillis();
        time = (endTime-startTime);
        System.out.println("\n-=Double Pivot Quick Sort=-\nTotal execution time: " + time +
                "ms\nNumber elements:10 millions in interval from 0 to 1000\nSum test:" + (checkSumTest(copyRandomArray)==sum) +
                " Valid :" + sortIsValid(copyRandomArray));
    }

    /**
     * Test 4 array med random number but with a lots of duplicates
     * if we have a lot of numbers in array , but bound is low it means that
     * we have a lot of duplicates
     */
    private static void test4(){
        System.out.println("\nTest 4 with random number in array but with a lot of duplicates");
        int[] randomArray = generateRandomArray(100000000,10000);
        int[] copyRandomArray = Arrays.copyOf(randomArray,randomArray.length);
        double sum = checkSumTest(randomArray);
        long startTime = System.currentTimeMillis();
        quicksort(randomArray,0,randomArray.length-1);
        long endTime = System.currentTimeMillis();
        long time = (endTime-startTime);
        System.out.println("\n-=Single Pivot Quick Sort=-\nTotal execution time: " + time +
                "ms\nNumber elements: 100 millions in interval from 0 to 10000\nSum test:" + (checkSumTest(randomArray)==sum) +
                " Valid :" + sortIsValid(randomArray));

        startTime = System.currentTimeMillis();
        dualPivotQuickSort(copyRandomArray,0,randomArray.length-1);
        endTime = System.currentTimeMillis();
        time = (endTime-startTime);
        System.out.println("\n-=Double Pivot Quick Sort=-\nTotal execution time: " + time +
                "ms\nNumber elements: 100 millions in interval from 0 to 10000\nSum test:" + (checkSumTest(copyRandomArray)==sum) +
                " Valid :" + sortIsValid(copyRandomArray));
    }

    /**
     * Test 5 array med random number but it sorted reverse
     */
    private static void test5(){
        System.out.println("\nTest 5 with random number in array but array originally sorted in reversed way");
        int[] randomArray = reverseArray(10000000,100000);
        int[] copyRandomArray = Arrays.copyOf(randomArray,randomArray.length);
        double sum = checkSumTest(randomArray);
        long startTime = System.currentTimeMillis();
        quicksort(randomArray,0,randomArray.length-1);
        long endTime = System.currentTimeMillis();
        long time = (endTime-startTime);
        System.out.println("\n-=Single Pivot Quick Sort=-\nTotal execution time: " + time +
                "ms\nNumber elements: 10 millions in interval from 0 to 100000\nSum test:" + (checkSumTest(randomArray)==sum) +
                " Valid :" + sortIsValid(randomArray));

        startTime = System.currentTimeMillis();
        dualPivotQuickSort(copyRandomArray,0,randomArray.length-1);
        endTime = System.currentTimeMillis();
        time = (endTime-startTime);
        System.out.println("\n-=Double Pivot Quick Sort=-\nTotal execution time: " + time +
                "ms\nNumber elements: 10 millions in interval from 0 to 100000\nSum test:" + (checkSumTest(copyRandomArray)==sum) +
                " Valid :" + sortIsValid(copyRandomArray));
    }
    /**
     * Test 6 array med random number but it sorted reverse
     */
    private static void test6(){
        System.out.println("\nTest 6 with random number in array but array originally sorted in reversed way");
        int[] randomArray = reverseArray(100000000,1000000);
        int[] copyRandomArray = Arrays.copyOf(randomArray,randomArray.length);
        double sum = checkSumTest(randomArray);
        long startTime = System.currentTimeMillis();
        quicksort(randomArray,0,randomArray.length-1);
        long endTime = System.currentTimeMillis();
        long time = (endTime-startTime);
        System.out.println("\n-=Single Pivot Quick Sort=-\nTotal execution time: " + time +
                "ms\nNumber elements: 100 millions in interval from 0 to 1 million\nSum test:" + (checkSumTest(randomArray)==sum) +
                " Valid :" + sortIsValid(randomArray));

        startTime = System.currentTimeMillis();
        dualPivotQuickSort(copyRandomArray,0,randomArray.length-1);
        endTime = System.currentTimeMillis();
        time = (endTime-startTime);
        System.out.println("\n-=Double Pivot Quick Sort=-\nTotal execution time: " + time +
                "ms\nNumber elements: 100 millions in interval from 0 to 1 million\nSum test:" + (checkSumTest(copyRandomArray)==sum) +
                " Valid :" + sortIsValid(copyRandomArray));
    }



}
