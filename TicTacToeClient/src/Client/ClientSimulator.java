package Client;

import java.io.*;

/**
 *
 * @author Anty
 */
public class ClientSimulator implements IClient {

    int size = 10;
    char color = 'X';
    char[][] gird;

    @Override
    public void connect(String IP, int port) throws IOException {
        createGird();
    }

    private void createGird() {
        gird = new char[size][size];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                gird[x][y] = '_';
            }
        }
    }

    @Override
    public void disconnect() throws IOException {

    }

    @Override
    public State getStatus() throws IOException {
        String response = "PLAY";
        System.out.println(response);
        if (response.equals("DEFEAT")) {
            return State.DEFEAT;
        }
        if (response.equals("PLAY")) {
            return State.PLAY;
        }
        if (response.equals("WAIT")) {
            return State.WAIT;
        }
        if (response.equals("WIN")) {
            return State.WIN;
        }
        return null;
    }

    @Override
    public int getSize() throws IOException {
        int response = size;
        System.out.println(response);
        return response;
    }

    @Override
    public char getColor() throws IOException {
        char response = color;
        System.out.println(response);
        return response;
    }

    @Override
    public char getGrid(Point point) throws IOException {
        char response = gird[point.getX()][point.getY()];
        System.out.println(response);
        return response;
    }

    @Override
    public int play(Point point) throws IOException {
        int response = -2;
        if (gird[point.getX()][point.getY()] == '_') {
            gird[point.getX()][point.getY()] = color;
            response = 0;
        } else {
            response = -3;
        }

        System.out.println(response);
        return response;
    }

    @Override
    public char[] getFullGrid() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getWinLength() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
