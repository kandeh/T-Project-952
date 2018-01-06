package Game;

public class Cell {
    
    private int Row;
    private int Col;
    private Rat ratInside = null;
    private boolean hasWall = false;
    private boolean hasLadder = false;
    private boolean hasCheese = false;
    
    public Cell(int Row, int Col) {
        reset(Row, Col);
    }

    public int getRow() {
        return Row;
    }

    public int getCol() {
        return Col;
    }
    
    public boolean hasWall() {
        return hasWall;
    }
    
    public boolean hasLadder() {
        return hasLadder;
    }
    
    public boolean hasCheese() {
        return hasCheese;
    }
    
    protected final void reset(int row, int col) {
        this.ratInside = null;
        this.hasWall = false;
        this.hasLadder = false;
        this.hasCheese = false;
        this.Row = row;
        this.Col = col;
    }

    protected void setHasWall() {
        this.hasWall = true;
    }
    
    protected void setHasLadder() {
        this.hasLadder = true;
    }
    
    protected void setHasCheese() {
        this.hasCheese = true;
    }
        
    protected void setRatInside(Rat r) {
        this.ratInside = r;
    }

    public Rat getRatInside() {
        return this.ratInside;
    }

    @Override
    public String toString() {
        return "Cell("  + this.Row + ", " + this.Col + ")";
    }
    
}
