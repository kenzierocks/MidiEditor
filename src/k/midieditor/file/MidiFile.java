package k.midieditor.file;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

import k.midieditor.MidiFilePlayer;

public class MidiFile {
    private boolean invalid = false;
    private File midi = null;
    private Sequence internal = null;

    public MidiFile(File path) {
        if (path.isDirectory()
                || (!path.getAbsolutePath().endsWith(".mid") && !path
                        .getAbsolutePath().endsWith(".midi"))) {
            System.err.println("Path '" + path.getAbsolutePath()
                    + "' is not a " + (path.isDirectory() ? "file!" : "midi!"));
            invalid = true;
            return;
        }
        reload(path);
    }

    private void reload(File f) {
        if (!f.exists()) {
            try {
                f.createNewFile();
                return; // Don't try to read a new file!
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("WARNING: MidiFile was unable to access '"
                        + f.getAbsolutePath() + "'!");
                return;
            }
        }
        midi = f.getAbsoluteFile();
        if (midi.length() == 0) {
            return;
        }
        try {
            internal = MidiSystem.getSequence(midi);
            MidiFilePlayer.open(internal);
            return;
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
            System.err.println("Bad MIDI data!");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Couldn't read data? Access might be denied!");
        }
        invalid = true;
    }

    public Sequence getSequence() {
        if (invalid)
            return null;
        return internal;
    }

    public void setSequnce(Sequence s) {
        if (invalid)
            return;
        internal = s;
    }

    public Track getTrackAt(int index) {
        if (invalid)
            return null;
        return getSequence().getTracks()[index];
    }

    public void setTrackAt(int index, Track t) {
        if (invalid)
            return;
        getSequence().getTracks()[index] = t;
    }

    public void play() {
        play(getSequence());
    }

    public static void play(Sequence s) {
        MidiFilePlayer.play();
    }

    public void save() {
        save(midi);
    }

    public void save(File dest) {
        if (invalid)
            return;
        try {
            if (internal == null) {
                internal = new Sequence(Sequence.PPQ, 32);
                internal.createTrack();
            }
            MidiSystem.write(internal,
                    MidiSystem.getMidiFileTypes(internal)[0], dest);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("No supported midi types!");
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    public File getFile() {
        if (invalid)
            return null;
        return midi;
    }

    public void setFileNoReload(File f) {
        setFile(f, false);
    }

    public void setFile(File f, boolean reload) {
        if (reload) {
            reload(f);
        } else {
            midi = f;
        }
    }
}
