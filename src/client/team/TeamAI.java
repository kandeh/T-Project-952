package client.team;

import client.game.Rat;
import client.game.Game;

public class TeamAI extends client.AI {

    @Override
    public String getTeamName() {
        return "RandomRat";
    }

    @Override
    public void think(Game game) {
        Rat myRat = game.getMyRat();
        if(myRat.getCell().hasCheese()) {
            myRat.eat();
        } else {
            switch((int) (Math.random() * 4)) {
                case 0: myRat.moveUp();     break;
                case 1: myRat.moveRight();  break;
                case 2: myRat.moveDown();   break;
                case 3: myRat.moveLeft();   break;
            }
        }
    }
    
}