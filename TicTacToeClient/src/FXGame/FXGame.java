/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FXGame;

import Client.*;
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
    private int port = 13248;

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
                        first++;
                        break;
                    case PLAY:
                        first = 0;
                        prepareToPlay();
                        break;
                    case WIN:
                        first = 0;
                        disconnect();
                        statusLogger.writeln("you are WINNER");
                        break;
                    case DEFEAT:
                        first = 0;
                        disconnect();
                        statusLogger.writeln("you are LOOSER");
                        break;
                }
            } catch (Exception ex) {
                statusLogger.writeln("[ERROR] while running -> " + ex.getMessage());
                break;
            }
            sleep();
        }
        run = false;
        disconnectGame();
    }

    public synchronized void connect(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.start();
        controler.setConnectDisabled(true);
    }

    private synchronized void connectGame() {
        try {
            statusLogger.writeln("STARTING game:");
            statusLogger.writeln("    Loading CLIENT...");
            client = new Client();
            statusLogger.writeln("    CONNECTING...");
            client.connect(ip, port);
            statusLogger.writeln("    Loading GAME_PLAYER...");
            gamePlayer = new GamePlayer(client, statusLogger);
            statusLogger.writeln("    Secesfully STARTED");
            refreshTextAreaGame();
        } catch (Exception ex) {
            statusLogger.writeln("[ERROR] while connecting -> " + ex.getMessage());
        }
    }

    public synchronized void disconnect() {
        this.stopRun();
        controler.setConnectDisabled(false);
        controler.setPlayDisabled(true);
    }

    private synchronized void disconnectGame() {
        statusLogger.writeln("disconnecting");
        try {
            gamePlayer.getGameField().fetchField(true);
            refreshTextAreaGame();
            client.disconnect();
        } catch (IOException ex) {
            statusLogger.writeln("[ERROR] while disconnecting -> " + ex.getMessage());
        }
        gamePlayer = null;
        statusLogger.writeln("disconnected");
    }

    public synchronized void play() {
        waitPlay = false;
        controler.setPlayDisabled(true);
    }

    private void prepareToPlay() throws Exception {
        move = gamePlayer.solveTurn();
        refreshTextAreaGame();
        statusLogger.writeln("Redy to play on: " + move.toString());
        if (!controler.checkBoxAutoPlay.isSelected()) {
            waitPlay = true;
            controler.setPlayDisabled(false);
            while (run && waitPlay) {
                gamePlayer.getGameField().setArea(move, gamePlayer.getColor());
                refreshTextAreaGame();
                sleep();
                gamePlayer.getGameField().setArea(move, '_');
                refreshTextAreaGame();
                sleep();
            }
            waitPlay = false;
        }
        if (run) {
            playTurn();
        }
    }

    private void playTurn() {
        try {
            statusLogger.writeln("playing");
            refreshTextAreaGame();
            if (move == null) {
                gamePlayer.playTurn(gamePlayer.solveTurn());
            } else {
                gamePlayer.playTurn(move);
                move = null;
            }
            refreshTextAreaGame();
            statusLogger.writeln("played");
        } catch (Exception ex) {
            statusLogger.writeln("[ERROR] while playing -> " + ex.getMessage());
        }
    }

    private synchronized void refreshTextAreaGame() {
        statusLogger.writeTable(gamePlayer.toString());
    }

    private void sleep() {
        yield();
        try {
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            statusLogger.writeln("[ERROR] in ThreadSleep -> " + ex.getMessage());
        }
    }

    private synchronized void stopRun() {
        run = false;
    }
}
