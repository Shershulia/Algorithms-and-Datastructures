import java.util.HashMap;
import java.util.Random;

public class Del2 {
    public static void main(String[] args) {

        int[] array = createRandomArray(10000000);
        DoubleHashTable dbHashTalbe = new DoubleHashTable(12000017);

        long startTime = System.currentTimeMillis();
        for (int number:array){
            dbHashTalbe.insert(number);
        }
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Time in millis of my hashtable:" + elapsedTime);
        System.out.println("Number of collisons: "+dbHashTalbe.getCollisions());
        System.out.println("Lestfaktoren:"+ dbHashTalbe.getLest());

        HashMap hashMap = new HashMap(12000017);
        startTime = System.currentTimeMillis();
        for (int i = 0;i< array.length ;i++){
            hashMap.put(i,array[i]);
        }
        stopTime = System.currentTimeMillis();
        elapsedTime = stopTime - startTime;
        System.out.println("Time in millis of hashMap:" + elapsedTime);

    }

    /**
     * Static method to create array of spesific size
     * in intervall from 0 to 15000000
     * @param size size of the array
     * @return array of integers
     */
    public static int[] createRandomArray(int size){
        int[] array = new int[size];
        Random rd = new Random();
        for (int i = 0;i<size;i++){
            array[i]= rd.nextInt(14999999)+1;
        }return array;

    }
}

/**
 * Class of Hash Table that uses double hashing
 */
class DoubleHashTable{
    private int[] array; //array of linked list
    private int arraySize; //size of hashtable
    private int collisions; //how many collisions
    private int objectsInside;

    /**
     * Constructor of hashTable
     * @param size size of hashTable
     */
    public DoubleHashTable(int size){
        this.arraySize=size;
        this.collisions=0;
        this.objectsInside=0;
        array=new int[size];
        for(int i =0;i<size;i++){
            array[i] = 0;
        }
    }

    /**
     * Multiplicative hash
     * @param number number we needs to hash
     * @return hash
     */
    public int hash1(int number){
        double A = (Math.sqrt(5)-1)/2;
        double var = ((number*A)%1);
        return (int) (arraySize*var);
    }

    /**
     * The second hash function
     * @param number number to hash
     * @return hash
     */
    public int hash2(int number){
        int hash = (2*Math.abs(number)+1)%arraySize;
        return hash;

    }

    /**
     * Method to insert the number in haashtable
     * Using double hashing
     * @param x number
     */
    public void insert(int x){
        int pos = hash1(x); //original position
        if (array[pos]==0){
            array[pos]=x;
            objectsInside++;
            return;
        }collisions++;
        int step = hash2(x); //step with help of hash2 function
        while (true){
            pos=(pos+step)%arraySize;
            if (array[pos]==0){
                array[pos]=x;
                objectsInside++;
                return;
            }collisions++;
        }
    }

    /**
     * Method to get the number of collisions
     * @return number of collisions
     */
    public int getCollisions(){
        return collisions;
    }

    /**
     * Method to get the Lestfaktoren
     * @return Lestfaktoren
     */
    public float getLest(){
        return (float) objectsInside/arraySize;
    }

}
