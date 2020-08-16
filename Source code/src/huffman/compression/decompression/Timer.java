/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huffman.compression.decompression;

/**
 *
 * @author cfast
 */
public class Timer {
    private long startTime;
    private long endTime;
    private long elapsedTime;
    
    public void start (){
        startTime = System.currentTimeMillis();
    }
    
    public void end(){
        endTime = System.currentTimeMillis();
        elapsedTime = endTime-startTime;
    }
    
    long getElapsedTime(){
        return this.elapsedTime;
    }
}
