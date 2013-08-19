package k.midieditor.gui.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;

import k.midieditor.gui.MidiEditorMain;

public class SAJMIActionListener extends JMIActionListener {
	public SAJMIActionListener() {
		super("Save as...", MidiEditorMain.SAVEA_JMIKEY,
				MidiEditorMain.FILE_KEY);
	}

	@Override
	public void onAction(ActionEvent e) {
		JFileChooser fd = ((OJMIActionListener) JMIActionListener.OPEN_LISTENER)
				.getDialog();
		fd.showSaveDialog(null);
		File out = fd.getSelectedFile();
		if (out != null) {
			MidiEditorMain.working.save(out);
		}
	}

}