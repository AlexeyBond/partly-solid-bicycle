package com.github.alexeybond.partly_solid_bicycle.ext.destruction;

/**
 *
 */
public class DestroyerConfig {
    /** Minimal area of triangle formed by three consequent points of a resulting polygon. */
    public float minTriArea = 1f;

    /** Minimal count of initial rays (cracks). Should be >= 5 */
    public int initialRaysMin = 5;

    /** Maximal count of initial rays. */
    public int initialRaysMax = 6;

    /** Minimal length of a crack before fork. */
    public float crackLengthMin = 10;

    /** Maximal length of not forked crack. */
    public float crackLengthMax = 20;

    /** Minimal count of rays started after a fork. Should be >= 2 */
    public int forkRaysMin = 2;

    /** Maximal count of rays started after a fork. */
    public int forkRaysMax = 3;

    /**
     * Half-size of range of angles the ray turns on fork.
     * Angles will be in range (-forkAngleRange, forkAngleRange).
     * <p>
     * Configuration parameters should meet the following inequality:
     * <pre>(forkAngleRange / forkRaysMin) * 2 < 180</pre>
     */
    public float forkAngleRange = 80;

    public float forkAngleRestrictRangeFraction = 0.1f;

    public void set(DestroyerConfig config) {
        this.minTriArea = config.minTriArea;
        this.initialRaysMin = config.initialRaysMin;
        this.initialRaysMax = config.initialRaysMax;
        this.crackLengthMin = config.crackLengthMin;
        this.crackLengthMax = config.crackLengthMax;
        this.forkRaysMin = config.forkRaysMin;
        this.forkRaysMax = config.forkRaysMax;
        this.forkAngleRange = config.forkAngleRange;
        this.forkAngleRestrictRangeFraction = config.forkAngleRestrictRangeFraction;
    }
}
