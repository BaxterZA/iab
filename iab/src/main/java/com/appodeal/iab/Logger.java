package com.appodeal.iab;


import android.util.Log;

public class Logger {

    private final static String TAG = "IAB";
    private static LOG_LEVEL LEVEL = LOG_LEVEL.verbose;

    private Logger() {
    }

    public static void d(String msg) {
        if (LEVEL.getValue() <= LOG_LEVEL.debug.getValue()) {
            Log.d(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (LEVEL.getValue() <= LOG_LEVEL.error.getValue()) {
            Log.e(TAG, msg);
        }
    }

    public static void i(String msg) {
        if (LEVEL.getValue() <= LOG_LEVEL.info.getValue()) {
            Log.i(TAG, msg);
        }
    }

    public static void v(String msg) {
        if (LEVEL.getValue() <= LOG_LEVEL.verbose.getValue()) {
            Log.v(TAG, msg);
        }
    }

    public static void w(String msg) {
        if (LEVEL.getValue() <= LOG_LEVEL.warning.getValue()) {
            Log.w(TAG, msg);
        }
    }

    public static void d(String subTag, String msg) {
        if (LEVEL.getValue() <= LOG_LEVEL.debug.getValue()) {
            msg = "[" + subTag + "] " + msg;
            Log.d(TAG, msg);
        }
    }

    public static void e(String subTag, String msg) {
        if (LEVEL.getValue() <= LOG_LEVEL.error.getValue()) {
            msg = "[" + subTag + "] " + msg;
            Log.e(TAG, msg);
        }
    }

    public static void e(String subTag, Throwable throwable) {
        if (LEVEL.getValue() <= LOG_LEVEL.error.getValue()) {
            String msg = "[" + subTag + "] ";
            if (throwable instanceof java.net.UnknownHostException) {
                msg += throwable.toString();
            }
            Log.e(TAG, msg, throwable);
        }
    }

    public static void i(String subTag, String msg) {
        if (LEVEL.getValue() <= LOG_LEVEL.info.getValue()) {
            msg = "[" + subTag + "] " + msg;
            Log.i(TAG, msg);
        }
    }

    public static void v(String subTag, String msg) {
        if (LEVEL.getValue() <= LOG_LEVEL.verbose.getValue()) {
            msg = "[" + subTag + "] " + msg;
            Log.v(TAG, msg);
        }
    }

    public static void w(String subTag, String msg) {
        if (LEVEL.getValue() <= LOG_LEVEL.warning.getValue()) {
            msg = "[" + subTag + "] " + msg;
            Log.w(TAG, msg);
        }
    }

    /**
     * Get current log level
     * @return {@link LOG_LEVEL}
     */
    public static LOG_LEVEL getLoggingLevel() {
        return LEVEL;
    }

    /**
     * Set log level to Mraid library
     * @param logLevel {@link LOG_LEVEL}
     */
    public static void setLoggingLevel(LOG_LEVEL logLevel) {
        Log.i(TAG, "Changing logging level from :" + LEVEL + ". To:" + logLevel);
        LEVEL = logLevel;
    }

    public enum LOG_LEVEL {

        verbose(1),
        debug(2),
        info(3),
        warning(4),
        error(5),
        none(6);

        private final int value;

        LOG_LEVEL(int value) {
            this.value = value;

        }

        public int getValue() {
            return value;
        }

    }
}
