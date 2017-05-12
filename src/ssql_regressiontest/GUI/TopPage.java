package ssql_regressiontest.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Timestamp;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ssql_regressiontest.Class.ConfigData;
import ssql_regressiontest.Common.Common;
import ssql_regressiontest.Common.GlobalEnv;

public class TopPage extends JFrame {
	private static final long serialVersionUID = 1L;
	static JButton exit_Button, aaaaa_Button;
	static int queruyNum = 0;
	public JTabbedPane tabbedpane;
	public static JPanel tabPanel0, tabPanel1, tabPanel2, tabPanel3;
	private JLabel regression_test_Label;
	static Preference preference;


	public static void display() {
		TopPage frame = new TopPage();
		frame.setBounds(10, 10, 1200, 1200);
		frame.setTitle("SuperSQL　RegressionTestTool");
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public TopPage() {
		setLocationRelativeTo(null);								//中央に表示させる
		addWindowListener(new MyWindowAdapter());					//閉じるボタンを押した時の処理
		
		//TODO
		try {
			Common.readConfig(
					GlobalEnv.CONFIG_FILE_NAME
					); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		//タブ管理
		JTabbedPane tabbedpane = new JTabbedPane();
		JPanel topPage_Panel = new JPanel();
		JLabel regression_test_Label = new JLabel();

		topPage_Panel.add(regression_test_Label);

		preference = new Preference();
		tabbedpane.addTab("Regression Test Run", new Test_Run_and_Result());
		tabbedpane.addTab("Test case List", new TestCase_List());
		tabbedpane.addTab("Tag List", new Tag_List());
		tabbedpane.addTab("Preference", preference);
		tabbedpane.setBackground(Color.WHITE);//ここですべてのタブの色を設定しています。

		// 設定ファイルの読み込み(.coufigファイルが存在していない時)
		if(!GlobalEnv.configFlag){
			tabbedpane.setSelectedIndex(3);
		}

		tabbedpane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if(!preference.checkInputValue()){
					//空値があった時はpreferenceから移動できないようにする
					tabbedpane.setSelectedIndex(3);
					if(tabbedpane.getSelectedIndex() == 3){
						System.out.println("NOOOOOOOOOOOOOOOO");
					}
					//					Dialog.ErrorDialog("Please input all preference values.");
					return;
				}else{
					//タブの切り替えの時の.configを保存
					configSave();
					//.configの読み込み
					Common.readConfig(GlobalEnv.CONFIG_FILE_NAME); 
				}
			}
		});

		//パネル設定、パネルレイアウト設定
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

		//アイコン表示
		ImageIcon icon1 = new ImageIcon("."+GlobalEnv.OS_FS+"image"+GlobalEnv.OS_FS+"ssqlimage.png");  // ImageIconクラスでアイコンとして画像を読み込みます。
		MediaTracker tracker = new MediaTracker(this);
		Image smallImg = icon1.getImage().getScaledInstance((int) (icon1.getIconWidth() * 0.15), -1, // getScaledInstanceで大きさを変更します。
				Image.SCALE_SMOOTH);
		tracker.addImage(smallImg, 1); 	// MediaTrackerで処理の終了を待ちます。
		ImageIcon smallIcon = new ImageIcon(smallImg);
		JLabel icon_Label = new JLabel(smallIcon, JLabel.LEFT);  // JLabelにアイコンを設定します。

		//ボタン作成
		exit_Button = new JButton("Exit");

		//位置調節
		icon_Label.setAlignmentX(Component.CENTER_ALIGNMENT);

		p.add(icon_Label);
		p.add(tabbedpane);
		p.add(exit_Button);
		getContentPane().add(p, BorderLayout.CENTER);

		//終了ボタン(閉じると同じ処理)
		exit_Button.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				configSave();
				System.exit(0);
			}
		});
	}

	//閉じるを押した時
	class MyWindowAdapter extends WindowAdapter {
		public void windowClosing(WindowEvent we) {
			configSave();
			System.exit(0);
		}
	}

	private void configSave(){
		ConfigData confData = new ConfigData();
		confData.setSsqlLocationPath(Preference.ssqlPathField.getText());
		confData.setSsqlLibsLocationPath(Preference.jarPathField.getText());
		confData.setSsqlOutLocationPath(Preference.outDirPathField.getText());
		confData.setSsqlResultLocationPath(Preference.resultDirPathField.getText());
		confData.setResultRegistNumber((Integer)Preference.model.getValue());
		confData.setUserName(Preference.username_Field.getText());
		confData.set_sqlite_db_path(Preference.sqlite_db_path_Field.getText());
		if(Common.saveConfig(confData)){
			System.out.println("Config save success");;
		} else {
			System.out.println("Config save fail");
		}
	}

}