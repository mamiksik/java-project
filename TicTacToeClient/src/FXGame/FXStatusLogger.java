/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FXGame;

import Client.IStatusLogger;
import javafx.scene.control.TextArea;

/**
 *
 * @author anty
 */
public final class FXStatusLogger implements IStatusLogger {

    TextArea log, table;
    private String logText = "", logTextLine = "";

    public FXStatusLogger(TextArea log, TextArea table) {
        this.log = log;
        this.table = table;
        writeln("Logging STARTED");
    }

    @Override
    public synchronized void clear() {
        logText = "";
        logTextLine = "";
        refresh();
    }

    @Override
    public synchronized void writeln(String text) {
        logText = logTextLine + "\n" + logText;
        logTextLine = text;
        refresh();
    }

    @Override
    public synchronized void reWriteln(String text) {
        logTextLine = text;
        refresh();
    }

    @Override
    public synchronized void write(String text) {
        logTextLine += text;
        refresh();
    }

    @Override
    public synchronized void writeTable(String text) {
        table.setText(text);
    }

    private void refresh() {
        log.setText(logTextLine + "\n" + logText);
    }
}
