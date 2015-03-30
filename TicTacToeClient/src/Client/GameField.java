/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.io.IOException;

/**
 * @author Martin
 */
public final class GameField implements IGameField {

    int size;
    Client client;
    char[][] field;

    public GameField(Client client) throws IOException {
        this.client = client;
        size = client.getSize();
        field = new char[size][size];
        fetchField();
    }

    @Override
    public char[][] getField() {
        return field;
    }

    @Override
    public char getArea(int x, int y) {
        return field[x][y];
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void fetchField() throws IOException {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                field[x][y] = client.getGrid(x, y);
            }
        }
    }

    @Override
    public void printField() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                System.out.print(field[x][y] + " ");
            }
            System.out.println("");
        }
    }

}
