import java.util.Date;

public class Rekursjon {
    public static void main(String[] args) {
        testAlgOne();
        testAlgTwo();
        testSysAlg();
    }

    /**
     * Test 1 where calculus is made with algorithm 1
     * That print on the window result of calculations
     * and that time did it take
      */
    private static void testAlgOne(){
        Date start = new Date();
        int round = 0;
        double time;
        Date end;
        double result;
        do {
            result = algOne(1.001,10000);
            end = new Date();
            ++round;
        } while (end.getTime() - start.getTime() < 1000);
        time = (double) (end.getTime() - start.getTime()) / round;
        String roundResult = String.format("%.5f", result);

        System.out.println("Algorithm 1 | Result: "+roundResult +" | Millisekund pr. runde:" + time + "\n");
    }
    /**
     * Test 2 where calculus is made with algorithm 2
     * That print on the window result of calculations
     * and that time did it take
     */
    private static void testAlgTwo(){
        Date start = new Date();
        int round = 0;
        double time;
        Date end;
        double result;
        do {
            result = algTwo(1.001,10000);
            end = new Date();
            ++round;
        } while (end.getTime() - start.getTime() < 1000);
        time = (double) (end.getTime() - start.getTime()) / round;
        String roundResult = String.format("%.5f", result);

        System.out.println("Algorithm 2 | Result: "+roundResult +" | Millisekund pr. runde:" + time + "\n");
    }
    /**
     * Test 3 where calculus is made with system algorithm math.pow(x,n)
     * That print on the window result of calculations
     * and that time did it take
     */
    private static void testSysAlg(){
        Date start = new Date();
        int round = 0;
        double time;
        Date end;
        double result;
        do {
            result = Math.pow(1.001,10000);
            end = new Date();
            ++round;
        } while (end.getTime() - start.getTime() < 1000);
        time = (double) (end.getTime() - start.getTime()) / round;
        String roundResult = String.format("%.5f", result);

        System.out.println("System algorithm | Result: "+roundResult +" | Millisekund pr. runde:" + time + "\n");
    }

    /**
     * Algorithm 1 which work according formula if n=0  return 1
     * else x^n=x*x^(n-1)
     * @param x double number
     * @param n degree
     * @return calculation
     */
    static double algOne(double x,int n){
        if (n==0) return 1;
        else return algOne(x,n-1)*x;
    }
    /**
     * Algorithm 2 which work according formula if n=0  return 1
     * else if n!=0 and n is odd x^n=x*(x^2)^(n-1/2)
     * and if n!=0 but n is even x^n=(x^2)^(n/2)
     * @param x number
     * @param n degree
     * @return calculation
     */
    static double algTwo(double x,int n){
        if (n==0) return 1;
        else if (n%2==1) return algTwo(x*x,(n-1)/2)*x;
        else return algTwo(x*x,n/2);
    }


}
