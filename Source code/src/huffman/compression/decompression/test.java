/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huffman.compression.decompression;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

/**
 *
 * @author cfast
 */
public class test {
     
    public static void main(String args[]) throws FileNotFoundException, IOException{
             FileOutputStream test = new FileOutputStream("test.txt");

        byte[] codedData = getByteByString("0110000101100010");
        System.out.println(codedData);
        test.write(codedData);
    }
    
    
    public static byte[] getByteByString(String binaryString) throws IOException {
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
            //System.out.println("Cannot convert binary string to byte[], because of the input length. '" +binaryString+"' % 8 != 0");
            //System.out.println(binaryString.length() % splitSize);
            binaryString = binaryString.substring(0, binaryString.length() - binaryString.length() % splitSize);
           return getByteByString(binaryString);
           
        }
    }
}
