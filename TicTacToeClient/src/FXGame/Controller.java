/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FXGame;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

/**
 *
 * @author anty
 */
public class Controller {
    
    public Button buttonConnect, buttonDisconnect, buttonPlay;
    public CheckBox checkBoxAutoPlay;

    public Controller(Button buttonConnect, Button buttonDisconnect, Button buttonPlay, CheckBox checkBoxAutoPlay) {
        this.buttonConnect = buttonConnect;
        this.buttonDisconnect = buttonDisconnect;
        this.buttonPlay = buttonPlay;
        this.checkBoxAutoPlay = checkBoxAutoPlay;
    }
}
