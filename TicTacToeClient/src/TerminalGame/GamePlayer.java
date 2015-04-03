/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TerminalGame;

import Client.IClient;
import Client.IGameField;
import Client.IGamePlayer;
import Client.IGameSolver;
import Client.IStatusLogger;
import Client.Point;
import java.io.IOException;

/**
 *
 * @author Martin
 */
public class GamePlayer implements IGamePlayer {

    private IClient client;
    private IGameField gameField;
    private IGameSolver solver;
    private IStatusLogger statusLogger;
    private int turnIndex = 0;
    private char color;

    public GamePlayer(IClient client, IStatusLogger statusLogger) throws IOException {
        this.client = client;
        this.statusLogger = statusLogger;
        this.gameField = new GameField(client, statusLogger);
        this.color = client.getColor();
        this.solver = new NewSolver(gameField, color);
        //this.solver = new OldSolver(gameField.getIntField());
    }

    @Override
    public void playTurn() throws IOException, Exception {
        turnIndex++;
        statusLogger.writeText("PLAYING: ");
        statusLogger.writeText("    Fatching...");
        gameField.fetchField();
        gameField.printField();

        statusLogger.writeText("    Solving...");
        solver.solve();

        statusLogger.writeText("    Getting best move...");
        Point bestMove = solver.getBestMove();

        statusLogger.writeText("    Playing...");
        if (client.play(bestMove.x, bestMove.y) < 0) {
            throw new Exception("[ERROR] while sending next turn");
        }
        gameField.setArea(bestMove.x, bestMove.y, color);
        gameField.printField();
    }

    @Override
    public Point solveTurn() throws IOException, Exception {
        statusLogger.writeText("SOLVING: ");
        statusLogger.writeText("    Fatching...");
        gameField.fetchField();

        statusLogger.writeText("    Solving...");
        solver.solve();

        statusLogger.writeText("    Getting best move...");
        return solver.getBestMove();
    }

    @Override
    public void playTurn(Point point) throws Exception {
        statusLogger.writeText("PLAYING...");
        if (client.play(point.x, point.y) < 0) {
            throw new Exception("[ERROR] while sending next turn");
        }
        gameField.setArea(point.x, point.y, color);
        turnIndex++;
    }

    @Override
    public char getColor() {
        return color;
    }

    @Override
    public int getTurnIndex() {
        return turnIndex;
    }

    @Override
    public IGameField getGameField() {
        return gameField;
    }

    @Override
    public IGameSolver getGameSolver() {
        return solver;
    }

    @Override
    public String toString() {
        return turnIndex + " turns played\nYour color is: " + color + "\n\n" + gameField.toString();
    }

}
