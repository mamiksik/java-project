/*
* The MIT License
*
* Copyright 2015 LukeMcNemee.
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
*/

package Client;

import java.io.*;
/**
 *
 * @author LukeMcNemee
 */
public class ClientSimulator implements IClient {
    int size = 10;
    char color = 'X';
    char[][] gird;
    
    @Override
    public void connect(String IP, int port)  throws IOException {
        createGird();
    }
    
    private void createGird() {
        gird = new char[size][size];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size;y++) {
                gird[x][y] = '_';
            }
        }
    }

    @Override
    public void disconnect()throws IOException{
        
    }

    @Override
    public State getStatus() throws IOException{
        String response = "PLAY";
        System.out.println(response);
        if(response.equals("DEFEAT")){
            return State.DEFEAT;
        }
        if(response.equals("PLAY")){
            return State.PLAY;
        }
        if(response.equals("WAIT")){
            return State.WAIT;
        }
        if(response.equals("WIN")){
            return State.WIN;
        }        
        return null;
    }

    @Override
    public int getSize() throws IOException{
        int response = size;
        System.out.println(response);
        return response;
    }

    @Override
    public char getColor() throws IOException{
        char response = color;
        System.out.println(response);
        return response;
    }

    @Override
    public char getGrid(Point point) throws IOException{
        char response = gird[point.x][point.y];
        System.out.println(response);
        return response;
    }

    @Override
    public int play(Point point) throws IOException {
        int response = -2;
        if (gird[point.x][point.y] == '_') {
            gird[point.x][point.y] = color;
            response = 0;
        } else
            response = -3;
        
        System.out.println(response);
        return response;
    }
    
}
