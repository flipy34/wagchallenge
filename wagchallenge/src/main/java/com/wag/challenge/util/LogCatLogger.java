package com.wag.challenge.util;

import android.util.Log;

import com.wag.challenge.BuildConfig;

/**
 * Created by PGomez on 9/21/2017.
 */

public class LogCatLogger {
    //region constants
    private static final int maxStringLength = 4000;
    private static final int type_error = 1;
    private static final int type_debug = 2;
    private static final int type_info = 3;
    //endregion


    public static void debug(String tag, String message) {
        log(type_debug, tag, message, null);
    }

    public static void error(String tag, String message) {
        log(type_error, tag, message, null);
    }

    public static void error(String tag, Exception e) {
        log(type_error, tag, "ERROR", e);
    }

    public static void debugTime(String tag, long time) {
        log(type_debug, tag, "Performance Time difference " + String.valueOf(System.currentTimeMillis() - time), null);
    }

    private static void log(final int type, final String tag, final String msg, final Exception e) {
        if (BuildConfig.DEBUG) {
            for (int i = 0; msg != null && i < msg.length(); i += maxStringLength) {
                final String tmpString = msg.substring(i, i + maxStringLength < msg.length() ? i + maxStringLength : msg.length());
                switch (type) {
                    case type_debug:
                        Log.d(tag, tmpString);
                        break;
                    case type_error:
                        if (e == null) {
                            Log.e(tag, tmpString);
                        } else {
                            Log.e(tag, tmpString, e);
                        }
                        break;
                    case type_info:
                        Log.i(tag, tmpString);
                        break;
                }
            }
        }
    }


}