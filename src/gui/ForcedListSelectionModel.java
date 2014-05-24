package gui;

import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;


@SuppressWarnings("serial")
public class ForcedListSelectionModel extends DefaultListSelectionModel {
    public ForcedListSelectionModel () {
    	super();
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    @Override
	public void clearSelection() {
    	return;
	}

    @Override
    public void removeSelectionInterval(int index0, int index1) {
    	return;
    }
}
