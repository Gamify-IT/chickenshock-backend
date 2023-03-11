package de.unistuttgart.chickenshockbackend;

/**
 * This class contains constants for defensive field checks
 * e.g. the game time cannot be smaller than 0 seconds
 */
public final class Constants {

    //----Game configuration attributes----

    //game time in seconds
    public static final int MIN_TIME = 0;

    //----Game result attributes----
    public static final int MIN_POINTS = -600;
    public static final int MAX_POINTS = 600;
    public static final int MIN_QUESTION_COUNT = 0;
    public static final int MAX_QUESTION_COUNT = 600;

    //----Overworld result attributes----

    //score saved in %
    public static final long MIN_SCORE = 0;
    public static final long MAX_SCORE = 100;

    private Constants() {}
}
