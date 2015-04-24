package TerminalGame;

import Client.IClient;
import Client.IGameField;
import Client.IStatusLogger;
import Client.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Anty
 */
public final class GameField implements IGameField {

    private int size;
    private final IClient client;
    private final char[][] field;
    private char color;
    private final IStatusLogger statusLogger;
    private Point lastX = new Point(-1, -1), lastO = new Point(-1, -1);

    public GameField(IClient client, IStatusLogger statusLogger) throws IOException {
        this.statusLogger = statusLogger;
        this.client = client;
        size = client.getSize();
        color = client.getColor();
        field = new char[size][size];
        fetchField(true);
    }

    @Override
    public synchronized char[][] getField() {
        return field;
    }

    @Override
    public synchronized char getArea(Point point) {
        return field[point.getX()][point.getY()];
    }

    @Override
    public synchronized int getSize() {
        return size;
    }

    @Override
    public synchronized int[][] getIntField() {
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
    public synchronized List<Point> getPlaces(char player) {
        List<Point> availablePoints = new ArrayList<>();
        for (int y = 0; y < field.length; y++) {
            for (int x = 0; x < field[y].length; x++) {
                if (field[x][y] == player) {
                    availablePoints.add(new Point(x, y));
                }
            }
        }
        return availablePoints;
    }

    @Override
    public void fetchField(boolean writeProgress) throws IOException {
        if (writeProgress) {
            statusLogger.writeln("    Fetching: 0%");
        }
        size = client.getSize();
        color = client.getColor();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                char newCH = client.getGrid(new Point(x, y));
                synchronized (this) {
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
                if (writeProgress) {
                    statusLogger.reWriteln("    Fetching: " + (int) ((((float) x + ((float) y / (float) size)) / (float) size) * 100f) + "%");
                }
            }
        }
        if (writeProgress) {
            statusLogger.reWriteln("    Fetched");
        }
    }

    @Override
    public void setArea(Point point, char toSet) {
        switch (toSet) {
            case 'X':
                lastX = point;
                break;
            case 'O':
                lastO = point;
                break;
        }
        field[point.getX()][point.getY()] = toSet;
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
        builder.append("  ");
        for (int i = 0; i < size; i++) {
            if (i > 99) {
                builder.append("++");
                continue;
            }
            if (i > 9) {
                builder.append(i);
                continue;
            }
            builder.append(i).append(" ");
        }
        builder.append("\n");
        for (int y = 0; y < size; y++) {
            if (y > 99) {
                builder.append("++");
            } else {
                if (y > 9) {
                    builder.append(y);
                    break;
                } else {
                    builder.append(y).append(" ");
                }
            }
            for (int x = 0; x < size; x++) {
                if ((x == lastX.getX() && y == lastX.getY()) || (x == lastO.getX() && y == lastO.getY())) {
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
