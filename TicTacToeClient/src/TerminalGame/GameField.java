/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TerminalGame;

import Client.IClient;
import Client.IGameField;
import Client.IStatusLogger;
import Client.Point;
import java.io.IOException;

/**
 * @author Martin
 */
public final class GameField implements IGameField {

    private int size;
    private final IClient client;
    private char[][] field;
    private char color;
    private final IStatusLogger statusLogger;
    private Point lastX = new Point(-1, -1), lastO = new Point(-1, -1);

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
                char newCH = client.getGrid(x, y);
                if (field[x][y] != newCH) {
                    switch (newCH) {
                        case 'X':
                            lastX = new Point(x, y);
                            break;
                        case 'O':
                            lastO = new Point(x, y);
                            break;
                    }
                    field[x][y] = newCH;
                }
            }
        }
    }

    @Override
    public void setArea(int x, int y, char toSet) {
        switch (toSet) {
            case 'X':
                lastX = new Point(x, y);
                break;
            case 'O':
                lastO = new Point(x, y);
                break;
        }
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
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if ((x == lastX.x && y == lastX.y) || (x == lastO.x && y == lastO.y)) {
                    builder.append(field[x][y]).append("<");
                } else {
                    builder.append(field[x][y]).append(" ");
                }
            }
            builder.append("\n");
        }
        return builder.toString();
    }

}
