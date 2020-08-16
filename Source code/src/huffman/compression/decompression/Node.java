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
public class Node implements Comparable<Node>{

    private char key;
    private int value;
    private Node leftNode;
    private Node rightNode;
    
    public Node(char key, int value, Node leftNode, Node rightNode){
        this.key = key;
        this.value = value;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }
    
    public Character getKey(){
        return this.key;
    }
    
    public Integer getValue(){
        return this.value;
    }
    
    @Override
    public int compareTo(Node other) {
        return this.getValue().compareTo(other.getValue());
    }
    
    public Node getLeftNode(){
        return this.leftNode;
    }
    
    public Node getRightNode(){
        return this.rightNode;
    }
    
}
