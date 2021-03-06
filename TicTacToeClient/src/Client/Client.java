/*
 * The MIT License
 *
 * Copyright 2015 LukeMcNemee.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package Client;

import java.io.*;
import java.net.*;

/**
 *
 * @author LukeMcNemee
 */
public class Client implements IClient {

    private Socket clientSocket;
    private DataOutputStream outToServer;
    private BufferedReader inFromServer;

    @Override
    public void connect(String IP, int port) throws IOException {
        clientSocket = new Socket(IP, port);
        outToServer = new DataOutputStream(clientSocket.getOutputStream());
        inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    @Override
    public void disconnect() throws IOException {
        clientSocket.close();
    }

    @Override
    public State getStatus() throws IOException {
        outToServer.writeBytes("STATUS" + "\n");
        String response = inFromServer.readLine();
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
        outToServer.writeBytes("SIZE" + "\n");
        String response = inFromServer.readLine();
        System.out.println(response);
        return Integer.parseInt(response);
    }

    @Override
    public int getWinLength() throws IOException {
        outToServer.writeBytes("WINLENGTH" + "\n");
        String response = inFromServer.readLine();
        System.out.println(response);
        int out;
        try {
            out = Integer.parseInt(response);
        } catch(Throwable e) {
            System.err.println(e.getMessage());
            out = 5;
        }
        return out;
    }

    @Override
    public char getColor() throws IOException {
        outToServer.writeBytes("COLOR" + "\n");
        String response = inFromServer.readLine();
        System.out.println(response);
        return response.charAt(0);
    }

    @Override
    public char getGrid(Point point) throws IOException {
        outToServer.writeBytes("GRID " + point.getX() + " " + point.getY() + "\n");
        String response = inFromServer.readLine();
        System.out.print(response + " ");
        return response.charAt(0);
    }

    @Override
    public char[] getFullGrid() throws IOException {
        outToServer.writeBytes("FULLGRID\n");
        String response = inFromServer.readLine();
        System.out.print(response + " ");
        return response.toCharArray();
    }

    @Override
    public int play(Point point) throws IOException {
        System.out.println(point);
        outToServer.writeBytes("PLAY " + point.getX() + " " + point.getY() + "\n");
        String response = inFromServer.readLine();
        System.out.println(response);
        return Integer.parseInt(response);
    }

}
