package com.eudycontreras.othello.tooling;

/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */


/**
 * Provides a number of built-in implementations of the {@link Interpolator}
 * interface.
 *
 * @author Chris Campbell
 */
public class Interpolators {

    private static final Interpolator DISCRETE_INSTANCE = new Discrete();
    private static final Interpolator LINEAR_INSTANCE = new Linear();
    private static final Interpolator EASING_INSTANCE = new Easing(0.2f, 0.2f);

    /**
     * Private constructor to prevent instantiation.
     */
    private Interpolators() {
    }

    /**
     * Returns a trivial implementation of Interpolator that provides discrete
     * time interpolation (the return value of {@code interpolate()} is 1f
     * only when the input "t" is 1f, and 0f otherwise).
     *
     * @return an instance of Interpolator that provides simple discrete
     *     time interpolation
     */
    public static Interpolator getDiscreteInstance() {
        return DISCRETE_INSTANCE;
    }

    /**
     * Returns a trivial implementation of Interpolator that provides linear time
     * interpolation (the input "t" value is simply returned unmodified).
     *
     * @return an instance of Interpolator that provides simple linear
     *     time interpolation
     */
    public static Interpolator getLinearInstance() {
        return LINEAR_INSTANCE;
    }

    /**
     * Returns a Interpolator instance that provides ease in/out behavior, using
     * default values of 0.2f and 0.2f for the acceleration and deceleration
     * factors, respectively.  Calling this method is equivalent to calling
     * {@code getEasingInstance(0.2f, 0.2f)}.
     *
     * @return an instance of Interpolator that provides acceleration and
     *     deceleration behavior
     */
    public static Interpolator getEasingInstance() {
        return EASING_INSTANCE;
    }

    /**
     * Returns a Interpolator instance that provides ease in/out behavior according
     * to the given acceleration and deceleration values.
     *
     * @param acceleration value in the range [0,1] indicating the fraction
     *     of time spent accelerating at the beginning of a timing cycle
     * @param deceleration value in the range [0,1] indicating the fraction
     *     of time spent decelerating at the end of a timing cycle
     * @return an instance of Interpolator that provides acceleration and
     *     deceleration behavior
     * @throws IllegalArgumentException if either the acceleration or
     *     deceleration value is outside the range [0,1]
     * @throws IllegalArgumentException if the acceleration value is
     *     greater than (1 - deceleration)
     * @throws IllegalArgumentException if the deceleration value is
     *     greater than (1 - acceleration)
     */
    public static Interpolator getEasingInstance(float acceleration,
                                                 float deceleration)
    {
        return new Easing(acceleration, deceleration);
    }

    /**
     * Returns a Interpolator instance that is shaped using the
     * spline control points defined by (x1, y1) and (x2, y2).  The anchor
     * points of the spline are implicitly defined as (0, 0) and (1, 1).
     *
     * @param x1 the X value of the first control point
     * @param y1 the Y value of the first control point
     * @param x2 the X value of the second control point
     * @param y2 the Y value of the second control point
     * @return an instance of Interpolator that is shaped by the given spline curve
     * @throws IllegalArgumentException if any of the control points
     *     is outside the range [0,1]
     */
    public static Interpolator getSplineInstance(float x1, float y1, float x2, float y2) {
        return new SplineInterpolator(x1, y1, x2, y2);
    }

    /**
     * Trivial implementation of Interpolator that provides discrete time
     * interpolation.
     */
    private static class Discrete implements Interpolator {
        @Override
		public float interpolate(float t) {
            return (t < 1f) ? 0f : 1f;
        }
    }

    /**
     * Trivial implementation of Interpolator that provides linear time
     * interpolation (the input "t" value is simply returned unmodified).
     */
    private static class Linear implements Interpolator {
        @Override
		public float interpolate(float t) {
            return t;
        }
    }

    /**
     * Simplified implementation of Interpolator that provides acceleration
     * and deceleration.  This behavior could plausibly be provided by
     * constructing a spline-based Interpolator, but the Easing
     * implementation is likely to be much more efficient, both in terms of
     * performance and memory usage (since there is no need to maintain
     * flattened curve segments).
     */
    private static class Easing implements Interpolator {
        private float acceleration;
        private float deceleration;

        /**
         * Creates a new easing time path with the given acceleration and
         * deceleration values.
         *
         * @param acceleration value in the range [0,1] indicating the fraction
         *     of time spent accelerating at the beginning of a timing cycle
         * @param deceleration value in the range [0,1] indicating the fraction
         *     of time spent decelerating at the end of a timing cycle
         * @throws IllegalArgumentException if either the acceleration or
         *     deceleration value is outside the range [0,1]
         * @throws IllegalArgumentException if the acceleration value is
         *     greater than (1 - deceleration)
         * @throws IllegalArgumentException if the deceleration value is
         *     greater than (1 - acceleration)
         */
        Easing(float acceleration, float deceleration) {
            if (acceleration < 0f || acceleration > 1f) {
                throw new IllegalArgumentException("Acceleration value cannot lie" +
                    " outside [0,1] range");
            }
            if (acceleration > (1f - deceleration)) {
                throw new IllegalArgumentException("Acceleration value cannot be" +
                    " greater than (1 - deceleration)");
            }
            if (deceleration < 0f || deceleration > 1f) {
                throw new IllegalArgumentException("Deceleration value cannot lie" +
                    " outside [0,1] range");
            }
            if (deceleration > (1f - acceleration)) {
                throw new IllegalArgumentException("Deceleration value cannot be" +
                    " greater than (1 - acceleration)");
            }

            this.acceleration = acceleration;
            this.deceleration = deceleration;
        }

        @Override
		public float interpolate(float fraction) {
            // First, take care of acceleration/deceleration factors
            if (acceleration != 0 || deceleration != 0.0f) {
                // See the SMIL 2.0 specification for details on this
                // calculation
                float runRate = 1.0f / (1.0f - acceleration/2.0f -
                        deceleration/2.0f);
                if (fraction < acceleration) {
                    float averageRunRate = runRate * (fraction / acceleration) / 2;
                    fraction *= averageRunRate;
                } else if (fraction > (1.0f - deceleration)) {
                    // time spent in deceleration portion
                    float tdec = fraction - (1.0f - deceleration);
                    // proportion of tdec to total deceleration time
                    float pdec = tdec / deceleration;
                    fraction = runRate * (1.0f - ( acceleration / 2) -
                            deceleration + tdec * (2 - pdec) / 2);
                } else {
                    fraction = runRate * (fraction - (acceleration / 2));
                }
                // clamp fraction to [0,1] since above calculations may
                // cause rounding errors
                if (fraction < 0f) {
                    fraction = 0f;
                } else if (fraction > 1f) {
                    fraction = 1f;
                }
            }
            return fraction;
        }
    }
}
