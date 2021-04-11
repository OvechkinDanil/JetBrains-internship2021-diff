package main.java.com.JetBrains;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] argv)
    {
        Logger logger = Logger.getLogger(Main.class.getName() + "Logger");
        if (argv.length != 2 || argv[0] == null || argv[1] == null)
        {
            logger.log(Level.SEVERE, RC.CODE_INVALID_ARGUMENT.getTitle());
            return;
        }

        Diff diff = new Diff(logger);

        RC rc = diff.run(argv[0], argv[1]);

        System.out.println(rc.getTitle());
    }
}
