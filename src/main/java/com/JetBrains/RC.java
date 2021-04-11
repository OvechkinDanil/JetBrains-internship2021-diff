package main.java.com.JetBrains;

/*
 * This enum needs to use for return codes of methods and for logger
 */
public enum RC
{
    CODE_SUCCESS("Done without errors"),
    CODE_PROBLEMS("Done with some errors. Please, check logger."),
    CODE_INVALID_ARGUMENT("Invalid arguments"),
    CODE_FAILED_TO_READ("Failed to read"),
    CODE_FAILED_TO_WRITE("Failed to write"),
    CODE_INVALID_INPUT_STREAM("Invalid input stream"),
    CODE_INVALID_OUTPUT_STREAM("Invalid output stream"),
    CODE_FAILED_TO_OPEN_FILE("Failed to open file");

    private String title;

    RC(String title) {
        this.title = title;
    }


    public String getTitle() {
        return title;
    }
}

