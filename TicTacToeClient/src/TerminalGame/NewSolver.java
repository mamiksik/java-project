/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TerminalGame;

import Client.Point;
import Client.IGameField;
import Client.IGameSolver;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 *
 * @author anty
 */
public class NewSolver implements IGameSolver {

    private final Random random = new Random();
    private IGameField gameField;
    private Point move;
    private final char color;
    private final int winLength = 5;

    public NewSolver(IGameField gameField, char color) {
        this.gameField = gameField;
        this.color = color;
    }

    @Override
    public boolean isGameOver() {
        return (hasWon('X') || hasWon('O') || getPlaces('_').isEmpty());
    }

    @Override
    public boolean hasWon(char player) {
        char[][] board = gameField.getField();
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                if (board[x][y] == player) {
                    if (getLineLength(new Point(x, y), board, 1, 0, player) >= winLength || getLineLength(new Point(x, y), board, 0, 1, player) >= winLength
                            || getLineLength(new Point(x, y), board, 1, 1, player) >= winLength || getLineLength(new Point(x, y), board, -1, 1, player) >= winLength) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private int getLineLength(Point startPosition, char[][] board, int xm, int ym, char player) {
        return getLineLen(startPosition, board, xm, ym, player) + getLineLen(startPosition, board, -xm, -ym, player) + 1;
    }

    private int getLineLen(Point startPosition, char[][] board, int xm, int ym, char player) {
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

    private int getFreeLength(Point startPosition, char[][] board, int xm, int ym, char player) {
        return getFreeLen(startPosition, board, xm, ym, player) + getFreeLen(startPosition, board, -xm, -ym, player) + 1;
    }

    private int getFreeLen(Point startPosition, char[][] board, int xm, int ym, char player) {
        int x = startPosition.x + xm, y = startPosition.y + ym, len = 0;
        while (x >= 0 && y >= 0 && x < board.length && y < board[x].length) {
            if (board[x][y] == player || board[x][y] == '_') {
                len++;
            } else {
                break;
            }
            x += xm;
            y += ym;
        }
        return len;
    }

    private List<Point> getPlaces(char player) {
        List<Point> availablePoints = new ArrayList<>();
        char[][] board = gameField.getField();
        for (int x = 0; x < board.length; ++x) {
            for (int y = 0; y < board[x].length; ++y) {
                if (board[x][y] == player) {
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
        return move.copy();
    }

    @Override
    public void solve() throws Exception {
        if (isGameOver()) {
            throw new Exception("[ERROR] while playing next turn - game is over");
        }

        if (gameField.isFullFree()) {
            move = new Point(gameField.getSize() / 2, gameField.getSize() / 2);
            return;
        }

        move = checkAllStates();
    }

    private Point checkAllStates() {
        List<Point> availablePoints = getPlaces('_');
        List<Float> availableScores = new ArrayList<>();
        for (Point anAvailablePoint : availablePoints) {
            availableScores.add(checkPoint(anAvailablePoint));
        }

        List<Integer> indexes = getMaxIndexes(availableScores);
        return availablePoints.get(indexes.get(random.nextInt(indexes.size())));
    }

    private float checkPoint(Point point) {
        float stateConstant = 0;
        char revColor = getRevertedColor(color);

        if ((getLineLength(point, gameField.getField(), 1, 0, revColor) >= winLength - 1 && getFreeLength(point, gameField.getField(), 1, 0, revColor) >= winLength)
                || (getLineLength(point, gameField.getField(), 0, 1, revColor) >= winLength - 1 && getFreeLength(point, gameField.getField(), 0, 1, revColor) >= winLength)
                || (getLineLength(point, gameField.getField(), 1, 1, revColor) >= winLength - 1 && getFreeLength(point, gameField.getField(), 1, 1, revColor) >= winLength)
                || (getLineLength(point, gameField.getField(), -1, 1, revColor) >= winLength - 1 && getFreeLength(point, gameField.getField(), -1, 1, revColor) >= winLength)) {
            stateConstant = Float.MAX_VALUE / 2f;
        }

        if (getLineLength(point, gameField.getField(), 1, 0, revColor) >= winLength || getLineLength(point, gameField.getField(), 0, 1, revColor) >= winLength
                || getLineLength(point, gameField.getField(), 1, 1, revColor) >= winLength || getLineLength(point, gameField.getField(), -1, 1, revColor) >= winLength) {
            stateConstant = Float.MAX_VALUE / 1.5f;
        }

        if (getLineLength(point, gameField.getField(), 1, 0, color) >= winLength || getLineLength(point, gameField.getField(), 0, 1, color) >= winLength
                || getLineLength(point, gameField.getField(), 1, 1, color) >= winLength || getLineLength(point, gameField.getField(), -1, 1, color) >= winLength) {
            stateConstant = Float.MAX_VALUE / 1.2f;
        }

        List<Float> lengths = new ArrayList<>();
        lengths.add(getFreeLength(point, gameField.getField(), 1, 0, color) >= winLength
                ? (float) getLineLength(point, gameField.getField(), 1, 0, color)
                : (float) getLineLength(point, gameField.getField(), 1, 0, color) / (float) winLength);

        lengths.add(getFreeLength(point, gameField.getField(), 0, 1, color) >= winLength
                ? (float) getLineLength(point, gameField.getField(), 0, 1, color)
                : (float) getLineLength(point, gameField.getField(), 0, 1, color) / (float) winLength);

        lengths.add(getFreeLength(point, gameField.getField(), 1, 1, color) >= winLength
                ? (float) getLineLength(point, gameField.getField(), 1, 1, color)
                : (float) getLineLength(point, gameField.getField(), 1, 1, color) / (float) winLength);

        lengths.add(getFreeLength(point, gameField.getField(), -1, 1, color) >= winLength
                ? (float) getLineLength(point, gameField.getField(), -1, 1, color)
                : (float) getLineLength(point, gameField.getField(), -1, 1, color) / (float) winLength);

        int index = getMaxIndex(lengths);

        List<Float> revLengths = new ArrayList<>();
        revLengths.add(getFreeLength(point, gameField.getField(), 1, 0, revColor) >= winLength
                ? (float) getLineLength(point, gameField.getField(), 1, 0, revColor)
                : (float) getLineLength(point, gameField.getField(), 1, 0, revColor) / (float) winLength);

        revLengths.add(getFreeLength(point, gameField.getField(), 0, 1, revColor) >= winLength
                ? (float) getLineLength(point, gameField.getField(), 0, 1, revColor)
                : (float) getLineLength(point, gameField.getField(), 0, 1, revColor) / (float) winLength);

        revLengths.add(getFreeLength(point, gameField.getField(), 1, 1, revColor) >= winLength
                ? (float) getLineLength(point, gameField.getField(), 1, 1, revColor)
                : (float) getLineLength(point, gameField.getField(), 1, 1, revColor) / (float) winLength);

        revLengths.add(getFreeLength(point, gameField.getField(), -1, 1, revColor) >= winLength
                ? (float) getLineLength(point, gameField.getField(), -1, 1, revColor)
                : (float) getLineLength(point, gameField.getField(), -1, 1, revColor) / (float) winLength);

        int revIndex = getMaxIndex(revLengths);
        float revPrum = getPrum(revLengths);

        return stateConstant + lengths.get(index) + (revLengths.get(revIndex) / (float) winLength) + ((revPrum / (float) winLength) / 10f);
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
    
    private float getPrum(List<Float> list) {
        int full = 0;
        for (Float listNum : list) {
            full += listNum;
        }
        return full / (float) list.size();
    }
}
