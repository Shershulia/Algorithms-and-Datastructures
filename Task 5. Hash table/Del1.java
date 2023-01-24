import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class Del1 {
    public static void main(String[] args) {
        HashTable hashTable = new HashTable(131); //work better if size is primtall
        hashTable.readFile(hashTable,"C:\\Users\\krol3\\Desktop\\name.txt"); //change path to yours
        System.out.println("Full table: ");
        hashTable.printHashTable();
        System.out.println("Collisions: ");
        hashTable.printAllColisions();
        System.out.println("Lestfaktoren: "+ hashTable.getLastfaktoren());
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the name:");
        String name = sc.nextLine();
        System.out.println(hashTable.findPerson(name));
    }

}

/**
 * Class for Hashtable
 */
class HashTable{
    private LinkedList[] array; //array of linked list
    private int arraySize; //size of hashtable
    private int objectsInside; //how many objects inside

    /**
     * Constructor of hashTable
     * @param size size of hashTable
     */
    public HashTable(int size){
        this.arraySize=size;
        this.objectsInside=0;
        array=new LinkedList[size];
        for(int i =0;i<size;i++){
            array[i] = new LinkedList<String>();
        }
    }

    /**
     * Print all hashtable
     */
    public void printHashTable(){
        int numRow=-1;
        for(LinkedList<String> list:array){
            numRow++;
            if (list.isEmpty()){
                System.out.println(numRow+". Empty row");
            }
            else {
                System.out.print(numRow+ ". ");
                for (String name : list) {
                    System.out.print(name + " ");
                }
                System.out.println();
            }

        }
    }

    /**
     * Hash function according to Horner`s method
     * a*n^3+b*n^2+c*n+d = ((a*n+b)*n+c)*n+d
     * also to avoid the overflowing of integer I added % operator to the method
     * @param name the data that we want to add in
     * @return unique hashCode of the name
     */
    public int hashFunc(String name) {
        int hashVal = 0;
        for(int j=0; j<name.length(); j++)
        {
            int letter = name.charAt(j);
            hashVal = (hashVal * 7 + letter) % arraySize;
        }
        return hashVal;
    }

    /**
     * Insert the data inside the hashtable
     * @param name data
     */
    public void insert(String name){
        int value = hashFunc(name);
        array[value].add(name);
        objectsInside++;
    }

    /**
     * Fill table with data from file
     * @param hashtable hashtable that we want to fill
     * @param pathName pathname to file with data
     */
    public void readFile(HashTable hashtable,String pathName){
        try {
            File myObj = new File(pathName);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                hashtable.insert(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Get lastfaktor
     * @return lastfaktor
     */
    public float getLastfaktoren(){
        return (float)objectsInside/arraySize;
    }

    /**
     * Method that prints all collisions
     * and in the finish number of collisions per person
     */
    public void printAllColisions(){
        int nmCol=0;
        int numb=-1;
        for(LinkedList<String> linkedList:array){
            numb++;
            if(linkedList.size()>=2){
                System.out.print(numb+". ");
                nmCol+=(linkedList.size()-1);
                for (String name:linkedList){
                    System.out.print(name+"->");
                }
                System.out.println();
            }
        }
        System.out.println("Collision per person:" + (float)nmCol/objectsInside);
        System.out.println("Number of collisions:" + nmCol);
    }

    /**
     * The method which return the string with info about person
     * @param value name of person
     * @return the info about the person
     */
    public String findPerson(String value){
        int hash = hashFunc(value);
        int numb = 0;
        LinkedList<String> list = array[hash];
        for (String a :list){
            if (value.equals(a)) {
                return "Hash: " + hash + " Number of element: " + numb;
            }else numb++;
        }return "Not found";
    }
}