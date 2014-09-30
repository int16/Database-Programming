package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

import scores.Archer;
import scores.Category;
import scores.Discipline;
import scores.Equipment;
import scores.RoundGroup;

import javax.swing.JDialog;

import database.ArcherData;
import exceptions.NoSuchEntryException;

public class ArcherDialog extends JDialog implements ActionListener {
	private JComboBox<Archer> archersCX = null;
	private JComboBox<Equipment> equipCB = null;
	private JComboBox<Category> catCB = null;
	private JComboBox<Discipline> disciplineCB = null;
	private Equipment[] equipment = null;
	private Discipline[] discipline = null;
	private Category[] categories = null;
	private JButton okButton = null;
	private JButton cancelButton = null;
	private int listIndex = -1;
	private boolean cancelled;
	private Archer[] archers;

	public boolean wasCancelled() {
		return cancelled;
	}

	public ArcherDialog(JFrame frame, boolean modal, Archer[] archers, boolean includeArchived, Equipment[] equip, Discipline[] disc, Category[] cats) {
		super(frame, "Pick archer", modal);

		equipment = equip;
		this.archers = archers;
		discipline = disc;
		categories = cats;

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		archersCX = new JComboBox<Archer>(archers);
		archersCX.setSelectedIndex(-1);
		archersCX.setKeySelectionManager(new ArcherKeySelectionManager());
		archersCX.addActionListener(new ComboBoxListener());
		archersCX.setPreferredSize(new Dimension(100, 25));
		panel.add(archersCX);
		getContentPane().setLayout(new GridLayout(5, 1));
		getContentPane().add(panel);

		JPanel panel2 = new JPanel();
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.PAGE_AXIS));
		panel2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		getContentPane().add(panel2);

		equipCB = new JComboBox<Equipment>(equip);
		equipCB.setKeySelectionManager(new ArcherKeySelectionManager());

		panel2.add(equipCB);
		panel2.add(Box.createRigidArea(new Dimension(0, 10)));

		JPanel panel2andahalf = new JPanel();
		panel2andahalf.setLayout(new BoxLayout(panel2andahalf, BoxLayout.PAGE_AXIS));
		panel2andahalf.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		getContentPane().add(panel2andahalf);

		catCB = new JComboBox<Category>(categories);
		catCB.setKeySelectionManager(new ArcherKeySelectionManager());

		panel2andahalf.add(catCB);
		panel2andahalf.add(Box.createRigidArea(new Dimension(0, 10)));

		JPanel panel3 = new JPanel();
		panel3.setLayout(new BoxLayout(panel3, BoxLayout.PAGE_AXIS));
		panel3.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		getContentPane().add(panel3);

		disciplineCB = new JComboBox<Discipline>(discipline);
		disciplineCB.setKeySelectionManager(new ArcherKeySelectionManager());

		panel3.add(disciplineCB);
		panel3.add(Box.createRigidArea(new Dimension(0, 10)));

		okButton = new JButton("Ok");
		okButton.addActionListener(this);
		okButton.setPreferredSize(new Dimension(80, 25));
		getRootPane().setDefaultButton(okButton);

		cancelButton = new JButton("Cancel");
		cancelButton.setPreferredSize(new Dimension(80, 25));
		cancelButton.addActionListener(this);

		JPanel buttonPane = new JPanel();

		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(cancelButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(okButton);

		getContentPane().add(buttonPane);
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		pack();
		setLocationRelativeTo(frame);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (okButton == e.getSource()) {
			cancelled = false;
			listIndex = archersCX.getSelectedIndex();
			archers[listIndex].setCurrentEquipment(((Equipment) equipCB.getSelectedItem()));
			archers[listIndex].setCurrentDiscipline(((Discipline) disciplineCB.getSelectedItem()));
			archers[listIndex].setCurrentCat(((Category) catCB.getSelectedItem()));
			ArcherData data = new ArcherData();
			try {
				// this shouldn't be here
				data.setCategoryForArcher(archers[listIndex]);
			} catch (ClassNotFoundException | SQLException | NoSuchEntryException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			setVisible(false);
		} else if (cancelButton == e.getSource()) {
			cancelled = true;
			listIndex = -1;
			setVisible(false);
		}

	}

	public int getListIndex() {
		return listIndex;
	}

	class ArcherKeySelectionManager implements JComboBox.KeySelectionManager {
		long lastKeyTime = 0;
		String pattern = "";

		@Override
		public int selectionForKey(char aKey, ComboBoxModel model) {
			int selIx = 01;
			Object sel = model.getSelectedItem();
			if (sel != null) {
				for (int i = 0; i < model.getSize(); i++) {
					if (sel.equals(model.getElementAt(i))) {
						selIx = i;
						break;
					}
				}
			}
			long curTime = System.currentTimeMillis();
			if (curTime - lastKeyTime < 300) {
				pattern += ("" + aKey).toLowerCase();
			} else {
				pattern = ("" + aKey).toLowerCase();
			}
			lastKeyTime = curTime;
			for (int i = selIx + 1; i < model.getSize(); i++) {
				String s = model.getElementAt(i).toString().toLowerCase();
				if (s.startsWith(pattern)) {
					return i;
				}
			}
			for (int i = 0; i < selIx; i++) {
				if (model.getElementAt(i) != null) {
					String s = model.getElementAt(i).toString().toLowerCase();
					if (s.startsWith(pattern)) {
						return i;
					}
				}
			}
			return -1;
		}

	}

	private class ComboBoxListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			selectEquipmentForArcher((Archer) archersCX.getSelectedItem());
			selectDisciplineForArcher((Archer) archersCX.getSelectedItem());
			selectCategoryForArcher((Archer) archersCX.getSelectedItem());
		}

	}

	private void selectCategoryForArcher(Archer a) {
		ArcherData ad = new ArcherData();
		try {
			ad.setCategoryForArcher(a);
			Category[] newCat = Category.getApplicableCategories(a.getCat(), categories);
			catCB.removeAllItems();
			int archerCat = -1;
			for (int i = 0; i < newCat.length; i++) {
				if (newCat[i].getId() == a.getCat().getId()) archerCat = i;
				catCB.addItem(newCat[i]);
			}
			catCB.setSelectedIndex(archerCat);
		} catch (ClassNotFoundException | SQLException | NoSuchEntryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void selectEquipmentForArcher(Archer a) {
		int i;
		for (i = 0; i < equipment.length; i++) {
			if (equipment[i].getId() == a.getDefaultEquipment().getId()) break;
		}

		equipCB.setSelectedItem(equipment[i]);
	}

	private void selectDisciplineForArcher(Archer a) {
		int i;
		for (i = 0; i < discipline.length; i++) {
			if (discipline[i].getId() == a.getDefaultDiscipline().getId()) break;
		}

		disciplineCB.setSelectedItem(discipline[i]);
	}
}
