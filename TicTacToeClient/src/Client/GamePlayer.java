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
    public void playTurn() throws IOException {
        turnIndex++;
        Solver solver = new Solver(gameField.getIntField());
        solver.callMinimax();
        Point bestMove = solver.returnBestMove();
        client.play(bestMove.x, bestMove.y);
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
