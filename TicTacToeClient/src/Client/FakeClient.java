package Client;

import java.io.IOException;

/**
 *
 * @author LukeMcNemee
 */
public class FakeClient implements Client {

    private char color;
    private int size;
    private char[][] gameGrid;

    public FakeClient() {
        color = 'X';
        size = 10;

        gameGrid = new char[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                gameGrid[i][j] = '_';
            }
        }
    }

    @Override
    public int connect(String IP, int port) throws IOException {
        System.out.println("Connected");
        return 0;
    }

    @Override
    public int disconnect() throws IOException {
        System.out.println("Disconnected");
        return 0;
    }

    @Override
    public State getStatus() throws IOException {
        return State.PLAY;
    }

    @Override
    public int getSize() throws IOException {
        return size;
    }

    @Override
    public char getColor() throws IOException {
        return color;
    }

    @Override
    public char getGrid(int x, int y) throws IOException {
        if (x >= size || y >= size || x < 0 || y < 0) {
            System.err.println("Wrong coordinates, out of grid");
            return '0';
        }
        return gameGrid[x][y];
    }

    @Override
    public String play(int x, int y) throws IOException {

        if (x >= size || y >= size || x < 0 || y < 0) {
            System.err.println("Wrong coordinates, out of grid");
            return "Wrong coordinates, out of grid";
        }
        if (gameGrid[x][y] != '_') {
            System.err.println("Wrong coordinates, already played");
            return "Wrong coordinates, already played";
        }
        gameGrid[x][y] = color;
        if (color == 'X') {
            color = 'O';
        } else {
            color = 'X';
        }
        return "played";
    }

}
