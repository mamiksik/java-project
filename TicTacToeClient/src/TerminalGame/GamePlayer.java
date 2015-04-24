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
 * @author Anty
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
        statusLogger.writeln("PLAYING: ");
        statusLogger.writeln("    Fatching...");
        gameField.fetchField(false);
        gameField.printField();

        statusLogger.writeln("    Solving...");
        solver.solve();

        statusLogger.writeln("    Getting best move...");
        Point bestMove = solver.getBestMove();

        statusLogger.writeln("    Sending...");
        if (client.play(bestMove) < 0) {
            throw new Exception("[ERROR] while sending next turn");
        }
        gameField.setArea(bestMove, color);
        gameField.printField();
        turnIndex++;
    }

    @Override
    public Point solveTurn() throws IOException, Exception {
        statusLogger.writeln("SOLVING: ");
        gameField.fetchField(true);

        statusLogger.writeln("    Solving...");
        solver.solve();

        statusLogger.writeln("    Getting best move...");
        return solver.getBestMove();
    }

    @Override
    public void playTurn(Point point) throws Exception {
        statusLogger.writeln("SENDING...");
        if (client.play(point) < 0) {
            throw new Exception("while sending next turn");
        }
        gameField.setArea(point, color);
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
