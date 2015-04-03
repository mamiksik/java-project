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
public class Point {

    public int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public Point copy() {
        return new Point(x, y);
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }
}
