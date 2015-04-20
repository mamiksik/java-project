/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FXGame;

import Client.Client;
//import Client.ClientSimulator;
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
        int first = 0;
        connectGame();
        while (run) {
            try {
                switch (client.getStatus()) {
                    case WAIT:
                        switch (first) {
                            case 0:
                                statusLogger.writeln("WAITING");
                                break;
                            case 6:
                                statusLogger.reWriteln("WAITING");
                                first = 0;
                                break;
                            default:
                                statusLogger.write(".");
                                break;
                        }
                        break;
                    case PLAY:
                        first = 0;
                        if (controler.checkBoxAutoPlay.isSelected()) {
                            playTurn();
                            break;
                        }
                        move = gamePlayer.solveTurn();
                        statusLogger.write("redy to play on: " + move.toString());
                        waitPlay = true;
                        controler.buttonPlay.setDisable(false);
                        while (run && waitPlay) {
                            gamePlayer.getGameField().setArea(move, gamePlayer.getColor());
                            refreshTextAreaGame();
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException ex) {
                                statusLogger.write("[ERROR] in ThreadSleep: " + ex.getMessage());
                            }
                            gamePlayer.getGameField().setArea(move, '_');
                            refreshTextAreaGame();
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException ex) {
                                statusLogger.write("[ERROR] in ThreadSleep: " + ex.getMessage());
                            }
                        }
                        waitPlay = false;
                        if (run) {
                            playTurn();
                        }
                        break;
                    case WIN:
                        first = 0;
                        disconnect();
                        statusLogger.write("you are WINNER");
                        break;
                    case DEFEAT:
                        first = 0;
                        disconnect();
                        statusLogger.write("you are LOOSER");
                        break;
                }
            } catch (IOException ex) {
                statusLogger.write("[ERROR] while running: " + ex.getMessage());
                break;
            } catch (Exception ex) {
                statusLogger.write(ex.getMessage());
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                statusLogger.write("[ERROR] in ThreadSleep: " + ex.getMessage());
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
            statusLogger.write("STARTING game:");
            statusLogger.write("    Loading CLIENT...");
            client = new Client();
            statusLogger.write("    CONNECTING...");
            client.connect(ip, port);
            statusLogger.write("    Loading GAME_PLAYER...");
            gamePlayer = new GamePlayer(client, statusLogger);
            statusLogger.write("    Secesfully STARTED");
            refreshTextAreaGame();
        } catch (IOException ex) {
            statusLogger.write("[ERROR] while connecting: " + ex.getMessage());
        }
    }

    public void disconnect() {
        this.stopRun();
        controler.buttonConnect.setDisable(false);
        controler.buttonDisconnect.setDisable(true);
        controler.buttonPlay.setDisable(true);
    }

    private void disconnectGame() {
        statusLogger.write("disconnecting");
        try {
            client.disconnect();
        } catch (IOException ex) {
            statusLogger.write("[ERROR] while disconnecting: " + ex.getMessage());
        }
        gamePlayer = null;
        statusLogger.write("disconnected");
    }

    public void play() {
        waitPlay = false;
        controler.buttonPlay.setDisable(true);
    }

    private void playTurn() {
        try {
            statusLogger.write("playing");
            refreshTextAreaGame();
            if (move == null) {
                gamePlayer.playTurn(gamePlayer.solveTurn());
            } else {
                gamePlayer.playTurn(move);
                move = null;
            }
            refreshTextAreaGame();
            statusLogger.write("played");
        } catch (IOException ex) {
            statusLogger.write("[ERROR] while playing: " + ex.getMessage());
        } catch (Exception ex) {
            statusLogger.write(ex.getMessage());
        }
    }

    private void refreshTextAreaGame() {
        statusLogger.writeTable(gamePlayer.toString());
    }

    public void stopRun() {
        run = false;
    }
}
