package ssql_regressiontest.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.Position.Bias;

import ssql_regressiontest.Class.QueryDetail;
import ssql_regressiontest.Class.QueryResults;
import ssql_regressiontest.Class.TagQuery;
import ssql_regressiontest.Common.Database;
import ssql_regressiontest.ExecRegressionTest.DB;
import ssql_regressiontest.ExecRegressionTest.Exec;
import ssql_regressiontest.GUI.TestCase_Add.DeleteAction;
import ssql_regressiontest.GUI.TestCase_List.ButtonEditor;
import ssql_regressiontest.GUI.TestCase_List.ButtonRenderer;
import ssql_regressiontest.GUI.TestCase_List.MyTableModel;

public class Tag_List extends JPanel {

	private static Tag_List frame = null;
	//	public static JList<String> jList_tag_name_A, jList_tag_name_B;
	public static DefaultListModel<String> tag_name_model_A, tag_name_model_B;
	private static HashMap<String, Integer> tagMap;
	public static ArrayList<String> tagNameList;
	private static JPanel  mainPanel, query_Add_Delete_Panel, query_List_A_Panel;
	public static JPanel query_List_Panel;
	private static JButton[] bt;
	private static int i;
	private final static String[] columnName_list = {"No", "Testcase_id", "Name", "Tag","Output", "Details"};
	private static JButton tag_addButton, tag_deleteButton;
	private static JTextField tag_name_Field;
	private static JTable table1;
	public static MyTableModel tag_list_Model;
	private static JScrollPane sp1;
	private static JScrollPane sp2;
	private final static int rowNum = 0;
	static TableColumn column;
	//	private static Integer current_query_id;
	private JButton detailButton = new JButton();
	private static JButton b;
	private static String tag_name;
	public static int Tag_ADD_NUM = 0;


	/**
	 * Launch the application.
	 */
	public static void display() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					initTable();
					if (frame != null)
						return;
					frame = new Tag_List();
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
	public Tag_List() {
		//		frame.add(sp2);

		list_get(); // タグ一覧を取得
		mainPanel = new JPanel();
		query_List_A_Panel = new JPanel();
		query_Add_Delete_Panel = new JPanel();
		tag_addButton = new JButton("Add");
		tag_deleteButton = new JButton("Delete");
		tag_deleteButton.setEnabled(false);
		query_Add_Delete_Panel.add(tag_addButton);
		query_Add_Delete_Panel.add(tag_deleteButton);

		tag_name_Field = new JTextField(10);

		// JList関係
		tag_name_model_A = new DefaultListModel<>();

		//テーブルモデル
		tag_list_Model = new MyTableModel(columnName_list, rowNum){
			@Override public Class<?> getColumnClass(int column) {
				return getValueAt(0, column).getClass();
			}};			
			table1 = new JTable(tag_list_Model){
				public Component prepareRenderer(TableCellRenderer tcr, int row, int column) {
					Component c = super.prepareRenderer(tcr, row, column);
					if (c instanceof JComponent) {
						JComponent l = (JComponent) c;
						Object o = getValueAt(row, column);
						String str = o.toString();
						if(column == 4){
							// ポップアップ表示してるとこ
							l.setToolTipText(str);
						}
					}
					return c;
				}
			};
			DefaultTableCellRenderer rend;
			rend = new DefaultTableCellRenderer();
			rend.setAlignmentX(CENTER_ALIGNMENT);
			table1.getColumnModel().getColumn(0).setCellRenderer(rend);
			table1.getColumnModel().getColumn(1).setCellRenderer(rend);
			table1.getColumnModel().getColumn(2).setCellRenderer(rend);
			table1.getColumnModel().getColumn(4).setCellRenderer(rend);
			//			table1.getColumn("Title").setCellRenderer.setAlignmentX(CENTER_ALIGNMENT);
			table1.getTableHeader().setReorderingAllowed(false);	

			//確認ボタン
			table1.getColumn("Details").setCellRenderer(new ButtonRenderer());
			table1.getColumn("Details").setCellEditor(new ButtonEditor(new JCheckBox()));
			table1.getTableHeader().setReorderingAllowed(false);		//ドラッグ&ドロップによる列の入れ替えを許可しない

			sp1 = new JScrollPane(table1);
			sp1.setPreferredSize(new Dimension(1100, 150));

			//カラム幅設定
			table1.getColumn("No").setPreferredWidth(20);
			table1.getColumn("Testcase_id").setPreferredWidth(0);
			//		table1.getColumn("Check").setPreferredWidth(50);
			table1.getColumn("Name").setPreferredWidth(200);
			table1.getColumn("Tag").setPreferredWidth(300);
			table1.getColumn("Output").setPreferredWidth(30);
			table1.getColumn("Details").setPreferredWidth(50);


			insert_Model();
			tag_Button();



			/**************
			 * タグ追加
			 ***************/
			//タグ追加ボタンが押された時の処理
			tag_addButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Tag_Add.display();	
				}
			});


			/**************
			 * タグ削除
			 ***************/
			//タグ削除(Delete)ボタン
			tag_deleteButton.addActionListener(new AbstractAction() {
				private static final long serialVersionUID = 1L;
				@Override
				public void actionPerformed(ActionEvent e) {
					tag_deleteButton.setEnabled(false);
					tag_deleteButton.setText("Delete...");
					Database.connect();
					boolean option = ssql_regressiontest.Common.Dialog.YesNoDialog("Are you sure you want to delete this Tag?");
					if (option == true){
						//tagidを探してきている！
						String select_sql = "select t_id from tag where t_name = '"+tag_name+"';";
						ResultSet tag_id_rs = Database.select(select_sql);
						int tag_id_num = 0;
						try {
							while (tag_id_rs.next()) {
								tag_id_num = tag_id_rs.getInt("t_id");
							}
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						//タグテーブルから削除
						String delete_tag = "delete from tag where t_id = "+tag_id_num+";";
						Database.delete(delete_tag);
						//クエリタグテーブルから削除
						String delete_query_tag = "delete from querytag where t_id = "+tag_id_num+";";
						Database.delete(delete_query_tag);

						Tag_List.init_tag_name(); //初期化OK
						Tag_List.list_get(); //タグ一覧取得してきてOK
						Tag_List.insert_Model();//モデルに入れるOK
						Tag_List.tag_Button();//表示

						tag_name_Field.setText("");
						Database.close();

					}else if (option == false){
						System.out.println("This Tag has not been deleted");
						tag_deleteButton.setEnabled(true);
					}
					//ここでクエリが削除された後に再び表示している
					//					initTable();  //初期化
					//					table.addColumn(column); //カラム追加
					//					getQueryList(); //クエリリスト取ってくる(テーブルモデルに値を入れている)
					//					table.setModel(tableModel_list); //テーブルリストモデルを表示している
					tag_deleteButton.setText("Delete");
					
					//Regression Test Runタブ左のツリーの更新
					Test_Run_and_Result.tree_Panel.removeAll();
					JPanel TreeTestPanel = new JTreeTest();
					TreeTestPanel.setPreferredSize(new Dimension(300,340));
					Test_Run_and_Result.tree_Panel.add(TreeTestPanel);
					Test_Run_and_Result.tree_Panel.setPreferredSize(new Dimension(300,340));
				}
			});


			//Details(詳細)ボタンの処理アクション！！
			detailButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					int row = table1.getEditingRow();
					final int QUERYID_COLUMN = 1;
					final int QUERYTAG_COLUMN = 4;

					int queryID = Integer.parseInt(table1.getValueAt(row, QUERYID_COLUMN).toString());
					String queryTag = table1.getValueAt(row, QUERYTAG_COLUMN).toString();
					TestCase_List_Detail.display(queryID, queryTag);				}
			});
			mainPanel.add(query_Add_Delete_Panel);
			add(mainPanel);
	}


	// タグ一覧取得
	public static void list_get() {
		Database.connect();
		String sql_tag_list = "SELECT * FROM tag;";
		ResultSet rs_tag_list = Database.select(sql_tag_list);
		tagMap = new HashMap();
		tagNameList = new ArrayList<>();
		// テーブル照会結果を出力
		try {
			while (rs_tag_list.next()) {
				String tag_name = rs_tag_list.getString("t_name");
				Integer tag_id = rs_tag_list.getInt("t_id");
				tagNameList.add(tag_name);
				tagMap.put(tag_name, tag_id);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Database.close();
	}


	//タグに関連したクエリを取ってくる
	public static ArrayList<String> getQueryList_Tag(Integer query_id) {
		ArrayList<String> tag_array = new ArrayList<String>();
		Database.connect();
		// 引数のquery_idのクエリが持つタグをとってくるSQLを書く
		String sql_tag_query = "SELECT distinct t_name, id, q_title, o_name FROM result r1, query q, tag t, output o , querytag qt "
				+ "WHERE NOT EXISTS (  SELECT 1  FROM result r2  WHERE r1.q_id = r2.q_id AND r1.a_day < r2.a_day ) "
				+ "AND r1.q_id = q.id AND q.output_id = o.o_id AND q.id = qt.q_id AND t.t_id = qt.t_id AND q.id = "
				+ query_id 
				+ "ORDER BY q.id ";
		System.out.println("asacha: " + sql_tag_query);
		ResultSet rs_tag = Database.select(sql_tag_query);
		//		TagQuery tagQuery = null;
		// テーブル照会結果を出力
		try {
			while (rs_tag.next()) {
				String tag_name = rs_tag.getString("t_name");
				tag_array.add(tag_name);
			}
			//			//クエリ削除の為に(隠してある)Testcase_idを取得、そのidのテストケースをDBから削除する
			//			column = table1.getColumn("Testcase_id");
			//			table1.removeColumn(column);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Database.close();
		return tag_array;
	}



	//テーブルの初期化
	private static void initTable() {
		try { tag_list_Model.setRowCount(0); } catch (Exception e) { }
		tableRowCount = 0;
	}


	//テーブルへデータを表示
	private static int tableRowCount = 0;
	public static void insertTableData(Integer query_id, String query_title, String query_tag,String query_output) {
		tag_list_Model.insertRow(tableRowCount++, 
				new Object[]{tableRowCount, query_id, query_title, query_tag, query_output,"Details"});
	}



	// tag_name_model_Aにリストの中身を入れている
	public static void insert_Model() {
		for (int i = 0; i < tagNameList.size(); i++) {
			tag_name_model_A.addElement(tagNameList.get(i));
			System.out.println(tagNameList.get(i));
		}
	};

	//JListDefaultModelの初期化
	static void init_tag_name() {
		try { 
			tag_name_model_A.clear(); 
			tagNameList.clear();
		} catch (Exception e) { }
	}


	/****
	 * タグボタン
	 * *****/

	public static void tag_Button(){
		Tag_ADD_NUM++;
		if(Tag_ADD_NUM == 1){

			query_List_Panel = new JPanel();
			sp2 = new JScrollPane(query_List_Panel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			sp2.setPreferredSize(new Dimension(1100, 300));

		}else{
			query_List_Panel.removeAll();
			//			query_Add_Detail_Panel.removeAll();
		}
		//		query_List_Panel.add(sp2);

		//ボタンを表示して、ボタンのアクションをつけている	
		int size = tag_name_model_A.getSize(); 
		for (i = 0; i < size; i++) {

			bt = new JButton[size];
			bt[i] = new JButton(tag_name_model_A.get(i));
			bt[i].setContentAreaFilled(false);
			bt[i].setBorderPainted(false);
			bt[i].setForeground(Color.BLACK);

			GridLayout l = new GridLayout(0,4,10,10);
			//			l.setColumns(5);
			//			l.setRows(0);
			query_List_Panel.setLayout(l);

			query_List_Panel.add(bt[i]);

			mainPanel.add(sp2);
			mainPanel.add(tag_name_Field);
			mainPanel.add(sp1);
			mainPanel.add(query_Add_Delete_Panel);
			mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

			//ボタンが押された時の処理
			bt[i].addActionListener(new ActionListener() {
				private ArrayList<String> tag_array;
				public void actionPerformed(ActionEvent e) {

					tag_deleteButton.setEnabled(true); //削除ボタンをtrueに設定する
					initTable(); //テーブルの初期化

					b = (JButton) e.getSource();
					tag_name = b.getText();

					tag_name_Field.setText(tag_name);//タグの名前を表示させている
					tag_name_Field.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 20));
					Database.connect();
					String sql_getTestCase = "SELECT distinct(id), q_title, q_name, t_name, o_name "
							+ "FROM result r1, query q, tag t, output o , querytag qt "
							+ "WHERE NOT EXISTS (  SELECT 1  FROM result r2  "
							+ "WHERE r1.q_id = r2.q_id AND r1.a_day < r2.a_day ) "
							+ "AND r1.q_id = q.id AND q.output_id = o.o_id AND q.id = qt.q_id AND t.t_id = qt.t_id "
							+ "AND t.t_name = '"
							+ tag_name
							+ "' ORDER BY q.id ;";
					System.out.println(sql_getTestCase);
					ResultSet rs = Database.select(sql_getTestCase);
					try {
						while (rs.next()) {
							//タグ以外のテーブルに表示する情報をとってくる
							Integer query_id = rs.getInt("id");
							String query_title  = rs.getString("q_title");
							String query_name  = rs.getString("q_name");
							String query_output = rs.getString("o_name");
							// これの返り値はArrayとかにする -> [padding, width, link] 
							tag_array = getQueryList_Tag(query_id);
							String tag = "";
							// tag = "padding, width, link"
							for(int i=0; i < tag_array.size(); i++){
								if(i==0){
									tag = tag_array.get(i);
								} else {
									tag+=", " + tag_array.get(i);
								}
							}
							// テーブルにインサート
							insertTableData(query_id, query_title,tag,query_output);
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Database.close();
				}

			});



			/**************
			 * ボタンアクション
			 * **************/

			//タグ名のボタンの上のアクション
			bt[i].addMouseListener(new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
				}
				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
				}
				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					JButton b = (JButton) e.getSource();
					b.setForeground(Color.BLACK);
				}
				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					JButton b = (JButton) e.getSource();
					b.setForeground(Color.RED);
					//					b.setToolTipText(getName());
					//					b.setToolTipText("AAA");
				}
				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
				}
			});	
		}
	}


	//テーブルへボタンを表示させるためのクラス1
	class ButtonRenderer extends JButton implements TableCellRenderer {
		private static final long serialVersionUID = 1L;
		public ButtonRenderer() {
			setOpaque(true);
		}
		public Component getTableCellRendererComponent(JTable table,
				Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			//			System.out.println(value);
			setText((value == null) ? "" : value.toString());
			if (value==null) {
				//				this.setEnabled(false);
				return null;
			}
			return this;
		}
	}
	//テーブルへボタンを表示させるためのクラス2
	class ButtonEditor extends DefaultCellEditor {
		private static final long serialVersionUID = 1L;
		private String label;
		public ButtonEditor(JCheckBox checkBox) {
			super(checkBox);
		}
		public Component getTableCellEditorComponent(JTable table,
				Object value,boolean isSelected, int row, int column) {
			label = (value == null) ? "" : value.toString();
			//			System.out.println("getTableCellEditorComponent");
			if(value==null){
				return null;
			}else{
				detailButton.setText(label);
				return detailButton;
			}
		}
		public Object getCellEditorValue() {
			//			System.out.println("getCellEditorValue  label = "+label);
			return new String(label);
		}
	}

	//チェックボックスをDefaultTableModelに表示している
	public class MyTableModel extends DefaultTableModel{
		MyTableModel(String[] columnNames, int rowNum){
			super(columnNames, rowNum);
		}

		public Class getColumnClass(int col){
			return getValueAt(0, col).getClass();
		}

	}

}
