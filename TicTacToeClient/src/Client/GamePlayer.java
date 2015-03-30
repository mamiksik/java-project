/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.io.IOException;

/**
 *
 * @author Martin
 */
public class GamePlayer implements IGamePlayer {

    Client client;
    IGameField gameField;
    int turnIndex = 0;
    char color;

    public GamePlayer(Client client, IGameField gameField) throws IOException {
        this.client = client;
        this.gameField = gameField;
        this.color = client.getColor();
    }

    @Override
    public void play() {
        turnIndex++;
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public char getColor() {
        return color;
    }

    @Override
    public int getTurnIndex() {
        return turnIndex;
    }

}
