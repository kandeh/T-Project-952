package server;

public class Cell {
    
    private Cheese cheeseInside = null;
    
    private boolean hasWall = false;
    private boolean hasLadder = false;
    
    public Cell(int Row, int Col) {
        this.hasWall = false;
    }
    
    public void setHasWall() {
        this.hasWall = true;
    }
    
    public void setHasLadder() {
        this.hasLadder = true;
    }
    
    public void setHasCheese(Cheese c) {
        this.cheeseInside = c;
    }
    
    public Cheese getCheeseInside() {
        return cheeseInside;
    }
    
    public boolean hasWall() {
        return hasWall;
    }
    
    public boolean hasLadder() {
        return hasLadder;
    }
     
}
