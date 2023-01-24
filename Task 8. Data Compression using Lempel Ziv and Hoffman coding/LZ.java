import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Class for solution oppgave 8. Implementation of
 * text compression that uses Lempel-Ziv and Huffman coding
 * @version 1.0
 * @author ivansh
 * @date 7.11.22
 */
public class LZ {
    /**
     * Main method for program
     * @param args arguments in command line
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        LZ le = new LZ();
        switch (args[0]){
            case "compress":{
                if (args.length==3) {
                        String l = le.createFromFile(args[1]);
                        String lz = le.encode(l);
                        le.compressHuffman(lz, args[2]);
                        break;
                }else if (args.length==4) {
                    if (args[1].equals("-lz")){
                        String l  = le.createFromFile(args[2]);
                        String lz = le.encode(l);
                        toFile(lz,args[3]);
                        System.out.println("Compressed according LZ:\n"+lz);
                        break;
                    }else if (args[1].equals("-hf")){
                        String l  = le.createFromFile(args[2]);
                        le.compressHuffman(l,args[3]);
                        break;
                    }else{
                        System.out.println("Not valid modificator");
                        break;
                    }
                }else {
                    System.out.println("Wrong format");
                    break;
                }
            } case "decompress": {
                if (args.length == 3) {
                        String lz = le.decompressHuffman(args[1]);
                        String l = le.decode(lz);
                        toFile(l, args[2]);
                        System.out.println("Decompressed file :\n" + l);
                        break;
                }else if (args.length==4){
                    if (args[1].equals("-lz")){
                        String lz  = le.createFromFile(args[2]);
                        String l = le.decode(lz);
                        toFile(l,args[3]);
                        System.out.println("Decompressed according LZ:\n"+l);
                        break;
                    }else if (args[1].equals("-hf")){
                        String l = le.decompressHuffman(args[2]);
                        toFile(l,args[3]);
                        System.out.println("Decompressed according Huffman:\n"+l);
                        break;
                    }else{
                        System.out.println("Not valid modificator");
                        break;
                    }
                }else {
                    System.out.println("Wrong format");
                    break;
                }
                }

            }
        }

    /**
     * Method which encodes string according Lempel-Ziv
     * @param s unencrypted string
     * @return encrypted string
     */
    public String encode(String s){
        String result = "", current = "",str = "";

        int start;
        for (int i = 0;i<s.length();i++){
            current+=s.charAt(i);
            if (current.matches("[a-zA-Z]")) {
                if (!str.contains(current)) {
                    result += current;
                } else {
                    start = i;
                    while (str.contains(current) && i < s.length() - 1) {
                        i++;
                        current += s.charAt(i);
                    }
                    String c= current.substring(0,current.length()-1);
                    int index = str.indexOf(c);
                    String brackets = "[" + (start-index) + "," + (i - start) + "]" + s.charAt(i);
                    if (brackets.length()<c.length()) result+=brackets;
                    else result += current;

                }
            }else {
                result += current;
            }
            str+=current;
            current = "";
        }
    

        return result;
    }

    /**
     * Create string from a file
     * @param pathName name of file in current directory
     * @return string file
     * @throws IOException
     */
    public String createFromFile(String pathName) throws IOException {
        DataInputStream inputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(pathName)));
        byte[] data = inputStream.readAllBytes();
        String s = new String(data, StandardCharsets.UTF_8);
        return s;
    }

    /**
     * Method to decode string that was encoded according Lempel-Ziv before
     * @param s encrypted according Lempel-Ziv
     * @return unencrypted string
     */
    public String decode(String s){
        String result= "";
        for (int i =0 ;i<s.length();i++){
            if (String.valueOf(s.charAt(i)).equals("[")){
                String brackets = "";
                while (!String.valueOf(s.charAt(i)).equals("]")){
                    brackets+=s.charAt(i);
                    if (i+1!=s.length())
                        i++;
                    else{
                        result+=brackets;
                        return result;
                    }

                }
                brackets=brackets.substring(1);

                String[] values = brackets.split(",");
                if (values.length==2 && values[0].matches("^[0-9]*$") && values[1].matches("^[0-9]*$") ) {
                        int[] value = new int[]{Integer.parseInt(values[0]), Integer.parseInt(values[1])};
                    int l = result.length();
                    int v = l- value[0];
                    String substring = result.substring(v, v + value[1]);
                    result += substring;
                }else result+="["+brackets + "]";


            }else result+=s.charAt(i);
        }


    return result;
    }

    /**
     * Method which writes text to file
     * @param data text that should be written
     * @param pathName name of file
     */
    public static void toFile(String data,String pathName){
        byte ptext[] = pathName.getBytes();
        String value = pathName;
        try {
            value = new String(ptext, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        File myObj = new File(value).getAbsoluteFile();
        Path path = myObj.toPath();

        try {
            Files.writeString(path,data, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Class Node for tree for huffman coding
     */
    class Node{

        int value; //how many repeats in text
        char c; // symbol

        Node left; //left child
        Node right; //right child

        public Node() {
        }

        public int getValue(){
            return value;
        }
    }

    /**
     * Compress text according to Huffman and write to file after it
     * @param s string to encode
     * @param to_file file name
     * @throws IOException
     */
    public void compressHuffman(String s, String to_file) throws IOException {
        byte[] b = s.getBytes(Charset.forName("UTF-8"));
        int[] frequency = new int[256];
        ArrayList<Byte> byteArrayList = new ArrayList<>();
        String[] bitstring = new String[256];
        for (int i : b) {
            if (i < 0) frequency[256 + i]++;
            else frequency[i]++;
        }

        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparing(Node::getValue));

        for (int i = 0; i < frequency.length; i++) {
            if (frequency[i] != 0) {
                Node huffman = new Node();
                huffman.left = null;
                huffman.right = null;
                huffman.value = frequency[i];
                huffman.c = (char) i;
                pq.add(huffman);
            }
        }

        while (pq.size() != 1) {
            Node one = pq.poll();

            Node two = pq.poll();

            Node rootOftheseTwo = new Node();
            rootOftheseTwo.value = one.value + two.value;
            rootOftheseTwo.c = '\0';
            rootOftheseTwo.left = one;
            rootOftheseTwo.right = two;

            pq.add(rootOftheseTwo);
        }

        Node root = pq.peek();
        codeHuffman(root, "", bitstring);

        DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(to_file));

        for (int i : frequency) {
            outputStream.writeInt(i);
        }

        int in , length = 0, j ;
        long current = 0L;

        for (int i = 0; i < b.length; i++) {
            in = b[i];
            if (in < 0){
                in += 256;
            }

            String str = bitstring[in];
            j = 0;

            while (j < str.length()) {
                if (str.charAt(j) == '0') current = (current << 1);
                else  current = ((current << 1) | 1);

                j++;
                length++;

                if (length == 8) {
                    byteArrayList.add((byte) current);
                    length = 0;
                    current = 0L;
                }
            }
        }

        while (length < 8 && length != 0) {
            current = (current << 1);
            length++;
        }
        byteArrayList.add((byte) current);

        for (Byte byt : byteArrayList){
            outputStream.write(byt);}


        outputStream.close();
    }

    /**
     * Decompress crypted with huffman text
     * @param file_in file to decrypt
     * @return decrypted string
     * @throws IOException
     */
    public String decompressHuffman(String file_in) throws IOException {
        DataInputStream inputStream = new DataInputStream(new FileInputStream(file_in));
        int[] frequency = new int[256];

        for (int i = 0; i < frequency.length; i++) {
            int j = inputStream.readInt();
            frequency[i] = j;
        }

        ArrayList<Byte> out = new ArrayList<>();


        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparing(Node::getValue));

        for (int i = 0; i < frequency.length; i++) {
            if (frequency[i] != 0) {
                Node huffman = new Node();
                huffman.left = null;
                huffman.right = null;
                huffman.value = frequency[i];
                huffman.c = (char) i;
                pq.add(huffman);
            }
        }

        while (pq.size() != 1) {
            Node one = pq.poll();

            Node two = pq.poll();

            Node rootOftheseTwo = new Node();
            rootOftheseTwo.value = one.value + two.value;
            rootOftheseTwo.c = '\0';
            rootOftheseTwo.left = one;
            rootOftheseTwo.right = two;

            pq.add(rootOftheseTwo);
        }

        Node root = pq.peek();

        byte[] inn  = inputStream.readAllBytes();

        inputStream.close();

        BitString  bitString = new BitString(0,0);



        for (int i = 0 ;i<inn.length;i++){
            byte b = inn[i];
            BitString bitString1 = new BitString(8,b);
            bitString = bitString.connect(bitString1);
            bitString = getNodeCharacter(root, bitString, out);
        }


        byte[] bytes = new byte[out.size()];

        for (int i = 0; i < out.size(); i++) {
            bytes[i] = out.get(i);
        }
        String s = new String(bytes, StandardCharsets.UTF_8);
        return s;
    }

    /**
     * Method that return BitString from a node of huffman tree
     * @param huffmanNode huffman node
     * @param bitStr BitString which will be returned during decompression
     * @param bytes all bytes
     * @return modified bitStr
     */
    public BitString getNodeCharacter(Node huffmanNode, BitString bitStr, ArrayList<Byte> bytes){
        Node tempHuffmanNode = huffmanNode;
        int s = 0;

        for (long i = 1 << bitStr.length - 1; i != 0; i >>= 1) {
            s++;
            if ((bitStr.bits & i) == 0) {
                tempHuffmanNode = tempHuffmanNode.left;
            }
            else {
                tempHuffmanNode = tempHuffmanNode.right;
            }
            if (tempHuffmanNode.left == null) {
                long val = tempHuffmanNode.c;
                bytes.add((byte) val);

                long current = -1;
                bitStr.bits = bitStr.bits & current;
                bitStr.length = bitStr.length - s;
                s = 0;
                tempHuffmanNode = huffmanNode;
            }
        }
        return bitStr;
    }

    /**
     * Class to hold string of bits
     */
     class BitString{
        int length;
        long bits;

        /**
         * Constructor if we have length and bits as long
         * @param length length of bitstring
         * @param bits bits in string as long
         */
        public BitString(int length, long bits) {
            this.length = length;
            this.bits = bits;
        }

        /**
         * Constructor if we have length and bits as byte
         * @param length length of bitstring
         * @param bits bits in string as byte
         */
         public BitString(int length, byte bits) {
             this.length = length;
             this.bits =Byte.toUnsignedLong(bits);
         }

        /**
         * Connect bitstring two to bitstring one
         * @param b2 bitstring we wanted to connect
         * @return bitstring one connected with bitstring two
         */
        public BitString connect(BitString b2){
            int length = this.length + b2.length;
            long bit = b2.bits | (this.bits << b2.length);
            if (length > 64){
                throw new IllegalArgumentException("String must be less then 64 digits ");
            }
            return new BitString(length, bit);
        }

    }

    /**
     * Method to create the bytes in binary tree according
     * Huffman then Node with the most frequency min digits
     * we goes from the top to the low (0 if left, 1 if right)
     * @param root from which node we are going
     * @param str string for nodes
     * @param bitString array with huffman codes
     */
    public void codeHuffman(Node root,String str, String[] bitString){
        if (root == null){
            return ;
        }
        if (root.left==null && root.right==null){
            bitString[root.c] = str;
        }
        codeHuffman(root.left,str+"0",bitString);
        codeHuffman(root.right,str+"1",bitString);


    }






}
