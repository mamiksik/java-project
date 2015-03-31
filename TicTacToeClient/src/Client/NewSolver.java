/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 *
 * @author anty
 */
public class NewSolver implements IGameSolver {
    Random random = new Random();
    IGameField gameField;
    Point move;
    char color;
    private final int winLength = 5;

    public NewSolver(IGameField gameField, char color) {
        this.gameField = gameField;
        this.color = color;
    }

    @Override
    public boolean isGameOver() {
        return (hasWon('X') || hasWon('O') || getAvailableStates().isEmpty());
    }

    @Override
    public boolean hasWon(char player) {
        char[][] board = gameField.getField();
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                if (board[x][y] == player) {
                    if (won(new Point(x, y), board, 1, 0, player) >= winLength || won(new Point(x, y), board, 0, 1, player) >= winLength
                            || won(new Point(x, y), board, 1, 1, player) >= winLength || won(new Point(x, y), board, -1, 1, player) >= winLength) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private int won(Point startPosition, char[][] board, int xm, int ym, char player) {
        return wonWhile(startPosition, board, xm, ym, player) + wonWhile(startPosition, board, -xm, -ym, player) + 1;
    }

    private int wonWhile(Point startPosition, char[][] board, int xm, int ym, char player) {
        int x = startPosition.x + xm, y = startPosition.y + ym, len = 0;
        while (x >= 0 && y >= 0 && x < board.length && y < board[x].length) {
            if (board[x][y] == player) {
                len++;
            } else {
                break;
            }
            x += xm;
            y += ym;
        }
        return len;
    }

    private List<Point> getAvailableStates() {
        List<Point> availablePoints = new ArrayList<>();
        char[][] board = gameField.getField();
        for (int x = 0; x < board.length; ++x) {
            for (int y = 0; y < board[x].length; ++y) {
                if (board[x][y] == '_') {
                    availablePoints.add(new Point(x, y));
                }
            }
        }
        return availablePoints;
    }

    @Override
    public void setGameField(IGameField gameField) {
        this.gameField = gameField;
    }

    /**
     *
     * @return
     */
    @Override
    public Point getBestMove() {
        return move;
    }

    @Override
    public boolean solve() {
        if (isGameOver()) {
            return false;
        }
        move = checkFullFree();
        if (move != null) {
            return true;
        }
        move = checkAllStates();
        return move != null;
    }

    private Point checkFullFree() {
        if (gameField.isFullFree()) {
            return new Point(gameField.getSize() / 2, gameField.getSize() / 2);
        }
        return null;
    }

    private Point checkAllStates() {
        List<Point> availablePoints = getAvailableStates();
        List<Float> availableScores = new ArrayList<>();
        for (Point anAvailablePoint : availablePoints) {
            availableScores.add(checkPoint(anAvailablePoint));
        }
        
        List<Integer> indexes = getMaxIndexes(availableScores);
        return availablePoints.get(indexes.get(random.nextInt(indexes.size())));
    }

    private float checkPoint(Point point) {
        char revColor = getRevertedColor(color);
        if (won(point, gameField.getField(), 1, 0, color) >= winLength || won(point, gameField.getField(), 0, 1, color) >= winLength
                || won(point, gameField.getField(), 1, 1, color) >= winLength || won(point, gameField.getField(), -1, 1, color) >= winLength)
            return Float.MAX_VALUE;
        
        if (won(point, gameField.getField(), 1, 0, revColor) >= winLength || won(point, gameField.getField(), 0, 1, revColor) >= winLength
                || won(point, gameField.getField(), 1, 1, revColor) >= winLength || won(point, gameField.getField(), -1, 1, revColor) >= winLength)
            return Float.MAX_VALUE / 1.5f;
        
        if (won(point, gameField.getField(), 1, 0, revColor) >= winLength - 1 || won(point, gameField.getField(), 0, 1, revColor) >= winLength - 1
                || won(point, gameField.getField(), 1, 1, revColor) >= winLength - 1 || won(point, gameField.getField(), -1, 1, revColor) >= winLength - 1)
            return Float.MAX_VALUE / 2f;
        
        List<Float> lengths = new ArrayList<>();
        lengths.add((float)won(point, gameField.getField(), 1, 0, color));
        lengths.add((float)won(point, gameField.getField(), 0, 1, color));
        lengths.add((float)won(point, gameField.getField(), 1, 1, color));
        lengths.add((float)won(point, gameField.getField(), -1, 1, color));
        int index = getMaxIndex(lengths);
        
        List<Float> revLengths = new ArrayList<>();
        revLengths.add((float)won(point, gameField.getField(), 1, 0, revColor));
        revLengths.add((float)won(point, gameField.getField(), 0, 1, revColor));
        revLengths.add((float)won(point, gameField.getField(), 1, 1, revColor));
        revLengths.add((float)won(point, gameField.getField(), -1, 1, revColor));
        int revIndex = getMaxIndex(revLengths);
        
        return (float)lengths.get(index) + ((float)revLengths.get(revIndex) / (float)winLength);
    }
    
    private char getRevertedColor(char color) {
        switch (color) {
            case 'X':
                return 'O';
            case 'O':
                return 'X';
            default:
                return '_';
        }
    }

    private List<Integer> getMaxIndexes(List<Float> list) {
        List<Integer> index = new ArrayList<>();
        index.add(0);
        for (int i = 0; i < list.size(); i++) {
            if (Objects.equals(list.get(i), list.get(index.get(0)))) {
                index.add(i);
            }
            if (list.get(i) > list.get(index.get(0))) {
                index.clear();
                index.add(i);
            }
        }
        return index;
    }
    
    private int getMaxIndex(List<Float> list) {
        int index = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) > list.get(index)) {
                index = i;
            }
        }
        return index;
    }
}
