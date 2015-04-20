package Client;

import java.io.IOException;
import FXGame.TicTacToeControlForm;

/**
 *
 * @author Anty
 */
public class Main {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length < 1) {
            TicTacToeControlForm.main(args);
            return;
        }
        switch (args[0]) {
            case "-g":
                TicTacToeControlForm.main(args);
                break;
            case "-t":
                TerminalGame.Main.main(args);
                break;
            default:
                TicTacToeControlForm.main(args);
        }
    }
}
