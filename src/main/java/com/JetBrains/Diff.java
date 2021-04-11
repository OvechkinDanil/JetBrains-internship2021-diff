package main.java.com.JetBrains;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Class, which calls the required methods to get output data and save it into project directory
 */
public class Diff {
    private final Logger logger;
    private final String pathOfTemplateHtml = System.getProperty("user.dir") + "\\resources\\templates\\template.html";

    Diff(Logger logger)
    {
        this.logger = logger;
    }

    public RC run(String path1, String path2)
    {
        Reader reader = new Reader(logger);

        String text1 = reader.readFile(path1);
        String text2 = reader.readFile(path2);

        if (text1 == null || text2 == null)
            return RC.CODE_PROBLEMS;

        LongestCommonSubsequence LCS = new LongestCommonSubsequence(logger);
        ArrayList<DiffInfo> diffs = LCS.create(text1.split("\n"), text2.split("\n"));
        if (diffs == null)
            return RC.CODE_PROBLEMS;

        Writer writer = new Writer();
        String htmlTemplate = reader.readFile(pathOfTemplateHtml);

        return writer.write(diffs, htmlTemplate, logger);
    }
}
