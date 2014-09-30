package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import scores.Archer;
import scores.Equipment;
import scores.Event;
import scores.EventLevel;
import scores.Round;
import scores.RoundGroup;
import scores.Score;
import scores.Utils;
import database.MiscellaneousData;
import database.ScoringData;

public class RoundScoreDetailsPN extends JPanel {
	private ScoreInputTP scoresTP;
	private String[] displayableLevels = null;
	private Equipment[] equipmentChoices;
	private Archer archer;
	private ArrayList<EventLevel> levels;
	private JTextField venueTF;
	private JTextField memberfTF;
	private JTextField memberlTF;
	private JTextField equipTF;
	private JTextField catTF;
	private JComboBox<String> evlvlCB;
	private JComboBox<Event> eventCB;
	private JComboBox<Round> roundCB;
	private MiscellaneousData miscData;
	private Round[] rounds;
	private Round[] activeRounds;
	private Utils utils;
	private Event[] events;
	private JCheckBox officialCB;
	private JCheckBox unOfficialCB;
	private ScoringData scoringData;
	private JCheckBox claimAwardCB;
	private RoundComboListener rcl;
	private CheckBoxListener cbl;
	private JComboBox<String> hourCB;
	private JComboBox<String> minuteCB;
	private JTextArea commentTA;
	private JTextField dateTF;

	public RoundScoreDetailsPN(Equipment[] equip, ArrayList<EventLevel> lvl) {
		this.equipmentChoices = equip;
		this.utils = new Utils();
		this.levels = lvl;
		miscData = new MiscellaneousData();

		scoresTP = new ScoreInputTP(this);
		this.setLayout(new GridLayout(1, 0));
		JPanel otherPN = new JPanel();
		JPanel detailsPN = new JPanel();

		this.add(otherPN);
		otherPN.add(detailsPN);
		this.add(scoresTP);
		scoringData = new ScoringData();

		detailsPN.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		//
		gc.weightx = 0.5;
		gc.anchor = GridBagConstraints.NORTHWEST;
		gc.insets = new Insets(5, 5, 5, 5);

		JLabel dateLBL = new JLabel("Date");
		gc.gridx = 0;
		gc.gridy = 0;
		detailsPN.add(dateLBL, gc);
		dateTF = new JTextField(utils.getCurrentDateFormatted());
		gc.gridx = 1;
		gc.gridy = 0;
		dateTF.setPreferredSize(new Dimension(100, 25));
		detailsPN.add(dateTF, gc);

		String[] hours = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24" };
		hourCB = new JComboBox<String>(hours);
		hourCB.setSelectedItem("09");
		hourCB.setPreferredSize(new Dimension(50, 25));
		detailsPN.add(hourCB);

		String[] minutes = { "00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55" };
		minuteCB = new JComboBox<String>(minutes);
		minuteCB.setPreferredSize(new Dimension(50, 25));
		detailsPN.add(minuteCB);
		gc.gridx = 2;
		gc.gridy = 0;
		gc.gridwidth = 1;

		JLabel eventLBL = new JLabel("Event");
		gc.gridx = 0;
		gc.gridy = 1;

		detailsPN.add(eventLBL, gc);

		eventCB = new JComboBox<Event>();
		gc.gridx = 1;
		gc.gridy = 1;
		gc.gridwidth = 3;
		gc.anchor = GridBagConstraints.WEST;
		eventCB.setPreferredSize(new Dimension(250, 25));
		detailsPN.add(eventCB, gc);

		JLabel venueLBL = new JLabel("Venue");
		gc.gridx = 0;
		gc.gridy = 2;
		gc.gridwidth = 1;
		detailsPN.add(venueLBL, gc);

		venueTF = new JTextField("Box Hill City Archers (Sparks Reserve)");
		gc.gridx = 1;
		gc.gridy = 2;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridwidth = 3;
		venueTF.setPreferredSize(new Dimension(250, 25));
		detailsPN.add(venueTF, gc);

		JLabel memberLBL = new JLabel("Archer");
		gc.gridx = 0;
		gc.gridy = 3;
		gc.gridwidth = 1;
		detailsPN.add(memberLBL, gc);

		memberfTF = new JTextField(" ");
		gc.gridx = 1;
		gc.gridy = 3;
		gc.fill = GridBagConstraints.HORIZONTAL;
		memberfTF.setPreferredSize(new Dimension(100, 25));
		detailsPN.add(memberfTF, gc);

		memberlTF = new JTextField(" ");
		gc.gridx = 2;
		gc.gridy = 3;
		gc.gridwidth = 2;
		gc.fill = GridBagConstraints.HORIZONTAL;
		memberlTF.setPreferredSize(new Dimension(100, 25));
		detailsPN.add(memberlTF, gc);

		JLabel equipLBL = new JLabel("Equipment");
		gc.gridx = 0;
		gc.gridy = 4;
		gc.gridwidth = 1;
		detailsPN.add(equipLBL, gc);

		equipTF = new JTextField();
		equipTF.setEditable(false);
		gc.gridx = 1;
		gc.gridy = 4;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridwidth = 2;
		equipTF.setPreferredSize(new Dimension(150, 25));
		detailsPN.add(equipTF, gc);

		JLabel catLBL = new JLabel("Category");
		gc.gridx = 0;
		gc.gridy = 5;
		gc.gridwidth = 1;
		detailsPN.add(catLBL, gc);

		catTF = new JTextField();
		catTF.setEditable(false);
		gc.gridx = 1;
		gc.gridy = 5;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridwidth = 2;
		catTF.setPreferredSize(new Dimension(150, 25));
		detailsPN.add(catTF, gc);

		JLabel evlvlLBL = new JLabel("Event level");
		gc.gridx = 0;
		gc.gridy = 6;
		gc.gridwidth = 1;
		detailsPN.add(evlvlLBL, gc);

		if (displayableLevels == null) makeDisplayableLevels();

		evlvlCB = new JComboBox<String>(displayableLevels);
		gc.gridx = 1;
		gc.gridy = 6;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridwidth = 2;
		evlvlCB.setPreferredSize(new Dimension(100, 30));
		detailsPN.add(evlvlCB, gc);

		JLabel roundLBL = new JLabel("Round");
		gc.gridx = 0;
		gc.gridy = 7;
		gc.gridwidth = 1;
		detailsPN.add(roundLBL, gc);

		roundCB = new JComboBox<Round>();
		gc.gridx = 1;
		gc.gridy = 7;
		gc.gridwidth = 3;
		gc.fill = GridBagConstraints.HORIZONTAL;
		detailsPN.add(roundCB, gc);

		cbl = new CheckBoxListener();
		JPanel pn1 = new JPanel();
		JLabel lbl1 = new JLabel("Official");
		pn1.add(lbl1);
		officialCB = new JCheckBox();
		officialCB.setSelected(true);
		officialCB.addItemListener(cbl);

		pn1.add(officialCB);
		gc.gridx = 1;
		gc.gridy = 8;
		gc.gridwidth = 2;
		detailsPN.add(pn1, gc);

		JLabel lbl2 = new JLabel("Unofficial");
		pn1.add(lbl2);
		unOfficialCB = new JCheckBox();
		unOfficialCB.addItemListener(cbl);
		pn1.add(unOfficialCB);

		JLabel commentLBL = new JLabel("Comment");
		gc.gridx = 0;
		gc.gridy = 9;
		gc.gridwidth = 1;
		gc.anchor = GridBagConstraints.NORTHWEST;
		detailsPN.add(commentLBL, gc);
		commentTA = new JTextArea(5, 30);
		commentTA.setBorder(BorderFactory.createEtchedBorder());
		commentTA.setLineWrap(true);
		int maxChars = 10;
		AbstractDocument pDoc = (AbstractDocument) commentTA.getDocument();

		pDoc.setDocumentFilter(new DocumentSizeFilter(256)); // ugly but stops entering too many chars
		gc.gridx = 1;
		gc.gridy = 9;
		gc.gridwidth = 3;
		detailsPN.add(commentTA, gc);

		JPanel pn2 = new JPanel();
		JLabel lbl3 = new JLabel("Claim award");
		pn2.add(lbl3);
		claimAwardCB = new JCheckBox();
		pn2.add(claimAwardCB);
		gc.gridx = 0;
		gc.gridy = 10;
		gc.gridwidth = 1;
		// gc.anchor = GridBagConstraints.CENTER;
		detailsPN.add(lbl3, gc);
		gc.gridx = 1;
		gc.gridy = 10;
		gc.gridwidth = 1;
		gc.insets = new Insets(25, 5, 5, 5);
		detailsPN.add(claimAwardCB, gc);
	}

	public void initialisePanel(Archer archer, Round[] rounds, Event[] events) {
		this.rounds = rounds;
		this.activeRounds = rounds;
		this.archer = archer;
		this.events = events;
		memberfTF.setText(archer.getFirstName());
		memberfTF.setEditable(false);
		memberlTF.setText(archer.getLastName());
		memberlTF.setEditable(false);
		// archer.getCurrentEquipment() is the chosen equipment, different from archer.getDefaultEquipment()
		equipTF.setText(archer.getCurrentEquipment().toString());
		catTF.setText(archer.getCurrentCat().toString());
		if (eventCB.getItemCount() == 0) {
			for (int i = 0; i < events.length; i++)
				eventCB.addItem(events[i]);
			eventCB.addActionListener(new ComboBoxListener());
		}
		eventCB.setSelectedIndex(-1);
		roundCB.removeAllItems();
		for (int i = 0; i < activeRounds.length; i++)
			roundCB.addItem(activeRounds[i]);
		roundCB.setSelectedIndex(-1);
		rcl = new RoundComboListener();
		roundCB.addActionListener(rcl);
		unOfficialCB.setEnabled(true);
		officialCB.setEnabled(true);

		this.revalidate();
		this.repaint();
	}

	private void makeDisplayableLevels() {
		displayableLevels = new String[levels.size()];
		for (int i = 0; i < levels.size(); i++) {
			displayableLevels[i] = levels.get(i).getDisplayableString();
		}
	}

	private void adjustRoundsToEvent(RoundGroup rg) {
		try {
			rcl.setActive(false);
			if (rg != null) {
				int[] possIds = miscData.getApplicableRounds(rg, archer);
				activeRounds = new Round[possIds.length];
				for (int i = 0; i < possIds.length; i++) {
					for (int j = 0; j < rounds.length; j++) {
						if (rounds[j].getId() == possIds[i]) {
							activeRounds[i] = rounds[j];
							break;
						}
					}
				}
			} else {
				// if we don't have a roundgroup, we just have the one round to shoot
				activeRounds = new Round[1];
				activeRounds[0] = events[eventCB.getSelectedIndex()].getRound();
			}

			roundCB.removeAllItems();
			for (int i = 0; i < activeRounds.length; i++)
				roundCB.addItem(activeRounds[i]);
			rcl.setActive(true);
			roundCB.setSelectedIndex(0);
			this.revalidate();
			this.repaint();

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addDetailsToScore(Score score) {
		Date roundShotTime;
		try {
			roundShotTime = (Date) new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH).parse(dateTF.getText() + " " + hourCB.getSelectedItem() + ":" + minuteCB.getSelectedItem());

			score.setRoundShotTime(roundShotTime);
			score.setArcher(this.archer);
			score.setComment(this.commentTA.getText());
			String evtlevel = (String) evlvlCB.getSelectedItem();
			score.setEventLevel(evtlevel);
			score.setVenue(venueTF.getText());
			if (eventCB.getSelectedIndex() > -1) score.setEvent(events[eventCB.getSelectedIndex()]);

		} catch (ParseException e) {
			JOptionPane.showMessageDialog(this, "Date format should be dd/mm/yyyy", "Event Date", JOptionPane.ERROR_MESSAGE);

		}

	}

	private class CheckBoxListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			// if we have an event, the round depends on that event
			if (eventCB.getSelectedIndex() >= 0) return;
			try {
				rounds = miscData.getRoundListing(officialCB.isSelected(), unOfficialCB.isSelected());
				activeRounds = rounds;
				roundCB.removeAllItems();
				for (int i = 0; i < activeRounds.length; i++) {
					roundCB.addItem(activeRounds[i]);
				}

			} catch (ClassNotFoundException | SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	private class ComboBoxListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource().equals(eventCB)) {
				int idx = eventCB.getSelectedIndex();
				if (idx == -1) return;
				try {
					RoundGroup rg = scoringData.getRoundGroupforRound(events[idx].getRound());

					adjustRoundsToEvent(rg);

					// disable round refetch
					officialCB.removeItemListener(cbl);
					officialCB.setSelected(true);
					officialCB.setEnabled(false);
					unOfficialCB.removeItemListener(cbl);
					unOfficialCB.setSelected(false);
					unOfficialCB.setEnabled(false);
					hourCB.removeAllItems();
					hourCB.addItem(new SimpleDateFormat("HH", Locale.ENGLISH).format(events[idx].getEventTime()));
					minuteCB.removeAllItems();
					minuteCB.addItem(new SimpleDateFormat("mm", Locale.ENGLISH).format(events[idx].getEventTime()));
					dateTF.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(events[idx].getEventTime()));
					dateTF.setEditable(false);

				} catch (ClassNotFoundException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	private class RoundComboListener implements ActionListener {
		private boolean active = true;

		public void setActive(boolean a) {
			this.active = a;
		}

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (active) {
				if (roundCB.getSelectedIndex() == -1) return;
				try {

					miscData.getDetailsForRound(activeRounds[roundCB.getSelectedIndex()]);
					scoresTP.initialiseScoresTable(activeRounds[roundCB.getSelectedIndex()]);
				} catch (ClassNotFoundException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public class DocumentSizeFilter extends DocumentFilter {
		int maxCharacters;

		public DocumentSizeFilter(int maxChars) {
			maxCharacters = maxChars;
		}

		public void insertString(FilterBypass fb, int offs, String str, AttributeSet a) throws BadLocationException {

			if ((fb.getDocument().getLength() + str.length()) <= maxCharacters) super.insertString(fb, offs, str, a);
			else Toolkit.getDefaultToolkit().beep();
		}

		public void replace(FilterBypass fb, int offs, int length, String str, AttributeSet a) throws BadLocationException {
			if ((fb.getDocument().getLength() + str.length() - length) <= maxCharacters) super.replace(fb, offs, length, str, a);
			else Toolkit.getDefaultToolkit().beep();
		}

	}

	public boolean claimAward() {
		return this.claimAwardCB.isSelected();
	}
}
