import java.util.Date;
import java.util.Random;

/**
 * Arbeidskrav 1. IDATT2101.
 * @author ivansh
 */
public class oppgave1 {
    public static void main(String[] args) {
        test1();
        test2();
        test3();
        test4();
    }

    /**
     * The algorithm which gives us max profit and days when you can
     * buy and sell
     * @param values changing of price
     * @return array in which in 0 position max profit, 1 position day when we will buy
     * 2 position of array when we will sell
     */
    public static int[] get_profit(int[] values) {
        int ind_min = 0, ind_max = 0, profit_main = 0;
        for (int i = 0; i < values.length; i++) {
            int profit_temp = 0;
            for (int j = i + 1; j < values.length; j++) {
                profit_temp += values[j];
                if (profit_temp > profit_main) {
                    profit_main = profit_temp;
                    ind_min = i;
                    ind_max = j;
                }
            }
        }return new int[]{profit_main,ind_min,ind_max};
    }

    /**
     * First test , when we have all data from textbook
     */
    public static void test1(){
        int[] array = new int[]{-1, 3, -9, 2, 2, -1, 2, -1, -5};
        Date start = new Date();
        int runder = 0;
        double tid;
        Date slutt;
        int[] test1;
        do {
            test1 = get_profit(array);
            slutt = new Date();
            ++runder;
        } while (slutt.getTime() - start.getTime() < 1000);
        tid = (double)
                (slutt.getTime() - start.getTime()) / runder;
        System.out.println("Best to buy: " + (test1[1] + 1) + "\nBest day to sale: " + (test1[2] + 1) +
                "\nProfit: " + test1[0]);
        System.out.println("Numbers: "+array.length+ " | Millisekund pr. runde:" + tid + "\n");

    }

    /**
     * Test 2 where we have 100 elements in array
     * In output we need only time in which algorithm is completed
     */
    public static void test2() {
        int[] array = generateRandomArray(100);
        Date start = new Date();
        int runder = 0;
        double tid;
        Date slutt;
        do {
            get_profit(array);
            slutt = new Date();
            ++runder;
        } while (slutt.getTime() - start.getTime() < 1000);
        tid = (double)
                (slutt.getTime() - start.getTime()) / runder;
        System.out.println("Number: 100 | Millisekund pr. runde:" + tid );
    }

    /**
     * Test 3 where we have 1000 elements in array
     * In output we need only time in which algorithm is completed
     */
    public static void test3(){
        int[] array = generateRandomArray(1000);
        Date start = new Date();
        int runder = 0;
        double tid;
        Date slutt;
        do {
            get_profit(array);
            slutt = new Date();
            ++runder;
        } while (slutt.getTime() - start.getTime() < 1000);
        tid = (double)
                (slutt.getTime() - start.getTime()) / runder;
        System.out.println("Number: 1000 | Millisekund pr. runde:" + tid );

    }
    /**
     * Test 4 where we have 10000 elements in array
     * In output we need only time in which algorithm is completed
     */
    public static void test4() {
        int[] array = generateRandomArray(10000);
        Date start = new Date();
        int runder = 0;
        double tid;
        Date slutt;
        do {
            get_profit(array);
            slutt = new Date();
            ++runder;
        } while (slutt.getTime() - start.getTime() < 1000);
        tid = (double)
                (slutt.getTime() - start.getTime()) / runder;
        System.out.println("Number: 10000 | Millisekund pr. runde:" + tid );
    }

    /**
     * The method which generate us array with n-elements
     * with 5 unique values from -2 to 2
     * @param number - number of element in array
     * @return
     */
    public static int[] generateRandomArray(int number){
        int[] array = new int[number];
        Random rd = new Random();
        for (int i = 0; i<number; i++){
            array[i] = rd.nextInt(4) - 2; //interval from -2 to 2
        }
        return array;
    }



}
