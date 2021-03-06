package k.midieditor;

import static k.core.util.Helper.ProgramProps.acceptPair;
import static k.core.util.Helper.ProgramProps.getProperty;
import static k.core.util.Helper.ProgramProps.hasKey;
import static k.core.util.Helper.ProgramProps.normalizeCommandArgs;

import java.awt.Toolkit;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import javax.swing.SwingUtilities;

import k.core.util.Helper;
import k.core.util.gui.SideConsole;
import k.midieditor.gui.MidiEditorMain;

public class MidiEditor {
    public static String VERSION = "1.3 alpha";
    public static MidiEditorMain mainwin;

    /**
     * @param args
     */
    public static void main(String[] args) {
        PrintStream temp = System.out, out = System.out, err = System.err;
        Helper.BetterArrays.print(args);
        args = normalizeCommandArgs(args);
        if (Arrays.asList(args).contains("-debug")) {
            out = SideConsole.earlyChainO(out);
            err = SideConsole.earlyChainE(err);
            System.setOut(out);
            System.setErr(err);
        }
        Helper.BetterArrays.print(args);
        for (int i = 0; i < args.length; i += 2) {
            int i2 = i + 1;
            String key = args[i];
            String val = args[i2];
            acceptPair(key, val);
        }
        if (hasKey("debug")) {
            System.out.println("Toolkit is of type "
                    + Toolkit.getDefaultToolkit().getClass());
        }
        if (hasKey("file")) {
            String val = getProperty("file");
            System.out.println(val);
        }
        if (hasKey("versionOverride")) {
            VERSION = getProperty("versionOverride", VERSION);
        }
        if (hasKey("version")) {
            temp.println("MidiEditor v" + VERSION);
            try {
                Thread.sleep(10);
                return;
            } catch (InterruptedException ie) {
            }

        }
        if (hasKey("versionAndLaunch")) {
            System.out.println("MidiEditor v" + VERSION);
        }
        try {
            SwingUtilities.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    mainwin = MidiEditorMain.create(VERSION);
                }
            });
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
