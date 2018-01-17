package beini.com.wifimanage.util;

import android.util.Log;


/**
 * Created by beini on 17/02/09.
 */
public class BLog {

    private static String TAG = "com.beini";
    private static Boolean DEBUG = true;

    public static void d(String msg) {
        if (DEBUG) {
            Log.d(TAG, "-------------------------------------->" + msg);
        }

    }

    public static void e(String msg) {
        if (DEBUG) {
            Log.e(TAG, "-------------------------------------->" + msg);
        }

    }

    public static void d(String customTag, String msg) {
        if (DEBUG) {
            Log.e(customTag, "-------------------------------------->" + msg);
        }
    }

    public static void e(String customTag, String msg) {
        if (DEBUG) {
            Log.e(customTag, "-------------------------------------->" + msg);
        }

    }
}
