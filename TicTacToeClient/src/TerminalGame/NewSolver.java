package TerminalGame;

import Client.Point;
import Client.IGameField;
import Client.IGameSolver;
import Client.PointAndScore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Anty
 */
public class NewSolver implements IGameSolver {

    private final Random random = new Random();
    private final IGameField gameField;
    private Point move = null;
    private List<PointAndScore> maxPointsAndScores = null, allPointsAndScores = null;
    private final char color;
    private final int winLength = 5;

    public NewSolver(IGameField gameField, char color) {
        this.gameField = gameField;
        this.color = color;
    }

    @Override
    public synchronized boolean isGameOver() {
        return (hasWon('X') || hasWon('O') || gameField.getPlaces('_').isEmpty());
    }

    @Override
    public synchronized boolean hasWon(char player) {
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
        return len;
    }

    private int getFreeLength(Point startPosition, char[][] board, int xm, int ym, char player) {
        return getFreeLen(startPosition, board, xm, ym, player) + getFreeLen(startPosition, board, -xm, -ym, player) + 1;
    }

    private int getFreeLen(Point startPosition, char[][] board, int xm, int ym, char player) {
        int x = startPosition.getX() + xm, y = startPosition.getY() + ym, len = 0;
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

    @Override
    public synchronized Point getBestMove() {
        return move.copy();
    }

    @Override
    public synchronized PointAndScore[] getMaxPointsAndScores() {
        return maxPointsAndScores.toArray(new PointAndScore[maxPointsAndScores.size()]);
    }

    @Override
    public synchronized PointAndScore[] getAllPointsAndScores() {
        return allPointsAndScores.toArray(new PointAndScore[allPointsAndScores.size()]);
    }

    @Override
    public synchronized void solve() throws Exception {
        if (isGameOver()) {
            throw new Exception("while solving next turn -> game is over");
        }

        try {
            this.allPointsAndScores = checkAllStates();
            this.maxPointsAndScores = getMaxPointsAndScores(allPointsAndScores);

            if (gameField.isFullFree()) {
                move = new Point(gameField.getSize() / 2, gameField.getSize() / 2);
                return;
            }
            move = maxPointsAndScores.get(random.nextInt(maxPointsAndScores.size())).getPoint();
        } catch (Exception e) {
            throw new Exception("while solving next turn -> solver error -> " + e.getMessage());
        }

        if (move == null) {
            throw new Exception("while solving next turn -> solver error -> null move");
        }
    }

    private List<PointAndScore> checkAllStates() {
        List<Point> availablePoints = gameField.getPlaces('_');
        List<PointAndScore> availableScores = new ArrayList<>();
        for (Point anAvailablePoint : availablePoints) {
            availableScores.add(new PointAndScore(anAvailablePoint, checkPoint(anAvailablePoint)));
        }
        return availableScores;
    }

    private float[] checkPoint(Point point) {
        final char revColor = getRevertedColor(color);
        final float[] stateConstant = getStateConstant(point);

        List<Float> lengths = getLengths(point, color);
        int index = getMaxIndex(lengths);
        float diameter = getDiameter(lengths);

        List<Float> revLengths = getLengths(point, revColor);
        int revIndex = getMaxIndex(revLengths);
        float revDiameter = getDiameter(revLengths);

        return new float[]{stateConstant[0], stateConstant[1], stateConstant[2], stateConstant[3], lengths.get(index), revLengths.get(revIndex), diameter, revDiameter};
    }

    private float[] getStateConstant(Point point) {
        final char revColor = getRevertedColor(color);
        final char[][] field = gameField.getField();
        final int[] lineLengths = new int[]{
            getLineLength(point, field, 1, 0, color),
            getLineLength(point, field, 0, 1, color),
            getLineLength(point, field, 1, 1, color),
            getLineLength(point, field, -1, 1, color)
        };
        final int[] revLineLengths = new int[]{
            getLineLength(point, field, 1, 0, revColor),
            getLineLength(point, field, 0, 1, revColor),
            getLineLength(point, field, 1, 1, revColor),
            getLineLength(point, field, -1, 1, revColor)
        };
        final int[] freeLengths = new int[]{
            getFreeLength(point, field, 1, 0, revColor),
            getFreeLength(point, field, 0, 1, revColor),
            getFreeLength(point, field, 1, 1, revColor),
            getFreeLength(point, field, -1, 1, revColor)
        };
        final float[] stateConstant = new float[4];

        if (lineLengths[0] >= winLength) {
            stateConstant[0] += 1000;
        }
        if (lineLengths[1] >= winLength) {
            stateConstant[0] += 1000;
        }
        if (lineLengths[2] >= winLength) {
            stateConstant[0] += 1000;
        }
        if (lineLengths[3] >= winLength) {
            stateConstant[0] += 1000;
        }

        if (revLineLengths[0] >= winLength) {
            stateConstant[1] += 500;
        }
        if (revLineLengths[1] >= winLength) {
            stateConstant[1] += 500;
        }
        if (revLineLengths[2] >= winLength) {
            stateConstant[1] += 500;
        }
        if (revLineLengths[3] >= winLength) {
            stateConstant[1] += 500;
        }

        if (revLineLengths[0] >= winLength - 1 && freeLengths[0] >= winLength) {
            stateConstant[2] += 100;
        }
        if (revLineLengths[1] >= winLength - 1 && freeLengths[1] >= winLength) {
            stateConstant[2] += 100;
        }
        if (revLineLengths[2] >= winLength - 1 && freeLengths[2] >= winLength) {
            stateConstant[2] += 100;
        }
        if (revLineLengths[3] >= winLength - 1 && freeLengths[3] >= winLength) {
            stateConstant[2] += 100;
        }

        if (revLineLengths[0] >= winLength - 2 && freeLengths[0] >= winLength) {
            stateConstant[3] += 10;
        }
        if (revLineLengths[1] >= winLength - 2 && freeLengths[1] >= winLength) {
            stateConstant[3] += 10;
        }
        if (revLineLengths[2] >= winLength - 2 && freeLengths[2] >= winLength) {
            stateConstant[3] += 10;
        }
        if (revLineLengths[3] >= winLength - 2 && freeLengths[3] >= winLength) {
            stateConstant[3] += 10;
        }

        if (stateConstant[3] < 20) {
            stateConstant[3] = 0;
        }
        return stateConstant;
    }

    private List<Float> getLengths(Point point, char color) {
        List<Float> lengths = new ArrayList<>();
        final char[][] field = gameField.getField();
        lengths.add(getFreeLength(point, field, 1, 0, color) >= winLength
                ? (float) getLineLength(point, field, 1, 0, color)
                : (float) getLineLength(point, field, 1, 0, color) / (float) winLength);

        lengths.add(getFreeLength(point, field, 0, 1, color) >= winLength
                ? (float) getLineLength(point, field, 0, 1, color)
                : (float) getLineLength(point, field, 0, 1, color) / (float) winLength);

        lengths.add(getFreeLength(point, field, 1, 1, color) >= winLength
                ? (float) getLineLength(point, field, 1, 1, color)
                : (float) getLineLength(point, field, 1, 1, color) / (float) winLength);

        lengths.add(getFreeLength(point, field, -1, 1, color) >= winLength
                ? (float) getLineLength(point, field, -1, 1, color)
                : (float) getLineLength(point, field, -1, 1, color) / (float) winLength);
        return lengths;
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

    private List<PointAndScore> getMaxPointsAndScores(List<PointAndScore> list) {
        List<PointAndScore> maxList = new ArrayList<>();
        maxList.add(list.get(0));
        for (int i = 0; i < list.size(); i++) {
            if (Arrays.equals(list.get(i).getScore(), maxList.get(0).getScore())) {
                maxList.add(list.get(i));
                continue;
            }
            for (int in = 0; in < maxList.get(0).getScore().length; in++) {
                if (list.get(i).getScore()[in] < maxList.get(0).getScore()[in]) {
                    break;
                }
                if (list.get(i).getScore()[in] > maxList.get(0).getScore()[in]) {
                    maxList.clear();
                    maxList.add(list.get(i));
                    break;
                }
            }
        }
        return maxList;
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

    private float getDiameter(List<Float> list) {
        int full = 0;
        for (Float listNum : list) {
            full += listNum;
        }
        return full / (float) list.size();
    }
}
