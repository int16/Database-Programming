package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RoundMemberDetailsTP extends JPanel {
	public RoundMemberDetailsTP() {
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		//
		gc.weightx = 0.5;
		gc.anchor = GridBagConstraints.WEST;
		gc.insets = new Insets(5, 5, 5, 5);

		JLabel dateLBL = new JLabel("Date");
		gc.gridx = 0;
		gc.gridy = 0;
		this.add(dateLBL, gc);
		JTextField dateTF = new JTextField(getCurrentDateFormatted());
		gc.gridx = 1;
		gc.gridy = 0;
		this.add(dateTF, gc);

		JLabel eventLBL = new JLabel("Event");
		gc.gridx = 2;
		gc.gridy = 0;
		gc.anchor = GridBagConstraints.EAST;
		this.add(eventLBL, gc);
		String[] eventData = { "None", "Make new" };
		JComboBox<String> eventCB = new JComboBox<String>(eventData);
		eventCB.setEditable(false);
		gc.gridx = 3;
		gc.gridy = 0;
		gc.anchor = GridBagConstraints.WEST;
		this.add(eventCB, gc);

		JLabel venueLBL = new JLabel("Venue");
		gc.gridx = 0;
		gc.gridy = 1;
		this.add(venueLBL, gc);

		JTextField venueTF = new JTextField("Box Hill City Archers");
		gc.gridx = 1;
		gc.gridy = 1;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridwidth = 3;
		this.add(venueTF, gc);

		JLabel memberLBL = new JLabel("Member");
		gc.gridx = 0;
		gc.gridy = 2;
		gc.gridwidth = 1;
		this.add(memberLBL, gc);

		JTextField memberfTF = new JTextField("firstname");
		gc.gridx = 1;
		gc.gridy = 2;
		gc.fill = GridBagConstraints.HORIZONTAL;
		this.add(memberfTF, gc);

		JTextField memberlTF = new JTextField("lastname");
		gc.gridx = 2;
		gc.gridy = 2;
		gc.fill = GridBagConstraints.HORIZONTAL;
		this.add(memberlTF, gc);

		JLabel equipLBL = new JLabel("Equipment");
		gc.gridx = 0;
		gc.gridy = 3;
		gc.gridwidth = 1;
		this.add(equipLBL, gc);

		JComboBox<String> equipCB = new JComboBox<String>();
		gc.gridx = 1;
		gc.gridy = 3;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridwidth = 2;
		this.add(equipCB, gc);

		JLabel evlvlLBL = new JLabel("Event level");
		gc.gridx = 0;
		gc.gridy = 4;
		gc.gridwidth = 1;
		this.add(evlvlLBL, gc);

		String[] evtlvlData = { "Club", "State", "National", "World" };
		JComboBox<String> evlvlCB = new JComboBox<String>(evtlvlData);
		gc.gridx = 1;
		gc.gridy = 4;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridwidth = 2;
		this.add(evlvlCB, gc);

		JLabel roundLBL = new JLabel("Round");
		gc.gridx = 0;
		gc.gridy = 8;
		gc.insets = new Insets(25, 5, 5, 5);
		gc.gridwidth = 1;
		this.add(roundLBL, gc);

		// String [] roundData = {"Can", "State", "National", "World"};
		JComboBox<String> roundCB = new JComboBox<String>();
		gc.gridx = 1;
		gc.gridy = 8;
		gc.gridwidth = 2;
		gc.fill = GridBagConstraints.HORIZONTAL;
		this.add(roundCB, gc);

	}

	private String getCurrentDateFormatted() {
		DateFormat format = DateFormat.getDateInstance();
		return format.format(new Date(System.currentTimeMillis()));
	}

}
