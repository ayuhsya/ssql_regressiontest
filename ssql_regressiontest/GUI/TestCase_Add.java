package ssql_regressiontest.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
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
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import javax.print.attribute.HashAttributeSet;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
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
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.omg.CORBA.PRIVATE_MEMBER;
import org.omg.CORBA.PUBLIC_MEMBER;

import ssql_regressiontest.Common.Common;
import ssql_regressiontest.Common.Database;
import ssql_regressiontest.Common.Dialog;
import ssql_regressiontest.Common.Functions;
import ssql_regressiontest.Common.GlobalEnv;
import ssql_regressiontest.ExecRegressionTest.Exec;
import ssql_regressiontest.ExecRegressionTest.SSQL_exec;
import ssql_regressiontest.GUI.TestResult_Detail.LineNumberView;
import sun.rmi.runtime.Log;

public class TestCase_Add extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel mainPanel, buttonPanel, tagPanel, queryTitle_Panel, queryAuthor_Panel, queryDescription_Panel, queryTag_Panel,queryOutput_Panel, queryContents_Panel, queryTag_Button;
	private JButton cancelButton, addButton, exitButton, createTagButton;
	static JRadioButton[] radioButton_Media;
	private JRadioButton radioButton_XML;
	//	private static ArrayList<QueryTag> tag_list;
	private static HashMap<String, Integer> tagMap;
	private static ArrayList<String> tagNameList;
	private JMenuItem deleteMenuItem;
	private JPopupMenu popupMenu;
	private static JTextField queryTitle, queryAuthor;
	private static JTextArea textArea, queryDescriptionArea;
	private JLabel q_Author, q_Genre, q_Tag, q_Title,queryContents, outputLabel;
	private JScrollPane scrollpane_description, scrollpane_contents;
	public static JList<String> jList_tag_name_A, jList_tag_name_B; //List表示
	public static DefaultListModel<String> tag_name_model_A, tag_name_model_B; 
	private static Timestamp time;


	/**
	 * Launch the application.
	 */
	public static void display() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestCase_Add frame = new TestCase_Add();
					frame.setLocationRelativeTo(null);
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
	public TestCase_Add() {

		setBounds(100, 100, 800, 700); //サイズ、位置設定
		addWindowListener(new MyWindowAdapter()); // 閉じるボタンを押した時の処理
		setTitle("New TestCase Add"); //タイトル
		list_get(); //タグ一覧を取得

		//右クリックで削除可能
		popupMenu = new JPopupMenu();
		deleteMenuItem = new JMenuItem("delete");
		popupMenu.add(deleteMenuItem);
		deleteMenuItem.addActionListener(new DeleteAction());

		// field
		mainPanel = new JPanel();
		queryTitle_Panel = new JPanel();
		queryAuthor_Panel = new JPanel();
		queryDescription_Panel = new JPanel();
		queryTag_Panel = new JPanel();
		queryContents_Panel = new JPanel();
		queryOutput_Panel = new JPanel();
		tagPanel = new JPanel();
		queryTag_Button = new JPanel();

		//タイトル
		q_Title = new JLabel("          TestName : "); //クエリタイトル名
		queryTitle = new JTextField("", 30);
		queryTitle_Panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		queryTitle_Panel.add(q_Title);
		queryTitle_Panel.add(queryTitle);

		//クエリ内容
		queryContents = new JLabel("         Query : "); //クエリ内容
		textArea = new JTextArea(10,15);
		textArea.setLineWrap(true); //まず、テキストエリアの文字列折り返しを有効にする
		textArea.setEditable(true);//編集可能
		scrollpane_contents = new JScrollPane(textArea); //スクロールパネル
		scrollpane_contents.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);//垂直方向
		scrollpane_contents.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);//水平方向禁止
		scrollpane_contents.setPreferredSize(new Dimension(500, 200)); //スクロールパネルサイズ
		//		queryContents_Panel.setLayout(new BoxLayout(queryContents_Panel, BoxLayout.Y_AXIS));
		queryContents_Panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		queryContents_Panel.add(queryContents);
		queryContents_Panel.add(scrollpane_contents);
		//		scrollpane_contents.setRowHeaderView(new result_Detail.LineNumberView(textArea)); //行番号表示

		//説明
		q_Genre = new JLabel("Description : "); //説明・メモ的なもの
		queryDescriptionArea = new JTextArea(6,15);
		queryDescriptionArea.setLineWrap(true); //まず、テキストエリアの文字列折り返しを有効にする
		queryDescriptionArea.setEditable(true);//編集可能
		scrollpane_description = new JScrollPane(queryDescriptionArea);
		scrollpane_description.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); //垂直方向
		scrollpane_description.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); //水平方向禁止
		scrollpane_description.setPreferredSize(new Dimension(500, 100));
		//		queryDescription_Panel.setLayout(new BoxLayout(queryDescription_Panel, BoxLayout.Y_AXIS));
		queryDescription_Panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		queryDescription_Panel.add(q_Genre);
		queryDescription_Panel.add(scrollpane_description);

		//作者
		q_Author = new JLabel("      Author : "); //クエリの作成者
		queryAuthor = new JTextField("", 30); 
		queryAuthor.setText(GlobalEnv.user_Name); //設定画面で設定したユーザー名を入力している
		queryAuthor.setEditable(false); //編集不可にしている
		queryAuthor_Panel.setLayout(new FlowLayout(FlowLayout.LEFT)); //左詰めにしている
		queryAuthor_Panel.add(q_Author);
		queryAuthor_Panel.add(queryAuthor);

		//出力形式
		radioButton_Media = new JRadioButton[3];
		radioButton_Media[0] = new JRadioButton("HTML",true);
		radioButton_Media[1] = new JRadioButton("Mobile_HTML5");
		radioButton_Media[2] = new JRadioButton("XML");
		//		radioButton_Media[3] = new JRadioButton("HTML_Flexbox");
		//		radioButton_Media[4] = new JRadioButton("PDF");
		//		radioButton_Media[5] = new JRadioButton("CSS");


		outputLabel = new JLabel("      Media : ");

		ButtonGroup group = new ButtonGroup();
		group.add(radioButton_Media[0]);
		group.add(radioButton_Media[1]);
		group.add(radioButton_Media[2]);

		queryOutput_Panel.setLayout(new FlowLayout(FlowLayout.LEFT)); //左詰めにしている
		queryOutput_Panel.add(outputLabel);
		queryOutput_Panel.add(radioButton_Media[0]);
		queryOutput_Panel.add(radioButton_Media[1]);
		queryOutput_Panel.add(radioButton_Media[2]);
		//		queryOutput_Panel.add(radioButton_HTML[3]);


		//タグ
		q_Tag = new JLabel("           Tag : "); 
		queryTag_Panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		queryTag_Panel.add(q_Tag);
		createTagButton = new JButton("+");
		queryTag_Panel.add(createTagButton); 

		//JList関係
		tag_name_model_A = new DefaultListModel<>();
		jList_tag_name_A = new JList<>(tag_name_model_A);
		tag_name_model_B = new DefaultListModel<>();
		jList_tag_name_B = new JList<>(tag_name_model_B);

		//アレイリストから情報取り出している
		//		String listget_tag_name = "";
		insert_Model();

		//スクロールペイン
		JScrollPane sp_A = new JScrollPane(jList_tag_name_A);  //スクロールペインを作る
		sp_A.setPreferredSize(new Dimension(200, 100));
		JScrollPane sp_B = new JScrollPane(jList_tag_name_B);  //スクロールペインを作る
		sp_B.setPreferredSize(new Dimension(200, 100));

		//マウスアクションとポップアップメニュー(削除)
		jList_tag_name_A.addMouseListener(new MouseActionA());
		jList_tag_name_B.addMouseListener(new MouseActionB());
		jList_tag_name_B.setComponentPopupMenu(popupMenu);

		getContentPane().add(queryTag_Panel, BorderLayout.CENTER);
		queryTag_Button.setLayout(new BoxLayout(queryTag_Button, BoxLayout.Y_AXIS));

		queryTag_Panel.add(sp_A);    //パネルにスクロールペインを追加
		queryTag_Panel.add(queryTag_Button);
		queryTag_Panel.add(sp_B);    //パネルにスクロールペインを追加

		//ボタン
		exitButton = new JButton("Exit");
		cancelButton = new JButton("Cancel");
		addButton = new JButton("Add");
		buttonPanel = new JPanel();
		buttonPanel.add(cancelButton);
		buttonPanel.add(addButton); 
		buttonPanel.add(exitButton);

		//レイアウトボックスレイアウト！
		mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(queryTitle_Panel);
		mainPanel.add(queryAuthor_Panel);
		mainPanel.add(queryOutput_Panel);
		mainPanel.add(queryTag_Panel);
		mainPanel.add(queryDescription_Panel);
		mainPanel.add(queryContents_Panel);
		mainPanel.add(buttonPanel);

		Container contentPane = getContentPane();
		contentPane.add(mainPanel, BorderLayout.CENTER);




		/****
		 * ボタンアクション
		 * *******/

		// 追加ボタンアクション
		addButton.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				// 別スレッド処理 (非同期処理)
				new Thread(new Runnable() {
					@Override
					public void run() {
						addButton.setEnabled(false);
						addButton.setText("Running...");
						//未入力のものを受け付けない場合
						if (queryTitle.getText().isEmpty() || queryAuthor.getText().isEmpty() || tag_name_model_B.getSize()==0 || queryDescriptionArea.getText().isEmpty() || queryContents.getText().isEmpty()) {
							System.out.println("@@@@queryAdd Failed@@@@");
							Dialog.ErrorDialog("未入力のフォームがある！");
							addButton.setEnabled(true);
							addButton.setText("Add");
						} else{ 
							//すべて入力されている場合
							Database.connect();
							//次のシリアルの値を取得している
//							String sql = "SELECT last_value+1 as seq_num FROM query_id_seq;";
							String sql = "SELECT seq+1 as seq_num FROM SQLITE_SEQUENCE WHERE name='query';";
							ResultSet count_rs = Database.select(sql);
							int seq_num = 1;
							try {
								while (count_rs.next()) {
									seq_num = count_rs.getInt("seq_num");
								}
							} catch (SQLException e) {
								e.printStackTrace();
							}
							System.out.println("seq_num = "+seq_num);
//							if(seq_num < 1){
//								//TODO エラー処理の追加
//							}

							/* 変数の定義 */
							String result = "";
							String workingDir = Functions.getWorkingDir();
							String path = workingDir + GlobalEnv.OS_FS +"test_files"+ GlobalEnv.OS_FS;
							//							String resultDir = workingDir + GlobalEnv.OS_FS +"test_results"+ GlobalEnv.OS_FS;
							String classPath = Common.getClassPath();
							String driver = GlobalEnv.SSQL_DRIVER;
							String db = GlobalEnv.SSQL_DB;
							String extension = GlobalEnv.SSQL_EXTENSION;
							String wd = Functions.getWorkingDir();

							String ssqlQueryName2 = path + "q" +seq_num + extension;
							String ssqlQuery = Functions.trim(textArea.getText().trim());


							/*****実行前にフォルダを作る***********/
							deleteResultDir(wd); //フォルダが存在していたら、フォルダ削除
							String resultDirTmp = createResultDir(wd);  //フォルダー作成している
							if (resultDirTmp.equals("")) {
								return; //フォルダーが作成されなかった場合
							}

							/** クエリファイルの作成 **/
							if (Common.createFile(ssqlQueryName2, ssqlQuery))
							{
								System.out.println("ファイルの作成に成功しました");
								// dispose();
							} else {
								System.out.println("ファイルの作成に失敗しました");
							}

							System.out.println("-----------------------------------------------");

							/* SSQL実行 */
							SSQL_exec.errLog = "";
							if (SSQL_exec.execSuperSQL(ssqlQueryName2,classPath, driver, db, null, null,resultDirTmp)) {
								result = "◯"; //SSQL実行成功
								System.out.println(result);
								////ブラウザ表示を行うためのパス
								//String uriString = "file:///" + resultDirTmp +"q"+ seq_num + ".html";
								//OpenBrowser(uriString);

								if(!Dialog.YesNoDialog("Resist OK?")){
									System.out.println("tourokusinai");
								}else{							
									//								DBに登録する部分(insert())
									if(insert(seq_num))
										ssql_regressiontest.Common.Dialog.NormalDialog("Succeed(queryAdd)");
									else
										ssql_regressiontest.Common.Dialog.NormalDialog("Failed(queryAdd)");	
									dispose();
								}

							} else {
								result = "×"; //SSQL実行失敗
								System.out.println(result);

								// SSQL実行が失敗した場合、クエリに誤りがあることを警告（SSQLの実行失敗ログを表示）して作成したテストファイルを削除
								String errMessage = "<HTML>SSQL Execution failed."
										+ "The query is incorrect.<br><br>"
										+ "<FONT COLOR='RED'>"
										+ SSQL_exec.errLog + "<br><br>"
										+ "<FONT COLOR='BLACK'>"
										+ "This TestCase create for error OK?</FONT></HTML>";
								//実行に失敗した時にエラーを登録するか、しないか…！
								if(!Dialog.YesNoDialog(errMessage)){ 
									//登録しない場合(作成していたファイルを削除する)
									File file = new File(ssqlQueryName2);
									if (file.exists()) {
										if (file.delete()) {
											System.out.println("ファイルを削除しました");
										} else {
											System.out.println("ファイルの削除に失敗しました");
										}
									} else {
										System.out.println("ファイルが見つかりません");
									}

								}else{ 

									//登録する場合
									//メディアを取得
									String output_Selected = null;
									int output_Selected_index = 0;
									for (int j = 0 ; j < 2; j++){
										if (radioButton_Media[j].isSelected()){
											output_Selected = radioButton_Media[j].getText();
											if (output_Selected.equals("HTML")) {
												output_Selected_index = 1;
											}else if(output_Selected.equals("Mobile_HTML5")) {
												output_Selected_index = 2;
											}else{
												output_Selected_index = 3;
											}
										}};

										String q_Title = queryTitle.getText();
										String fn = "q"+seq_num; //生成されるファイルネーム決めている
										String q = textArea.getText();
										String err = SSQL_exec.errLog.replace("'", "''").replace("<br>", "\n");

										//query_tagテーブルへの挿入
										for(int i1=0 ; i1<  tag_name_model_B.getSize() ; i1++){
											String insert_tagquery = "INSERT INTO querytag "
													+ "(q_id,t_id) VALUES"+ "('"
													+ seq_num+"','"
													+ tagMap.get(tag_name_model_B.getElementAt(i1))+"');";
											System.out.println(insert_tagquery);
											Database.insert(insert_tagquery);
										}
										//エラー文章のクエリインサート文
										String insert_sql_error = "INSERT INTO query "
												+ "(q_title, q_name, q_contents, q_description, "
												+ "q_author, output_id) VALUES "
												+ "("
												+ "'"+Database.getEscapedString(q_Title)+"',"
												+ "'"+Database.getEscapedString(fn)+".ssql"+"',"
												+ "'"+Database.getEscapedString(q)+"',"
												+ "'"+Database.getEscapedString(queryDescriptionArea.getText())+"',"
												+ "'"+Database.getEscapedString(queryAuthor.getText())+"',"
												+ "'"+output_Selected_index+"');" ;
										Database.insert(insert_sql_error);
										System.out.println(insert_sql_error);
										//エラー文章の解答インサート文
										String errorFileName = fn + "error.log";
										String insert_err_result = "INSERT INTO result "
												+ "(q_id, a_id, f_name, a_author, a_contents) VALUES "
												+ "("+seq_num+ "," + 1 +","
												+ "'"+Database.getEscapedString(errorFileName)+"' ,"
												+ "'"+Database.getEscapedString(queryAuthor.getText())+"',"
												+ "'"+Database.getEscapedString(err)+"');";
										Database.insert(insert_err_result);
										System.out.println(insert_err_result);

										Database.close();
										dispose();
								}
							}
							System.out.println("================================================");
							System.out.println("");
							addButton.setEnabled(true);
							addButton.setText("Add");
							TestCase_List.initTable();
							//							queryList.table.addColumn(queryList.column); //カラム追加
							TestCase_List.getQueryList();
							TestCase_List.table.setModel(TestCase_List.tableModel_list);
							//							JTreeTest.initTree();

							//Regression Test Runタブ左のツリーの更新
							Test_Run_and_Result.tree_Panel.removeAll();
							JPanel TreeTestPanel = new JTreeTest();
							TreeTestPanel.setPreferredSize(new Dimension(300,340));
							Test_Run_and_Result.tree_Panel.add(TreeTestPanel);
							Test_Run_and_Result.tree_Panel.setPreferredSize(new Dimension(300,340));

						}
					}
				}).start();
			}
		});

		//キャンセルボタンアクション
		cancelButton.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});

		//終了ボタンアクション
		exitButton.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		//タグ作成ボタンアクション
		createTagButton.addActionListener(new AbstractAction() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				Tag_Add.display();
			}
		});
	}



	/*****
	 * マウスアクション
	 * *******/

	//マウスアクションA
	private class MouseActionA implements MouseListener{
		@Override
		public void mouseClicked(MouseEvent e){
			//ダブルクリックされた時の処理
			if(e.getClickCount() == 2){ 
				//現在選択されている値
				String tmp = jList_tag_name_A.getSelectedValue(); 
				//Bのサイズ
				int size = tag_name_model_B.getSize();
				//フラグ
				boolean flag = false; 
				//Bのサイズの分だけ、for文を回して、同じものがあればfalseにする。
				for(int i=0 ; i<size ; i++){ 
					if(tmp.equals(tag_name_model_B.get(i))){
						flag = true;
					}
				}
				//選択済みでない場合、追加する
				if (!flag) {
					System.out.println("tmpは"+tmp);
					tag_name_model_B.addElement(tmp);
					jList_tag_name_B.setModel(tag_name_model_B);
					System.out.println(tagMap.get(tmp));
				}
			}
		}
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
		}
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
		}
	}

	//マウスアクションB
	private class MouseActionB implements MouseListener{
		@Override
		public void mouseClicked(MouseEvent e){
			if((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0){
				Point p = e.getPoint();
				int index = jList_tag_name_B.locationToIndex(p);
				jList_tag_name_B.setSelectedIndex(index);
			}
		}
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
		}
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
		}
	}

	//削除のアクション(右クリックの時)
	public class DeleteAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			JMenuItem mi = (JMenuItem) e.getSource();
			if (mi == deleteMenuItem) {
				System.out.println(tagMap.get(jList_tag_name_B.getSelectedValue()));
				//BのJListから削除する
				tag_name_model_B.remove(jList_tag_name_B.getSelectedIndex()); 
			}
		}
	}


	//ファイル作成
	static boolean createFile(String filename, String s) {
		try {
			// s = s.replaceAll(GlobalEnv.OS_LS + "$", ""); // 末尾の改行コードを削除
			PrintWriter pw;
			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(filename), "UTF-8")));
			pw.println(s);
			pw.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}


	/*****
	 * タグ関係
	 * *******/
	//tag_name_model_Aテーブルの初期化
	public static void initTable() {
		try { tag_name_model_A.clear(); } catch (Exception e) { }
	};

	//tag_name_model_Aにリストの中身を入れている
	public static void 	insert_Model(){
		for(int i=0 ; i<tagNameList.size() ; i++){
			tag_name_model_A.addElement(tagNameList.get(i));
		}
	};

	//タグ一覧取得
	public static void list_get(){
		Database.connect();
		String sql_tag_list = "SELECT * FROM tag;";
		ResultSet rs_tag_list = Database.select(sql_tag_list);
		tagMap = new HashMap();
		tagNameList = new ArrayList<>();
		// テーブル照会結果を出力
		try {
			while (rs_tag_list.next()) {
				String tag_name  = rs_tag_list.getString("t_name");
				Integer tag_id  = rs_tag_list.getInt("t_id");
				tagNameList.add(tag_name);
				tagMap.put(tag_name, tag_id);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Database.close();
	}


	/*****
	 * クエリ・解答をインサート
	 * *******/

	//解答とクエリをインサート
	public static boolean insert(int num) {
		String q_Title = queryTitle.getText();
		String fn = "q"+num;
		String q = textArea.getText();

		//		//選択されているタグを取得している
		//		int i;
		//		for(i=0 ; i<  tag_name_model_B.getSize() ; i++){
		//			Integer insert_tagquery = tagMap.get(tag_name_model_B.getElementAt(i));
		//		}

		//メディア取得
		String output_Selected = null;
		int output_Selected_index = 0;
		for (int j = 0 ; j < 2; j++){
			if (radioButton_Media[j].isSelected()){
				output_Selected = radioButton_Media[j].getText();
				if (output_Selected.equals("HTML")) {
					output_Selected_index = 1;
				}else{
					output_Selected_index = 2;
				}
			}};

			//queryテーブルへインサート（挿入）
			String insert_sql = "INSERT INTO query (q_title, q_name, q_contents, q_description, q_author, output_id) VALUES "
					+ "("
					+ "'"+Database.getEscapedString(q_Title)+"',"
					+ "'"+Database.getEscapedString(fn)+".ssql"+"',"
					+ "'"+Database.getEscapedString(q)+"',"
					+ "'"+Database.getEscapedString(queryDescriptionArea.getText())+"',"
					+ "'"+Database.getEscapedString(queryAuthor.getText())+"',"
					+ "'"+output_Selected_index+"');" ;
			System.out.println(insert_sql);
			Database.insert(insert_sql);

			//tagの次のidの番号を取得している
//			String tag_count_sql = "SELECT last_value+1 as tag_num FROM tag_t_id_seq;";
			String tag_count_sql = "SELECT seq+1 as tag_num FROM SQLITE_SEQUENCE WHERE name='tag';";
			ResultSet tag_count_rs = Database.select(tag_count_sql);
			int tag_num = 0;
			try {
				while (tag_count_rs.next()) {
					tag_num = tag_count_rs.getInt("tag_num");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if(tag_num < 1){
				//TODO エラー処理の追加
			}

			/***query_tagテーブルへの挿入***/
			System.out.println("tag_name_model_B.getSize() = "+tag_name_model_B.getSize());
			for(int i1=0 ; i1<  tag_name_model_B.getSize() ; i1++){
				String insert_tagquery = "INSERT INTO querytag (q_id,t_id) VALUES"+ "('"+num+"','"+tagMap.get(tag_name_model_B.getElementAt(i1))+"');";
				System.out.println(insert_tagquery);
				Database.insert(insert_tagquery);
			}


			/******JS/CSS*****そのフォルダーの中の名前を取ってきている****/
			// クエリの追加で生成する一時ファイルのディレクトリ（.html, jscss等）
			String tmpFileDir = Functions.getWorkingDir() + GlobalEnv.OS_FS + "tmp";
			File dir1 = new File(tmpFileDir);
			//ディレクトリを再帰的に読む
			time = new Timestamp(System.currentTimeMillis());
			readFolder(dir1, tmpFileDir, num);
			Database.close();
			
			System.out.println("end");
			
			return true;
	}

	/**
	 * ディレクトリを再帰的に読む
	 * 
	 * @param folderPath
	 */
	public static void readFolder(File dir, String tmpFileDirName, int num) {
		File[] files = dir.listFiles();
		if (files == null)
			return;
		for (File file : files) {
			if (!file.exists())
				continue;
			else if (file.isDirectory()){
				readFolder(file, tmpFileDirName, num);
			}
			else if (file.isFile()){
				execute(file, tmpFileDirName, num);
			}
		}
	}

	/**
	 * ファイルの処理
	 * 
	 * @param filePath
	 */
	public static void execute(File file, String tmpFileDirName, int num) {
		String tmpFilePath = file.toString();  //ファイルのフルパス
		String result_contents1 = Common.readFile(tmpFilePath).replace("'", "''");

		//		if (tmpFileDirName.length() >= tmpFilePath.length()) {
		//			return false;
		//		}
		String tmpFileName = tmpFilePath.substring(tmpFileDirName.length() + 1);
		System.out.println("挿入するファイルの名前: " + tmpFileName);
		//実行結果をresultテーブルへ挿入する
		String insert_result = 
				"INSERT INTO result (q_id, a_id, f_name ,a_author, a_day, a_contents) VALUES "
						+ "("+num+ "," + 1 +","
						+ "'"+Database.getEscapedString(tmpFileName)+"',"
						+ "'"+Database.getEscapedString(queryAuthor.getText())+"', "
						+ "'"+time+"',"
						+ "'"+Database.getEscapedString(result_contents1)+"');";
		System.out.println("クエリ: " + insert_result);
		Database.insert(insert_result);
	}

	//ブラウザ表示
	private static void OpenBrowser(String uriString) {
		Desktop desktop = Desktop.getDesktop();
		//			String uriString = "http://www.google.co.jp";
		try {
			URI uri = new URI(uriString);
			desktop.browse(uri);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	};


	//解答を追加する前にディレクトリ削除
	private static void deleteResultDir(String wd){
		String resultDir = wd+ GlobalEnv.OS_FS +"tmp";
		File newfile = new File(resultDir);
		Functions.delete(newfile);
	};

	//解答を入れておくためのディレクトリ作成
	private static String createResultDir(String wd) {
		String resultDir = wd+ GlobalEnv.OS_FS +"tmp"+ GlobalEnv.OS_FS;
		if(new File(resultDir).exists()){
			return resultDir;
		}
		File newfile = new File(resultDir);
		if (newfile.mkdir()){
			System.out.println("ディレクトリの作成に成功しました");
			return resultDir;
		}else{
			System.out.println("ディレクトリの作成に失敗しました");
			return ""; 
		}
	}

}


class MyWindowAdapter extends WindowAdapter {
	public void windowClosing(WindowEvent we) {
		// System.exit(0);
	}
}

