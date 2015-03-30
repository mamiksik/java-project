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
public interface IGameField {

    public char[][] getField();

    public void fetchField() throws IOException;

    public char getArea(int x, int y);

    public int getSize();

    public void printField();
}
