/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

/**
 *
 * @author Martin
 */
public class GameSolver implements IGameSolver{

    Client client;
    IGameField gameField;
    
    public GameSolver(Client client, IGameField gameField)
    {
        this.client = client;
        this.gameField = gameField;
    }
    
    @Override
    public void play() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
