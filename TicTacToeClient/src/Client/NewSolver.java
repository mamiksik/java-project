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
public class NewSolver implements IGameSolver {
    
    IGameField gameField;
    Point move;
    
    public NewSolver (IGameField gameField) {
        this.gameField = gameField;
    }

    @Override
    public boolean isGameOver() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean hasWon(int player) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void updateGameField(IGameField gameField) {
        this.gameField = gameField;
    }

    @Override
    public Point getBestMove() {
        return move;
    }

    @Override
    public boolean solve() {
        move = null;
        if (gameField.isFullFree()) {
            System.out.println("    Playing...");
            move = new Point(gameField.getSize() / 2, gameField.getSize() / 2);
            return true;
        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
