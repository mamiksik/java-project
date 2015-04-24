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
        statusLogger.writeln("STARTING game:");
        statusLogger.writeln("    Loading CLIENT...");
        client = new Client();
        statusLogger.writeln("    CONNECTING...");
        client.connect("127.0.0.1", 3248);
        statusLogger.writeln("    Loading GAME_PLAYER...");
        gamePlayer = new GamePlayer(client, statusLogger);
        statusLogger.writeln("    Secesfully STARTED");
        statusLogger.writeln("My color is: " + gamePlayer.getColor());
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
                    statusLogger.writeln("waiting...");
                    first = false;
                }
            }
        }
        statusLogger.writeln("I am " + (status == State.WIN ? "WINNER" : "LOOSER"));
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
