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
    IGameSolver solver;
    int turnIndex = 0;
    char color;

    public GamePlayer(Client client, IGameField gameField) throws IOException {
        this.client = client;
        this.gameField = gameField;
        this.solver = new NewSolver(gameField);
        //this.solver = new OldSolver(gameField.getIntField());
        this.color = client.getColor();
    }

    @Override
    public boolean playTurn() throws IOException {
        turnIndex++;
        System.out.println("PLAYING: ");
        System.out.println("    Fatching...");
        gameField.fetchField();

        System.out.println("    Solving...");
        if (!solver.solve()) {
            playError(solver);
            return false;
        }

        System.out.println("    Getting best move...");
        Point bestMove = solver.getBestMove();

        System.out.println("    Playing...");
        if (client.play(bestMove.x, bestMove.y) < 0) {
            playError(solver);
            return false;
        }
        return true;
    }

    private void playError(IGameSolver solver) {
        gameField.printField();
        System.out.println("[ERROR] in playing turn");
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
