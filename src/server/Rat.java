package server;

public class Rat {
    public int last_cmd = -1;
    
    public class Command {
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
        public static final int LEFT = 4;
        public static final int EAT = 5;
    }
    
    public int row = 0;
    public int col = 0;
    
    public int nextRow = 0;
    public int nextCol = 0;
    
    private Integer playerID = -1;
    
    private int team = 0;
    private static Integer Last_Created_Player_id = 0;
    

    public Rat(int team, int row, int col) {
        this.team = team;
        this.row = row;
        this.col = col;
        playerID = Last_Created_Player_id++;
    }
    
    public int getTeamID() {
        return team;
    }

    public Integer getID() {
        return playerID;
    }
    
}
