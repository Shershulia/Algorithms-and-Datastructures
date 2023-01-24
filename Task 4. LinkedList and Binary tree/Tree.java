import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.Scanner;

/**
 * Second oppgave
 */
public class Tree {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String status = "";
        while (!Objects.equals(status, "0")){
            BinaryTree bt = new BinaryTree();

            System.out.println("Enter string");

            String str = sc.nextLine();
            String[] words = str.split(" ");
            for (String word : words) {
                bt.add(word);
            }
            bt.printTree();

            System.out.println("\nWould you continue? Print 0 if you want to exit");
            status = sc.nextLine();

        }

    }
}

/**
 * Class for binary tree
 */
    class BinaryTree {
        private NodeTree head; //root of binary tree

    /**
     * Constructor for empty binary tree
     */
    public BinaryTree()  {
            NodeTree head = null;
        }

    /**
     * Add node in binary tree according its string value
     * @param value string that we want to enter
     */
    public void add(String value) {
            NodeTree newNode = new NodeTree(value); //generate new node
            if (head == null) { //if tree is empty
                head = newNode;
            } else {
                NodeTree current = head; //we start search from root
                while (true) {
                    if (value.compareTo(current.getString()) <= 0 && current.right != null) { //if it has right child already
                        current = current.right; //we move
                    } else if (value.compareTo(current.getString()) > 0 && current.left != null) { //if it has left child already
                        current = current.left; //we move
                    } else if (value.compareTo(current.getString()) <= 0 && current.right == null) { //if we find there to insert node
                        current.right = newNode; // insert
                        break; //come out the loop
                    } else if (value.compareTo(current.getString()) > 0 && current.left == null) {
                        current.left = newNode;
                        break;
                    }
                }

            }
        }

    /**
     * Method to print first 4 levels of the tree
     */
    public void printTree() {
            Queue<NodeTree> currentLvl = new LinkedList<>(); //current level queue
            Queue<NodeTree> nextLvl = new LinkedList<>(); //next level queue
            currentLvl.add(head);
            int i = 0; //i-varialbe for level
            while (i < 4) { //4 levels
                NodeTree current = currentLvl.remove(); //we take node from current level
                if (current != null) { //node is not null we out its value
                    switch (i) { //formatting string according each level
                        case (0):
                            System.out.printf("%64.64s", current.getString());
                            break;
                        case (1):
                            System.out.printf("%32.32s%32s", current.getString()," ");
                            break;

                        case (2):
                            System.out.printf("%16.16s%16s", current.getString()," ");
                            break;
                        case (3):
                            System.out.printf("%8.8s%8s", current.getString()," ");
                            break;

                    }
                    nextLvl.add(current.right); //add right child to nextlvl queue
                    nextLvl.add(current.left); //add left child to nextlvl queue
                } else { //if current node is null we out --
                    switch (i) { //formatting according each level
                        case (0):
                            System.out.printf("%64s", "--");
                            break;
                        case (1):
                            System.out.printf("%32s%32s", "--"," ");
                            break;

                        case (2):
                            System.out.printf("%16s%16s", "--"," ");
                            break;
                        case (3):
                            System.out.printf("%8s%8s", "--"," ");
                            break;
                    }
                    nextLvl.add(null); //add null as right child of null
                    nextLvl.add(null); //add null as left child of null
                }
                if (currentLvl.isEmpty()) { //if current level is over
                    currentLvl = nextLvl; //we move to next
                    nextLvl = new LinkedList<>();
                    i++;
                    System.out.println(); //in the next string
                }
            }
        }
    }


/**
 * Class for tree node
 */
class NodeTree {
        private String name; //data
        public NodeTree left; //refer to next node
        public NodeTree right; //refer to previous

    /**
     * Constructor of node tree
     * @param name string value
     */
    public NodeTree(String name) {
            this.name = name;
            this.left = null;
            this.right = null;
        }

    /**
     * Get value of node
     * @return string inside of node
     */
    public String getString() {
            return name;
        }
    }


