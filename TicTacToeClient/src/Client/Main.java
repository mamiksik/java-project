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

import java.io.IOException;

/**
 *
 * @author LukeMcNemee
 */
public class Main {
    private static Client client;
    private static IGameField gameField;
    private static IGamePlayer gamePlayer;

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        initialize();
        run();
        close();
    }
    
    private static void initialize() throws IOException {
        System.out.println("STARTING:");
        System.out.println("    Loading CLIENT...");
        client = new ClientImpl();
        System.out.println("    CONNECTING...");
        client.connect("127.0.0.1", 3248);
        System.out.println("    Loading GAME_FIELD...");
        gameField = new GameField(client);
        System.out.println("    Loading GAME_PLAYER...");
        gamePlayer = new GamePlayer(client, gameField);
        System.out.println("    Secesfully STARTED");
        System.out.println("My color is: " + gamePlayer.getColor());
    }
    
    private static void run() throws IOException, InterruptedException {
        State status = client.getStatus();
        while (status.equals(State.WAIT) || status.equals(State.PLAY)) {
            Thread.sleep(10000);
            status = client.getStatus();
            if (status.equals(State.PLAY)) {
                 if (!gamePlayer.playTurn()) {
                     status = State.DEFEAT;
                 }
            } else {
                System.out.println("Waiting...");
            }
        }
        System.out.println("I am " + (status == State.WIN ? "WINNER" : "LOOSER"));
    }
    
    private static void close() throws IOException {
        client.disconnect();
    }

}
