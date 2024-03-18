package com.mirohaap.towerofhanoitutor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

public class AnalyticsUtil {

    public static int fetchNumberOfMoves(boolean validity) {
        int count = 0;
        ArrayList<Boolean> moves = Repository.getInstance().getOptimalMoves();
        for (Boolean bool : moves) {
            if (validity) {
                if (bool) {
                    ++count;
                }
            } else {
                if (!bool) {
                    ++count;
                }
            }
        }
        return count;
    }

    public static long fetchInGameTime() {
        return Repository.getInstance().calculateElapsedTime() / 1000;
    }
}
