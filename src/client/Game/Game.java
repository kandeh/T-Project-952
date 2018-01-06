package Game;

import java.util.Scanner;

public class Game {
    
    private Cell Map[][] = null;
    private int myTeamID;
    private int totalTurns = 0;
    private int turnNumber = 0;
    private int rows = 0;
    private int cols = 0;
    private Rat rats[];
    private int scores[] = new int[2];

    public Cell[][] getMap() {
        return Map;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public int getTotalTurns() {
        return totalTurns;
    }

    public int getMyTeamID() {
        return myTeamID;
    }
    
    public int getOpponentTeamID() {
        return 1 - myTeamID;
    }

    public int getNumberOfColumns() {
        return cols;
    }
    
    public int getNumberOfRows() {
        return rows;
    }
    
    public Rat getMyRat() {
        return rats[myTeamID];
    }
    
    public Rat getOppRat() {
        return rats[1 - myTeamID];
    }
    
    public int getMyScore() {
        return scores[myTeamID];
    }
    
    public int getOppScore() {
        return scores[1 - myTeamID];
    }
    
    private void updateMap(Scanner scn) throws Exception {
        int r, c;
        String line;
        char ch;
        r = scn.nextInt();
        c = scn.nextInt();
        scn.nextLine();
        this.rows = r;
        this.cols = c;
        if(Map == null || Map.length != rows || Map[0] == null || Map[0].length != cols) {
            Map = new Cell[r][c];
        }
        for(int i = 0; i < rows; i++) {
            line = scn.nextLine();
            for(int j = 0; j < cols; j++) {
                if(Map[i][j] == null) {
                    Map[i][j] = new Cell(i, j);
                }
                ch = line.charAt(j);
                Map[i][j].reset(i, j);
                switch(ch) {
                    case '#': Map[i][j].setHasWall(); break;
                    case '$': Map[i][j].setHasLadder(); break;
                    case '@': Map[i][j].setHasCheese(); break;
                }
                
            }
        }
    }
    
    private void updateRats(Scanner scn) throws Exception {
        if(rats == null || rats[0] == null || rats[1] == null) {
            rats = new Rat[2];
            rats[0] = new Rat();
            rats[1] = new Rat();
        }
        int ci, cj;
        for(int i = 0; i < 2; i++) {
            ci = scn.nextInt();
            cj = scn.nextInt();
            rats[i].update(Map[ci][cj], i);
            Map[ci][cj].setRatInside(rats[i]);
        }
    }
    
    public void update(String world_str) throws Exception {
        Scanner scn = new Scanner(world_str);
        myTeamID = scn.nextInt();  
        turnNumber = scn.nextInt();
        totalTurns = scn.nextInt();
        scores[0] = scn.nextInt();
        scores[1] = scn.nextInt();
        updateMap(scn);
        updateRats(scn);
    }
    
}
