import java.util.Objects;
import java.util.Scanner;

/**
 * First oppgave
 * @author ivansh
 */
public class DobbeltLink {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        String status = "";
        while (!Objects.equals(status, "0")){
            System.out.println("Enter number one:");
            String a = sc.nextLine();
            System.out.println("Enter number two:");
            String b = sc.nextLine();
            System.out.println("Enter operation + or -:");
            String operation = sc.nextLine();
            if (operation.equals("+")){
                addition(a,b);
            }else if (operation.equals("-")){
                subtraction(a,b);
            }else{
                System.out.println("Error");
                break;
            }

            System.out.println("\nWould you continue? Print 0 if you want to exit");
            status = sc.nextLine();

        }
    }

    /**
     * Addition of to numbers using DoubleLinked List
     * My logic is that we add second number to first
     * from the lowest dozen to highest (lowest in beginning of list)
     * @param a first number
     * @param b second number
     */
    private static void addition(String a,String b){
        int flag_One = 0, flag_Two = 0;
        DoubleLinkedList list_a = DoubleLinkedList.generateList(a);
        DoubleLinkedList list_b = DoubleLinkedList.generateList(b);
        DoubleLinkedList result = new DoubleLinkedList();
        list_a.printList();
        System.out.println("\n+");
        list_b.printList();
        System.out.println("\n==");
        Node current_a = list_a.getHead();
        Node current_b = list_b.getHead();
        while (flag_One!=1 || flag_Two!=1){
            long sum = current_a.getNumber()+current_b.getNumber();

            if (sum>=10) {
                if (current_a.next==null){
                    list_a.addNode(0);
                }
                current_a.next.addNumber(1);
                result.addNode(sum % 10);
            }else result.addNode(sum);


            if (current_a.next==null){
                list_a.addNode(0);
                flag_One=1;
            }

            if (current_b.next==null) {
                list_b.addNode(0);
                flag_Two = 1;
            }
            current_a=current_a.next;
            current_b=current_b.next;


        }
        result.printList();
    }
    /**
     * Subtraction of to numbers using DoubleLinked List
     * My logic is that we substract from first number second
     * from the lowes dozen to highest (lowest in beginning of list)
     * @param a first number
     * @param b second number
     */
    private static void subtraction(String a,String b){
        int flag_One = 0, flag_Two = 0;
        DoubleLinkedList list_a = DoubleLinkedList.generateList(a);
        DoubleLinkedList list_b = DoubleLinkedList.generateList(b);
        DoubleLinkedList result = new DoubleLinkedList();
        list_a.printList();
        System.out.println("\n-");
        list_b.printList();
        System.out.println("\n==");
        Node current_a = list_a.getHead();
        Node current_b = list_b.getHead();
        while (flag_One!=1 || flag_Two!=1){
            long sub = current_a.getNumber()-current_b.getNumber();

            if (sub<0) {
                current_a.next.subNumber(1);
                result.addNode(sub+10);
            }else result.addNode(sub);


            if (current_a.next==null){
                list_a.addNode(0);
                flag_One=1;
            }

            if (current_b.next==null) {
                list_b.addNode(0);
                flag_Two = 1;
            }
            current_a=current_a.next;
            current_b=current_b.next;
        }
        result.printList();
    }

}

/**
 * Class Node for Linked List
 */
class Node {
    private long number; //data
    public Node next; //refer to next node
    public Node previous; //refer to previous

    /**
     * Constructor for node
     * @param number number inside of node
     */
    public Node(long number) {
        this.number = number;
    }

    /**
     * Get data inside of node
     * @return number
     */
    public long getNumber(){
        return number;
    }

    /**
     * Modify data inside the node by adding something
     * @param add number
     */
    public void addNumber(int add){
        number+=add;
    }
    /**
     * Modify data inside the node by subtracting something
     * @param add number
     */
    public void subNumber(int add){
        number-=add;
    }
}

/**
 * Class for DoubleLinkedList
 */
class DoubleLinkedList {

    private Node head ; //head of linked list
    private Node tail ; //tail

    /**
     * Constructor for empty list
     */
    public DoubleLinkedList(){
        Node head = null;
        Node tail = null;
    }

    /**
     * Add node for in linked list
     * @param item
     */
    public void addNode(long item) {
        Node newNode = new Node(item);
        if (head == null) { //if no nodes inside
            head = tail = newNode;
            head.previous = null;
            tail.next = null;
        } else {
            tail.next = newNode;
            newNode.previous = tail;
            tail = newNode;
            tail.next = null;
        }
    }

        /**
         * Method that generates Linked list according number
         * it is generates in order from the lowest dozen til highest
         * @param number number that should be reformed in list
         */
    public static DoubleLinkedList generateList(String number) {
        DoubleLinkedList db = new DoubleLinkedList();
        String[] dozen = number.split("");
        for (int i = dozen.length-1;i>=0;i--) {
            db.addNode(Integer.parseInt(dozen[i]));
        }

        return db;
    }

    /**
     * Print list in output
     * as we have lists from the lowes dozen
     * we print it in reversed order
     */
    public void printList(){
        Node current = tail;
        boolean zeroOut = false;
        while (current!=null) {
            if (current.getNumber() != 0) {
                System.out.print(current.getNumber());
                current = current.previous;
                zeroOut = true;
            }else if (zeroOut){
                System.out.print(current.getNumber());
                current=current.previous;
            }else if (current.previous==null) {
                System.out.println(current.getNumber());
                current=current.previous;}
            else current=current.previous;

        }
    }

    /**
     * get head of list
     * @return node
     */
    public Node getHead(){
        return head;
    }

    }
