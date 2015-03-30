/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author anty
 */
public class Solver {

    private List<Point> availablePoints;
    private final int[][] board;
    private List<PointsAndScores> rootsChildrenScores;
    protected int winLength = 5;

    public Solver(int[][] board) {
        this.board = board;
    }

    public boolean isGameOver() {
        //Game is over is someone has won, or board is full (draw)
        return (hasWon(1) || hasWon(2) || getAvailableStates().isEmpty());
    }

    private boolean won(Point startPosition, int xm, int ym, int player) {
        if (board[startPosition.x][startPosition.y] != player) {
            return false;
        }

        return wonWhile(startPosition, xm, ym, player) + wonWhile(startPosition, -xm, -ym, player) + 1 >= winLength;
    }

    private int wonWhile(Point startPosition, int xm, int ym, int player) {
        int x = startPosition.x, y = startPosition.y, len = 0;
        while (x > 0 && y > 0 && x < board.length - 1 && y < board[x].length - 1) {
            x = x + xm;
            y = y + ym;
            if (board[x][y] == player) {
                len++;
            } else {
                break;
            }
        }
        return len;
    }

    public boolean hasWon(int player) { // X = 1; O = 2;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (won(new Point(i, j), 1, 0, player) || won(new Point(i, j), 0, 1, player) || won(new Point(i, j), 1, 1, player)) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<Point> getAvailableStates() {
        availablePoints = new ArrayList<>();
        for (int i = 0; i < board.length; ++i) {
            for (int j = 0; j < board[i].length; ++j) {
                if (board[i][j] == 0) {
                    availablePoints.add(new Point(i, j));
                }
            }
        }
        return availablePoints;
    }

    private void placeAMove(Point point, int player) {
        board[point.x][point.y] = player;   //player = 1 for X, 2 for O
    }

    public Point returnBestMove() {
        int MAX = rootsChildrenScores.get(0).score;
        int best = 0;

        for (int i = 0; i < rootsChildrenScores.size(); ++i) {
            if (MAX < rootsChildrenScores.get(i).score) {
                MAX = rootsChildrenScores.get(i).score;
                best = i;
            }
        }

        return rootsChildrenScores.get(best).point;
    }

    private int returnMin(List<Integer> list) {
        int min = Integer.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) < min) {
                min = list.get(i);
                index = i;
            }
        }
        return list.get(index);
    }

    private int returnMax(List<Integer> list) {
        int max = Integer.MIN_VALUE;
        int index = -1;
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) > max) {
                max = list.get(i);
                index = i;
            }
        }
        return list.get(index);
    }

    public void callMinimax() {
        rootsChildrenScores = new ArrayList<>();
        minimax(0, 1);
    }

    public int minimax(int depth, int turn) {

        if (hasWon(1)) {
            return +1;
        }
        if (hasWon(2)) {
            return -1;
        }

        List<Point> pointsAvailable = getAvailableStates();
        if (pointsAvailable.isEmpty()) {
            return 0;
        }

        List<Integer> scores = new ArrayList<>();

        for (int i = 0; i < pointsAvailable.size(); ++i) {
            Point point = pointsAvailable.get(i);

            if (turn == 1) { //X's turn select the highest from below minimax() call
                placeAMove(point, 1);
                int currentScore = minimax(depth + 1, 2);
                scores.add(currentScore);

                if (depth == 0) {
                    rootsChildrenScores.add(new PointsAndScores(currentScore, point));
                }

            } else if (turn == 2) {//O's turn select the lowest from below minimax() call
                placeAMove(point, 2);
                scores.add(minimax(depth + 1, 1));
            }
            board[point.x][point.y] = 0; //Reset this point
        }
        return turn == 1 ? returnMax(scores) : returnMin(scores);
    }
}
