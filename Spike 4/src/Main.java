import gui.MainWindow;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

	public static void main(String[] args) {
		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				createAndShowGUI(6, 5, 4);
			}
		});
		// System.out.println( HandicapCalculator.getArcherExpScore(50, 65, "L")*90);

	}

	public static void createAndShowGUI(int endLength, int ends, int noRanges) {
		// Create and set up the window.

		ImageIcon icon = createImageIcon("bhca_logo.jpg");

		MainWindow frame = new MainWindow("Archery Score Recording", icon);
		frame.getBaseData();
		frame.initialiseMainWindow();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.pack();
		frame.setVisible(true);

	}

	protected static ImageIcon createImageIcon(String path) {
		return new ImageIcon(path);

	}

}
