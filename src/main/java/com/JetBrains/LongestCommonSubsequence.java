package main.java.com.JetBrains;

import main.java.com.JetBrains.DiffInfo.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * This class implements the algorithm for finding the Longest Common Subsequence
 */
public class LongestCommonSubsequence {
    private String[] linesText1;
    private String[] linesText2;
    private int numLinesInText1;
    private int numLinesInText2;
    private Direction[][] direction;
    private final Logger logger;


    LongestCommonSubsequence(Logger logger) {
        this.logger = logger;
    }

    /**
     * This public method needs to create LCS
     * @param lines1 array of lines in original text
     * @param lines2 array of lines in changed text
     * @return ArrayList<DiffInfo> that collect all information about lines(changed, equal, insert, delete)
     */
    public ArrayList<DiffInfo> create(String[] lines1, String[] lines2) {
        if (lines1 == null || lines2 == null) {
            logger.log(Level.SEVERE, RC.CODE_INVALID_ARGUMENT.getTitle());
            return null;
        }

        linesText1 = lines1;
        linesText2 = lines2;
        numLinesInText1 = linesText1.length;
        numLinesInText2 = linesText2.length;

        direction = new Direction[numLinesInText1 + 1][numLinesInText2 + 1];

        FillDirections();

        ArrayList<DiffInfo> infos = deriveLCSFromOperations();

        return findEditLines(infos);
    }

    /**
     * This method find lines that can be changed into EDIT
     * @param infos collect all information about changed, equal, inserted and deleted lines
     * @return changed ArrayList
     */
    private ArrayList<DiffInfo> findEditLines(ArrayList<DiffInfo> infos) {
        int i, j;

        for (i = 0; i < infos.size(); i++)
            for (j = 0; j < infos.size(); j++) {
                if (infos.get(j).operation == Operation.INSERT && infos.get(i).operation == Operation.DELETE) {
                    int k = getLengthOfLCS(infos.get(i).text, infos.get(j).text);
                    if (k != 0 && k > (infos.get(i).text.length() / 2) && k > (infos.get(j).text.length() / 2) && k != infos.get(i).text.length()) {
                        infos.get(i).operation = Operation.EDITLEFT;
                        infos.get(j).operation = Operation.EDITRIGHT;
                    }
                }
            }

        return infos;
    }

    /**
     * This method find the size of Longest Common Subsequence of two strings
     * @param line1 compare with line2
     * @param line2 compare with line1
     * @return size of LCS
     */
    private int getLengthOfLCS(String line1, String line2) {
        int[][] lcsArr = new int[line1.length() + 1][line2.length() + 1];
        int i, j = 0;

        for (i = 0; i < line1.length() + 1; i++) {
            for (j = 0; j < line2.length() + 1; j++) {
                if (i == 0 || j == 0) {
                    lcsArr[i][j] = 0;
                } else if (line1.charAt(i - 1) == line2.charAt(j - 1)) {
                    lcsArr[i][j] = lcsArr[i - 1][j - 1] + 1;
                } else
                    lcsArr[i][j] = Math.max(lcsArr[i - 1][j], lcsArr[i][j - 1]);
            }
        }
        return lcsArr[i - 1][j - 1];
    }

    /**
     * Method use the arrays of strings, that collected in class, and create the table for LCS problem by using the
     * dynamic programming
     * Also it fill directions in order to restore the lcs
     */
    private void FillDirections() {
        int[][] lcsLen = new int[numLinesInText1 + 1][numLinesInText2 + 1];

        for (int i = 0; i < numLinesInText1 + 1; i++)
            for (int j = 0; j < numLinesInText2 + 1; j++) {
                if (i == 0 || j == 0) {
                    lcsLen[i][j] = 0;
                    direction[i][j] = Direction.EMPTY;
                } else if (linesText1[i - 1].equals(linesText2[j - 1])) {
                    lcsLen[i][j] = lcsLen[i - 1][j - 1] + 1;
                    direction[i][j] = Direction.DIAGONAL;
                } else {
                    if (lcsLen[i - 1][j - 1] >= lcsLen[i][j - 1]) {
                        lcsLen[i][j] = lcsLen[i - 1][j];
                        direction[i][j] = Direction.LEFT;
                    } else {
                        lcsLen[i][j] = lcsLen[i][j - 1];
                        direction[i][j] = Direction.UPPER;
                    }
                }
            }
    }

    /**
     * Restore lcs by using the directions
     * @return ArrayList<DiffInfo> that collect all information about lines(changed, equal, insert, delete)
     */
    private ArrayList<DiffInfo> deriveLCSFromOperations() {
        int i = numLinesInText1, j = numLinesInText2;
        ArrayList<DiffInfo> operations = new ArrayList<>();

        while (i > 0 || j > 0) {
            switch (direction[i][j]) {
                case DIAGONAL:
                    operations.add(new DiffInfo(Operation.EQUAL, linesText1[i - 1]));
                    i -= 1;
                    j -= 1;
                    break;
                case UPPER:
                    operations.add(new DiffInfo(Operation.INSERT, linesText2[j - 1]));
                    j -= 1;
                    break;
                case LEFT:
                    operations.add(new DiffInfo(Operation.DELETE, linesText1[i - 1]));
                    i -= 1;
                    break;
                case EMPTY:
                    if (i == 0) {
                        operations.add(new DiffInfo(Operation.INSERT, linesText2[j - 1]));
                        j -= 1;
                    } else if (j == 0) {
                        operations.add(new DiffInfo(Operation.DELETE, linesText1[i - 1]));
                        i -= 1;
                    }
                    break;
            }
        }
        Collections.reverse(operations);
        return operations;
    }

    /**
     * This enum uses for unification all directions
     */
    private enum Direction {
        EMPTY,
        DIAGONAL,
        LEFT,
        UPPER
    }
}
