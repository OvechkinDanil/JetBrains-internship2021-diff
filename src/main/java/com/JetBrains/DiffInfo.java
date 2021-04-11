package main.java.com.JetBrains;

/**
 * Class representing one diff operation with text.
 */
public class DiffInfo {
    public Operation operation;
    public String text;

    public DiffInfo(Operation op, String text)
    {
        this.operation = op;
        this.text = text;
    }

    @Override
    public String toString()
    {
        String goodText = this.text.replace('\n', '\u00b6');
        return "DiffInfo: Operation: " + this.operation + "| " + goodText + " |";
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null || getClass() != obj.getClass())
            return false;

        if (this == obj)
            return true;

        DiffInfo otherDiff = (DiffInfo) obj;
        if (operation != otherDiff.operation)
            return false;

        if (text == null && otherDiff.text != null || otherDiff.text == null && text != null)
            return false;
        else return text.equals(otherDiff.text);
    }

    public enum Operation
    {
        EQUAL,
        INSERT,
        DELETE,
        EDITLEFT,
        EDITRIGHT
    }
}
