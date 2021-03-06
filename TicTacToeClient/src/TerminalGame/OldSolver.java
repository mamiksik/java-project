/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TerminalGame;

import Client.PointsAndScores;
import Client.Point;
import Client.IGameSolver;
import Client.PointAndScore;
import java.util.ArrayList;
import java.util.List;

/**
 *Warning: OBSOLETE!!!
 * @author anty
 */
public class OldSolver implements IGameSolver {

    private List<Point> availablePoints;
    private final int[][] board;
    private List<PointsAndScores> rootsChildrenScores;
    private Point computersMove;
    private final int winLength = 3, maxDepth = 10;///4;

    public OldSolver(int[][] board) {
        this.board = board;
    }

    @Override
    public boolean isGameOver() {
        //Game is over is someone has won, or board is full (draw)
        return (hasWon('X') || hasWon('O') || getAvailableStates().isEmpty());
    }

    private boolean won(Point startPosition, int xm, int ym, int player) {
        if (board[startPosition.getX()][startPosition.getY()] != player) {
            return false;
        }
        //int len = wonWhile(startPosition, xm, ym, player) + wonWhile(startPosition, -xm, -ym, player) + 1;
        //System.out.println(player + ". LEN: " + len);
        return wonWhile(startPosition, xm, ym, player) + wonWhile(startPosition, -xm, -ym, player) + 1 >= winLength;
    }

    private int wonWhile(Point startPosition, int xm, int ym, int player) {
        int x = startPosition.getX() + xm, y = startPosition.getY() + ym, len = 0;
        while (x >= 0 && y >= 0 && x < board.length && y < board[x].length) {
            if (board[x][y] == player) {
                len++;
            } else {
                break;
            }
            x += xm;
            y += ym;
        }
        //System.out.println(player + ". Sx: " + startPosition.x + ", Sy: " + startPosition.y + ", x: " + x + ", y: " + y + ", xm: " + xm + ", ym: " + ym + ", LEN: " + len);
        return len;
    }

    @Override
    public boolean hasWon(char player2) { // X = 1; O = 2;
        int player = player2 == 'X' ? 1 : 2;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (won(new Point(i, j), 1, 0, player) || won(new Point(i, j), 0, 1, player) || won(new Point(i, j), 1, 1, player)) {
                    //System.out.println(player + ". " + i + ", " + j + ": true");
                    return true;
                }
                //System.out.println(player + ". " + i + ", " + j + ": false");
            }
        }
        //System.out.println(player + ". false");
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
        board[point.getX()][point.getY()] = player;   //player = 1 for X, 2 for O
    }

    private Point returnBestMove() {
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

    public void printField() {
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                System.out.print(y + "" + x + " ");
            }
            System.out.println();
        }

        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                System.out.print(board[y][x] + " ");
            }
            System.out.println();
        }
    }

    private void callMinimax() {
        rootsChildrenScores = new ArrayList<>();
        minimax(0, 1);
    }

    private int minimax(int depth, int turn) {
        //System.out.println("Depth: " + depth + " Turn: " + turn);
        //printField();

        if (hasWon('X')) {
            return +1;
        }
        if (hasWon('O')) {
            return -1;
        }

        List<Point> pointsAvailable = getAvailableStates();
        if (pointsAvailable.isEmpty()) {
            //System.out.println("EMPTY");
            return 0;
        }

        List<Integer> scores = new ArrayList<>();

        for (int i = 0; i < pointsAvailable.size(); ++i) {
            Point point = pointsAvailable.get(i);

            if (turn == 1) { //X's turn select the highest from below minimax() call
                placeAMove(point, 1);
                int currentScore = depth < maxDepth ? minimax(depth + 1, 2) : 0;
                scores.add(currentScore);

                if (depth == 0) {
                    System.out.println("rootsChildrenScores.add(currentScore: " + currentScore + ", point: " + point + ")");
                    rootsChildrenScores.add(new PointsAndScores(currentScore, point));
                }

            } else if (turn == 2) {//O's turn select the lowest from below minimax() call
                placeAMove(point, 2);
                scores.add(depth < maxDepth ? minimax(depth + 1, 1) : 0);
            }
            board[point.getX()][point.getY()] = 0; //Reset this point
        }
        return turn == 1 ? returnMax(scores) : returnMin(scores);
    }

    @Override
    public Point getBestMove() {
        return computersMove;
    }

    @Override
    public void solve() throws Exception {
        computersMove = null;
        minimaxV2(0, 1);
        if (computersMove == null) {
            throw new Exception("[ERROR] while solving next turn");
        }
    }

    private int minimaxV2(int depth, int turn) {
        //System.out.println("Depth: " + depth + ", Turn: " + turn);

        if (depth >= maxDepth) {
            return 0;
        }
        if (hasWon('X')) {
            return +1;
        }
        if (hasWon('O')) {
            return -1;
        }

        List<Point> pointsAvailable = getAvailableStates();
        //System.out.println("Available: " + pointsAvailable.size());
        if (pointsAvailable.isEmpty()) {
            return 0;
        }

        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;

        for (int i = 0; i < pointsAvailable.size(); ++i) {
            Point point = pointsAvailable.get(i);
            //System.out.println("Index: " + i + ", Point: " + point);
            if (turn == 1) {
                placeAMove(point, 1);
                int currentScore = minimaxV2(depth + 1, 2);
                max = Math.max(currentScore, max);

                if (depth == 0) {
                    System.out.println("Score for position " + (i + 1) + " = " + currentScore);
                }
                if (currentScore >= 0) {
                    if (depth == 0) {
                        computersMove = point;
                    }
                }
                if (currentScore == 1) {
                    board[point.getX()][point.getY()] = 0;
                    break;
                }
                if (i == pointsAvailable.size() - 1 && max < 0) {
                    if (depth == 0) {
                        computersMove = point;
                    }
                }
            } else if (turn == 2) {
                placeAMove(point, 2);
                int currentScore = minimaxV2(depth + 1, 1);
                min = Math.min(currentScore, min);
                if (min == -1) {
                    board[point.getX()][point.getY()] = 0;
                    break;
                }
            }
            board[point.getX()][point.getY()] = 0; //Reset this point
        }
        return turn == 1 ? max : min;
    }

    @Override
    public PointAndScore[] getMaxPointsAndScores() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PointAndScore[] getAllPointsAndScores() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
