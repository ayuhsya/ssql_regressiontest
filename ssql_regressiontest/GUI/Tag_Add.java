package ssql_regressiontest.GUI;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.print.attribute.HashAttributeSet;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.omg.CORBA.PRIVATE_MEMBER;

import ssql_regressiontest.Common.Common;
import ssql_regressiontest.Common.Database;
import ssql_regressiontest.Common.Dialog;
import ssql_regressiontest.Common.Functions;
import ssql_regressiontest.Common.GlobalEnv;
import ssql_regressiontest.ExecRegressionTest.SSQL_exec;
import ssql_regressiontest.GUI.TestResult_Detail.LineNumberView;

public class Tag_Add extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel mainPanel, tagButton_Field_Panel, tagLabelPanel;
	private JButton tagButton;
	private JTextField tagField;
	private JLabel tagLabel;

	public static void display() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Tag_Add frame = new Tag_Add();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Tag_Add() {

		setBounds(100, 100, 500, 100); //サイズ、位置設定
		addWindowListener(new MyWindowAdapter()); 		// 閉じるボタンを押した時の処理
		setTitle("New Tag Add"); //タイトル

		//タグ・ボタン
		tagField = new JTextField(20);
		tagButton = new JButton("Add");
		tagButton_Field_Panel = new JPanel();
		tagButton_Field_Panel.add(tagField);
		tagButton_Field_Panel.add(tagButton);

		//ラベル
		tagLabel = new JLabel("タグを新規作成 : ");
		tagLabelPanel = new JPanel();
		tagLabelPanel.add(tagLabel);

		//メインパネルへの追加
		mainPanel = new JPanel();
		mainPanel.add(tagLabelPanel);
		mainPanel.add(tagButton_Field_Panel);

		Container contentPane = getContentPane();
		contentPane.add(mainPanel, BorderLayout.CENTER);

		//閉じるときの処理
		class MyWindowAdapter extends WindowAdapter {
			public void windowClosing(WindowEvent we) {
				// System.exit(0);
			}
		}

		//追加ボタン
		tagButton.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				//tag_name_model_Aモデルの初期化
				TestCase_Add.initTable();
				Database.connect();
				//tagを新規に追加する
				//記入されているタグがDBに登録されているかを検査している(個数数えている)
				String search_tag_sql = "select count(*) as number from tag where t_name= '"+tagField.getText()+"';";
				ResultSet search_tag_rs = Database.select(search_tag_sql);
				int number = 0;	
				try {
					while (search_tag_rs.next()) {
						number = search_tag_rs.getInt("number");	}
					search_tag_rs.close();
					if (number == 0) {
						//重複していない場合(個数が0)
						String insert_new_tag = "INSERT INTO tag(t_name) VALUES('"+Database.getEscapedString(tagField.getText())+"');";
						//							System.out.println(insert_new_tag);
						Database.insert(insert_new_tag);
					}else{
						//重複している場合(個数が0以外の場合)
						Dialog.NormalDialog(tagField.getText()+" is already exists");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Database.close();

				if(TestCase_Add.jList_tag_name_A != null){
					//リスト一覧を取得する
					TestCase_Add.list_get(); 
					//モデルに値を挿入する
					TestCase_Add.insert_Model(); 
					//モデルをセットする
					TestCase_Add.jList_tag_name_A.setModel(TestCase_Add.tag_name_model_A); 
					System.out.println("queryAdd");
				}				

				if(Tag_List.tag_name_model_A != null){
					Tag_List.init_tag_name(); //初期化OK
					Tag_List.list_get(); //タグ一覧取得してきてOK
					Tag_List.insert_Model();//モデルに入れるOK
					Tag_List.tag_Button();//表示
					System.out.println("TagList");
				}


				dispose();
//				tagList.init_tag_name();
//				tagList.list_get();
//				tagList.insert_Model(); //モデルに値を挿入する
//				tagList.tag_Button();

				//				table.setModel(tableModel_list); //テーブルリストモデルを表示している


			}
		});
	}
};

