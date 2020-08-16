/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huffman.compression.decompression;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

/**
 *
 * @author cfast
 */
public class Compressor {
    private File file;
    private HashMap<Character, Integer> frequency = new HashMap();
    private FileReader inputStream = null;
    private PriorityQueue<Node> huffmanTree = new PriorityQueue<Node>();
    private HashMap<String, String> newCode = new HashMap();
    private  FileOutputStream fos = null;


    Compressor(File file) throws FileNotFoundException, IOException{
        this.file = file;
        
        inputStream = new FileReader(file);
        fos = new FileOutputStream(file.getName().replace(".txt", "")+".comp");
    }
    
    
    void calculateFrequency() throws IOException{
        int c;
        while((c = inputStream.read()) != -1){
            if(frequency.containsKey((char)c)){
               
                frequency.put((char)c, frequency.get((char)c)+1);
            }else{
                frequency.put((char)c, 1);
                
            }
        }
        //System.out.println(frequency);
    }
    
    
    void createTree(){
            for (char i : frequency.keySet()){
                huffmanTree.add(new Node(i, frequency.get(i), null, null));
            }
            if(huffmanTree.size()==1){
                newCode.put(Integer.toBinaryString(huffmanTree.poll().getKey()), "1");
                return;
            }
            Node root = null;
            for(int i=1; i<frequency.size();i++){
                Node nodeL, nodeR;
                nodeL = huffmanTree.poll();
                nodeR = huffmanTree.poll();
                Node parentNode = new Node('\0', nodeL.getValue()+nodeR.getValue(), nodeL, nodeR);
                root = parentNode;
                huffmanTree.add(parentNode);
            }
            //.out.println(frequency);
            findCode(root, "");
    }

    void compress() throws IOException{
        calculateFrequency();
        createTree();
        createCompressedFile();
        //System.out.println(newCode);
    }
    
    
    void createCompressedFile() throws FileNotFoundException, IOException{
        
        
        
        StringBuilder sb = new StringBuilder();
            inputStream = new FileReader(file);
            //System.out.println("FILE SIZE: "+ file.length());
        int c;
         while((c = inputStream.read()) != -1){
             sb.append(newCode.get(Integer.toBinaryString((char)c)));
             
             //System.out.println((char)c);

        }
        // System.out.println("SIZE: " + sb.length());
         String EOF;
         if(sb.length()%8==0){
             EOF = "T";
         }else{
             EOF = "F";
         }
        // fos.write(((newCode.toString())+EOF+"\n").getBytes());
         
         for(String x : newCode.keySet()){
             fos.write((x+" "+newCode.get(x)+" ").getBytes());
            
         }
          fos.write((EOF+"\n").getBytes());
         //sb.append(File.separatorChar);
         //sb.append('\0');
  
         //System.out.println(sb);
         //byte[] codedData = String.valueOf(sb).getBytes();
         //byte[] codedData = new BigInteger(String.valueOf(sb), 2).toByteArray();
           byte[] codedData = getByteByString(String.valueOf(sb));
         
         
         //System.out.println("FINISHED");
        // System.out.println(Arrays.toString(codedData));
         fos.write(codedData);
         fos.flush();
         fos.close();
         
       
       

    
    }
    public void findCode(Node root, String s) 
    { 
        if (root.getLeftNode() == null && root.getRightNode() == null) { 
  
            //System.out.println((char)Integer.parseInt(Integer.toBinaryString(root.getKey()), 2)+ ": "+s); 
            newCode.put(Integer.toBinaryString(root.getKey()), s);
  
            return; 
        } 
  
        findCode(root.getLeftNode(), s + "0"); 
        findCode(root.getRightNode(), s + "1"); 
    }

    public byte[] getByteByString(String binaryString) throws IOException {
        int splitSize = 8;
            

        if(binaryString.length() % splitSize == 0){ 
            int index = 0;
            int position = 0;

            byte[] resultByteArray = new byte[binaryString.length()/splitSize];
            StringBuilder text = new StringBuilder(binaryString);

            while (index < text.length()) {
                String binaryStringChunk = text.substring(index, Math.min(index + splitSize, text.length()));
                Integer byteAsInt = Integer.parseInt(binaryStringChunk, 2);
                resultByteArray[position] = byteAsInt.byteValue();
                index += splitSize;
                position ++;
            }
            return resultByteArray;
        }
        else{
            fos.write((binaryString.substring(binaryString.length()-binaryString.length() % splitSize, binaryString.length())+"\n").getBytes());
            //System.out.println("Cannot convert binary string to byte[], because of the input length. '" +binaryString+"' % 8 != 0");
            //System.out.println(binaryString.length() % splitSize);
            binaryString = binaryString.substring(0, binaryString.length() - binaryString.length() % splitSize);
           return getByteByString(binaryString);
           
        }
    }
}   
