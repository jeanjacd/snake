public class Node {
    int x, y;
    
    public Node(int x, int y) { //casual constructor
        this.x = x;
        this.y = y;
    }

    public Node(Node other){ //other constructor for food comparisons
        this.x = other.x;
        this.y = other.y;
    }

     public boolean equals(Node other){ // quick equals method
        return this.x == other.x && this.y == other.y;
}}
