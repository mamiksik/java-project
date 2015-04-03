/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FXGame;

import Client.Client;
import Client.IClient;
import Client.IGamePlayer;
import Client.IStatusLogger;
import Client.Point;
import TerminalGame.GamePlayer;
import java.io.IOException;

/**
 *
 * @author anty
 */
public class FXGame extends Thread {

    private IClient client;
    private IGamePlayer gamePlayer;
    private final IStatusLogger statusLogger;
    private final Controller controler;
    private boolean run = false, waitPlay = true;
    private Point move = null;
    private String ip = "127.0.0.1";
    private int port = 3248;

    public FXGame(Controller controller, IStatusLogger statusLogger) {
        this.controler = controller;
        this.statusLogger = statusLogger;
        this.setDaemon(true);
    }

    @Override
    public void run() {
        run = true;
        boolean first = true;
        connectGame();
        while (run) {
            try {
                switch (client.getStatus()) {
                    case WAIT:
                        if (first) {
                            statusLogger.writeText("waiting...");
                            first = false;
                        }
                        break;
                    case PLAY:
                        first = true;
                        if (controler.checkBoxAutoPlay.isSelected()) {
                            playTurn();
                            break;
                        }
                        move = gamePlayer.solveTurn();
                        statusLogger.writeText("redy to play on: " + move.toString());
                        waitPlay = true;
                        controler.buttonPlay.setDisable(false);
                        while (run && waitPlay) {
                            gamePlayer.getGameField().setArea(move.x, move.y, gamePlayer.getColor());
                            refreshTextAreaGame();
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException ex) {
                                statusLogger.writeText("[ERROR] in ThreadSleep: " + ex.getMessage());
                            }
                            gamePlayer.getGameField().setArea(move.x, move.y, '_');
                            refreshTextAreaGame();
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException ex) {
                                statusLogger.writeText("[ERROR] in ThreadSleep: " + ex.getMessage());
                            }
                        }
                        waitPlay = false;
                        if (run) {
                            playTurn();
                        }
                        break;
                    case WIN:
                        first = true;
                        disconnect();
                        statusLogger.writeText("you are WINNER");
                        break;
                    case DEFEAT:
                        first = true;
                        disconnect();
                        statusLogger.writeText("you are LOOSER");
                        break;
                }
            } catch (IOException ex) {
                statusLogger.writeText("[ERROR] while running: " + ex.getMessage());
                break;
            } catch (Exception ex) {
                statusLogger.writeText(ex.getMessage());
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                statusLogger.writeText("[ERROR] in ThreadSleep: " + ex.getMessage());
            }
        }
        run = false;
        disconnectGame();
    }

    public void connect(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.start();
        controler.buttonConnect.setDisable(true);
        controler.buttonDisconnect.setDisable(false);
    }

    private void connectGame() {
        try {
            statusLogger.writeText("STARTING game:");
            statusLogger.writeText("    Loading CLIENT...");
            client = new Client();
            statusLogger.writeText("    CONNECTING...");
            client.connect(ip, port);
            statusLogger.writeText("    Loading GAME_PLAYER...");
            gamePlayer = new GamePlayer(client, statusLogger);
            statusLogger.writeText("    Secesfully STARTED");
            refreshTextAreaGame();
        } catch (IOException ex) {
            statusLogger.writeText("[ERROR] while connecting: " + ex.getMessage());
        }
    }

    public void disconnect() {
        this.stopRun();
        controler.buttonConnect.setDisable(false);
        controler.buttonDisconnect.setDisable(true);
        controler.buttonPlay.setDisable(true);
    }

    private void disconnectGame() {
        statusLogger.writeText("disconnecting");
        try {
            client.disconnect();
        } catch (IOException ex) {
            statusLogger.writeText("[ERROR] while disconnecting: " + ex.getMessage());
        }
        statusLogger.writeText("disconnected");
    }

    public void play() {
        waitPlay = false;
        controler.buttonPlay.setDisable(true);
    }

    private void playTurn() {
        try {
            statusLogger.writeText("playing");
            refreshTextAreaGame();
            if (move == null) {
                gamePlayer.playTurn(gamePlayer.solveTurn());
            } else {
                gamePlayer.playTurn(move);
                move = null;
            }
            refreshTextAreaGame();
            statusLogger.writeText("played");
        } catch (IOException ex) {
            statusLogger.writeText("[ERROR] while playing: " + ex.getMessage());
        } catch (Exception ex) {
            statusLogger.writeText(ex.getMessage());
        }
    }

    private void refreshTextAreaGame() {
        statusLogger.writeTable(gamePlayer.toString());
    }

    public void stopRun() {
        run = false;
    }
}
