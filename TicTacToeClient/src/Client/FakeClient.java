package Client;

import java.io.IOException;

/**
 *
 * @author LukeMcNemee
 */
public class FakeClient implements IClient {

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
    public void connect(String IP, int port) throws IOException {
        System.out.println("Connected");
    }

    @Override
    public void disconnect() throws IOException {
        System.out.println("Disconnected");
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
    public char getGrid(Point point) throws IOException {
        int x = point.x, y = point.y;
        if (x >= size || y >= size || x < 0 || y < 0) {
            System.err.println("Wrong coordinates, out of grid");
            return '0';
        }
        return gameGrid[x][y];
    }

    @Override
    public int play(Point point) throws IOException {
        if (play2(point) == "played") 
            return 0;
        return -1;
    }
    
    public String play2(Point point) throws IOException {
        int x = point.x, y = point.y;
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
