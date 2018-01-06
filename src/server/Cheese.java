package server;

public class Cheese {
    
    private Integer cheeseID = -1;
    private boolean poisoned = false;
    private static Integer lastCreatedCheeseID = 7000;
    int row;
    int col;
    private int size = 3;
    
    public Integer getCheeseId() {
        return cheeseID;
    }

    public int getSize() {
        return size;
    }
    
    public void decSize() {
        size--;
    }
    
    public Cheese(int row, int col, boolean poisoned) {
        this.poisoned = poisoned;
        this.row = row;
        this.col = col;
        this.size = 3;
        cheeseID = lastCreatedCheeseID++;
    }

    public boolean isPoisoned() {
        return poisoned;
    }
}
