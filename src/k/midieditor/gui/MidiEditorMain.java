package k.midieditor.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFrame;

import k.core.util.Helper.ProgramProps;
import k.core.util.gui.JMIActionListener;
import k.core.util.gui.Menu;
import k.core.util.gui.SideConsole;
import k.core.util.gui.SwingAWTUtils;
import k.midieditor.file.MidiFile;
import k.midieditor.gui.actions.LJIMIActionListener;
import k.midieditor.gui.actions.NJMIActionListener;
import k.midieditor.gui.actions.OJMIActionListener;
import k.midieditor.gui.actions.PAJMIActionListener;
import k.midieditor.gui.actions.PLJMIActionListener;
import k.midieditor.gui.actions.RJNIActionListener;
import k.midieditor.gui.actions.SAJMIActionListener;
import k.midieditor.gui.actions.SJMIActionListener;
import k.midieditor.gui.actions.STJMIActionListener;
import k.midieditor.gui.actions.UJMIActionListener;

public class MidiEditorMain extends JFrame {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /* Menu creation constants */
    public static final String FILE_KEY = "f_menu", EDIT_KEY = "e_menu",
            PLAYBACK_KEY = "playback_menu", UNDO_KEY = "undo_menu",
            REDO_KEY = "redo_menu", NEW_JMIKEY = "new", OPEN_JMIKEY = "open",
            SAVE_JMIKEY = "save", SAVEA_JMIKEY = "savea", UNDO_JMIKEY = "undo",
            UNDOR_JMIKEY = "undor", REDO_JMIKEY = "redo",
            REDOR_JMIKEY = "redor", PLAY_JMIKEY = "play",
            PAUSE_JMIKEY = "pause", STOP_JMIKEY = "stop", LOOP_JMIKEY = "loop";

    public static final JMIActionListener NEW_LISTENER = new NJMIActionListener(),
            OPEN_LISTENER = new OJMIActionListener(),
            SAVE_LISTENER = new SJMIActionListener(),
            SAVEA_LISTENER = new SAJMIActionListener(),
            UNDO_LISTENER = new UJMIActionListener("Undo",
                    MidiEditorMain.UNDO_JMIKEY),
            UNDOR_LISTENER = new UJMIActionListener("Undo...",
                    MidiEditorMain.UNDOR_JMIKEY),
            REDO_LISTENER = new RJNIActionListener("Redo",
                    MidiEditorMain.REDO_JMIKEY),
            REDOR_LISTENER = new RJNIActionListener("Redo...",
                    MidiEditorMain.REDOR_JMIKEY),
            PLAY_LISTENER = new PLJMIActionListener(),
            PAUSE_LISTENER = new PAJMIActionListener(),
            STOP_LISTENER = new STJMIActionListener(),
            LOOP_LISTENER = new LJIMIActionListener();

    private static MidiEditorMain inst = null;

    public static MidiFile working = null;

    public static SideConsole console;

    public static MidiEditorMain create(String ver) {
        if (inst == null) {
            return new MidiEditorMain(ver);
        } else {
            inst.setTitle("MidiEditor v" + ver);
            return inst;
        }
    }

    private MidiEditorMain(String ver) {
        super("MidiEditor v" + ver);
        if (inst != null) {
            throw new IllegalStateException(
                    "attempted to create > 1 MidiEditorMain");
        }
        if (ProgramProps.hasKey("debug")) {
            console = new SideConsole(true);
        }
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (console != null) {
                    SwingAWTUtils.kill(console);
                }
            }
        });
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        SwingAWTUtils.setBackground(new Color(100, 0, 0), this);
        this.setPreferredSize(new Dimension(800, 600));
        this.setSize(getPreferredSize());
        SwingAWTUtils.drop(this);
        addAllComponents();
        this.setVisible(true);
        inst = this;
        refresh();
    }

    public void addAllComponents() {
        addMenu();
    }

    private void addMenu() {
        // Create menu creator //
        Menu m = Menu.create("midieditormain");

        // Menu items visible on bar //
        m.addMenuByName(FILE_KEY, "File");
        m.addMenuByName(EDIT_KEY, "Edit");
        m.addMenuByName(PLAYBACK_KEY, "Playback");

        // Menu Items inside bar-visible ones //
        // File menu (new, open, save, save as)
        m.addMenuItemToMenuByName(FILE_KEY, NEW_JMIKEY, "New...");
        m.addMenuItemToMenuByName(FILE_KEY, OPEN_JMIKEY, "Open");
        m.addMenuItemToMenuByName(FILE_KEY, SAVE_JMIKEY, "Save");
        m.addMenuItemToMenuByName(FILE_KEY, SAVEA_JMIKEY, "Save as...");
        // End file menu //
        // Edit menu (undo menu, redo menu) //
        // Undo menu (undo, repeat undo) //
        m.addMenuToMenuByName(EDIT_KEY, UNDO_KEY, "Undo");
        m.addMenuItemToMenuByName(UNDO_KEY, UNDO_JMIKEY, "Undo");
        m.addMenuItemToMenuByName(UNDO_KEY, UNDOR_JMIKEY, "Undo...");
        // Redo menu (redo, repeat redo) //
        m.addMenuToMenuByName(EDIT_KEY, REDO_KEY, "Redo");
        m.addMenuItemToMenuByName(REDO_KEY, REDO_JMIKEY, "Redo");
        m.addMenuItemToMenuByName(REDO_KEY, REDOR_JMIKEY, "Redo...");
        // End edit menu //
        // ??? menu (play, pause, stop) //
        m.addMenuItemToMenuByName(PLAYBACK_KEY, PLAY_JMIKEY, "Play");
        m.addMenuItemToMenuByName(PLAYBACK_KEY, PAUSE_JMIKEY, "Pause");
        m.addMenuItemToMenuByName(PLAYBACK_KEY, STOP_JMIKEY, "Stop");
        m.addMenuItemToMenuByName(PLAYBACK_KEY, LOOP_JMIKEY, "Loop...");
        // End ??? menu //
        // Add actions //
        m.setActionListenerAll(JMIActionListener.instForMenu("midieditormain"));

        // Show the menu //
        m.display(this);
    }

    public void refresh() {
        this.update(getGraphics());
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    public static MidiEditorMain reboot(String version) {
        SwingAWTUtils.kill(inst);
        inst = null;
        return new MidiEditorMain(version);
    }

    public static void kill() {
        SwingAWTUtils.kill(inst);
        inst = null;
    }

    public File getMidiFile() {
        if (working == null) {
            working = new MidiFile(null);
        }
        return working.getFile();
    }

    public void setMidiFile(File midi) {
        if (working == null) {
            working = new MidiFile(midi);
        }
        working.setFileNoReload(midi);
        refresh();
    }

    public void setMidiFileReload(File midi) {
        if (working == null) {
            working = new MidiFile(midi);
        }
        working.setFile(midi, true);
        refresh();
    }
}
