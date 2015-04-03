/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TerminalGame;

import java.io.IOException;

/**
 *
 * @author anty
 */
public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        Game game = new Game("127.0.0.1", 3248, new SystemStatusLogger());
        game.run();
        game.close();
    }
}
