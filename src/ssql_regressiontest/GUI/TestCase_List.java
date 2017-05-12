package ssql_regressiontest.GUI;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import ssql_regressiontest.Class.QueryDetail;
import ssql_regressiontest.Common.Database;
import ssql_regressiontest.Common.Functions;
import ssql_regressiontest.ExecRegressionTest.Exec;


public class TestCase_List extends JPanel {
	private static ArrayList<QueryDetail> query_detail_list;
	private static final long serialVersionUID = 1L;
	private JPanel mainPanel, buttonPanel;
	private JButton addButton, deleteButton;
	private JScrollPane sp;
	public static JTable table;
	public static MyTableModel tableModel_list;
	private final static String[] columnName_list = {"No", "Testcase_id","Check", "Name", "Tag","Media", "Details"};
	private final static int rowNum = 0;
	private JButton Detail_Button = new JButton();
	public static TestCase_List frame = null;
	static TableColumn column;



	/**
	 * Launch the application.
	 */
	public static void display() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					initTable();
					//					initResultArray();
					if(frame != null)	return;
					frame = new TestCase_List();
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
	public TestCase_List() {
//		setBounds(100, 100, 450, 500);
		mainPanel = new JPanel();

		//テーブルモデル
		tableModel_list = new MyTableModel(columnName_list, rowNum){
			@Override public Class<?> getColumnClass(int column) {
				return getValueAt(0, column).getClass();
			}};			
			table = new JTable(tableModel_list){
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
			//			table.getColumnModel().getColumn(0).setCellRenderer(rend);
			//			table.getColumnModel().getColumn(1).setCellRenderer(rend);
			//			table.getColumnModel().getColumn(2).setCellRenderer(rend);
			//		table.getColumnModel().getColumn(3).setCellRenderer(rend);
			table.getColumnModel().getColumn(4).setCellRenderer(rend);
			//		//		table.getColumn("Title").setCellRenderer.setAlignmentX(CENTER_ALIGNMENT);
			//			table.getTableHeader().setReorderingAllowed(false);	




			// Detailボタン
			table.getColumn("Details").setCellRenderer(new ButtonRenderer());
			table.getColumn("Details").setCellEditor(new ButtonEditor(new JCheckBox()));
			table.getTableHeader().setReorderingAllowed(false);		//ドラッグ&ドロップによる列の入れ替えを許可しない
			sp = new JScrollPane(table);
			sp.setPreferredSize(new Dimension(800, 400));

			//カラム幅設定
			table.getColumn("No").setPreferredWidth(40);
			table.getColumn("Testcase_id").setPreferredWidth(80);
			table.getColumn("Check").setPreferredWidth(60);
			table.getColumn("Name").setPreferredWidth(300);
			table.getColumn("Tag").setPreferredWidth(300);
			table.getColumn("Details").setPreferredWidth(80);

			//ボタン
			addButton = new JButton("Add");
			deleteButton = new JButton("Delete");
			buttonPanel = new JPanel();
			buttonPanel.add(addButton);
			buttonPanel.add(deleteButton);

			//レイアウト！ボックスレイアウト
			mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

			mainPanel.add(sp);
			mainPanel.add(buttonPanel);

			//DBに接続して必要な情報を取ってくる
			getQueryList();

			add(mainPanel);

			//Details(詳細)ボタンの処理アクション！！
			Detail_Button.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					int row = table.getEditingRow();
					final int QUERYID_COLUMN = 1;
					final int QUERYTAG_COLUMN = 4;
					
//					String query_author = query_detail_list.get(row).getQueryAuthor();
//					String query_day = query_detail_list.get(row).getQueryDay();
//					String query_Title = query_detail_list.get(row).getQueryTitle();
//					String query_name = query_detail_list.get(row).getQueryName();
//					String query_contents = query_detail_list.get(row).getQueryContents();
//					String query_description = query_detail_list.get(row).getQueryDescription();
//					String result_contets = query_detail_list.get(row).getResultContents();
//					String result_author = query_detail_list.get(row).getResultAuthor();
//					String result_day = query_detail_list.get(row).getResultDay();
//					String query_output = query_detail_list.get(row).getQueryOutput();
//					String query_tag = query_detail_list.get(row).getQueryTag();

//					queryList_ditail.display(query_Title, query_name, query_contents, query_description, query_author,
//							query_day, result_contets, result_author, result_day, query_tag, query_output);
					int queryID = Integer.parseInt(table.getValueAt(row, QUERYID_COLUMN).toString());
					String queryTag = table.getValueAt(row, QUERYTAG_COLUMN).toString();
					TestCase_List_Detail.display(queryID, queryTag);
				}
			});

			//追加(Add)ボタン
			addButton.addActionListener(new AbstractAction() {
				private static final long serialVersionUID = 1L;
				@Override
				public void actionPerformed(ActionEvent e) {
					//				Database.connect();
					//				String sql_tag = "select * from tag;";
					//				ResultSet rs_tag = Database.select(sql_tag);
					//				Database.close();
					TestCase_Add.display();
					//				initTable();
					//				table.addColumn(column); //カラム追加
					//				getQueryList();
					//				table.setModel(tableModel_list);
					//ここでクエリが削除された後に再び表示している

					//				if(queryAdd){ //クエリ追加が行われた場合…？
					//					//ここでクエリが追加された後に再び表示している
					//					table.addColumn(column); //カラム追加
					//					getQueryList(); //クエリリスト取ってくる(テーブルモデルに値を入れている)
					//					table.setModel(tableModel_list); //テーブルリストモデルを表示している
					//				}

				}

			});


			//削除(Delete)ボタン
			deleteButton.addActionListener(new AbstractAction() {
				private static final long serialVersionUID = 1L;
				@Override
				public void actionPerformed(ActionEvent e) {
					deleteButton.setEnabled(false);
					deleteButton.setText("Delete...");

					Database.connect();

					boolean option = ssql_regressiontest.Common.Dialog.YesNoDialog("Are you sure you want to delete this Query?");

					if (option == true){
						for(int i=0; i < tableModel_list.getRowCount(); i++){
							if((boolean) tableModel_list.getValueAt(i, 2)){
								String sql1 = "delete from query q where q.id = "+tableModel_list.getValueAt(i, 1)+";";
								Database.delete(sql1);
								String sql2 = "delete from result where q_id = "+tableModel_list.getValueAt(i, 1)+";";
								Database.delete(sql2);
								String sql3 = "delete from querytag where q_id = "+tableModel_list.getValueAt(i, 1)+";";
								Database.delete(sql3);
							}
						}
						ssql_regressiontest.Common.Dialog.NormalDialog("The Query has been deleted");
					}else if (option == false){
						System.out.println("The Query has not been deleted");
					}
					Database.close();
					//ここでクエリが削除された後に再び表示している
					initTable();  //初期化
//					table.addColumn(column); //カラム追加
					getQueryList(); //クエリリスト取ってくる(テーブルモデルに値を入れている)
					table.setModel(tableModel_list); //テーブルリストモデルを表示している
					deleteButton.setEnabled(true);
					deleteButton.setText("Delete");
					
					
					//Regression Test Runタブ左のツリーの更新
					Test_Run_and_Result.tree_Panel.removeAll();
					JPanel TreeTestPanel = new JTreeTest();
					TreeTestPanel.setPreferredSize(new Dimension(300,340));
					Test_Run_and_Result.tree_Panel.add(TreeTestPanel);
					Test_Run_and_Result.tree_Panel.setPreferredSize(new Dimension(300,340));
					
				}
			});
	}


	//テーブルの初期化
	private static void initTable2() {
		try { tableModel_list.setRowCount(0); } catch (Exception e) { }
		tableRowCount = 0;
	}

	//クエリ一覧を取ってくる
	public static void getQueryList() {
		int query_id, current_query_id = 0;
		String query_title, current_query_title = "";
		String query_tag = "", current_query_tag = "";
		String query_output, current_query_output = "";

		Database.connect();

		String sql = "SELECT distinct(id), q_title, q_name, t_name, o_name "
				+ "FROM result r1, query q, tag t, output o , querytag qt "
				+ "WHERE NOT EXISTS (  SELECT 1  FROM result r2  "
				+ "WHERE r1.q_id = r2.q_id AND r1.a_day < r2.a_day ) "
				+ "AND r1.q_id = q.id AND q.output_id = o.o_id AND q.id = qt.q_id AND t.t_id = qt.t_id "
				+ "ORDER BY q.id ";
//		String sql = "SELECT id, q_title, q_name, q_contents, q_description, q_author, q_day, a_contents ,a_author, a_day, t_name, o_name FROM result r1, query q, tag t, output o , querytag qt "
//				+ "WHERE NOT EXISTS (  SELECT 1  FROM result r2  WHERE r1.q_id = r2.q_id AND r1.a_day < r2.a_day ) "
//				+ "AND r1.q_id = q.id AND q.output_id = o.o_id AND q.id = qt.q_id AND t.t_id = qt.t_id "
//				+ "ORDER BY q.id ";

		ResultSet rs = Database.select(sql);
		query_detail_list = new ArrayList<>();
		// テーブル照会結果を出力
		try {

			while (rs.next()) {
				query_id  = rs.getInt("id");
				query_title  = rs.getString("q_title");
				query_tag = rs.getString("t_name");
				query_output = rs.getString("o_name");
				// 初回
				if(current_query_id == 0){
					current_query_id = query_id;
					current_query_title = query_title;
					current_query_tag = query_tag;
					current_query_output = query_output;

					if(rs.isLast()){
//						queryDetail = new QueryDetail(current_query_id, current_query_title, current_query_name, current_query_contents, current_query_description, current_query_author, current_query_day, current_result_contents, current_result_author, current_result_day,current_query_tag,current_query_output);
//						query_detail_list.add(queryDetail);
						insertTableData(current_query_id, current_query_title, current_query_tag, current_query_output);
						return;
					}
				} else {
					if(current_query_id == query_id){
						current_query_tag += ", " + query_tag;
						if(rs.isLast()){
//							queryDetail = new QueryDetail(current_query_id, current_query_title, current_query_name, current_query_contents, current_query_description, current_query_author, current_query_day, current_result_contents, current_result_author, current_result_day,current_query_tag,current_query_output);
//							query_detail_list.add(queryDetail);
							insertTableData(current_query_id, current_query_title, current_query_tag, current_query_output);
							return;
						}
					} else {
//						queryDetail = new QueryDetail(current_query_id, current_query_title, current_query_name, current_query_contents, current_query_description, current_query_author, current_query_day, current_result_contents, current_result_author, current_result_day,current_query_tag,current_query_output);
//						query_detail_list.add(queryDetail);
						insertTableData(current_query_id, current_query_title, current_query_tag, current_query_output);
						// 現在のデータの更新
						current_query_id = query_id;
						current_query_title = query_title;
						current_query_tag = query_tag;
						current_query_output = query_output;
					}


					if(rs.isLast()){
//						queryDetail = new QueryDetail(current_query_id, current_query_title, current_query_name, current_query_contents, current_query_description, current_query_author, current_query_day, current_result_contents, current_result_author, current_result_day,current_query_tag,current_query_output);
//						query_detail_list.add(queryDetail);
						insertTableData(current_query_id, current_query_title, current_query_tag, current_query_output);
						return;
					}
				}
			}
			//クエリ削除の為に(隠してある)Testcase_idを取得、そのidのテストケースをDBから削除する
//			column = table.getColumn("Testcase_id");
//			table.removeColumn(column);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Database.close();
	}

	//テーブルへデータを表示
	private static int tableRowCount = 0;
	public static void insertTableData(Integer query_id, String query_title, String query_tag, String query_output) {
		tableModel_list.insertRow(tableRowCount++, 
				new Object[]{tableRowCount, query_id, new Boolean(false), query_title, query_tag, query_output,"Details"});
	}


	//テーブルの初期化
	public static void initTable() {
		try { tableModel_list.setRowCount(0); } catch (Exception e) { }
		tableRowCount = 0;
	}


	//	//ArrayListに格納されている結果の初期化
	//	private static void initResultArray() {
	//	}



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
				Detail_Button.setText(label);
				return Detail_Button;
			}
		}
		public Object getCellEditorValue() {
			//			System.out.println("getCellEditorValue  label = "+label);
			return new String(label);
		}
	}

	//×ボタン
	class MyWindowAdapter extends WindowAdapter {
		public void windowClosing(WindowEvent we) {
			frame = null;
			// System.exit(0);
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
