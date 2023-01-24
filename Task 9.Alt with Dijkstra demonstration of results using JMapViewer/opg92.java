import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.*;

public class opg92 {
    public static void main(String[] args) throws IOException {

        DijkstraGraph util = new DijkstraGraph(1);
        DijkstraGraph original = util.graphFromFile("noder.txt", "kanter.txt", "interessepkt.txt");
        Map sample2 = new Map();
        //int start = original.getNrByName("\"Stavanger\"");
        int finish = original.getNrByName("\"Tampere\"");
        sample2.createMapInteressePunkt(original,finish,"OvernattingSted");


    }

    /**
     * Static class to draw path between all given nodes in the map
     */
    static class RoutePainter implements Painter<JXMapViewer> {
    private Color color = Color.RED;
    private boolean antiAlias = true;

    private List<GeoPosition> track;

    /**Constructor which take the path of
     * @param track the track
     */
    public RoutePainter(List<GeoPosition> track)
    {
        // copy the list so that changes in the
        // original list do not have an effect here
        this.track = new ArrayList<GeoPosition>(track);
    }

    @Override
    public void paint(Graphics2D g, JXMapViewer map, int w, int h)
    {
        g = (Graphics2D) g.create();

        // convert from viewport to world bitmap
        Rectangle rect = map.getViewportBounds();
        g.translate(-rect.x, -rect.y);

        if (antiAlias)
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // do the drawing
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1));

        drawRoute(g, map);

        // do the drawing again
        g.setColor(color);
        g.setStroke(new BasicStroke(1));

        drawRoute(g, map);

        g.dispose();
    }

    /** Method to drawRoute between GeoPositions
     * @param g the graphics object
     * @param map the map
     */
    private void drawRoute(Graphics2D g, JXMapViewer map)
    {
        int lastX = 0;
        int lastY = 0;

        boolean first = true;

        for (GeoPosition gp : track)
        {
            // convert geo-coordinate to world bitmap pixel
            Point2D pt = map.getTileFactory().geoToPixel(gp, map.getZoom());

            if (first)
            {
                first = false;
            }
            else
            {
                g.drawLine(lastX, lastY, (int) pt.getX(), (int) pt.getY());
            }

            lastX = (int) pt.getX();
            lastY = (int) pt.getY();
        }
    }
}

    /**
     * Class to output the map
     */
    static class Map {
        /**
         * Method implementation dijkstra`s shortest path in map
         * (to show shortest path from start node to finish)
         * @param dijkstraGraph graph where nodes and vertices is allocated
         * @param start start node number
         * @param finish finish node number
         */
        public void createMapDij(DijkstraGraph dijkstraGraph,int start,int finish) {
        JXMapViewer mapViewer = new JXMapViewer();
        JFrame frame = new JFrame("Default");
        // Display the viewer in a JFrame
        frame = new JFrame("Dijkstra algorithm");



        frame.getContentPane().add(mapViewer);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Create a TileFactoryInfo for OpenStreetMap
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);


        // Create a track from the geo-positions
        List<GeoPosition> track = new ArrayList<>();

        //Dijkstra
        long startTime = System.currentTimeMillis();
        ArrayList<Integer> result = dijkstraGraph.dijkstraAlgFromTo(start, finish);
        long endTime = System.currentTimeMillis();
        System.out.println("That took " + (endTime - startTime) + " milliseconds ");
        System.out.println("Nodes in rute:" + result.size());

            for (int i = result.size()-1;i>0;i--){
            Node current_node = dijkstraGraph.nodes.get(result.get(i));
            track.add(new GeoPosition(current_node.x,current_node.y));
        }

        RoutePainter routePainter = new RoutePainter(track);


        // Set the focus
        mapViewer.zoomToBestFit(new HashSet<GeoPosition>(track), 0.7);

        // Create waypoints from the geo-positions
        Set<Waypoint> waypoints = new HashSet<Waypoint>();
        for (int i = 0;i<track.size();i++) {
            waypoints.add(new DefaultWaypoint(track.get(i)));
        }

        // Create a waypoint painter that takes all the waypoints
        WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<Waypoint>();
        waypointPainter.setWaypoints(waypoints);

        // Create a compound painter that uses both the route-painter and the waypoint-painter
        List<Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();
        painters.add(routePainter);
        painters.add(waypointPainter);

        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
        mapViewer.setOverlayPainter(painter);
    }

        /**
         * Implementation of alt in Map
         * (to find shortest path using alt with one landmark)
         * @param dijkstraGraph graph where nodes and vertices is allocated
         * @param start start node number
         * @param finish finish node number
         * @param landmark landmark node number
         */
    public void createMapALT(DijkstraGraph dijkstraGraph,int start,int finish,int landmark) {
        JXMapViewer mapViewer = new JXMapViewer();
        JFrame frame = new JFrame("Default");
        // Display the viewer in a JFrame
        frame = new JFrame("ALT algorithm");

        frame.getContentPane().add(mapViewer);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Create a TileFactoryInfo for OpenStreetMap
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);


        // Create a track from the geo-positions
        List<GeoPosition> track = new ArrayList<>();

        //ALT
        ArrayList<Integer> result = dijkstraGraph.altAlg(start, finish, landmark);

        for (int i = result.size()-1;i>0;i--){
            Node current_node = dijkstraGraph.nodes.get(result.get(i));
            track.add(new GeoPosition(current_node.x,current_node.y));
        }



        RoutePainter routePainter = new RoutePainter(track);


        // Set the focus
        mapViewer.zoomToBestFit(new HashSet<GeoPosition>(track), 0.7);

        // Create waypoints from the geo-positions
        Set<Waypoint> waypoints = new HashSet<Waypoint>();
        for (int i = 0;i<track.size();i++) {
            waypoints.add(new DefaultWaypoint(track.get(i)));
        }

        // Create a waypoint painter that takes all the waypoints
        WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<Waypoint>();
        waypointPainter.setWaypoints(waypoints);

        // Create a compound painter that uses both the route-painter and the waypoint-painter
        List<Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();
        painters.add(routePainter);
        painters.add(waypointPainter);

        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
        mapViewer.setOverlayPainter(painter);
    }

        /**
         * Implementation of finding 8 interesse nodes in Map
         * @param dijkstraGraph graph where nodes and vertices is allocated
         * @param start start node number
         * @param modificator modificator(which types of nodes to find)
         */
    public void createMapInteressePunkt(DijkstraGraph dijkstraGraph,int start,String modificator) {
        JXMapViewer mapViewer = new JXMapViewer();
        JFrame frame = new JFrame("Default");
        // Display the viewer in a JFrame
        String title = "8 "+ modificator + " sted i node "+ start;
        frame = new JFrame(title);



        frame.getContentPane().add(mapViewer);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Create a TileFactoryInfo for OpenStreetMap
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);


        // Create a track from the geo-positions
        List<GeoPosition> track = new ArrayList<>();

        //Dijkstra
        long startTime = System.currentTimeMillis();
        ArrayList<Node> result = dijkstraGraph.findInteressPunkt(start, modificator);
        long endTime = System.currentTimeMillis();
        System.out.println("That took " + (endTime - startTime) + " milliseconds");

        System.out.println("Their name from closest:");
        for (Node e : result){
            track.add(new GeoPosition(e.x,e.y));
            System.out.println(e.name);
        }


        // Set the focus
        mapViewer.zoomToBestFit(new HashSet<GeoPosition>(track), 0.7);

        // Create waypoints from the geo-positions
        Set<Waypoint> waypoints = new HashSet<Waypoint>();
        for (int i = 0;i<track.size();i++) {
            waypoints.add(new DefaultWaypoint(track.get(i)));
        }

        // Create a waypoint painter that takes all the waypoints
        WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<Waypoint>();
        waypointPainter.setWaypoints(waypoints);

        // Create a compound painter that uses both the route-painter and the waypoint-painter
        List<Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();
        painters.add(waypointPainter);

        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
        mapViewer.setOverlayPainter(painter);
    }
}

    /**
     * Class for nodes in Graph
     */
    static class Node{
        private int node_nr; //number of nodes
        private float x; //breddegrad
        private float y; //lengdegrad
        private int code; //code of node to find type of node
        private String name; //name of node

        public int sum =0; //sum for dijkstra

        public int previous; //previous node

        /**
         * Simple constructor for node that takes 3 variables:
         * @param node_nr number of node
         * @param x breddegrad
         * @param y lengdegrad
         */
    public Node(int node_nr,float x,float y) {
        this.node_nr = node_nr;
        this.code = 0;
        this.name = null;
        this.x=x;
        this.y=y;
    }

        /**
         * Method to get sum of node
         * @return sum of nodes
         */
    public int getSum() {
        return sum;
    }

        /**
         * Set sum
         * @param sum sum need to be setted
         */
    public void setSum(int sum) {
        this.sum = sum;
    }

        /**
         * Set name of node
         * @param name node to be setted
         */
    public void setName(String name){
        this.name=name;
    }

        /**
         * Set code of node
         * @param code code to be setted
         */
    public void setCode(int code) {
        this.code = code;
    }

    // THERE GOES 6 METHODES THAT RETURN TRUE IF NODE IS ONE OF MODIFICATORS
    public boolean testStedsNavn(){
        if ((code & 1) == 1) return true;
        else return false;
    }
    public boolean testBensin(){
        if ((code & 2) == 2) return true;
        else return false;
    }
    public boolean testLadeStasjon(){
        if ((code & 4) == 4) return true;
        else return false;
    }
    public boolean testSpiseSted(){
        if ((code & 8) == 8) return true;
        else return false;
    }
    public boolean testDrikkeSted(){
        if ((code & 16) == 16) return true;
        else return false;
    }
    public boolean testOvernattingSted(){
        if ((code & 32) == 32) return true;
        else return false;
    }
}

    /**
     * Class for vertex
     * in graph
     */
    static class Vertex{
        private int from_node; //from node
        private int til_node; //to node
        private int driving_time; //driving time of vertex

        /**
         * Simple constructor for vertex
         * @param from_node from node
         * @param til_node to node
         * @param driving_time driving time between these two nodes
         */
    public Vertex(int from_node,int til_node, int driving_time) {
        this.from_node = from_node;
        this.til_node = til_node;
        this.driving_time = driving_time;
    }

        /**
         * Get driving time for vertex
         * @return driving time
         */
    public int getDriving_time(){
        return driving_time;
    }
}

    /**
     * Class for dijkstra graph
     */
    static class DijkstraGraph {
    ArrayList<Node> nodes = new ArrayList<>(); // array list with all nodes
    HashMap<Integer, ArrayList<Vertex>> hashMap; //map there inside is all relations
    int vertexes; //how many nodes inside

        /**
         * Simple constructor for dijkstra graph
         * @param vertexes how many nodes inside
         */
    public DijkstraGraph(int vertexes) {
        this.hashMap = new HashMap<>();
        this.vertexes = vertexes;
    }

        /**
         * Method to find node according it name
         * @param gg name of node
         * @return number of node
         */
    public int getNrByName(String gg) {
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).name != null) {
                if (nodes.get(i).name.equals(gg)) return i;
            }
        }
        return -1;
    }

        /**
         * Generate graph from file method
         * which take 3 files: nodes, kant, and interessepkt
         * @param nodetxt file with nodes
         * @param kanttxt file with relations
         * @param intertxt specification of nodes
         * @return graph
         * @throws IOException
         */
    public DijkstraGraph graphFromFile(String nodetxt, String kanttxt, String intertxt) throws IOException {
        nodes = getNodesFromFiles(nodetxt, intertxt);
        DijkstraGraph dijkstraGraph = new DijkstraGraph(nodes.size());
        dijkstraGraph.nodes = nodes;
        dijkstraGraph.addVertexFromFile(kanttxt);
        return dijkstraGraph;
    }

        /**
         * Add realtion
         * @param from from node
         * @param to to node
         * @param driving_time driving time from from_node til to_node
         */
    public void addVertex(int from, int to, int driving_time) {
        if (!hashMap.containsKey(from)) hashMap.put(from, new ArrayList<>());
        hashMap.get(from).add(new Vertex(from, to, driving_time));
    }

    /**
     * Algorithm for Dijkstra from point to point
     * @param from from which point
     * @param to   to which point
     * @return shortest path using dijkstra
     */
    public ArrayList<Integer> dijkstraAlgFromTo(int from, int to) {
        int polling_of_queue = 0;
        int[] distance = new int[nodes.size()];
        int[] previous = new int[nodes.size() + 1];
        Arrays.fill(distance, -1);
        Comparator<Vertex> nameSorter = Comparator.comparing(Vertex::getDriving_time);
        PriorityQueue<Vertex> queue = new PriorityQueue<>(nameSorter);
        queue.add(new Vertex(from, from, 0));
        Vertex current_vertex;
        while (queue.size() != 0) {
            current_vertex = queue.poll();
            polling_of_queue++;
            if (distance[current_vertex.til_node] == -1) { //if vi don`t have distance til previous node
                distance[current_vertex.til_node]    = current_vertex.driving_time;
                previous[current_vertex.til_node] = current_vertex.from_node;

                if (current_vertex.til_node == to) {
                    System.out.println("Dijkstra algorithm:");
                    int seconds = current_vertex.driving_time / 100;
                    int hours = seconds / 3600, min = seconds / 60 % 60;
                    seconds = seconds % 60;
                    System.out.println("Estimatite driving time: " + hours + ":" + min + ":" + seconds);
                    System.out.println("Polling from the queue:" + polling_of_queue);
                    break;
                }

                if (hashMap.containsKey(current_vertex.til_node)) { //from the last node
                    for (Vertex x : hashMap.get(current_vertex.til_node)) {
                        if (distance[x.til_node] == -1) {
                            queue.add(new Vertex(current_vertex.til_node, x.til_node, current_vertex.driving_time + x.driving_time));
                        }
                    }
                }

            }
        }

        int current = previous[to];
        ArrayList<Integer> result = new ArrayList<>();
        result.add(to);
        while (current != from) {
            result.add(current);
            current = previous[current];
        }
        result.add(from);

        return result;
    }

        /**
         * Alt algorithm for dijkstra node
         * @param from start node
         * @param to source node
         * @param landmark landmar node
         * @return shortest path using alt algorithm
         */
    public ArrayList<Integer> altAlg(int from, int to, int landmark) {
        int counter = 0;

        System.out.println("It is doing preprocesses");
        HashMap<Integer, Integer> distancesFromLandmark = dijkstraAlgFrom(landmark,from,to);
        int fromLandmarkToFinish = distancesFromLandmark.get(to);
        for (int x : distancesFromLandmark.keySet()){
            distancesFromLandmark.replace(x,distancesFromLandmark.get(x),Math.abs(fromLandmarkToFinish-distancesFromLandmark.get(x)));
        }

        System.out.println("Heuristic values is get");
        HashMap<Integer, Integer> distancesTilEveryNode = dijkstraAlgFrom(from,landmark,to);
        HashSet<Integer> in_queue = new HashSet<>();
        System.out.println("Distance til every node is counted");
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparing(Node::getSum));
        queue.add(nodes.get(from));
        in_queue.add(nodes.get(from).node_nr);
        Node current_node;
        HashMap<Integer,Integer> ALT = new HashMap<>();
        int i =0;
        long startTime = System.currentTimeMillis();
        while (queue.size() != 0) {
            current_node = queue.poll();
            in_queue.remove(current_node.node_nr);
            counter++;
            if (!ALT.containsKey(current_node.node_nr)) {
                ALT.put(current_node.node_nr,current_node.sum);
                if (current_node.node_nr == to) {
                    System.out.println("ALT algorithm:");
                    int seconds = current_node.getSum() / 100;
                    int hours = (seconds / 3600), min = (seconds / 60 % 60);
                    seconds %= 60;
                    System.out.println("Estimatite driving time: " + hours + ":" + min + ":" + seconds);
                    System.out.println("Polling from the queue:" + counter);
                    break;
                }
            }
            if (hashMap.containsKey(current_node.node_nr)) { //from the last node
                for (Vertex x : hashMap.get(current_node.node_nr)) {
                    if (!ALT.containsKey(x.til_node) && distancesFromLandmark.get(x.til_node) != null && distancesTilEveryNode.get(x.til_node) != null) {
                        if(!in_queue.contains(x.til_node)){
                            nodes.get(x.til_node).setSum(distancesTilEveryNode.get(x.til_node)+ distancesFromLandmark.get(x.til_node));
                            nodes.get(x.til_node).previous=current_node.node_nr;
                            queue.add(nodes.get(x.til_node));
                            in_queue.add(x.til_node);
                        }else{
                            if(nodes.get(x.til_node).sum>distancesTilEveryNode.get(x.til_node)+ distancesFromLandmark.get(x.til_node)){
                                queue.remove(nodes.get(x.til_node));
                                nodes.get(x.til_node).setSum(distancesTilEveryNode.get(x.til_node)+ distancesFromLandmark.get(x.til_node));
                                nodes.get(x.til_node).previous=current_node.node_nr;
                                queue.add(nodes.get(x.til_node));
                                in_queue.add(x.til_node);
                            }
                        }
                    }
                }
            }

        }
        long endTime = System.currentTimeMillis();
        System.out.println("That took " + (endTime - startTime) + " milliseconds ");

        int current = nodes.get(to).node_nr;
        ArrayList<Integer> result = new ArrayList<>();

        while (current != from) {
            result.add(current);
            current = nodes.get(current).previous;
        }
        result.add(from);
        return result;
    }



    /**
     * Special dijkstra for alt algorithm
     * There are two main differences of original dijkstra
     * it returns hash map
     * and end then it was found shortest path to node one and node two
     * @param from landmark
     * @param one node that should be found shortest path til
     * @param two node that should be found shortest path til
     * @return HashMap with distances til x. It holds information such as Node(key):DistanceTilValueFROMfromNode(value)
     */
    public HashMap<Integer,Integer> dijkstraAlgFrom(int from,int one,int two){

        HashMap<Integer,Integer> distance = new HashMap<>();


        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparing(Node::getSum));
        HashSet<Integer> in_queue= new HashSet<>();
        queue.add(nodes.get(from));
        Node current_node ;

        while (queue.size()!=0){
            current_node = queue.poll();
            in_queue.remove(current_node.node_nr);

            if (!distance.containsKey(current_node.node_nr)){ //if vi don`t have distance til previous node
                distance.put(current_node.node_nr, current_node.sum);
                if (distance.containsKey(one)&& distance.containsKey(two)) break;
                if(hashMap.containsKey(current_node.node_nr)){ //from the last node
                    for (Vertex x : hashMap.get(current_node.node_nr)){
                        if (!distance.containsKey(x.til_node)){
                            if (!in_queue.contains(x.til_node)){
                                nodes.get(x.til_node).setSum(x.driving_time+nodes.get(x.from_node).sum);
                                queue.add(nodes.get(x.til_node));
                                in_queue.add(x.til_node);
                            }else{
                                if (x.driving_time+nodes.get(x.from_node).sum<nodes.get(x.til_node).sum){
                                    queue.remove(nodes.get(x.til_node));
                                    nodes.get(x.til_node).setSum(x.driving_time+nodes.get(x.from_node).sum);
                                    queue.add(nodes.get(x.til_node));
                                    in_queue.add(x.til_node);
                                }
                            }
                        }
                    }
                }

            }
        }cleanAllNodes();
        return distance;
    }

        /**
         * Method to set all nodes` sum to zero
         */
    public void cleanAllNodes(){
        nodes.forEach(s->s.sum=0);
    }

        /**
         * Method to
         * Find 8 interess punkts
         * method uses my first variation of dijkstra
         * @param from from node
         * @param modificator that type of nodes to find
         * @return arraylist with 8 nodes of define type
         */
    public ArrayList<Node> findInteressPunkt(int from, String modificator){
        int counter = 0;
        LinkedHashMap<Integer,Integer> distance = new LinkedHashMap<Integer, Integer>();
        ArrayList<Node> result = new ArrayList<>(8);
        PriorityQueue<Vertex> queue = new PriorityQueue<>(Comparator.comparing(Vertex::getDriving_time));
        queue.add(new Vertex(from, from, 0));
        Vertex current_vertex ;
        while (queue.size()!=0 && counter <8){
            current_vertex = queue.poll();
            if (!distance.containsKey(current_vertex.til_node)){ //if vi don`t have distance til previous node
                distance.put(current_vertex.til_node,current_vertex.driving_time); //we put distance
                switch (modificator) {
                    case "name":{
                        if (nodes.get(current_vertex.til_node).testStedsNavn()) {
                            result.add(nodes.get(current_vertex.til_node));
                            counter++;
                        }
                        break;
                    }
                    case "benStasjon":{
                        if (nodes.get(current_vertex.til_node).testBensin()) {
                            result.add(nodes.get(current_vertex.til_node));
                            counter++;
                        }
                        break;
                    }
                    case "ladeStasjon":{
                        if (nodes.get(current_vertex.til_node).testLadeStasjon()) {
                            result.add(nodes.get(current_vertex.til_node));
                            counter++;
                        }
                        break;
                    }
                    case "spiseSted":{
                        if (nodes.get(current_vertex.til_node).testSpiseSted()) {
                            result.add(nodes.get(current_vertex.til_node));
                            counter++;
                        }
                        break;
                    }
                    case "drikkeSted":{
                        if (nodes.get(current_vertex.til_node).testDrikkeSted()) {
                            result.add(nodes.get(current_vertex.til_node));
                            counter++;
                        }
                        break;
                    }case "OverNattingSted":{
                        if (nodes.get(current_vertex.til_node).testOvernattingSted()) {
                            result.add(nodes.get(current_vertex.til_node));
                            counter++;
                        }
                        break;
                    }
                }
                if(hashMap.containsKey(current_vertex.til_node)){ //from the last node
                    for (Vertex x : hashMap.get(current_vertex.til_node)){
                        if (!distance.containsKey(x.til_node)){
                            queue.add(new Vertex(current_vertex.til_node, x.til_node, current_vertex.driving_time + x.driving_time));
                        }
                    }
                }

            }
        }return result;

    }

        /**
         * Add relationships between nodes
         * @param nameFile name of file with vertexes
         * @throws IOException
         */
    public void addVertexFromFile(String nameFile) throws IOException {
        File file = new File(nameFile).getAbsoluteFile();
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String data = bufferedReader.readLine();
        String[] ver;
        while (bufferedReader.ready()){
            data = bufferedReader.readLine();
            ver = data.trim().split("\\s+");
            addVertex(Integer.parseInt(ver[0]),Integer.parseInt(ver[1]),Integer.parseInt(ver[2]));
        }
        System.out.println("Success vertexes");

    }

        /**
         * Set nodes in graph
         * @param nodetxt nodes file with coordinates and its number
         * @param intertxt nodes file with it specification
         * @return ArrayList with nodes from file
         * @throws IOException
         */
    public ArrayList<Node> getNodesFromFiles(String nodetxt, String intertxt) throws IOException {
        File file = new File(nodetxt).getAbsoluteFile();
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String current = bufferedReader.readLine().trim();
        ArrayList<Node> result  = new ArrayList<Node>(Integer.parseInt(current));
        String[] ver;
        while (bufferedReader.ready()) {
            current=bufferedReader.readLine();
            ver=current.trim().split("\\s+");
            result.add(new Node(Integer.parseInt(ver[0]),Float.parseFloat(ver[1]),Float.parseFloat(ver[2])));
        }
        file = new File(intertxt).getAbsoluteFile();
        fileReader = new FileReader(file);
        bufferedReader = new BufferedReader(fileReader);
        current = bufferedReader.readLine().trim();
        while (bufferedReader.ready()) {
            current=bufferedReader.readLine();
            ver=current.trim().split("\\s+");
            result.get(Integer.parseInt(ver[0])).setCode(Integer.parseInt(ver[1]));
            result.get(Integer.parseInt(ver[0])).setName(ver[2]);
        }
        System.out.println("Suksess nodes");
        return result;

    }

    }
}

