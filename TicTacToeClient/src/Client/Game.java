/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.io.Closeable;
import java.io.IOException;

/**
 *
 * @author anty
 */
public class Game implements Closeable {
    private final Client client;
    private final IGameField gameField;
    private final IGamePlayer gamePlayer;
    
    public Game() throws IOException {
        System.out.println("STARTING game:");
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
    
    public void run() throws IOException, InterruptedException {
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
    
    @Override
    public void close() throws IOException {
        client.disconnect();
    }
    
}
