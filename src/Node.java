public class Node {
    int x, y;
    
    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Node(Node other){
        this.x = other.x;
        this.y = other.y;
    }

     public boolean equals(Node other){
        return this.x == other.x && this.y == other.y;
}}
