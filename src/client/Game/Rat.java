package Game;

public class Rat {
    
    private int teamID = 0;
    private Cell cell = null;
    
    protected String cmd = "none";
    
    public void moveUp() {
        cmd = "up";
    }
    
    public void moveRight() {
        cmd = "right";
    }

    public void moveDown() {
        cmd = "down";
    }
    
    public void moveLeft() {
        cmd = "left";
    }
    
    public void eat() {
        cmd = "eat";
    }

    public int getTeamID() {
        return teamID;
    }

    public Cell getCell() {
        return cell;
    }
    
    public String getCommand() {
        return cmd;
    }
    
    protected void update(Cell cell, int team) {
       this.cell = cell;
       this.teamID = team;
       this.cmd = "none";
    }
    
}
