/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

/**
 *Slouží ke zjištění dalšího tahu, co je potřeba udělat
 * @author anty
 */
public interface IGameSolver {

    /**
     *Slouží ke zjištění, zda je hra už ukonce
     * @return true - hra je už u konce, jinak false
     */
    public boolean isGameOver();

    /**
     *Slouží ke zjištění, zda zadaný hráč už vyhrál
     * @param player znak hráče ('X' nebo 'Y')
     * @return true - zadaný hráč již vyhrál, jinak false
     */
    public boolean hasWon(char player);

    /**
     *Vrací pozici dalšího tahu, nejprve je třeba zavolat solve
     * @return position for next best move
     */
    public Point getBestMove();

    /**
     *Připraví game solver pro získání dalšího tahu
     * @throws Exception v případě chybnéhu stavu herní tabulky nebo při chybném řešení
     */
    public void solve() throws Exception;

}
