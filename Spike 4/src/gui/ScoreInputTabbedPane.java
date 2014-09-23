package gui;

/*
 * TabbedPaneDemo.java requires one additional file:
 *   images/middle.gif.
 */

import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Robot;

import javax.swing.Action;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ScoreInputTabbedPane extends JPanel 
{
	private int arrowsPerEnd;
	private int ends;
	private int noRanges;
	private int paneWidth;
	private int paneHeight;
	private int rowHeight;
	
	private JTabbedPane tabbedPane;
	private JTextField [] tfs;
	
	private static final String shift = "Shift";
	private static final String change = "Change";
	private RangeTableModel rtm;
	
	
	
    public ScoreInputTabbedPane(int endLength, int ends, int noRanges) {
        super(new GridLayout(2, 1));
        this.arrowsPerEnd = endLength;
        this.ends = ends;
        this.noRanges = noRanges;
        tfs = new JTextField[noRanges];
        paneWidth = 60 * endLength + 10;
        rowHeight = 20;
        paneHeight = rowHeight * (ends+1) + rowHeight;
        
        
        tabbedPane = new JTabbedPane();
       // ImageIcon icon = createImageIcon("images/middle.gif");
        int rangeCount = 4;
        for (int i = 0; i < noRanges; i++) 
        {
        	 JComponent panel1 = makeRangePanel( i);
             tabbedPane.addTab("End "+(i+1), panel1);
             panel1.setPreferredSize(new Dimension(paneWidth, paneHeight+40));
           /*  JTextField rangeTotalTF = new JTextField("range Total");
             rangeTotalTF.setEditable(false);
             panel1.add(rangeTotalTF);*/
            // tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);

		}
  
        add(tabbedPane);
        
        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        
        JPanel controlPNL = new JPanel();
        controlPNL.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        
        gc.anchor = GridBagConstraints.PAGE_END;
        gc.gridx= 0;
        gc.gridy = 0;
        gc.ipadx = 50;
        gc.insets = new Insets(0,0,0,30);
        

        JPanel ratingPNL = new JPanel(new GridLayout(1,2));
        JLabel ratingLBL = new JLabel("Round Rating");
        JLabel ratingValueLBL = new JLabel("ssss");
        ratingValueLBL.setBorder(BasicBorders.getTextFieldBorder());
        ratingPNL.add(ratingLBL);
        ratingPNL.add(ratingValueLBL);
        controlPNL.add(ratingPNL, gc);
        add(controlPNL);
        
        
        gc.gridx= 1;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.EAST;
        gc.insets = new Insets(30,0,0,0);
        gc.ipadx = 50;
        
        JPanel buttonPNL = new JPanel(new GridLayout(3,1));
        JButton saveBTN = new JButton("Save");
        JButton cancelBTN = new JButton("Cancel");
        JButton ratingBTN = new JButton("Rating");
        buttonPNL.add(saveBTN);
        buttonPNL.add(cancelBTN);
        buttonPNL.add(ratingBTN);
        controlPNL.add(buttonPNL, gc);
       
    }
    
 
    protected JComponent makeRangePanel( int panelNo) {
    	JPanel panel = new JPanel(false);
    	// JLabel filler = new JLabel(text);
    	rtm = new RangeTableModel();

    	rtm.initialiseColumnNames();
    	rtm.initialiseData();

    	JTable table = new JTable(rtm)
    	{ 
    		DefaultTableCellRenderer renderRight=new RangeCellRenderer();

    		{//initializer block
    			renderRight.setHorizontalAlignment(SwingConstants.RIGHT);      
    		}

    		@Override
    		public TableCellRenderer getCellRenderer(int arg0, int arg1) {

    			// System.out.println("Arg 0 "+arg0+" arg 1 "+arg1); 
    			return renderRight;

    		}

    		public void changeSelection(final int row, final int column, boolean toggle, boolean extend)
    		{
    			//System.out.println("Current value = "+super.getModel().getValueAt(row, column ));
    			if (column < super.getColumnCount()-1)
    				super.getModel().setValueAt("", row, column);
    			super.changeSelection(row, column, toggle, extend);
    			if (!isCellEditable(row, column) && row+1 < getRowCount())
    			{
    				//here we want to make sure the row exists and has not been filled in yet - lest we overwrite the next row
    				if ( super.getModel().getValueAt(row+1, 0 ).equals("") )
    					changeSelection(row+1, 0, toggle, extend);
    			}
    			else{
    				editCellAt(row, column);

    				transferFocus();
    			}
    		}



    	};
    	table.setFont(new Font("Verdana", Font.BOLD, 12));

    	table.setPreferredScrollableViewportSize(new Dimension(paneWidth-10, (paneHeight-rowHeight)));
    	table.setRowHeight(rowHeight);

    
    	Action action = new FocusShiftAction();
    	KeyStroke numpad = KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD0, 0);    	
    	table.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(numpad, shift);
    	numpad = KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD1, 0);    	
    	table.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(numpad, shift);
    	numpad = KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD2, 0);    	
    	table.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(numpad, shift);
    	numpad = KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD3, 0);    	
    	table.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(numpad, shift);
    	numpad = KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD4, 0);    	
    	table.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(numpad, shift);
    	numpad = KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD5, 0);    	
    	table.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(numpad, shift);
    	numpad = KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD6, 0);    	
    	table.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(numpad, shift);
    	numpad = KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD7, 0);    	
    	table.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(numpad, shift);
    	numpad = KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD8, 0);    	
    	table.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(numpad, shift);
    	numpad = KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD9, 0);    	
    	table.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(numpad, shift);
    	numpad = KeyStroke.getKeyStroke(KeyEvent.VK_DIVIDE, 0);    	
    	table.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(numpad, shift);
    	numpad = KeyStroke.getKeyStroke(KeyEvent.VK_MULTIPLY, 0);    	
    	table.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(numpad, shift);
    	table.getActionMap().put(shift, action);

    	action = new TabChangeAction();
    	numpad = KeyStroke.getKeyStroke(KeyEvent.VK_ADD, 0);
    	tabbedPane.getInputMap(JTabbedPane.WHEN_IN_FOCUSED_WINDOW).put(numpad, change);
    	tabbedPane.getActionMap().put(change, action);
 
    	//table.setDefaultEditor(String.class, rce);
    	table.setFillsViewportHeight(true);
    	panel.setLayout(new GridBagLayout());
    	GridBagConstraints gc = new GridBagConstraints();
    	gc.fill = GridBagConstraints.HORIZONTAL;
    	gc.weightx = 0.5;
        gc.gridx = 0;
        gc.gridy = 0;
    	JScrollPane scrollPane = new JScrollPane(table);

    	panel.add(scrollPane, gc);
    	
    	JPanel totalsPN = new JPanel(false);
    	totalsPN.setLayout(new FlowLayout(FlowLayout.RIGHT));
    	JLabel lbl = new JLabel("Range total");
    	JTextField totalTF = new JTextField(0);
    	totalTF.setEditable(false);
    	totalTF.setPreferredSize(new Dimension(50, 20));
    	totalTF.setHorizontalAlignment(JTextField.RIGHT);
    	tfs[panelNo] = totalTF;
    	totalsPN.add(lbl);
    	totalsPN.add(totalTF);
    	gc.gridx = 0;
        gc.gridy = 1;
    	panel.add(totalsPN, gc);
 
    	return panel;
    }
    
    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = ScoreInputTabbedPane.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
   
   
    
    class RangeTableModel extends AbstractTableModel {
  		
    	private String[] columnNames;
    	private String[][] data ;
    	
    	   private void initialiseColumnNames() {
    			if (arrowsPerEnd == 6)
    			{
    				columnNames = new String[7];
    				columnNames[0] = "1st";
    				columnNames[1] = "2nd";
    				columnNames[2] = "3rd";
    				columnNames[3] = "4th";
    				columnNames[4] = "5th";
    				columnNames[5] = "6th";
    				columnNames[6] = "Total";
    			}
    				
    			else if(arrowsPerEnd == 3)
    			{
    				columnNames = new String[4];
    				columnNames[0] = "1st";
    				columnNames[1] = "2nd";
    				columnNames[2] = "3rd";
    				columnNames[6] = "Total";			
    			}
    			else
    			{
    				System.err.println("Weird number of arrows per end. Can't deal with this");
    				System.exit(0);
    			}
    			
    		}

    	
    	public void initialiseData() {
    		data = new String[ends][arrowsPerEnd+1];
    		for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data[i].length; j++) {
					data[i][j] = "";
				}
			}
	
}


		
 
        public int getColumnCount() {
            return arrowsPerEnd+1;
        }
 
        public int getRowCount() {
            return ends;
        }
 
        public String getColumnName(int col) {
            return columnNames[col];
        }
 
        public Object getValueAt(int row, int col) {
            return data[row][col];
        }
 
        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
        	Class cl = getValueAt(0, c).getClass();
        	
            return getValueAt(0, c).getClass();
        	
        }
 
        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col < arrowsPerEnd) {
                return true;
            } else {
                return false;
            }
        }
 
   
        public void setValueAt(Object value, int row, int col) 
        {
        	if (value.equals("/"))
        		data[row][col] = "X";
        	else if(value.equals("*"))
        		data[row][col] = "10";
        	else if(value.equals("0"))
        		data[row][col] = "M";
        	else
        		data[row][col] = (String)value;
        	calculateTotals(row, col);
        	calculateRangeTotals();
            fireTableCellUpdated(row, col);
            fireTableCellUpdated(row, arrowsPerEnd);
        }
        
        private void calculateTotals(int row, int column)
        {
        	int rowTotal = 0;
        	for (int i = 0; i < data[row].length-1; i++) 
        	{
				switch ( data[row][i]) {
				case "X":
					rowTotal += 10;
					break;
				case "M":
					break;
				case "":
					break;
				default:
					rowTotal += Integer.parseInt(data[row][i]);
					break;
				}
			}
        	data[row][data[row].length-1] = ""+ rowTotal;
        }
        private void calculateRangeTotals()
        {
        	int total = 0;
        	for (int i = 0; i < data.length; i++) 
        	{
        		try {
        			total += Integer.parseInt(data[i][data[i].length-1]);
				} catch (NumberFormatException e) {
					continue;
				}
					
			}
        	tfs[tabbedPane.getSelectedIndex()].setText(""+total);
        }
 
        private void printDebugData() {
            int numRows = getRowCount();
            int numCols = getColumnCount();
 
            for (int i=0; i < numRows; i++) {
                System.out.print("    row " + i + ":");
                for (int j=0; j < numCols; j++) {
                    System.out.print("  " + data[i][j]);
                }
                System.out.println();
            }
            System.out.println("--------------------------");
        }
    }


	
	 class RangeCellRenderer extends DefaultTableCellRenderer {
	    public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	        Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	        if (column == 6)
	        {	
	        	//System.out.println("Row "+row+" Column "+column);
	        	cellComponent.setBackground(java.awt.Color.GRAY.brighter());
	        }
	        else 
	        {
	        		cellComponent.setBackground(Color.WHITE);
	        }
	       
	        return cellComponent;
	    }
	    
    
	}

	    class RangeCellEditor extends AbstractCellEditor implements TableCellEditor
	    {
	    	JComponent component = new JTextField();
			@Override
			public Object getCellEditorValue() {
				System.out.println("Cell editor");
				return ((JTextField)component).getText();
			}

			@Override
			public Component getTableCellEditorComponent(JTable table,
					Object value, boolean isSelected, int row, int column) 
			{
				((JTextField)component).setText("");
				System.out.println("Cell editor component");
				return component;
			}
	


			
	    }

	      private class FocusShiftAction extends AbstractAction 
	      {
	    	  private JTable table;

	    	 
	           @Override
	           public void actionPerformed(ActionEvent e) 
	           {
	        	  
	             //  System.out.println("Row "+table.getSelectedRow() + " column "+table.getSelectedColumn());
	               Robot robot;
				try {
					robot = new Robot();
					robot.keyPress(KeyEvent.VK_TAB);
					
				} catch (AWTException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	              
	           }
	       }


	      private class TabChangeAction extends AbstractAction 
	      {
	    	  private JTable table;

	    	
	           @Override
	           public void actionPerformed(ActionEvent e) 
	           {
	        	   if(tabbedPane.getSelectedIndex()+1 < tabbedPane.getTabCount())
	        		   tabbedPane.setSelectedIndex((tabbedPane.getSelectedIndex()+1));
	           }
	       }

	
}
