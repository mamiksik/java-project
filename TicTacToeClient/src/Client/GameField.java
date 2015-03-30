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
    char color;

    public GameField(Client client) throws IOException {
        this.client = client;
        size = client.getSize();
        field = new char[size][size];
        color = client.getColor();
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

    @Override
    public int[][] getIntField() {
        int[][] intField = new int[size][size];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                intField[x][y] = getInt(x, y);
            }
        }
        return intField;
    }
    
    private int getInt(int x, int y) {
        switch (field[x][y]) {
            case '_':
                return 0;
            case 'X':
                if (color == 'X')
                    return 1;
                return 2;
            case 'Y':
                if (color == 'Y')
                    return 1;
                return 2;
        }
        return 0;
    }

}
