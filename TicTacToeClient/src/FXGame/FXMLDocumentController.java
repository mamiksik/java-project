/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FXGame;

import Client.IStatusLogger;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

/**
 *
 * @author anty
 */
public class FXMLDocumentController implements Initializable {

    private IStatusLogger statusLogger;
    private Controller controller;
    private FXGame fxGame;
    @FXML
    private Button buttonConnect, buttonDisconnect, buttonPlay;
    @FXML
    private TextField textFieldPort, textFieldIP;
    @FXML
    private CheckBox checkBoxAutoPlay;
    @FXML
    private TextArea textAreaGame, textAreaLogger;

    @FXML
    private void handleButtonConnectAction(ActionEvent event) {
        if (fxGame != null) {
            fxGame.disconnect();
        }
        fxGame = new FXGame(controller, statusLogger);
        fxGame.connect(textFieldIP.getText(), Integer.parseInt(textFieldPort.getText()));
    }

    @FXML
    private void handleButtonDisconnectAction(ActionEvent event) {
        if (fxGame != null) {
            fxGame.disconnect();
        }
        fxGame = null;
    }

    @FXML
    private void handleButtonPlayAction(ActionEvent event) {
        if (fxGame != null) {
            fxGame.play();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        statusLogger = new FXStatusLogger(textAreaLogger, textAreaGame);
        statusLogger.writeln("Select CONNECT from menu to START");
        controller = new Controller(buttonConnect, buttonDisconnect, buttonPlay, checkBoxAutoPlay);
    }

}
