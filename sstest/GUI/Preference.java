package sstest.GUI;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import sstest.Common.GlobalEnv;

public class Preference extends JPanel {

	private static final long serialVersionUID = 1L;
	public static String username, selectedWorkingDir = "",  selectedOutDir = "" ,  selectedResultDir = "", selectedJar = "", sqlite_db_path1 = "", sqlite_db_path2 = "";
	private JPanel mainPanel, buttonPanel, ssqlPath_Panel, username_Panel, outDir_Fielld_Panel, resultDir_Fielld_Panel, sqlite_db_path_Panel1, sqlite_db_path_Panel2;
	private JButton pathButton, outDirButton, resultDirButton, jarButton, sqlite_db_path_Button1, sqlite_db_path_Button2;
	public static JTextField ssqlPathField;
	public static JTextField outDirPathField;
	public static JTextField resultDirPathField;
	public static JTextField resultNumberField;
	public static JTextField jarPathField;
	public static JTextField username_Field;
	public static JTextField sqlite_db_path_Field1, sqlite_db_path_Field2;
	private JLabel ssqlPlace, username_Label, outDirLabel, resultDirLabel, resultNumberLabel, jarLabel, sqlite_db_path_Label1, sqlite_db_path_Label2;
	private JPanel resultNumber_Fielld_Panel, jar_Fielld_Panel;
	private JSpinner spinner;
	public static SpinnerNumberModel model;


	/**
	 * Launch the application.
	 */
	public static void display() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Preference frame = new Preference();
					//					frame.setLocationRelativeTo(null);
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
	public Preference() {

		setBounds(100, 100, 450, 500);
		mainPanel = new JPanel();
		// field
		ssqlPath_Panel = new JPanel();
		username_Panel = new JPanel();
		sqlite_db_path_Panel1 = new JPanel();
		sqlite_db_path_Panel2 = new JPanel();
		outDir_Fielld_Panel = new JPanel();
		resultDir_Fielld_Panel = new JPanel();
		resultNumber_Fielld_Panel = new JPanel();
		jar_Fielld_Panel = new JPanel();
		//Path
		ssqlPathField = new JTextField("", 35);
		// 初期値
		ssqlPathField.setText(GlobalEnv.ssql_Path);
		ssqlPlace = new JLabel("SSQL本体の位置:");
		pathButton = new JButton("Select");
		ssqlPath_Panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		ssqlPath_Panel.add(ssqlPlace);
		ssqlPath_Panel.add(ssqlPathField);
		ssqlPath_Panel.add(pathButton);
		//SSQL本体で使用するライブラリを格納しているディレクトリ指定
		jarPathField = new JTextField(GlobalEnv.libs_Path, 35);
		jarLabel = new JLabel("使用するライブラリを格納しているディレクトリ:");
		jarButton = new JButton("Select");
		jar_Fielld_Panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		jar_Fielld_Panel.add(jarLabel);
		jar_Fielld_Panel.add(jarPathField);
		jar_Fielld_Panel.add(jarButton);
		//出力先
		outDirPathField = new JTextField(GlobalEnv.outDir_Path, 35);
		outDirLabel = new JLabel("実行結果出力先:");
		outDirButton = new JButton("Select");
		outDir_Fielld_Panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		outDir_Fielld_Panel.add(outDirLabel);
		outDir_Fielld_Panel.add(outDirPathField);
		outDir_Fielld_Panel.add(outDirButton);
		//解答出力先
		resultDirPathField = new JTextField(GlobalEnv.resultDir_Path, 35);
		resultDirLabel = new JLabel("解答出力先:");
		resultDirButton = new JButton("Select");
		resultDir_Fielld_Panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		resultDir_Fielld_Panel.add(resultDirLabel);
		resultDir_Fielld_Panel.add(resultDirPathField);
		resultDir_Fielld_Panel.add(resultDirButton);
		//解答保持数
		resultNumberLabel = new JLabel("Result holds the number:");
		model = new SpinnerNumberModel(GlobalEnv.result_Num, 3, 200, 1); //初期値・最小・最大 スピナー！
		spinner = new JSpinner(model);
		JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinner, "#,##0");
		spinner.setEditor(editor);
		spinner.setPreferredSize(new Dimension(150, 35));
		resultNumber_Fielld_Panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		resultNumber_Fielld_Panel.add(resultNumberLabel);
		resultNumber_Fielld_Panel.add(spinner);
		//		resultNumberField = new JTextField("", 15);
		//		resultNumber_Fielld_Panel.add(resultNumberField);

		//UserName
		username_Label = new JLabel("User name:");
		username_Field = new JTextField(GlobalEnv.user_Name, 35);
		username_Panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		username_Panel.add(username_Label);
		username_Panel.add(username_Field);

		//sqlite_db_path
		sqlite_db_path_Label1 = new JLabel("Test Sets Database:");
		sqlite_db_path_Label2 = new JLabel("Test Data Database:");
		sqlite_db_path_Button1 = new JButton("Select");
		sqlite_db_path_Button2 = new JButton("Select");
		sqlite_db_path_Field1 = new JTextField(GlobalEnv.sqlite_db_path1, 60);
		sqlite_db_path_Panel1.setLayout(new FlowLayout(FlowLayout.LEFT));
		sqlite_db_path_Panel1.add(sqlite_db_path_Label1);
		sqlite_db_path_Panel1.add(sqlite_db_path_Field1);
		sqlite_db_path_Panel1.add(sqlite_db_path_Button1);
		sqlite_db_path_Field1.setEnabled(false);
		sqlite_db_path_Button1.setEnabled(false);
		sqlite_db_path_Field2 = new JTextField(GlobalEnv.sqlite_db_path2, 60);
		sqlite_db_path_Panel2.setLayout(new FlowLayout(FlowLayout.LEFT));
		sqlite_db_path_Panel2.add(sqlite_db_path_Label2);
		sqlite_db_path_Panel2.add(sqlite_db_path_Field2);
		sqlite_db_path_Panel2.add(sqlite_db_path_Button2);
		sqlite_db_path_Field2.setEnabled(false);
		sqlite_db_path_Button2.setEnabled(false);
		

		buttonPanel = new JPanel();
		//		registButton = new JButton("Regist");

		//未入力の場合は登録されないようにする
		//ユーザー名とパス記入されているかどうかで場合分けする
		if (username == null || selectedWorkingDir == null || selectedOutDir == null || selectedResultDir == null || selectedJar == null || sqlite_db_path1 == null || sqlite_db_path2 == null) {
			System.out.println("empty");
		} else {
			ssqlPathField.setText(selectedWorkingDir);
			outDirPathField.setText(selectedOutDir);
			resultDirPathField.setText(selectedResultDir);
			jarPathField.setText(selectedJar);
			username_Field.setText(username);
			sqlite_db_path_Field1.setText(sqlite_db_path1);
			sqlite_db_path_Field2.setText(sqlite_db_path2);
		}	

		//レイアウトボックスレイアウト！
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(new JSeparator(JSeparator.HORIZONTAL));
		mainPanel.add(ssqlPath_Panel);
		mainPanel.add(jar_Fielld_Panel);
		mainPanel.add(outDir_Fielld_Panel);
		mainPanel.add(resultDir_Fielld_Panel);
		mainPanel.add(resultNumber_Fielld_Panel);
		mainPanel.add(username_Panel);
		mainPanel.add(buttonPanel);
		mainPanel.add(new JSeparator(JSeparator.HORIZONTAL));
		mainPanel.add(sqlite_db_path_Panel1);
		mainPanel.add(sqlite_db_path_Panel2);
		mainPanel.add(new JSeparator(JSeparator.HORIZONTAL));
		add(mainPanel);

		//	//			登録ボタン
		//		registButton.addActionListener(new AbstractAction() {
		//			@Override
		//			public void actionPerformed(ActionEvent e) {
		//				// TODO Auto-generated method stub
		//				if(!checkInputValue()){
		//					Dialog.ErrorDialog("failed;;;;;;");
		//				}else{
		//					ConfigData confData = new ConfigData();
		//					confData.setSsqlLocationPath(ssqlPathField.getText());
		//					confData.setSsqlLibsLocationPath(jarPathField.getText());
		//					confData.setSsqlOutLocationPath(outDirPathField.getText());
		//					confData.setSsqlResultLocationPath(resultDirPathField.getText());
		//					confData.setResultRegistNumber((Integer)model.getValue());
		//					confData.setUserName(username_Field.getText());
		//
		//					if(Common.saveConfig(confData)){
		//						System.out.println("success");;
		//					} else {
		//						System.out.println("fail");
		//					}
		//				}
		//			}
		//		});

		//Path設定
		pathButton.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e){
				JFileChooser filechooser = new JFileChooser("c:¥¥temp");
				filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int selected = filechooser.showSaveDialog(null);
				if (selected == JFileChooser.APPROVE_OPTION){
					java.io.File file = filechooser.getSelectedFile();
					ssqlPathField.setText(file.getAbsolutePath());
				}
			}
		});

		//ディレクトリ設定
		jarButton.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e){
				JFileChooser filechooser4 = new JFileChooser("c:¥¥temp");
				filechooser4.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int selected = filechooser4.showSaveDialog(null);
				if (selected == JFileChooser.APPROVE_OPTION){
					java.io.File file = filechooser4.getSelectedFile();
					jarPathField.setText(file.getAbsolutePath());
				}
			}
		});

		//出力先設定
		outDirButton.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e){
				JFileChooser filechooser2 = new JFileChooser("c:¥¥temp");
				filechooser2.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int selected = filechooser2.showSaveDialog(null);
				if (selected == JFileChooser.APPROVE_OPTION){
					java.io.File file = filechooser2.getSelectedFile();
					outDirPathField.setText(file.getAbsolutePath());
				}
			}
		});

		//出力先設定
		resultDirButton.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e){
				JFileChooser filechooser3 = new JFileChooser("c:¥¥temp");
				filechooser3.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int selected = filechooser3.showSaveDialog(null);
				if (selected == JFileChooser.APPROVE_OPTION){
					java.io.File file = filechooser3.getSelectedFile();
					resultDirPathField.setText(file.getAbsolutePath());
				}
			}
		});
		
		//SQLite3 Database Path 1
		sqlite_db_path_Button1.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e){
				JFileChooser filechooser3 = new JFileChooser("c:¥¥temp");
				filechooser3.setFileSelectionMode(JFileChooser.FILES_ONLY);
				//filechooser3.setFileFilter(new FileNameExtensionFilter(".db", "db"));
				int selected = filechooser3.showSaveDialog(null);
				if (selected == JFileChooser.APPROVE_OPTION){
					java.io.File file = filechooser3.getSelectedFile();
					sqlite_db_path_Field1.setText(file.getAbsolutePath());
				}
			}
		});
		//SQLite3 Database Path 2
		sqlite_db_path_Button2.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e){
				JFileChooser filechooser3 = new JFileChooser("c:¥¥temp");
				filechooser3.setFileSelectionMode(JFileChooser.FILES_ONLY);
				//filechooser3.setFileFilter(new FileNameExtensionFilter(".db", "db"));
				int selected = filechooser3.showSaveDialog(null);
				if (selected == JFileChooser.APPROVE_OPTION){
					java.io.File file = filechooser3.getSelectedFile();
					sqlite_db_path_Field2.setText(file.getAbsolutePath());
				}
			}
		});
	}

	//入力に空がないか検査
	public boolean checkInputValue() {
		if (username_Field.getText().isEmpty()
				|| ssqlPathField.getText().isEmpty()
				|| outDirPathField.getText().isEmpty()
				|| resultDirPathField.getText().isEmpty()
				|| sqlite_db_path_Field1.getText().isEmpty()
				|| sqlite_db_path_Field2.getText().isEmpty()) {
			return false;
		} else 
			return true;
	}

}
