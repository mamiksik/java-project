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
public interface IGamePlayer {

    public char getColor();

    public int getTurnIndex();

    public IGameField getGameField();

    public IGameSolver getGameSolver();

    public void playTurn() throws IOException, Exception;

    public Point solveTurn() throws IOException, Exception;

    public void playTurn(Point point) throws IOException, Exception;
}
