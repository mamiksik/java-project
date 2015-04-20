package Client;

/**
 *
 * @author anty
 */
public interface IStatusLogger {
    
    public void clear();

    public void write(String text);

    public void writeTable(String text);
}
