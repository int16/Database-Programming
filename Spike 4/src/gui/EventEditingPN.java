package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import scores.Discipline;
import scores.Event;
import scores.MultiEvent;
import scores.Round;
import scores.Utils;
import database.EventData;
import database.MiscellaneousData;

public class EventEditingPN extends JPanel {
	private static final long serialVersionUID = 1L;

	private JPanel midPanel;
	private JButton okBTN;
	private JButton cancelBTN;
	private JComboBox<Round> roundCB;
	private JComboBox<MultiEvent> multiEventCB;
	private JComboBox<Discipline> disciplineCB;
	private JTextField dateTF;
	private JTextField venueTF;
	private JTextField nameTF;
	private JComboBox<String> hourCB;
	private JComboBox<String> minuteCB;
	private Round[] rounds = null;
	private Round[] filteredRounds = null;
	private MultiEvent[] multiEvents = null;
	private MiscellaneousData miscData;
	private JCheckBox officialCB;
	private JCheckBox unOfficialCB;
	private Utils utils;
	private Discipline[] disciplines = null;

	public EventEditingPN() {
		utils = new Utils();
		miscData = new MiscellaneousData();
		midPanel = new JPanel();

		// this.setLayout(new GridLayout(1,0));
		this.add(midPanel);

		midPanel.setLayout(new GridBagLayout());
		midPanel.setBorder(BorderFactory.createTitledBorder("Single Event"));

		GridBagConstraints gc = new GridBagConstraints();

		gc.weightx = 0.5;
		gc.anchor = GridBagConstraints.WEST;
		gc.insets = new Insets(5, 5, 5, 5);

		JLabel compLBL = new JLabel("Event Name");
		gc.gridx = 0;
		gc.gridy = 0;
		gc.gridwidth = 1;
		midPanel.add(compLBL, gc);

		nameTF = new JTextField();
		nameTF.setPreferredSize(new Dimension(250, 30));
		gc.gridx = 1;
		gc.gridy = 0;
		gc.gridwidth = 2;
		midPanel.add(nameTF, gc);

		JLabel discLBL = new JLabel("Discipline");
		gc.gridx = 0;
		gc.gridy = 1;
		gc.gridwidth = 1;
		midPanel.add(discLBL, gc);

		disciplineCB = new JComboBox();

		disciplineCB.setPreferredSize(new Dimension(250, 30));
		gc.gridx = 1;
		gc.gridy = 1;
		gc.gridwidth = 2;
		midPanel.add(disciplineCB, gc);

		JLabel multiLBL = new JLabel("Part of Multi-Event");
		gc.gridx = 0;
		gc.gridy = 2;
		gc.gridwidth = 1;
		midPanel.add(multiLBL, gc);

		multiEventCB = new JComboBox();

		multiEventCB.setPreferredSize(new Dimension(250, 30));
		gc.gridx = 1;
		gc.gridy = 2;
		gc.gridwidth = 2;
		midPanel.add(multiEventCB, gc);

		JLabel roundLBL = new JLabel("Round");
		gc.gridx = 0;
		gc.gridy = 3;
		gc.gridwidth = 1;
		midPanel.add(roundLBL, gc);

		roundCB = new JComboBox();

		roundCB.setPreferredSize(new Dimension(250, 30));
		roundCB.setEditable(false);
		gc.gridx = 1;
		gc.gridy = 3;
		gc.gridwidth = 2;
		midPanel.add(roundCB, gc);

		CheckBoxListener cbl = new CheckBoxListener();
		JPanel pn1 = new JPanel();
		JLabel lbl1 = new JLabel("Official");
		pn1.add(lbl1);
		officialCB = new JCheckBox();
		officialCB.setSelected(true);
		officialCB.addItemListener(cbl);

		pn1.add(officialCB);
		gc.gridx = 1;
		gc.gridy = 4;
		gc.gridwidth = 1;
		midPanel.add(pn1, gc);

		JPanel pn2 = new JPanel();
		JLabel lbl2 = new JLabel("Unofficial");
		pn2.add(lbl2);
		unOfficialCB = new JCheckBox();
		unOfficialCB.addItemListener(cbl);
		pn2.add(unOfficialCB);
		gc.gridx = 2;
		gc.gridy = 4;
		gc.gridwidth = 1;
		midPanel.add(pn2, gc);

		JLabel venueLBL = new JLabel("Venue");
		gc.gridx = 0;
		gc.gridy = 5;
		gc.gridwidth = 1;
		midPanel.add(venueLBL, gc);

		venueTF = new JTextField();
		venueTF.setPreferredSize(new Dimension(250, 30));
		gc.gridx = 1;
		gc.gridy = 5;
		gc.gridwidth = 2;
		midPanel.add(venueTF, gc);

		JLabel dateLBL = new JLabel("Date and Time");
		gc.gridx = 0;
		gc.gridy = 6;
		gc.gridwidth = 1;
		midPanel.add(dateLBL, gc);

		dateTF = new JTextField(utils.getCurrentDateFormatted());
		dateTF.setPreferredSize(new Dimension(100, 30));
		gc.gridx = 1;
		gc.gridy = 6;
		gc.gridwidth = 1;
		midPanel.add(dateTF, gc);

		JPanel hourPN = new JPanel();
		String[] hours = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24" };
		hourCB = new JComboBox<String>(hours);
		hourCB.setSelectedItem("9");
		hourPN.add(hourCB);

		String[] minutes = { "00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55" };
		minuteCB = new JComboBox<String>(minutes);
		hourPN.add(minuteCB);
		gc.gridx = 2;
		gc.gridy = 6;
		gc.gridwidth = 1;
		midPanel.add(hourPN, gc);

		cancelBTN = new JButton("Cancel");
		cancelBTN.addActionListener(new EventSaveListener());
		cancelBTN.setPreferredSize(new Dimension(100, 30));
		gc.gridx = 1;
		gc.gridy = 7;
		gc.gridwidth = 1;
		midPanel.add(cancelBTN, gc);

		okBTN = new JButton("Save");
		okBTN.addActionListener(new EventSaveListener());
		okBTN.setPreferredSize(new Dimension(100, 30));
		gc.gridx = 2;
		gc.gridy = 7;
		gc.gridwidth = 1;
		midPanel.add(okBTN, gc);
	}

	public void initialisePanel(Discipline[] discs) {
		Date now = new Date(System.currentTimeMillis());
		nameTF.setText("Round of the Week " + new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(now));
		venueTF.setText("Box Hill - Sparks Reserve");
		if (disciplines == null) {
			disciplines = discs;
			for (int i = 0; i < disciplines.length; i++) {
				disciplineCB.addItem(disciplines[i]);
			}

		}
		// default is target
		disciplineCB.setSelectedIndex(0);
		if (rounds == null) {
			try {
				rounds = miscData.getRoundListing(true, false);
				filteredRounds = new Round[rounds.length];
				int idx = 0;
				for (int i = 0; i < rounds.length; i++) {
					// ugly; default is target
					if (rounds[i].getDiscipline().getId() == disciplines[0].getId()) {
						filteredRounds[idx++] = rounds[i];
						roundCB.addItem(rounds[i]);
					}
				}

			} catch (ClassNotFoundException | SQLException e) {
				JOptionPane.showMessageDialog(this, "Can't find rounds in database.", "Data Retrieval Problem", JOptionPane.ERROR_MESSAGE);
			}
		}

		if (multiEvents == null) {
			try {
				multiEvents = miscData.getMultiEventListing();
				for (int i = 0; i < multiEvents.length; i++) {
					multiEventCB.addItem(multiEvents[i]);
				}
			} catch (ClassNotFoundException | SQLException e1) {
				JOptionPane.showMessageDialog(this, "Can't find multi-event competitions in database.", "Data Retrieval Problem", JOptionPane.ERROR_MESSAGE);
			}

		}
		disciplineCB.addActionListener(new ComboBoxListener());
		multiEventCB.setSelectedIndex(-1);

	}

	private void clearPanel() {
		nameTF.setText("");
		venueTF.setText("");
	}

	private void doSaveAction() {
		try {
			Event e = new Event();
			Date start = (Date) new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH).parse(dateTF.getText() + " " + hourCB.getSelectedItem() + ":" + minuteCB.getSelectedItem());
			e.setEventTime(start);
			System.out.println(start);
			System.out.println(e.getEventTime());
			System.out.println(e.getEventTime().getTime());
			if (multiEventCB.getSelectedIndex() >= 0) e.setMultiEvent(multiEvents[multiEventCB.getSelectedIndex()]);
			e.setName(nameTF.getText());
			e.setRound(filteredRounds[roundCB.getSelectedIndex()]);
			e.setVenue(venueTF.getText());
			EventData data = new EventData();
			data.storeNewEvent(e);
			JOptionPane.showMessageDialog(this, "Entry saved.", "", JOptionPane.INFORMATION_MESSAGE);

		} catch (ParseException e1) {
			JOptionPane.showMessageDialog(this, "Date format should be dd/mm/yyyy", "Event Date", JOptionPane.ERROR_MESSAGE);
			return;
		} catch (ClassNotFoundException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	private void adjustDisplayedRounds() {
		roundCB.removeAllItems();
		filteredRounds = new Round[rounds.length];
		int idx = 0;
		Discipline chosen = (Discipline) disciplineCB.getSelectedItem();
		for (int i = 0; i < rounds.length; i++) {
			if (rounds[i].getDiscipline().getId() == chosen.getId()) {
				roundCB.addItem(rounds[i]);
				filteredRounds[idx++] = rounds[i];
			}
		}

	}

	class CheckBoxListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			try {
				rounds = miscData.getRoundListing(officialCB.isSelected(), unOfficialCB.isSelected());
				adjustDisplayedRounds();

			} catch (ClassNotFoundException | SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	private class EventSaveListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(okBTN)) doSaveAction();

			clearPanel();
			MainWindow.resetWindow();
		}

	}

	private class ComboBoxListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			adjustDisplayedRounds();

		}

	}
}
