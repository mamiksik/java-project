package Client;

import java.io.IOException;
import java.util.List;

/**
 * Interface pro herní pole
 *
 * @author Anty
 */
public interface IGameField {

    /**
     * Vrací herní pole jako char[][]
     *
     * @return game field
     */
    public char[][] getField();

    /**
     * Vrací herní pole jako int[][] 0 == '_', 1 == tvůj znak, 2 == soupeřův
     * znak
     *
     * @return game field
     */
    public int[][] getIntField();

    List<Point> getPlaces(char player);

    /**
     * Vrátí char na určité pozici
     *
     * @param point position
     * @return
     */
    public char getArea(Point point);

    /**
     * Vrátí serverem požadovanou velikost herní mřížky
     *
     * @return field size
     */
    public int getSize();

    /**
     * Stáhne ze serveru aktuální herní mřížku
     *
     * @param writeProgress
     * @throws IOException connection exception
     */
    public void fetchField(boolean writeProgress) throws IOException;

    /**
     * Nastaví na určitou pozici určitý znak
     *
     * @param point position
     * @param toSet char to set on position
     */
    public void setArea(Point point, char toSet);

    /**
     * Vykreslí herní pole do loggeru
     */
    public void printField();

    /**
     * Vrátí zda je herní pole celé prázdné
     *
     * @return true if field is free
     */
    public boolean isFullFree();
}
