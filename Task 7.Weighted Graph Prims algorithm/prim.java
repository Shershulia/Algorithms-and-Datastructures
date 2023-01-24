import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class prim {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("\nEnter name of file with graph in current directory and from which node to start\n"+
                "Example(name_file.txt start_node).Enter to quit\n");
        String str = sc.nextLine();
        while(!str.isEmpty()){
            String[] words = str.trim().split("\\s+");
            System.out.println("Graph "+ words[0] + " with start node "+ words[1]);
            Graph graph = Graph.createFromFile(words[0]);
            graph.primsAlg(Integer.parseInt(words[1]));
            System.out.println("\nEnter name of file with graph in current directory and from which node to start\n"+
                    "Example(name_file.txt start_node).Enter to quit\n");
            str = sc.nextLine();
        }sc.close();

    }
}

/**
 * Class for vertex that innholds information
 */
class Vertex{
    private int from;
    private int to;
    private int weight;

    /**
     * Constructor method
     * @param from from node
     * @param to to node
     * @param weight weight of vertex
     */
    public Vertex(int from, int to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    /**
     * Get fromNode
     * @return int fromNode
     */
    public int getFrom(){
        return from;
    }

    /**
     * Get toNode
     * @return int toNode
     */
    public int getTo(){
        return to;
    }

    /**
     * Get weight
     * @return weight of vertex
     */
    public int getWeight(){
        return weight;
    }
}

/**
 * Class for graph
 */
class Graph{
    private int sumNode; //how many nodes in graph
    private LinkedList<Vertex>[] connectedTo; //each [i] element have vertex inside
    private int vertices; // how many vertexes inside


    /**
     * Constructor
     * @param nodes how many nodes
     * @param vert how many vertexes
     */
    public Graph(int nodes,int vert) {
        this.sumNode=nodes;
        connectedTo = new LinkedList[nodes];
        for(int i=0;i<nodes;i++){
            this.connectedTo[i] = new LinkedList<>();
        }
        this.vertices= vert;
    }

    /**
     * Static method to create graph from the file
     * @param pathName name of file in current directory
     * @return created graph
     */
    public static Graph createFromFile(String pathName){
        byte ptext[] = pathName.getBytes();
        String value = pathName;

        try {
            value = new String(ptext, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        File myObj = new File(value).getAbsoluteFile();

        Scanner myReader = null;

        try {
            myReader = new Scanner(myObj);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String data = myReader.nextLine();
        String[] words = data.trim().split("\\s+");
        Graph graph = new Graph(Integer.parseInt(words[0]),Integer.parseInt(words[1]));
        while (myReader.hasNextLine()) {
            data = myReader.nextLine();
            words = data.trim().split("\\s+");
            int a = Integer.parseInt(words[0]),b=Integer.parseInt(words[1]),c=Integer.parseInt(words[2]);
            graph.connectedTo[a].add(new Vertex(a,b,c));
            graph.connectedTo[b].add(new Vertex(b,a,c));

        }
        myReader.close();
        return graph;
    }

    /**
     * Prim`s algorithm to graph from start node
     * @param startNode from this node we will start
     */
    public void primsAlg(int startNode){
        int sum = 0; //sum of nodes
        PriorityQueue<Vertex> vertixes = new PriorityQueue<>(vertices, Comparator.comparing(Vertex::getWeight)); //priority queue of verticles
        boolean[] visited = new boolean[sumNode]; //visited
        int sumNodesInMST = 1; //nodes in prim
        vertixes.addAll(connectedTo[startNode]); //we add all to PQ
        visited[startNode]=true; //set node as visited
        Vertex current = vertixes.poll(); //take the weight less component
        while ((current!=null) && (sumNodesInMST!=sumNode)){ //while PQ is empty or we visited all nodes
            if (!visited[current.getTo()] && visited[current.getFrom()]){ //if we don`t visit getTo node
                String from = String.format("From:%10d| ", current.getFrom()); //we visit it
                String to = String.format("To:%10d | ", current.getTo());
                String result_str = String.format("Weight:%10d | ", current.getWeight());
                System.out.println(from+to+result_str); // we output information
                visited[current.getTo()]=true;//setting it to visited
                vertixes.addAll(connectedTo[current.getTo()]); //add all verticles which we can possibly come to
                sumNodesInMST++; //one more visited node in prims alg
                sum+=current.getWeight(); //we add weight to sum
            }
            current = vertixes.poll(); //take node from list
        }
        //if was visited not all nodes
        //we search unvisited and out them
        if (sumNodesInMST!=sumNode){
            for (int i = 0 ;i<sumNode;i++){
                if (!visited[i]){
                    System.out.println("Node "+ i + " not reached with Prim`s algorithm");
                }
            }
        }
        System.out.println("Sum of nodes :" + sum);


    }


}
