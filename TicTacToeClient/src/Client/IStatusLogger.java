/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

/**
 *
 * @author anty
 */
public interface IStatusLogger {

    public void clearText();

    public void writeText(String text);

    public void writeTable(String text);
}
