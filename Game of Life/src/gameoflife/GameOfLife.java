
package gameoflife;

import java.io.*;
import java.util.Scanner;
import java.awt.*; //needed for graphics
import java.awt.image.BufferedImage;
import javax.swing.*; //needed for graphics
import static javax.swing.JFrame.EXIT_ON_CLOSE; //needed for graphics

public class GameOfLife extends JFrame {

    //FIELDS
    int numGenerations = 100000;
    int currGeneration = 1;
    
    Color aliveColor = Color.white;
    Color deadColor = Color.black;
    
    String fileName = "presets3.txt";
    //0 - empty
    //1 - random runner
    //2 - flip flop
    //3 - glider gun
    //4 - lightweight spaceship
    

    int width = 989; //width of the window in pixels
    int height = 748;
    int borderWidth = 50;

    int numCellsX = 100; //width of the grid (in cells)
    int numCellsY = 70;

    boolean alive[][] = new boolean[numCellsY][numCellsX]; //REPLACE null WITH THE CORRECT DECLARATION 
    boolean aliveNext[][] = new boolean[numCellsY][numCellsX]; //REPLACE null WITH THE CORRECT DECLARATION
    
    int cellWidth = 1000/numCellsX; //replace with the correct formula that uses width, borderWidth and numCellsX
 

    //METHODS
    public void plantFirstGeneration() throws IOException {
        makeEveryoneDead();
        plantFromFile( fileName );
        
        //OR plant the first generation systematically using a pattern, using
        //one of these methods, for example:
        
        //plantGlider(2,2,"SE");
        //plantGlider(70,2,"SW");
        //plantGlider(50,50,"NW");
        //plantGlider(10,50,"NE");
        /*plantBlock(10,25,20,7);
        plantBlock(15,25,7,8);
        plantBlock(50,50,10,10);
        plantBlock(5,10,3,4);*/
        
    }

    
    //Sets all cells to dead
    public void makeEveryoneDead() {
        for (int i = 0; i < numCellsY; i++) {
            for (int j = 0; j < numCellsX; j++) {
                alive[i][j] = false;
            }
        }
    }

    
    //reads the first generations' alive cells from a file
    public void plantFromFile(String fileName) throws IOException {

        FileReader f = new FileReader(fileName);
        Scanner s = new Scanner(f);

        int x, y;

        while ( s.hasNext() ) {
            x = s.nextInt();
            y = s.nextInt();
            alive[y][x] = true;
            
        }
    }

    //get rid of the flashing
    private Image renderImage(){
        int x=0, y=50, i, j;
        BufferedImage bi = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.getGraphics();
        
        
        for (i = 0; i < numCellsY; i++) {
            for (j = 0; j < numCellsX; j++) { 
                if (alive[i][j]==true){
                    g.setColor(aliveColor);
                } else{
                    g.setColor(deadColor);
                }
                g.fillRect(x,y,cellWidth,cellWidth);
                x += cellWidth;
            }
            x=0;
            y += cellWidth;
        }
        
        
        drawLabel(g, currGeneration);
        currGeneration++;
        return bi;
    }
    
    public void paint(Graphics g){
        Image img = renderImage(); //smoothes out everything
        g.drawImage(img, 0, 20, this);
    }
    
    //Plants a solid rectangle of alive cells.  Would be used in place of plantFromFile()
    public void plantBlock(int startX, int startY, int numColumns, int numRows) {
        
        int endCol = Math.min(startX + numColumns, numCellsX);
        int endRow = Math.min(startY + numRows, numCellsY);

        for (int i = startX; i < endCol; i++) {
            for (int j = startY; j < endRow; j++) {
                alive[i][j] = true;
            }
        }
    }

    
    //Plants a "glider" group, which is a cluster of living cells that migrates across the grid from 1 generation to the next
    public void plantGlider(int startX, int startY, String direction) { //direction can be "SW", "NW", "SE", or "NE"
        if (direction.equals("SE")){
            alive[startY][startX+1] = true;
            alive[startY+1][startX+2] = true;
            alive[startY+2][startX] = true;
            alive[startY+2][startX+1] = true;
            alive[startY+2][startX+2] = true;
        } else if (direction.equals("SW")){
            alive[startY][startX+1] = true;
            alive[startY+1][startX] = true;
            alive[startY+2][startX] = true;
            alive[startY+2][startX+1] = true;
            alive[startY+2][startX+2] = true;
        } else if (direction.equals("NW")){
            alive[startY][startX] = true;
            alive[startY][startX+1] = true;
            alive[startY][startX+2] = true;
            alive[startY+1][startX] = true;
            alive[startY+2][startX+1] = true;
        } else if (direction.equals("NE")){
            alive[startY][startX] = true;
            alive[startY][startX+1] = true;
            alive[startY][startX+2] = true;
            alive[startY+1][startX+2] = true;
            alive[startY+2][startX+1] = true;
        }
    }

     
    //Applies the rules of The Game of Life to set the true-false values of the aliveNext[][] array,
    //based on the current values in the alive[][] array
    public void computeNextGeneration() {        
        //Count every cell
        for(int i=0;i<numCellsY;i++){
            for(int k=0;k<numCellsX;k++){
                int neighbors = countLivingNeighbors(i,k);
                
                //Game of Life Rules
                if (alive[i][k]==true){
                    if ((neighbors<2)||(neighbors>3)){
                        aliveNext[i][k] = false;

                    } else{
                        aliveNext[i][k] = true;
                    }
                } else{
                    if (neighbors==3){
                        aliveNext[i][k] = true;
                    }
                }
            }
        }
    }

    
    //Overwrites the current generation's 2-D array with the values from the next generation's 2-D array
    public void plantNextGeneration() {
        for(int i=0;i<numCellsY;i++){
            for(int k=0;k<numCellsX;k++){
                alive[i][k] = aliveNext[i][k];
            }
        }
    }

    
    //Counts the number of living cells adjacent to cell (i, j)
    public int countLivingNeighbors(int i, int j) {
        int k,l,count;
        //Set initial count value
        if(alive[i][j]==true){
            count = -1;     //set count to -1 if alive since it will count itself
        } else{
            count = 0;      //set count to 0 if dead
        }
        
        
        if (i!=0&&j!=0&&i!=69&&j!=99){  //do not count the border cells
            for(k=-1;k<2;k++){          //checks from -1 to +1 of cell for both x and y
                for(l=-1;l<2;l++){
                    if (alive[i+l][j+k]==true){
                        count++;
                    }
                }    
            }
        }

        return count; //make it return the right thing
    }
    

    
    //Makes the pause between generations
    public static void sleep(int duration) {
        try {
            Thread.sleep(duration);
        } 
        catch (Exception e) {}
    }

    
    //Displays the statistics at the top of the screen
    void drawLabel(Graphics g, int state) {
        g.setColor(Color.black);
        g.fillRect(0, 0, 1000, 50);
        g.setColor(Color.yellow);
        g.drawString("Generation: " + state, 450, 37);
    }

    //Sets up the JFrame screen
    public void initializeWindow() {
        setTitle("Game of Life Simulator");
        setSize(width, height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(Color.black);
        setVisible(true); //calls paint() for the first time
    }
    
    
    //Main algorithm
    public static void main(String args[]) throws IOException {

        GameOfLife currGame = new GameOfLife();
        currGame.initializeWindow();
        currGame.plantFirstGeneration(); //Sets the initial generation of living cells, either by reading from a file or creating them algorithmically
        
        for (int i = 1; i <= currGame.numGenerations; i++) {
            currGame.sleep(25);
            currGame.computeNextGeneration();
            currGame.plantNextGeneration();
            currGame.repaint();
        }
    } 
    
} //end of class
