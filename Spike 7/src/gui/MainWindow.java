package gui;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import scores.Archer;
import scores.Category;
import scores.Discipline;
import scores.Equipment;
import scores.Event;
import scores.EventLevel;
import database.ArcherData;
import database.MiscellaneousData;
import exceptions.NoSuchEntryException;
import exceptions.TooManyEntriesException;

import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;

import scores.Round;

public class MainWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	private static MainWindow window;
	private JMenuBar menuBar;
	private JMenuItem newArcherMI, editArcherMI;
	private JMenuItem newScoreMI, editScoreMI;
	private JMenuItem newMultiEventMI, editMultiEventMI, newEventMI, editEventMI;
	private ArcherData archerData;
	private MiscellaneousData miscData;
	private Archer currentArcher;
	private ArcherDialog findArcherDLG;
	private Equipment[] equipmentChoices = null;
	private Discipline[] disciplineChoices = null;
	private Category[] categoryChoices = null;
	private ArrayList<EventLevel> eventLevels = null;
	final static String EMPTY = "empty";
	final static String SCORES = "scorepanel";
	final static String MULTI = "multieventpanel";
	final static String EVENT = "eventpanel";
	private RoundScoreDetailsPN roundScoreDetailsPN;
	private MultiEventEditingPN multieventPN;
	private EventEditingPN eventPN;

	// needed everywhere

	public MainWindow(String title, ImageIcon icon) {
		super(title);
		// Set the menu bar to be used if the OS is Mac.
		if (System.getProperty("os.name").contains("Mac")) System.setProperty("apple.laf.useScreenMenuBar", "true");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		window = this;
		archerData = new ArcherData();
		miscData = new MiscellaneousData();
		Image image = icon.getImage();
		this.setIconImage(image);

		createMenuBar();
		this.setJMenuBar(menuBar);
		this.setPreferredSize(new Dimension(900, 600));
		this.pack();
		this.setLocationRelativeTo(null);
		// set defaults to get the tabbed pane built.
	}

	public void initialiseMainWindow() {
		this.getContentPane().setLayout(new CardLayout());
		JPanel emptyPN = new JPanel();

		roundScoreDetailsPN = new RoundScoreDetailsPN(equipmentChoices, eventLevels);

		multieventPN = new MultiEventEditingPN();
		eventPN = new EventEditingPN();

		this.getContentPane().add(EMPTY, emptyPN);
		this.getContentPane().add(SCORES, roundScoreDetailsPN);
		this.getContentPane().add(MULTI, multieventPN);
		this.getContentPane().add(EVENT, eventPN);
	}

	private void openScoringPanel(Equipment[] equip, ArrayList<EventLevel> levels, Round[] rounds, Event[] events) {
		roundScoreDetailsPN.initialisePanel(currentArcher, rounds, events);
		CardLayout cl = (CardLayout) (this.getContentPane().getLayout());
		cl.show(this.getContentPane(), SCORES);
	}

	private void createMenuBar() {

		JMenu archerMenu, eventMenu, scoresMenu;

		MenuListener ml = new MenuListener();

		// Create the menu bar.
		menuBar = new JMenuBar();

		archerMenu = new JMenu("Archer");
		archerMenu.setMnemonic(KeyEvent.VK_A);
		// archerMenu.getAccessibleContext().setAccessibleDescription("Everything to do with the archer.");
		menuBar.add(archerMenu);

		newArcherMI = new JMenuItem("New Archer");
		newArcherMI.addActionListener(ml);

		editArcherMI = new JMenuItem("Edit Archer Details");
		editArcherMI.addActionListener(ml);

		// menuItem.setMnemonic(KeyEvent.VK_T); //used constructor instead
		// menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
		// menuItem.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
		archerMenu.add(newArcherMI);
		archerMenu.add(editArcherMI);

		eventMenu = new JMenu("Events");
		eventMenu.setMnemonic(KeyEvent.VK_E);

		menuBar.add(eventMenu);

		newMultiEventMI = new JMenuItem("New Multi-Event Competition");
		newMultiEventMI.addActionListener(ml);
		newEventMI = new JMenuItem("New Event");
		newEventMI.addActionListener(ml);
		editMultiEventMI = new JMenuItem("Edit Multi-Event Competition");
		editMultiEventMI.addActionListener(ml);
		editEventMI = new JMenuItem("Edit Event");
		editEventMI.addActionListener(ml);

		eventMenu.add(newMultiEventMI);
		eventMenu.add(editMultiEventMI);
		eventMenu.add(newEventMI);
		eventMenu.add(editEventMI);

		scoresMenu = new JMenu("Scores");
		scoresMenu.setMnemonic(KeyEvent.VK_S);

		menuBar.add(scoresMenu);

		newScoreMI = new JMenuItem("New Score");
		editScoreMI = new JMenuItem("Edit Score");
		newScoreMI.addActionListener(ml);
		editScoreMI.addActionListener(ml);

		scoresMenu.add(newScoreMI);
		scoresMenu.add(editScoreMI);

	}

	class MenuListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JMenuItem source = (JMenuItem) (e.getSource());
			if (source.equals(newScoreMI)) openNewScore();
			else if (source.equals(newEventMI)) openNewEvent();
			else if (source.equals(newMultiEventMI)) openNewMultiEvent();
			else if (source.equals(editMultiEventMI)) openEditMultiEvent();
			else if (source.equals(newArcherMI)) openNewMultiEvent();
			else if (source.equals(editScoreMI)) openEditScore();
			else if (source.equals(editArcherMI)) openEditArcher();
			else if (source.equals(editEventMI)) openEditEvent();
			else if (source.equals(newMultiEventMI)) openEditMultiEvent();

		}

	}

	private void openEditArcher() {
	}

	private void openEditMultiEvent() {
		// TODO Auto-generated method stub
	}

	private void openEditEvent() {
		// TODO Auto-generated method stub
	}

	private void openEditScore() {
		findArcher();
		if (findArcherDLG.wasCancelled()) return;
		Round[] rounds = null;
		Event[] events = null;
		try {
			rounds = miscData.getRoundListing(true, false);
			events = miscData.getLatestEventListing(50);
			ArcherData archerData = new ArcherData();
			int id = currentArcher.getId();
			
			archerData.getCurrentRatingForArcher(currentArcher);

		} catch (ClassNotFoundException | SQLException e) {
			JOptionPane.showMessageDialog(this, e.getMessage() + ". This is serious.", "Database Problem", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		} catch (TooManyEntriesException | NoSuchEntryException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Rating", JOptionPane.INFORMATION_MESSAGE);
		} finally {
			this.openScoringPanel(equipmentChoices, eventLevels, rounds, events);
		}

	}

	private void openNewScore() {
		findArcher();
		if (findArcherDLG.wasCancelled()) return;
		Round[] rounds = null;
		Event[] events = null;
		try {
			rounds = miscData.getRoundListing(true, false);
			events = miscData.getLatestEventListing(50);
			ArcherData archerData = new ArcherData();
			archerData.getCurrentRatingForArcher(currentArcher);

		} catch (ClassNotFoundException | SQLException e) {
			JOptionPane.showMessageDialog(this, e.getMessage() + ". This is serious.", "Database Problem", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		} catch (TooManyEntriesException | NoSuchEntryException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Rating", JOptionPane.INFORMATION_MESSAGE);
		} finally {
			this.openScoringPanel(equipmentChoices, eventLevels, rounds, events);
		}

	}

	private void openNewMultiEvent() {
		multieventPN.initialisePanel(disciplineChoices);
		CardLayout cl = (CardLayout) (this.getContentPane().getLayout());
		cl.show(this.getContentPane(), MULTI);
	}

	private void openNewEvent() {
		eventPN.initialisePanel(disciplineChoices);
		CardLayout cl = (CardLayout) (this.getContentPane().getLayout());
		cl.show(this.getContentPane(), EVENT);
	}

	private void findArcher() {
		try {
			Archer[] list = archerData.getListOfArchers(false);
			findArcherDLG = new ArcherDialog(this, true, list, false, equipmentChoices, disciplineChoices, categoryChoices);

			if (findArcherDLG.getListIndex() != -1) {
				currentArcher = list[findArcherDLG.getListIndex()];
			}

		} catch (ClassNotFoundException | SQLException e) {
			JOptionPane.showMessageDialog(this, "Blame the person who wrote this program. " + e.getStackTrace(), "Database Problems", JOptionPane.ERROR_MESSAGE);

		}

	}

	public void getBaseData() {
		fetchEquipmentChoices();
		fetchDisciplineChoices();
		fetchEventLevels();
		fetchCategories();
	}

	private void fetchEquipmentChoices() {
		try {
			equipmentChoices = miscData.getEquipmentListing();
		} catch (ClassNotFoundException | SQLException e) {
			JOptionPane.showMessageDialog(this, "Problem fetching equipment listing.", "Database Problems", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void fetchDisciplineChoices() {
		try {
			disciplineChoices = miscData.getDisciplineListing();
		} catch (ClassNotFoundException | SQLException e) {
			JOptionPane.showMessageDialog(this, "Problem fetching discipline listing.", "Database Problems", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void fetchEventLevels() {
		try {
			eventLevels = miscData.getEventLevelListing();
		} catch (ClassNotFoundException | SQLException e) {
			JOptionPane.showMessageDialog(this, "Problem fetching event levels. " + e.getStackTrace(), "Database Problems", JOptionPane.ERROR_MESSAGE);
		}

	}

	private void fetchCategories() {
		try {
			categoryChoices = miscData.getCategoryListing();
		} catch (ClassNotFoundException | SQLException e) {
			JOptionPane.showMessageDialog(this, "Problem fetching categories. " + e.getStackTrace(), "Database Problems", JOptionPane.ERROR_MESSAGE);
		}

	}

	public static void resetWindow() {
		CardLayout cl = (CardLayout) (window.getContentPane().getLayout());
		cl.show(window.getContentPane(), EMPTY);
	}
}
