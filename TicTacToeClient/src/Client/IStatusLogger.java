package Client;

/**
 *
 * @author anty
 */
public interface IStatusLogger {

    public void clear();

    public void writeln(String text);

    public void reWriteln(String text);

    public void write(String text);

    public void writeTable(String text);
}
