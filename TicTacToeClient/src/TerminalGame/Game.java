/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TerminalGame;

import Client.Client;
import Client.IClient;
import Client.IGamePlayer;
import Client.IStatusLogger;
import Client.State;
import java.io.Closeable;
import java.io.IOException;

/**
 *
 * @author anty
 */
public class Game implements Closeable {

    private final IClient client;
    private final IGamePlayer gamePlayer;
    private final IStatusLogger statusLogger;

    public Game(String ip, int port, IStatusLogger statusLogger) throws IOException {
        this.statusLogger = statusLogger;
        statusLogger.writeText("STARTING game:");
        statusLogger.writeText("    Loading CLIENT...");
        client = new Client();
        statusLogger.writeText("    CONNECTING...");
        client.connect("127.0.0.1", 3248);
        statusLogger.writeText("    Loading GAME_PLAYER...");
        gamePlayer = new GamePlayer(client, statusLogger);
        statusLogger.writeText("    Secesfully STARTED");
        statusLogger.writeText("My color is: " + gamePlayer.getColor());
    }

    public void run() throws IOException, InterruptedException {
        State status = client.getStatus();
        boolean first = true;
        while (status.equals(State.WAIT) || status.equals(State.PLAY)) {
            Thread.sleep(1000);
            status = client.getStatus();
            if (status.equals(State.PLAY)) {
                first = true;
                try {
                    gamePlayer.playTurn();
                } catch (Exception ex) {
                    status = State.DEFEAT;
                }
            } else {
                if (first) {
                    statusLogger.writeText("waiting...");
                    first = false;
                }
            }
        }
        statusLogger.writeText("I am " + (status == State.WIN ? "WINNER" : "LOOSER"));
    }

    public State getStatus() throws IOException {
        return client.getStatus();
    }

    public IGamePlayer getGamePlayer() {
        return gamePlayer;
    }

    @Override
    public String toString() {
        return gamePlayer.getGameField().toString();
    }

    @Override
    public void close() throws IOException {
        client.disconnect();
    }

}
