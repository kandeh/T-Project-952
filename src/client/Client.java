package client;

import Game.Game;
import client.Team.TeamAI;

public class Client {
    
    private AI teamAi;
    private Network network;
    private Game game;
    
    private void setNetworkListener() {
        network.setNetListener(new Network.NetListener() {
            
            @Override
            public void onReceive(String msg) {
                try {
                    game.update(msg);
                    teamAi.think(game);
                    network.send(game.getMyRat().getCommand());
                } catch (Exception e) {
                    //network.Close();
                    e.printStackTrace();
                }
            }
            
        });
    }
    
    private Client() {
        try {
            network = new Network();
            network.ConnectTo(Constants.Net.ip, Constants.Net.port);
            teamAi = new TeamAI();
            game = new Game();
            setNetworkListener();
            network.send("TEAM: " + teamAi.getTeamName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        System.out.println(Constants.Project.to_String());
        System.out.println();
        new Client();
    }
    
}
