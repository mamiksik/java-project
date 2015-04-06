package Client;

import java.io.IOException;

/**
 *
 * @author Anty
 */
public interface IGamePlayer {

    /**
     *Vrací tvou barvu ('X' nebo 'O')
     * @return your color
     */
    public char getColor();

    /**
     *Vrací počet odehraných kol
     * @return number of last turn
     */
    public int getTurnIndex();

    /**
     *Vrací gameField
     * @return game field
     */
    public IGameField getGameField();

    /**
     *Vrací solver
     * @return game solver
     */
    public IGameSolver getGameSolver();

    /**
     *Zahraje další tah
     * @throws IOException při chybě připojení
     * @throws Exception při chybě při hraní dalšího tahu
     */
    public void playTurn() throws IOException, Exception;

    /**
     *Vrátí point, na který by zahrál
     * @return place to play
     * @throws IOException při chybě připojení
     * @throws Exception při chybě při řešení dalšího tahu
     */
    public Point solveTurn() throws IOException, Exception;

    /**
     *Zahraje další tah na požadovanou pozici
     * @param point place to play
     * @throws IOException při chybě připojení
     * @throws Exception při chybě při hraní dalšího tahu
     */
    public void playTurn(Point point) throws IOException, Exception;
}
