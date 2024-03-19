package com.mirohaap.towerofhanoitutor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;

public class AnalyticsUtil {
    private int previousOptimalMoves = 0;
    private int previousUnoptimalMoves = 0;
    private int previousElapsedTime = 0;
    private static AnalyticsUtil _instance;
    private int optimalMoves = 0;
    private int unoptimalMoves = 0;
    private long elapsedTime = 0;
    private boolean openedThisSession = false;

    private ArrayList<Integer> optimalMovesOverTime = new ArrayList<Integer>();

    private AnalyticsUtil() {
        fetchPreviousAnalyticData();
    }

    private void calculateValues() {
        optimalMoves = previousOptimalMoves + fetchNumberOfMovesFromCurrentSession(true);
        unoptimalMoves = previousUnoptimalMoves + fetchNumberOfMovesFromCurrentSession(false);
        elapsedTime = previousElapsedTime + fetchInGameTimeFromCurrentSession();
    }

    public static AnalyticsUtil getInstance() {
        if (_instance == null) {
            _instance = new AnalyticsUtil();
        }
        _instance.calculateValues();
        return _instance;
    }

    /**
     * Fetches analytics data from previous session.
     * @return True if file was found, false is not and if new file was created.
     */
    public void fetchPreviousAnalyticData() {
    //    boolean fileFound = false;
        try {
            File data = new File("analytics.txt");
            Scanner read = new Scanner(data);
            if (read.hasNextLine()) {
                previousOptimalMoves = Integer.parseInt(read.nextLine());
            }
            if (read.hasNextLine()) {
                previousUnoptimalMoves = Integer.parseInt(read.nextLine());
            }
            if (read.hasNextLine()) {
                previousElapsedTime = Integer.parseInt(read.nextLine());

            }
            while (read.hasNextLine()) {
                String line = read.nextLine();
                optimalMovesOverTime.add(Integer.parseInt(line));
            }
            read.close();
    //        fileFound = true;
        } catch (FileNotFoundException e) {
            try {
                File data = new File("analytics.txt");
                    data.createNewFile();
            } catch (IOException e2) {
                System.out.println("Error creating analytics file.");
                e.printStackTrace();
            }
        }
   //     return fileFound;
    }

    /**
     * Writes analytics data from current session to file.
     */

    public void writeAnalyticDataToFile() {
        try {
            FileWriter myWriter = new FileWriter("analytics.txt");
            myWriter.write(Integer.toString(optimalMoves));
            myWriter.write("\n");
            myWriter.write(Integer.toString(unoptimalMoves));
            myWriter.write("\n");
            myWriter.write(Integer.toString((int)elapsedTime));
            myWriter.write("\n");
            for (Integer in : optimalMovesOverTime) {
                myWriter.write(Integer.toString(in));
                myWriter.write("\n");
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("Error writing to analytics file.");
            e.printStackTrace();
        }
    }

    public void logOptimalMoves() {
        optimalMovesOverTime.add(optimalMoves);
    }

    private int fetchNumberOfMovesFromCurrentSession(boolean moveOptimality) {
        int count = 0;
        ArrayList<Boolean> moves = Repository.getInstance().getOptimalMoves();
        for (Boolean optimal : moves) {
            if (moveOptimality) {
                if (optimal) {
                    ++count;
                }
            } else {
                if (!optimal) {
                    ++count;
                }
            }
        }
        return count;
    }

    private long fetchInGameTimeFromCurrentSession() {
        return Repository.getInstance().calculateElapsedTime() / 1000;
    }

    public int getNumberOfOptimalMoves() {
        return optimalMoves;
    }

    public int getNumberOfUnoptimalMoves() {
        return unoptimalMoves;
    }

    public ArrayList<Integer> getOptimalMovesOverTime() {
        return optimalMovesOverTime;
    }

    public long getElapsedTime() {
        return elapsedTime + fetchInGameTimeFromCurrentSession();
    }
}
