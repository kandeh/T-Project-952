package server;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import server.view.Animation;
import server.view.Item;
import server.view.View;

public class Game {
    
    public static final Object lock = new Object();
    private TeamSocket teams[];
    private Rat rats[];
    private HashMap<Integer, Cheese> Cheeses;
    private HashMap<Integer, Item> mapItems;
    private View view;
    int turn = 0;
    int mapRows;
    int mapCols;
    
    private Cell Map[][];
    
    public Game() {
        rats = new Rat[2];
        Cheeses = new HashMap<Integer, Cheese>();
        loadMap();
        teams = new TeamSocket[2];
        view = new View(mapRows, mapCols);
        view.setItems(mapItems);
        team_counter = 0;
        view.setTurnNum("0 / " + totalTurns);
        view.setScores(0, 0);
        view.setScores(1, 0);
        view.setTeamName(0, "waiting...");
        view.setTeamName(1, "...waiting");
        view.setVisible(true);
        teams[0] = null;
        teams[1] = null;
        waitForTeams(Constants.Net.port);
        if(isReady()) {
            play();
        } else {
            close();
        }
    }
    
    public void closeSockets() {
        for(int i = 0; i < 2; i++) {
            if(teams[i] != null) {
                teams[i].close();
            }
        }
    }
    
    public void close() {
        closeSockets();
        System.exit(0);
    }
    
    private Socket sock;
    private ServerSocket socket;
    
    public boolean isReady() {
        return teams[0] != null && teams[1] != null;
    }
    
    public void waitForTeams(int port) {

        try {
            TeamSocket team;
            socket = new ServerSocket(port);
            for(int t = 0; t < teams.length;) {
                sock = socket.accept();
                team = new TeamSocket(sock, t);
                Thread.sleep(500);
                if(team.isOk()) {
                    teams[t++] = team;
                    view.setTeamName((t - 1), team.getTeamName());
                    System.out.println("team " + (t + 0) + " is ready. " + "[" + team.getTeamName() + "]");
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private int team_counter = 0;
    private int totalTurns = 0;
    
    public void addTeam(TeamSocket team) {
        teams[team_counter++] = team;
    }
    
    private void send() {
        String update = turn + " " +  totalTurns + "\n";
        update += teamScores[0] + " " + teamScores[1] + "\n";
        
        update += mapRows + " " + mapCols + "\n";
        for(int i = 0; i <mapRows; i++) {
            for(int j = 0; j < mapCols; j++) {
                if(Map[i][j].hasWall()) {
                    update += "#";
                } else if(Map[i][j].hasLadder()) {
                    update += "$";
                }  else if(Map[i][j].getCheeseInside() != null) {
                    update += "@";
                } else {
                    update += " ";
                }
            }
            update += "\n";
        }
        
        update += rats[0].row + " " + rats[0].col + " " + rats[1].row + " " + rats[1].col + "\n";
        
        teams[0].send("0\n" + update);
        teams[1].send("1\n" + update);
    }
    
    public int getCommand(String cmd) {
        if(cmd == null) {
            return -1;
        }
        if(cmd.equals("up")) {
            return Rat.Command.UP;
        } else if(cmd.equals("right")) {
            return Rat.Command.RIGHT;
        } else if(cmd.equals("down")) {
            return Rat.Command.DOWN;
        } else if(cmd.equals("left")) {
            return Rat.Command.LEFT;
        } else if(cmd.equals("eat")) {
            return Rat.Command.EAT;
        }
        return -1;
    }
    
    public boolean isInBoard(int row, int col) {
        return (row >= 0 && row < mapRows) && (col >= 0 && col < mapCols);
    }
    
    public void addMoveAnim(Rat p) {
        int cmd = p.last_cmd;
        Item item = mapItems.get(p.getID());
        if(cmd == -1) {
            return;
        }
        switch(cmd) {
            case Rat.Command.DOWN: {
                double r = 0 - (item.fixDegree % 360);
                r = calcSmallerDegree(r);
                item.addAnimation(new Animation(Item.cellSize, 0, r, 0));
                break;
            }
            case Rat.Command.RIGHT: {
                double r = 270 - (item.fixDegree % 360);
                r = calcSmallerDegree(r);
                item.addAnimation(new Animation(0, Item.cellSize, r, 0));
                break;
            }
            case Rat.Command.UP: {
                double r = 180 - (item.fixDegree % 360);
                r = calcSmallerDegree(r);
                item.addAnimation(new Animation(-Item.cellSize, 0, r, 0));
                break;
            }
            case Rat.Command.LEFT: {
                double r = 90 - (item.fixDegree % 360);
                r = calcSmallerDegree(r);
                item.addAnimation(new Animation(0, -Item.cellSize, r, 0));
                break;
            }
        }

    }
    
    public double calcSmallerDegree(double d) {
        d = d % 360;
        if(d > 180) {
            d = d - 360;
        } else if(d < -180) {
            d = d + 360;
        }
        return d;
    }
    
    private int teamScores[] = new int[2];
    
    
    public void initItems() {
        for(Map.Entry<Integer, Item> item: mapItems.entrySet()) {
            item.getValue().init();
        } 
    }
    
    public void doMoves() {
        rats[0].nextRow = rats[0].row;
        rats[0].nextCol = rats[0].col;
        rats[1].nextRow = rats[1].row;
        rats[1].nextCol = rats[1].col;
        switch(rats[0].last_cmd) {
            case Rat.Command.DOWN: {
                rats[0].nextRow++;
                break;
            }
            case Rat.Command.RIGHT: {
                rats[0].nextCol++;
                break;
            }
            case Rat.Command.UP: {
                rats[0].nextRow--;
                break;
            }
            case Rat.Command.LEFT: {
                rats[0].nextCol--;
                break;
            }
        }
        switch(rats[1].last_cmd) {
            case Rat.Command.DOWN: {
                rats[1].nextRow++;
                break;
            }
            case Rat.Command.RIGHT: {
                rats[1].nextCol++;
                break;
            }
            case Rat.Command.UP: {
                rats[1].nextRow--;
                break;
            }
            case Rat.Command.LEFT: {
                rats[1].nextCol--;
                break;
            }
        }

        if(!(isInBoard(rats[0].nextRow, rats[0].nextCol) && (Map[rats[0].nextRow][rats[0].nextCol].hasWall() == false || Map[rats[0].row][rats[0].col].hasLadder() || Map[rats[0].row][rats[0].col].hasWall()))) {
            rats[0].nextRow = rats[0].row;
            rats[0].nextCol = rats[0].col;
        }
        if(!(isInBoard(rats[1].nextRow, rats[1].nextCol) && (Map[rats[1].nextRow][rats[1].nextCol].hasWall() == false || Map[rats[1].row][rats[1].col].hasLadder() || Map[rats[1].row][rats[1].col].hasWall()))) {
            rats[1].nextRow = rats[1].row;
            rats[1].nextCol = rats[1].col;
        }
        
        if(rats[1].nextRow == rats[0].nextRow && rats[1].nextCol == rats[0].nextCol) {
            if(Math.random() >= 0.5) {
                rats[0].nextRow = rats[0].row;
                rats[0].nextCol = rats[0].col;
            } else {
                rats[1].nextRow = rats[1].row;
                rats[1].nextCol = rats[1].col;
            }
        }
        
        if(rats[1].nextRow == rats[0].nextRow && rats[1].nextCol == rats[0].nextCol) {
            rats[0].nextRow = rats[0].row;
            rats[0].nextCol = rats[0].col;
            rats[1].nextRow = rats[1].row;
            rats[1].nextCol = rats[1].col;
        }
        
        if(rats[1].row != rats[1].nextRow || rats[1].col != rats[1].nextCol) {
            addMoveAnim(rats[1]);
            rats[1].row = rats[1].nextRow;
            rats[1].col = rats[1].nextCol;
        }

        if(rats[0].row != rats[0].nextRow || rats[0].col != rats[0].nextCol) {
            addMoveAnim(rats[0]);
            rats[0].row = rats[0].nextRow;
            rats[0].col = rats[0].nextCol;
        }
        
    }
    
    public void doEats() {
        Cheese ch;
        for(int i = 0; i < 2; i++) {
            if(rats[i].last_cmd == Rat.Command.EAT) {
                ch = Map[rats[i].row][rats[i].col].getCheeseInside();
                if(ch != null) {
                    ch.decSize();
                    if(ch.getSize() == 0) {
                        mapItems.remove(ch.getCheeseId());
                        Map[rats[i].row][rats[i].col].setHasCheese(null);
                    }
                    if(ch.isPoisoned()) {
                        teamScores[i]--;
                    } else {
                        teamScores[i]++;
                    }
                }
            }
        }

    }
    
    public void doCommands() {
        rats[0].last_cmd = getCommand(teams[0].response);
        rats[1].last_cmd = getCommand(teams[1].response);
        initItems();
        doEats();
        doMoves();
        teams[0].response = null;
        teams[1].response = null;
        rats[0].last_cmd = -1;
        rats[1].last_cmd = -1;
    }

    public void play() {
        long t = 0;
        turn = 0;
        teamScores[0] = teamScores[1] = 0;
        for(; turn <= totalTurns;) {
            //  || (Teams[0].response != null && Teams[1].response != null && System.currentTimeMillis() - t >= 400)
            if(System.currentTimeMillis() - t >= 750) {
                if(turn > 0) {
                    synchronized(Game.lock) {
                        doCommands();
                    }
                }
                view.setTurnNum(turn + " / " + totalTurns);
                view.setScores(0, teamScores[0]);
                view.setScores(1, teamScores[1]);
                send();
                turn++;
                t = System.currentTimeMillis();
            }
        }
        closeSockets();
    }
    
    private void loadMap() {
        try {
            Scanner scn = new Scanner(new File(Constants.Game.map));
            mapItems = new HashMap<Integer, Item>();
            char ch;
            String str;
            totalTurns = scn.nextInt();
            mapRows = scn.nextInt();
            mapCols = scn.nextInt(); 
            Map = new Cell[mapRows][mapCols];
            
            scn.nextLine();
            int block_id = 5000;
            
            for(int i = 0; i < mapRows; i++) {
                str = scn.nextLine();
                for(int j = 0; j < mapCols; j++) {
                    Map[i][j] = new Cell(i, j);
                        Cheese cheese = null;
                        ch = str.charAt(j);
                        switch(ch) {
                            case ' ': break;
                            case '#': {
                                addMapItem(block_id++, new Item(Item.Type.Wall, null, i, j, block_id - 1));
                                Map[i][j].setHasWall();
                                break;
                            }
                            case '$': {
                                addMapItem(block_id++, new Item(Item.Type.Ladder, null, i, j, block_id - 1));
                                Map[i][j].setHasLadder();
                                break;
                            }
                            case 'C':  {
                                cheese = new Cheese(i, j, false);
                                Map[i][j].setHasCheese(cheese);
                                Cheeses.put(cheese.getCheeseId(), cheese);
                                addMapItem(cheese.getCheeseId(), new Item(Item.Type.CHEESE, cheese, i, j, cheese.getCheeseId()));
                                break;
                            }
                            
                            case 'P':  {
                                cheese = new Cheese(i, j, true);
                                Map[i][j].setHasCheese(cheese);
                                Cheeses.put(cheese.getCheeseId(), cheese);
                                addMapItem(cheese.getCheeseId(), new Item(Item.Type.POISON, cheese, i, j, cheese.getCheeseId()));
                                break;
                            }
                            
                            case 'r': {
                                Rat rat = new Rat(0, i, j);
                                rats[0] = rat;
                                addMapItem(rat.getID(), new Item(Item.Type.BROWN_RAT, rat, i, j, rat.getID()));
                                break;
                            }
                            
                            case 'R': {
                                Rat rat = new Rat(1, i, j);
                                rats[1] = rat;
                                addMapItem(rat.getID(), new Item(Item.Type.GRAY_RAT, rat, i, j, rat.getID()));
                                break;
                            }
                        }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void addMapItem(Integer id, Item item) {
        mapItems.put(id, item);
    }

}
