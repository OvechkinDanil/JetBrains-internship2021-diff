package main.java.com.JetBrains;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * This class include method that read file line-by-line and save it in StringBuilder
 */
public class Reader {
    private final Logger logger;

    Reader(Logger logger)
    {
        this.logger = logger;
    }
    public String readFile(String filename){
        if (filename == null)
        {
            logger.log(Level.SEVERE, RC.CODE_INVALID_ARGUMENT.getTitle());
            return null;
        }

        StringBuilder sb = new StringBuilder();
        String line;

        try(FileReader inputStream = new FileReader(filename))
        {
            try (BufferedReader br = new BufferedReader(inputStream)) {
                line = br.readLine();
                while (line != null) {
                    sb.append(line).append('\n');
                    line = br.readLine();
                }
            } finally {
                inputStream.close();
            }
        }
        catch (FileNotFoundException e)
        {
            logger.log(Level.SEVERE, RC.CODE_FAILED_TO_OPEN_FILE.getTitle());
            return null;
        }
        catch (IOException e)
        {
            logger.log(Level.SEVERE, RC.CODE_INVALID_INPUT_STREAM.getTitle());
            return null;
        }

        return sb.toString();
    }
}
