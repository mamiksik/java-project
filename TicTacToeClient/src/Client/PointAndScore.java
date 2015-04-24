package Client;

import java.util.Arrays;

/**
 * Created by anty on 23.4.15.
 *
 * @author anty
 */
public class PointAndScore {

    private final Point point;
    private final float[] score;

    public PointAndScore(Point point, float[] score) {
        this.point = point;
        this.score = score;
    }

    public Point getPoint() {
        return point;
    }

    public float[] getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "[" + point.toString() + ", " + Arrays.toString(score) + "]";
    }

    public static float[] findScoreByPoint(Point pointToFind, PointAndScore[] pointAndScores) {
        for (PointAndScore pointAndScore : pointAndScores) {
            if (pointAndScore.getPoint().getX() == pointToFind.getX() && pointAndScore.getPoint().getY() == pointToFind.getY()) {
                return pointAndScore.getScore();
            }
        }
        return null;
    }
}
