/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TerminalGame;

import Client.IClient;
import Client.IGameField;
import Client.IStatusLogger;
import java.io.IOException;

/**
 * @author Martin
 */
public final class GameField implements IGameField {

    private int size;
    private final IClient client;
    private final char[][] field;
    private char color;
    private final IStatusLogger statusLogger;

    public GameField(IClient client, IStatusLogger statusLogger) throws IOException {
        this.statusLogger = statusLogger;
        this.client = client;
        size = client.getSize();
        color = client.getColor();
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
                if (color == 'X') {
                    return 1;
                }
                return 2;
            case 'O':
                if (color == 'O') {
                    return 1;
                }
                return 2;
        }
        return 0;
    }

    @Override
    public void fetchField() throws IOException {
        size = client.getSize();
        color = client.getColor();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                field[x][y] = client.getGrid(x, y);
            }
        }
    }
    
    @Override
    public void setArea(int x, int y, char toSet) {
        field[x][y] = toSet;
    }
    
    @Override
    public void printField() {
        statusLogger.writeTable(toString());
    }
    
    @Override
    public boolean isFullFree() {
        for (char[] fieldx : field) {
            for (char fieldy : fieldx) {
                if (fieldy != '_') {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                builder.append(field[j][i]).append(" ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

}
