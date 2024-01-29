import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.RenderingHints.Key;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.Timer;

public class Snake extends JPanel implements KeyListener, ActionListener {
    private Node[] snake; //snake body in array form, still using my node class
    private int length = 1; //snake length
    private int direction; //direction of snake
    private Node food; //i made food a node because it's basically just a compartmentalized x and y, no need for a 2d array
    private boolean canChangeDirection = true; //boolean to prevent the snake from turning too viciously between time frames
    private static final int SIZE = 20, WIDTH = 20, HEIGHT = 20;
    private Timer timer;

    public Snake() {
        setPreferredSize(new Dimension(WIDTH * SIZE, HEIGHT * SIZE));
        this.addKeyListener(this);
        this.setFocusable(true);
        snake = new Node[WIDTH * HEIGHT];
        snake[0] = new Node(WIDTH / 2, HEIGHT / 2);
        foodSpawn();
        timer = new Timer(100, this);
        timer.start();
    }

    private void foodSpawn() { //spawns food in a random location after its consumed by snake
        Random rand = new Random();
        do {
            int x = rand.nextInt(WIDTH);
            int y = rand.nextInt(HEIGHT);
            food = new Node(x, y);
        } while (snakeIn(food));
    }

    private boolean snakeIn(Node n) { //checks if the snake is in a certain node for food detection, equals is something i made for Node class
        for (int i = 0; i < length; i++) {
            if (snake[i].equals(n)) {
                return true;
            }
        }
        return false;
    }

    public void actionPerformed(ActionEvent e) { //this is the main game loop, it's called every time the timer ticks
        canChangeDirection = true;
        Node head = new Node(snake[0]);

        switch (direction) { //i used some very simple switch statements instead of if statements just for cleanliness (updates direction)
            case KeyEvent.VK_W:
                head.y--;
                break;
            case KeyEvent.VK_S:
                head.y++;
                break;
            case KeyEvent.VK_A:
                head.x--;
                break;
            case KeyEvent.VK_D:
                head.x++;
                break;
        }

        if(head.equals(food)){
            System.arraycopy(snake, 0, snake, 1, length); //this is a very useful method that copies an array from one index to another
            snake[0] = food; //it's used here to move the snake forward
            length++; //increase length
            foodSpawn(); //spawn new food
        }
        else{
            System.arraycopy(snake, 0, snake, 1, length); //same thing here, but without the food
            snake[0] = head; //move snake forward
        }

        if(head.x < 0 || head.x >= WIDTH || head.y < 0 || head.y >= HEIGHT){ //check if snake is out of bounds
            timer.stop();
            JOptionPane.showMessageDialog(null, "You lose!");
            System.exit(0);
        }

        for(int i = 0; i < length; i++){ //check if snake is eating itself
            if(snake[i].equals(head) && i != 0){
                timer.stop();
                JOptionPane.showMessageDialog(null, "You lose!");
                System.exit(0);
            }
        }

        repaint();

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
       if (canChangeDirection){
        if (e.getKeyCode() == KeyEvent.VK_W && direction != KeyEvent.VK_S) { //check if the snake is trying to go in the opposite direction
            direction = KeyEvent.VK_W;
        } else if (e.getKeyCode() == KeyEvent.VK_S && direction != KeyEvent.VK_W) { //if it is, don't let it
            direction = KeyEvent.VK_S;
        } else if (e.getKeyCode() == KeyEvent.VK_A && direction != KeyEvent.VK_D) { 
            direction = KeyEvent.VK_A;
        } else if (e.getKeyCode() == KeyEvent.VK_D && direction != KeyEvent.VK_A) {
            direction = KeyEvent.VK_D;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {  //custom boosting method, i made it so that the snake can go faster if you hold space
            timer.setDelay(50); // Using half the normal delay for double speed (also my special feature)
        }
        canChangeDirection = false;
       }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            timer.setDelay(100); // Reset the delay to normal
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.LIGHT_GRAY); //custom checkerboard that alternates between white and gray, a little redundant but it looks nice
        for(int i = 0; i < HEIGHT; i++){
            for(int j = 0; j < WIDTH; j++){
                int x = j * SIZE;
                int y = i * SIZE;

                if((i + j) % 2 == 0){
                    g.setColor(Color.LIGHT_GRAY);
                } else {
                    g.setColor(Color.WHITE);
                }

                g.fillRect(x + 1, y + 1, SIZE - 2, SIZE - 2); //i used a 1 pixel offset to make the checkerboard look nicer
            }
        }
       for(int i = 0; i < length; i++){ //draw the snake
           Node n = snake[i]; //i used a node class for the snake because it's easier to work with than a 2d array
           if(n != null){ 
            g.setColor(Color.BLACK);
            g.fillRect(n.x * SIZE, n.y * SIZE, SIZE, SIZE);
            g.setColor(Color.RED);
            g.fillRect(n.x * SIZE + 1, n.y * SIZE + 1, SIZE - 2, SIZE - 2); //same thing here, 1 pixel offset
           }
       }

       g.setColor(Color.BLACK); //draw the food
       g.fillOval(food.x * SIZE, food.y * SIZE, SIZE, SIZE);
    }


    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("Snake");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new Snake(), BorderLayout.CENTER);
        frame.pack();   
        frame.setLocationRelativeTo(null);
        frame.setVisible(true); //create the frames

    }
}
