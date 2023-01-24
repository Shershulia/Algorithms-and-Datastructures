import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Oppgave6
 */
public class Opg6 {
    public static void main(String[] args) {
                Graph.finalScc("ø6g1.txt");
                System.out.println("---------------------------------------------------------------------------");
                Graph.finalScc("ø6g6.txt");
                System.out.println("---------------------------------------------------------------------------");
                Graph.finalScc("ø6g5.txt");
                System.out.println("---------------------------------------------------------------------------");
                Graph.finalScc("ø6g2.txt");

    }
}

/**
 * Class for graph
 */
class Graph {
    private int sumNodes;//how many nodes
    private LinkedList<Integer> connectedTo[]; //each node is number of linked list (inside linked list that it connected to)

    /**
     * Constructor to initialise empty graph
     * @param nodes
     */
    public Graph(int nodes) {
        this.sumNodes = nodes;
        connectedTo = new LinkedList[nodes];
        for (int i = 0; i < nodes; i++) {
            connectedTo[i] = new LinkedList<>();
        }
    }

    /**
     * Step one of strongly connected components algorithm
     * @param node from which node is DFS
     * @param visited which node was visited before
     * @return the list, so the FIRST completed nodes come first
     */
    public ArrayList<Integer> Step1(int node, int[] visited) {
        LinkedList<Integer> connectedToNew[] = new LinkedList[sumNodes];
        for (int i = 0; i < sumNodes; i++) {
            connectedToNew[i] = (LinkedList<Integer>) connectedTo[i].clone();
        }

        ArrayList<Integer> result = new ArrayList<>(sumNodes);

        Stack<Integer> stack = new Stack();
        stack.push(node);

        while (!stack.isEmpty()) {
            node = stack.peek();
            if (visited[node] == 0){
                visited[node] = 1;
            }
            if (connectedToNew[node].peek() != null) {
                int j = connectedToNew[node].poll();
                if (visited[j]==0) stack.push(j);
            }
            else {
                result.add(stack.pop());

            }
        }return result;
    }

    /**
     * Usually DFS for graph
     * @param node from which node start we search
     * @param visited table of visited nodes before
     * @return list as was visited
     */
    public ArrayList<Integer> DFS(int node, int[] visited) {
        LinkedList<Integer> connectedToNew[] = new LinkedList[sumNodes];
        for (int i = 0; i < sumNodes; i++) {
            connectedToNew[i] = (LinkedList<Integer>) connectedTo[i].clone();
        }

        ArrayList<Integer> result = new ArrayList<>(sumNodes);

        Stack<Integer> stack = new Stack();
        stack.push(node);

        while (!stack.isEmpty()) {
            node = stack.peek();
            if (visited[node] == 0){
                result.add(node);
                visited[node] = 1;
            }
            if (connectedToNew[node].peek() != null) {
                stack.push(connectedToNew[node].poll());
            }
            else {
                stack.pop();
            }
        }return result;
    }

    /**
     * Add edge in graph
     * @param FromNode from node ->
     * @param toNode    to node
     */
    public void addEdge(int FromNode, int toNode) {
        connectedTo[FromNode].add(toNode);
    }

    /**
     * Static method to create graph from file of format
     * @param pathName name of the file
     * @return new graph
     */
    public static Graph createFromFile(String pathName) {
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
        Graph graph = new Graph(Integer.parseInt(words[0]));
        while (myReader.hasNextLine()) {
            data = myReader.nextLine();
            words = data.trim().split("\\s+");
            graph.addEdge(Integer.parseInt(words[0]), Integer.parseInt(words[1]));
        }
        myReader.close();
        return graph;
    }

    /**
     * Make transpose graph
     * @param oldGraph graph before transposing
     * @return transposed graph
     */
    public static Graph transposeGraph(Graph oldGraph){
        Graph newGraph = new Graph(oldGraph.sumNodes);
        int i = 0 ;
        for (LinkedList<Integer> linkls: oldGraph.connectedTo){
            for (int j:linkls){
                newGraph.addEdge(j,i);
            }i++;
        }return newGraph;
    }



    /**
     * Strongly Connected Components
     * main algorithm
     * @param graph which we will find in
     * @return list for result - print out
     */
    private static LinkedList<Integer>[] SccAlg(Graph graph){


        Graph trGraph = transposeGraph(graph);

        int[] visited = new int[trGraph.sumNodes];

        Stack<Integer> stackForTrGraph = new Stack<>();
        for (int i=0;i< trGraph.sumNodes;i++) {
            if (visited[i]== 0) {
                ArrayList<Integer> as = trGraph.Step1(i,visited);
                for (int n=0; n<=as.size()-1;n++) {
                    stackForTrGraph.push(as.get(n));
                }
            }
        }

        visited = new int[graph.sumNodes];

        LinkedList<Integer> result[] = new LinkedList[graph.sumNodes];
        for (int i = 0; i < graph.sumNodes; i++) {
            result[i] = new LinkedList<>();
        }


        int j=0;
        while(!stackForTrGraph.isEmpty()){
            int i = stackForTrGraph.pop();
            ArrayList<Integer> as = graph.DFS(i,visited);
            if (!as.isEmpty()) {
                for (int n : as) {
                    result[j].add(n);
                }
                j++;
            }
        }
        return result;
    }

    /**
     * Print graph
     */
    public void printGraph(){
        for (int i=0;i<sumNodes;i++){
            System.out.print(i+ "-> ");
            for (Integer integer : connectedTo[i]) {
                System.out.print(integer + " ");
            }
            System.out.println();
        }
    }

    /**
     * Method to print result of SCC alg
     * @param result list for result - print out
     */
    private static void printScc(LinkedList<Integer> result[]){
        int i = 0;
        for(LinkedList<Integer> ls:result) {
            if (ls.size()>0){
                System.out.print(i + ": ");
                for (int j : ls) {
                    System.out.print(j + " ");
                }
                i++;
                System.out.println();
            }
        }
        System.out.println("Det er " + i + " sterkt sammenhengende komponenter i graphen.");
    }

    /**
     * final algorithm SCC which creates graph,makes SCC and print out result
     * @param path to file
     */
    public static void finalScc(String path){
        Graph secondGraph = createFromFile(path);
        LinkedList<Integer> result[] = SccAlg(secondGraph);
        printScc(result);
    }


}



