package main.java.com.JetBrains;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Writer {
    private String originalTextInHtml;
    private String newTextInHtml;
    private HashMap<DiffInfo.Operation, Tags> styleTags;

    /**
     * Write all information into html file
     * @param diffs collect all information about changed, equal, inserted and deleted lines
     * @param htmlTemplate string, which read from template html file
     * @param logger for logging errors into console or log file
     * @return RC code of success or problems
     */
    public RC write(ArrayList<DiffInfo> diffs, String htmlTemplate, Logger logger) {
        createHtmlTagsForStyle();

        String htmlCode = toHtml(diffs, htmlTemplate);

        File f = new File(System.getProperty("user.dir") + "\\resources\\diff.html");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            bw.write(htmlCode);
            bw.flush();
            return RC.CODE_SUCCESS;
        } catch (IOException e) {
            logger.log(Level.SEVERE, RC.CODE_FAILED_TO_WRITE.getTitle());
            return RC.CODE_FAILED_TO_WRITE;
        }
    }

    /**
     * This class needs for creating pair of open and close tags and put them into Map
     */
    private static class Tags {
        Tags(String open, String close) {
            this.openTag = open;
            this.closeTag = close;
        }

        public String openTag;
        public String closeTag;
    }

    private void createHtmlTagsForStyle() {
        styleTags = new HashMap<>();
        for (DiffInfo.Operation op : DiffInfo.Operation.values()) {
            String openTag = HtmlLiteral.OPENBRACKET.getTitle() + op + HtmlLiteral.ClOSEBRACKET.getTitle();
            String closeTag = "</" + op + ">";
            styleTags.put(op, new Tags(openTag, closeTag));
        }
    }

    /**
     * Convert result data to Html code with using Html template, which saved in project directory
     * @param diffs: collect all information about changed, equal, inserted and deleted lines
     */
    private void convertDiffsToHtmlCode(ArrayList<DiffInfo> diffs) {
        String openStyleTag, closeStyleTag;
        String saveSpacesTagOpen = HtmlLiteral.PRE.getTitle();
        String lineBreak = "\n";
        String tabulation = "\t\t\t\t";

        originalTextInHtml = newTextInHtml = saveSpacesTagOpen + styleTags.get(DiffInfo.Operation.EQUAL).openTag;


        for (int i = 0; i < diffs.size(); i++) {
            DiffInfo.Operation op = diffs.get(i).operation;

            openStyleTag = styleTags.get(op).openTag;
            closeStyleTag = styleTags.get(op).closeTag;

            String lineForOutput = tabulation + openStyleTag + diffs.get(i).text + closeStyleTag + lineBreak;

            switch (op) {
                case EQUAL: {
                    originalTextInHtml += lineForOutput;
                    newTextInHtml += lineForOutput;
                    break;
                }
                case EDITLEFT:
                case DELETE: {
                    originalTextInHtml += lineForOutput;
                    break;
                }
                case EDITRIGHT:
                case INSERT: {
                    newTextInHtml += lineForOutput;
                    break;
                }
            }
        }
        originalTextInHtml += HtmlLiteral.CLOSEDEQUAL.getTitle() + HtmlLiteral.CLOSEDPRE.getTitle() +"\n";

        newTextInHtml += HtmlLiteral.CLOSEDEQUAL.getTitle() + HtmlLiteral.CLOSEDPRE.getTitle() +"\n";

    }

    /**
     * Collect all information in one html code
     * @param diffs collect all information about changed, equal, inserted and deleted lines
     * @param htmlTemplate string, which read from template html file
     * @return html code that will write into output file
     */
    private String toHtml(ArrayList<DiffInfo> diffs, String htmlTemplate) {
        convertDiffsToHtmlCode(diffs);

        htmlTemplate = htmlTemplate.replace(HtmlLiteral.TEMPLATE_LEFTSIDE.getTitle(), originalTextInHtml);
        htmlTemplate = htmlTemplate.replace(HtmlLiteral.TEMPLATE_RIGHTSIDE.getTitle(), newTextInHtml);


        return htmlTemplate;
    }

    /**
     * This enum needs for collect all literals in this class
     */
    private enum HtmlLiteral
    {
        OPENBRACKET("<"),
        ClOSEBRACKET(">"),
        PRE("<pre>"),
        CLOSEDPRE("</pre>"),
        TEMPLATE_LEFTSIDE("$$Leftside$$"),
        TEMPLATE_RIGHTSIDE("$$RighSide$$"),
        CLOSEDEQUAL("</EQUAL>");


        private String title;

        HtmlLiteral(String title) {
            this.title = title;
        }


        public String getTitle() {
            return title;
        }
    }
}
