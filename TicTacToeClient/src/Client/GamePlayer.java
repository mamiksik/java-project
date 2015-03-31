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
        System.out.println("PLAYING: ");
        System.out.println("    Fatching...");
        gameField.fetchField();
        System.out.println("    Creating solver...");
        Solver solver = new Solver(gameField.getIntField());
        System.out.println("    Calculateing minimax...");
        solver.minimaxV2(0, 1);
        System.out.println("    Getting best move...");
        Point bestMove = solver.getMove();
        System.out.println("    Playing...");
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
