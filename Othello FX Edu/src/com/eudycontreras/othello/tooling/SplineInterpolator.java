package com.eudycontreras.othello.tooling;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point2D;

public class SplineInterpolator implements Interpolator{

    private final float points[];
    private final List<PointUnit> normalisedCurve;

    public SplineInterpolator(float x1, float y1, float x2, float y2) {
        points = new float[]{ x1, y1, x2, y2 };

        final List<Float> baseLengths = new ArrayList<>();
        float prevX = 0;
        float prevY = 0;
        float cumulativeLength = 0;
        for (float t = 0; t <= 1; t += 0.01) {
            Point2D xy = getXY(t);
            float length = (float) (cumulativeLength + Math.sqrt((xy.getX() - prevX) * (xy.getX() - prevX)
                                            + (xy.getY() - prevY) * (xy.getY() - prevY)));

            baseLengths.add(length);
            cumulativeLength = length;
            prevX = (float) xy.getX();
            prevY = (float) xy.getY();
        }

        normalisedCurve = new ArrayList<>(baseLengths.size());
        int index = 0;
        for (float t = 0; t <= 1; t += 0.01) {
            float length = baseLengths.get(index++);
            float normalLength = length / cumulativeLength;
            normalisedCurve.add(new PointUnit(t, normalLength));
        }
    }

	@Override
    public float interpolate(float fraction) {
        int low = 1;
        int high = normalisedCurve.size() - 1;
        int mid = 0;
        while (low <= high) {
            mid = (low + high) / 2;

            if (fraction > normalisedCurve.get(mid).getPoint()) {
                low = mid + 1;
            } else if (mid > 0 && fraction < normalisedCurve.get(mid - 1).getPoint()) {
                high = mid - 1;
            } else {
                break;
            }
        }
        /*
         * The answer lies between the "mid" item and its predecessor.
         */
        final PointUnit prevItem = normalisedCurve.get(mid - 1);
        final float prevFraction = prevItem.getPoint();
        final float prevT = prevItem.getDistance();

        final PointUnit item = normalisedCurve.get(mid);
        final float proportion = (fraction - prevFraction) / (item.getPoint() - prevFraction);
        final float interpolatedT = prevT + (proportion * (item.getDistance() - prevT));
        return getY(interpolatedT);
    }

    protected Point2D getXY(float t) {
        final float invT = 1 - t;
        final float b1 = 3 * t * invT * invT;
        final float b2 = 3 * t * t * invT;
        final float b3 = t * t * t;
        final Point2D xy = new Point2D((b1 * points[0]) + (b2 * points[2]) + b3, (b1 * points[1]) + (b2 * points[3]) + b3);
        return xy;
    }

    protected float getY(float t) {
        final float invT = 1 - t;
        final float b1 = 3 * t * invT * invT;
        final float b2 = 3 * t * t * invT;
        final float b3 = t * t * t;
        return (b1 * points[2]) + (b2 * points[3]) + b3;
    }

    public class PointUnit {

        private final float distance;
        private final float point;

        public PointUnit(float distance, float point) {
            this.distance = distance;
            this.point = point;
        }

        public float getDistance() {
            return distance;
        }

        public float getPoint() {
            return point;
        }

    }

}