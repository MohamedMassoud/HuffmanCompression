/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huffman.compression.decompression;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author cfast
 */
public class Decompressor {
    
    private File file;
    private FileReader inputStream = null;
    private HashMap<String, String> codeMap = new HashMap();
    private BufferedReader br = null;
    private String extraBits = null;
    private String EOF = null;
    private StringBuilder sb;
    private StringBuilder originalCode;
    private FileOutputStream fos = null;
    private File tempFile;

    Decompressor(File file) throws FileNotFoundException, UnsupportedEncodingException{
        this.file = file;
        inputStream = new FileReader(file);
        br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        sb = new StringBuilder();
        originalCode = new StringBuilder();
        fos = new FileOutputStream(file.getName().replace(".comp", ".txt"));
    }
    
    public void decompress() throws IOException{
        createMap();
        createDecompressedFile();
        tempFile.delete();
    }
    
    void createDecompressedFile() throws IOException{
        char charArr[] = sb.toString().toCharArray();
        
        // System.out.println("SIZE: " + originalCode.length());
         
         StringBuilder temp = new StringBuilder();
         
         for(char x : charArr){
             temp.append(x);
             //System.out.println(temp);
             if(codeMap.containsKey(temp.toString())){
                 //System.out.println(temp.toString()+": "+(char)Integer.parseInt(codeMap.get(temp.toString()),2)+": "+ (codeMap.get(temp.toString())));
                 fos.write((char)Integer.parseInt((codeMap.get(temp.toString())), 2));
                 temp = new StringBuilder();
             }
        }
    }
    
    void createMap() throws IOException{
        String mapStr = br.readLine();
        
        String[] splited = mapStr.split("\\s+");
        for(int i=0; i<splited.length;i++){
            if(!"T".equals(splited[i])&&!"F".equals(splited[i])){
                codeMap.put(splited[i+1], splited[i]);
                i++;
            }else{
                EOF = splited[i];
                if("F".equals(EOF)){
                    extraBits=br.readLine();
                 //   System.out.println(extraBits);
                }
            }
        }
        /*String line = br.readLine();
        
        while(line!=null){
            sb.append(line);
            line = br.readLine();
        }
        sb.append(extraBits);*/
        tempFile = new File("temp.txt");
        copyFileUsingFileStreams(file, tempFile);
        removeFirstLine(tempFile);
        try{
            if(EOF.equals("F"))removeFirstLine(tempFile);
        }catch(Exception e){
        }
        
        
        byte[] bytes = Files.readAllBytes(tempFile.toPath());
        //System.out.println("DATA: " + Arrays.toString(bytes));
        for(byte x : bytes)
        sb.append(Integer.toBinaryString((x & 0xFF) + 0x100).substring(1));
        sb.append(extraBits);
        //System.out.println(sb);
        //String text = new String(bytes, "UTF-8");
       //  chars = text.toCharArray();
        // System.out.println(chars);
       // System.out.println(sb.length());
       // System.out.println(Arrays.toString(bytes));
       // System.out.println(codeMap);
       // System.out.println(EOF);
        //System.out.println(extraBits);
    }
    
   public  void removeFirstLine(File file) throws IOException {  
    RandomAccessFile raf = new RandomAccessFile(file, "rw");          
     //Initial write position                                             
    long writePosition = raf.getFilePointer();                            
    raf.readLine();                                                       
    // Shift the next lines upwards.                                      
    long readPosition = raf.getFilePointer();                             

    byte[] buff = new byte[1024];                                         
    int n;                                                                
    while (-1 != (n = raf.read(buff))) {                                  
        raf.seek(writePosition);                                          
        raf.write(buff, 0, n);                                            
        readPosition += n;                                                
        writePosition += n;                                               
        raf.seek(readPosition);                                           
    }                                                                     
    raf.setLength(writePosition);                                         
    raf.close();                                                          
    }
   
   
   
   private static void copyFileUsingFileStreams(File source, File dest)

        throws IOException {

    InputStream input = null;

    OutputStream output = null;

   try {

        input = new FileInputStream(source);

        output = new FileOutputStream(dest);

        byte[] buf = new byte[1024];

        int bytesRead;

        while ((bytesRead = input.read(buf)) > 0) {

            output.write(buf, 0, bytesRead);

        }

    } finally {

        input.close();

        output.close();

    }

}

}

