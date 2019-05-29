import java.io.*;
import java.util.*;

import static java.util.stream.Collectors.toMap;

public class AllParts {

    public static String textFile="C:/Users/Тогжан/Desktop/IT project/Text.txt";
    public static String encryptedFile="C:/Users/Тогжан/Desktop/IT project/EncText.txt";
    public static String decryptedFile="C:/Users/Тогжан/Desktop/IT project/DecText.txt";
    public static String HammingFile="C:/Users/Тогжан/Desktop/IT project/HammingText.txt";
    public static HashMap<Character,Integer> hashMap = new HashMap<Character, Integer>();
    public static HashMap<String,Character> hashMapToFile = new HashMap<String, Character>();
    public static String source_text = "";
    public static String ss = "";
    public static String hamming_text = "";

    static class HuffmanNode {
        int data;
        char c;

        HuffmanNode leftNode;
        HuffmanNode rightNode;
    }
    //Comparator class helps us to campare two nodes
    static class MyComparator implements Comparator<HuffmanNode> {
        public int compare(HuffmanNode x, HuffmanNode y) {
            return x.data - y.data;
        }
    }

    //recursive function to print huffman code through the tree
    static void printCode(HuffmanNode root, String s){
        if(root.leftNode == null && root.rightNode == null && Character.isDefined(root.c)){
            System.out.println(root.c + ":" + s);
            ss+=s;
            hashMapToFile.put(s,root.c);
            return;
        }
        printCode(root.leftNode, s + "0");
        printCode(root.rightNode, s + "1");
    }


    static void sorted(){
        source_text = readFromFile(textFile);
        int length = source_text.length();
        for(int i=0; i<length;i++){
            int count = 0;
            for(int j=0;j<length;j++){
                if(source_text.charAt(i)==source_text.charAt(j)){
                    count++;
                }
            }
            hashMap.put(source_text.charAt(i), count);
        }

        //for sorting sort by value
        hashMap = hashMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,LinkedHashMap::new));
        for (Map.Entry entry : hashMap.entrySet()){
            System.out.println("Key: " + entry.getKey() + " - Freq: " + entry.getValue());
        }
    }

    static void encodeHuffman(){
        int n = hashMap.size();
        char[] charArray = new char[n];
        int[] charFreq = new int[n];

        int count = 0;
        for (Map.Entry entry : hashMap.entrySet()){
            charArray[count] = (char) entry.getKey();
            charFreq[count] = (int) entry.getValue();
            count++;
        }

        PriorityQueue<HuffmanNode> q = new PriorityQueue<HuffmanNode>(n, new MyComparator());

        for (int i = 0; i < n; i++) {
            HuffmanNode node = new HuffmanNode();
            node.c = charArray[i];
            node.data = charFreq[i];

            node.leftNode = null;
            node.rightNode = null;

            q.add(node);
        }

        HuffmanNode root = null;
        while (q.size() > 1){
            HuffmanNode x = q.peek();
            q.poll();

            HuffmanNode y = q.peek();
            q.poll();

            HuffmanNode f = new HuffmanNode();
            f.data = x.data + y.data;
            f.c = '-';

            f.leftNode = x;
            f.rightNode = y;
            root = f;
            q.add(f);

        }
        printCode(root, "");

        String encrypted_text = "";
        int c = 0;
        while (c<=source_text.length()-1){
            for (Map.Entry entry : hashMapToFile.entrySet()){
                if(source_text.charAt(c)==(char)entry.getValue()){
                    encrypted_text+=entry.getKey();
                    break;
                }
            }
            c++;
        }
        writeIntoFile(encrypted_text,encryptedFile);


    }

    static String decodeHuffman(){
        String encrypted_text = readFromFile(encryptedFile);
        String decrypted_text = "";
        String temp = "";
        int count = 0;
        while (count<=encrypted_text.length()-1){
            temp += encrypted_text.charAt(count);
            for (Map.Entry entry : hashMapToFile.entrySet()){
                if(temp.equals(entry.getKey())){
                    decrypted_text+=entry.getValue();
                    temp="";
                }
            }
            count++;
        }
        writeIntoFile(decrypted_text,decryptedFile);
        return decrypted_text;
    }

    static void HammingEncode(){
        String b_text = readFromFile(encryptedFile);
        Boolean[] arr = new Boolean[7];
        Arrays.fill(arr, Boolean.FALSE);
        for (int i = 0; i < b_text.length() - 3; i+=4) {
            arr[0] = b_text.charAt(i) == '1';
            arr[1] = b_text.charAt(i+1) == '1';
            arr[2] = b_text.charAt(i+2) == '1';
            arr[3] = b_text.charAt(i+3) == '1';
            arr[4] = arr[0]^arr[1]^arr[2];
            arr[5] = arr[1]^arr[2]^arr[3];
            arr[6] = arr[0]^arr[1]^arr[3];
            writeIntoString(arr);
        }
        writeIntoFile(hamming_text, HammingFile);
    }

    static void writeIntoString(Boolean arr[]){
        for (int i = 0; i < arr.length ; i++) {
            hamming_text += arr[i] == false ? 0 : 1;
        }
    }

    public static void main(String[] args){
        sorted();
        encodeHuffman();
        writeIntoFile(ss,encryptedFile);
        decodeHuffman();
        HammingEncode();
    }




    public static String readFromFile(String file){
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                contentBuilder.append(sCurrentLine).append("\n");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }
    public static void writeIntoFile(String encrypted, String text){
        try {
            File newTextFile = new File(text);
            FileWriter fw = new FileWriter(newTextFile);
            fw.write(encrypted);
            fw.close();
        } catch (Exception R){
            R.printStackTrace();
        }
    }

}
