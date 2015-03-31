/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

/**
 *
 * @author anty
 */
public interface IGameSolver {
    
    public boolean isGameOver();
    
    public boolean hasWon(int player);
    
    public void updateGameField(IGameField gameField);
    
    public Point getBestMove();
    
    public boolean solve();
    
}
