package sstest.GUI;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class Generate_Reference extends JPanel {
	// TODO need variables to store list of filenames
	// make file selector with multiple selections
	// backstopJS call here?
	// use regressiontestresults class
	private static final long serialVersionUID = 1L;
	private JPanel mainPanel, buttonPanel;
	private JButton refreshButton, generateButton;
	public static RefTableModel refList;
	public static JTable refTable;
	private JScrollPane sp;
	private final static String columns[] = {"Filename", "Path"};
	private final static int rowNum = 0;
	public static Generate_Reference frame = null;
	private static int tableRowCount = 0;
	
	// Path to the reference HTMLs
	private static String backstopReferencesPath = "/Users/ayushya/Documents/eclipse-workspace/sstest/backstop/backstop_data/reference_htmls";
	private static String backstopRootPath = "/Users/ayushya/Documents/eclipse-workspace/sstest/backstop";
	private static String concatenatedPathString = "";
	private static String[] envVars = {"PATH=/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin"};
	
	/**
	 * Launch the application.
	 */
	public static void display() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					initTable();
					
					if (frame != null) return;
					frame = new Generate_Reference();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Generate_Reference() {
		setBounds(100, 100, 800, 700);
		mainPanel = new JPanel();
		
		refList = new RefTableModel(columns, rowNum){
			private static final long serialVersionUID = 1L;
			@Override public Class<?> getColumnClass(int column) {
				return getValueAt(0, column).getClass();
			}
		};
		refTable = new JTable(refList){
			private static final long serialVersionUID = 1L;
			public Component prepareRenderer(TableCellRenderer tcr, int row, int column) {
				Component c = super.prepareRenderer(tcr, row, column);
				if (c instanceof JComponent) {
					JComponent l = (JComponent) c;
					Object o = getValueAt(row, column);
					String str = o.toString();/*
					if(column == 4){
						// ポップアップ表示してるとこ
						l.setToolTipText(str);
					}*/
				}
				return c;
			}
		};
		
		DefaultTableCellRenderer rend;
		rend = new DefaultTableCellRenderer();
		rend.setAlignmentX(CENTER_ALIGNMENT);
		
		sp = new JScrollPane(refTable);
		sp.setPreferredSize(new Dimension(800, 400));
		
		refTable.getColumn("Filename").setPreferredWidth(200);
		refTable.getColumn("Path").setPreferredWidth(600);
			
		
		refreshButton = new JButton("Refresh");
		generateButton = new JButton("Generate");
		buttonPanel = new JPanel();
		buttonPanel.add(refreshButton);
		buttonPanel.add(generateButton);

		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		mainPanel.add(sp);
		mainPanel.add(buttonPanel);
		
		add(mainPanel);
		//addWindowListener(new MyWindowAdapter());
		
		refreshButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final File folder = new File(backstopReferencesPath);
				listFilesForFolder(folder);
				System.out.println(concatenatedPathString);
			}
		});
		
		generateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("In generate BUTTON! " + concatenatedPathString);
				final String[] Command = {"/usr/local/bin/backstop", "reference", "--configPath=backstop-settings.js", "--paths=" + concatenatedPathString};   //Bash Command
				//final String Command = "printenv";
				
				// create a process and execute
			    try {
			    		ProcessBuilder builder = new ProcessBuilder(Command);
			    		builder.redirectErrorStream(true);
			    		builder.directory(new File(backstopRootPath));
			    		Map<String, String> env =  builder.environment();
			    		env.put("PATH", "/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin");
					Process p = builder.start();

					BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
					String line;
					while ((line = in.readLine()) != null) {
					    System.out.println("STDOUT: " + line);
					}
					try {
						p.waitFor();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					System.out.println("Generate reference ended.");
					
					in.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}
	
	public class RefTableModel extends DefaultTableModel{
		RefTableModel(String[] columnNames, int rowNum){
			super(columnNames, rowNum);
		}

		public Class getColumnClass(int col){
			return getValueAt(0, col).getClass();
		}

	}
	
	public static void insertTableData(String filename, String path) {
		refList.insertRow(tableRowCount++, 
				new Object[]{filename, path});
	}
	
	public static void initTable() {
		try { refList.setRowCount(0); } catch (Exception e) { }
		tableRowCount = 0;
	}
	
	public void listFilesForFolder(final File folder) {
		boolean flag = true;
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else {
	        		insertTableData(fileEntry.getName(), fileEntry.getAbsolutePath());
	        		
	        		// To remove ending commas
	        		if (flag) {
	        			concatenatedPathString += fileEntry.getName();
	        			flag = false;
	        		} else {
	        			concatenatedPathString += "," + fileEntry.getName();
	        		}
	        }
	    }
	}

}
