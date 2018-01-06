package client;

import client.game.Game;

public abstract class AI {
    
    public abstract String getTeamName();
    public abstract void think(Game game);

}
