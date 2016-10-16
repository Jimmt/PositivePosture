import java.util.Scanner;

import java.io.IOException;
import java.lang.Runtime;

public class MacNotification {

    /**
     * displays notification, currently triggered by typing "fix". trigger
     * can/will be changed
     * 
     * @param args
     */
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        String in = scan.nextLine();

        // JOptionPane.showMessageDialog(null, "fix your posture!", "HEY!",
        // JOptionPane.ERROR_MESSAGE);
        Runtime runtime = Runtime.getRuntime();
        String applescriptCommand = "display notification \"Fix Your Posture\" with title \"Hello\" sound name \"Glass\"";

        String[] args2 = { "osascript", "-e", applescriptCommand };
        if (in.equals("fix")) {
            try {
                runtime.exec(args2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
