package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.DateFormat;
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
import scores.MultiEvent;
import scores.Round;
import database.EventData;
import database.MiscellaneousData;

public class MultiEventEditingPN extends JPanel {
	private JButton rulesBTN;
	private JButton okBTN;
	private JButton cancelBTN;
	private JButton[] removeBTN;
	private JPanel midPanel;
	private GridBagConstraints gc;
	private JPanel[] rulesPNs;
	private int rulesCounter = 0;
	private JPanel rulesPN;
	private MiscellaneousData miscData;
	private Round[] rounds;
	private JCheckBox offCB;
	private JCheckBox unoffCB;
	private JTextField nameTF;
	private JTextField startDateTF;
	private JTextField endDateTF;
	private JComboBox<Discipline> disciplineCB;
	private Discipline[] disciplines = null;

	public MultiEventEditingPN() {
		miscData = new MiscellaneousData();
		midPanel = new JPanel();

		rulesPNs = new JPanel[10]; // enough??
		removeBTN = new JButton[10];
		this.setLayout(new GridLayout(1, 0));

		this.add(midPanel);

		rulesPN = new JPanel();
		rulesPN.setLayout(new GridLayout(10, 1, 5, 5));

		this.add(rulesPN);

		midPanel.setLayout(new GridBagLayout());
		midPanel.setBorder(BorderFactory.createTitledBorder("Multi-Event Competition"));
		// midPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		gc = new GridBagConstraints();
		//
		gc.weightx = 0.5;
		gc.anchor = GridBagConstraints.WEST;
		gc.insets = new Insets(5, 5, 5, 5);

		JLabel compLBL = new JLabel("Competition Name");
		gc.gridx = 0;
		gc.gridy = 0;
		gc.gridwidth = 2;
		midPanel.add(compLBL, gc);
		nameTF = new JTextField();
		nameTF.setPreferredSize(new Dimension(250, 30));
		gc.gridx = 0;
		gc.gridy = 1;
		gc.gridwidth = 3;
		midPanel.add(nameTF, gc);

		JLabel discLBL = new JLabel("Discipline");
		gc.gridx = 0;
		gc.gridy = 2;
		gc.gridwidth = 1;
		midPanel.add(discLBL, gc);

		disciplineCB = new JComboBox();

		disciplineCB.setPreferredSize(new Dimension(120, 30));
		gc.gridx = 1;
		gc.gridy = 2;
		gc.gridwidth = 2;
		midPanel.add(disciplineCB, gc);

		JLabel dateLBL = new JLabel("Start Date");
		gc.gridx = 0;
		gc.gridy = 3;
		gc.gridwidth = 1;
		midPanel.add(dateLBL, gc);
		startDateTF = new JTextField(getCurrentDateFormatted());
		startDateTF.setPreferredSize(new Dimension(120, 30));
		gc.gridx = 1;
		gc.gridy = 3;
		gc.gridwidth = 2;
		midPanel.add(startDateTF, gc);

		JLabel endDateLBL = new JLabel("End Date");
		gc.gridx = 0;
		gc.gridy = 4;
		gc.gridwidth = 1;
		midPanel.add(endDateLBL, gc);
		endDateTF = new JTextField();
		endDateTF.setPreferredSize(new Dimension(120, 30));
		gc.gridx = 1;
		gc.gridy = 4;
		gc.gridwidth = 2;
		midPanel.add(endDateTF, gc);

		JLabel lbl1 = new JLabel("Add rounds?");
		gc.gridx = 0;
		gc.gridy = 5;
		gc.gridwidth = 2;
		midPanel.add(lbl1, gc);

		offCB = new JCheckBox("Include official");
		gc.gridx = 1;
		gc.gridy = 5;
		gc.gridwidth = 1;
		midPanel.add(offCB, gc);
		unoffCB = new JCheckBox("Include unofficial");
		gc.gridx = 2;
		gc.gridy = 5;
		gc.gridwidth = 1;
		midPanel.add(unoffCB, gc);

		rulesBTN = new JButton("Add round");
		rulesBTN.addActionListener(new RulesAddingListener());
		rulesBTN.setPreferredSize(new Dimension(120, 30));
		gc.gridx = 1;
		gc.gridy = 6;
		gc.gridwidth = 2;
		midPanel.add(rulesBTN, gc);

		cancelBTN = new JButton("Cancel");
		cancelBTN.addActionListener(new MultiEventSaveListener());
		cancelBTN.setPreferredSize(new Dimension(120, 30));
		gc.gridx = 1;
		gc.gridy = 7;
		gc.gridwidth = 1;
		midPanel.add(cancelBTN, gc);

		okBTN = new JButton("Save");
		okBTN.addActionListener(new MultiEventSaveListener());
		okBTN.setPreferredSize(new Dimension(120, 30));
		gc.gridx = 2;
		gc.gridy = 7;
		gc.gridwidth = 1;
		midPanel.add(okBTN, gc);
	}

	public void initialisePanel(Discipline[] discs) {
		if (disciplines == null) {
			disciplines = discs;
			for (int i = 0; i < disciplines.length; i++) {
				disciplineCB.addItem(disciplines[i]);
			}

		}
	}

	private void addRules() {

		if (rulesCounter >= rulesPNs.length) {
			JOptionPane.showMessageDialog(this, "Max 10 rules. Blame the person who wrote this program. ", "Number of Rules Exceeded", JOptionPane.ERROR_MESSAGE);
		} else {
			fetchRounds(offCB.isSelected(), unoffCB.isSelected());
			if (rounds == null) {
				JOptionPane.showMessageDialog(this, "You have to include some rounds, official or unofficial. ", "Try again", JOptionPane.WARNING_MESSAGE);
				return;
			}
			rounds = filterRoundsByDiscipline(rounds);
			rulesPN.setBorder(BorderFactory.createBevelBorder(1));

			rulesPNs[rulesCounter] = new JPanel();
			rulesPNs[rulesCounter].setBorder(BorderFactory.createEtchedBorder());
			rulesPNs[rulesCounter].setLayout(new GridLayout(0, 4));

			JLabel lbl1 = new JLabel("Round " + (rulesCounter + 1) + " type");
			rulesPNs[rulesCounter].add(lbl1);
			JLabel lbl2 = new JLabel("Inclusion rule");
			rulesPNs[rulesCounter].add(lbl2);
			JLabel lbl3 = new JLabel("Number included");
			rulesPNs[rulesCounter].add(lbl3);
			rulesPNs[rulesCounter].add(new JLabel());

			JComboBox roundCB = new JComboBox(rounds);
			roundCB.setEditable(false);
			rulesPNs[rulesCounter].add(roundCB);
			String[] rules = { "MAX", "ALL", "AVG" };
			JComboBox ruleCB = new JComboBox(rules);
			rulesPNs[rulesCounter].add(ruleCB);
			JTextField includedNoTF = new JTextField();
			rulesPNs[rulesCounter].add(includedNoTF);
			removeBTN[rulesCounter] = new JButton("Remove");
			removeBTN[rulesCounter].addActionListener(new RulesAddingListener());
			rulesPNs[rulesCounter].add(removeBTN[rulesCounter]);

			rulesPN.add(rulesPNs[rulesCounter]);
			rulesCounter++;
			this.revalidate();
			this.repaint();

		}

	}

	private Round[] filterRoundsByDiscipline(Round[] rounds2) {

		int idx = 0;
		Discipline chosen = (Discipline) disciplineCB.getSelectedItem();
		for (int i = 0; i < rounds.length; i++) {

			if (rounds[i].getDiscipline().getId() == chosen.getId()) {
				idx++;
			}
		}
		Round[] filteredRounds = new Round[idx];
		idx = 0;
		for (int i = 0; i < rounds.length; i++) {

			if (rounds[i].getDiscipline().getId() == chosen.getId()) {
				filteredRounds[idx++] = rounds[i];
			}
		}
		return filteredRounds;
	}

	private void fetchRounds(boolean official, boolean inofficial) {
		try {
			rounds = miscData.getRoundListing(official, inofficial);
		} catch (ClassNotFoundException | SQLException e) {
			JOptionPane.showMessageDialog(this, "Blame the person who wrote this program. " + e.getStackTrace(), "Database Problems", JOptionPane.ERROR_MESSAGE);
		}

	}

	private void removeRules(int index) {
		rulesPN.remove(rulesPNs[rulesCounter - 1]);
		for (int i = index; i < rulesPNs.length - 1; i++) {
			rulesPNs[i] = rulesPNs[i + 1];
		}
		System.out.println(rulesCounter);
		rulesPNs[rulesPNs.length - 1] = null;
		rulesPN.revalidate();
		rulesPN.repaint();
		rulesCounter--;
	}

	private String getCurrentDateFormatted() {
		DateFormat format = DateFormat.getDateInstance();
		return format.format(new Date(System.currentTimeMillis()));
	}

	private class RulesAddingListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			if (event.getSource().equals(rulesBTN)) addRules();
			else {
				int which = -1;
				for (int i = 0; i < removeBTN.length; i++) {
					if (event.getSource().equals(removeBTN[i])) which = i;
				}
				removeRules(which);
			}
		}
	}

	private void removeAllRules() {
		while (rulesCounter != 0)
			removeRules(rulesCounter);
	}

	private void clearPanel() {
		removeAllRules();
		offCB.setSelected(false);
		unoffCB.setSelected(false);
		nameTF.setText("");
	}

	private void doSaveAction() {
		if (nameTF.getText().equals("")) {
			JOptionPane.showMessageDialog(this, "Event needs a name.", "New Multieventcompetition", JOptionPane.ERROR_MESSAGE);
			return;
		}
		MultiEvent e = new MultiEvent(rulesCounter);
		e.setName(nameTF.getText());
		try {
			Date start = (Date) new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(startDateTF.getText());
			e.setStart(start);
			Date end = (Date) new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(endDateTF.getText());
			e.setEnd(end);
		} catch (ParseException e1) {
			JOptionPane.showMessageDialog(this, "Date format should be dd/mm/yyyy ", "Start/end Date", JOptionPane.ERROR_MESSAGE);
			return;
		}
		for (int i = 0; i < rulesCounter; i++) {
			JComboBox<Round> roundCB = (JComboBox<Round>) rulesPNs[i].getComponent(4);
			JComboBox<String> calcCB = (JComboBox<String>) rulesPNs[i].getComponent(5);
			JTextField noTF = (JTextField) rulesPNs[i].getComponent(6);
			Round round = (Round) roundCB.getSelectedItem();
			int no = 0;
			if (!noTF.getText().equals("")) no = Integer.parseInt(noTF.getText());
			e.addRule(round.getId(), ((String) calcCB.getSelectedItem()), no);

		}
		EventData data = new EventData();
		try {
			data.storeNewMultiEvent(e);
			JOptionPane.showMessageDialog(this, "Entry saved.", "", JOptionPane.INFORMATION_MESSAGE);
		} catch (ClassNotFoundException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private class MultiEventSaveListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			if (event.getSource().equals(okBTN)) {
				doSaveAction();

			}
			clearPanel();
			MainWindow.resetWindow();

		}

	}

}
