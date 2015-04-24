/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TerminalGame;

import Client.IStatusLogger;

/**
 *
 * @author anty
 */
public class SystemStatusLogger implements IStatusLogger {

    @Override
    public void clear() {
        //throw new UnsupportedOperationException("Not supported."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void writeln(String text) {
        System.out.println(text);
    }

    @Override
    public void writeTable(String text) {
        System.out.println(text);
    }

    @Override
    public void reWriteln(String text) {
        System.out.println(text);
    }

    @Override
    public void write(String text) {
        System.out.print(text);
    }
}
