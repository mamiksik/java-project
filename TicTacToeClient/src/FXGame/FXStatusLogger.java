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
public class FXStatusLogger implements IStatusLogger {

    TextArea log, table;

    public FXStatusLogger(TextArea log, TextArea table) {
        this.log = log;
        this.table = table;
        log.clear();
        writeText("Logging STARTED");
    }

    @Override
    public void clearText() {
        log.setText("");
    }

    @Override
    public void writeText(String text) {
        log.setText(text + "\n" + log.getText());
    }

    @Override
    public void writeTable(String text) {
        table.setText(text);
    }
}
