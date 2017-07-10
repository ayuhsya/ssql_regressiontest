package sstest.GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sstest.Class.QueryResults;
import sstest.Class.RegressionTestResult;
import sstest.Class.SsqlResult;
import sstest.Class.TreeData;
import sstest.Common.Database;
import sstest.Common.Dialog;
import sstest.Common.GlobalEnv;
import sstest.ExecRegressionTest.DB;
import sstest.ExecRegressionTest.Exec;

public class Test_Run_and_Result extends JPanel {
	public static ArrayList<RegressionTestResult> result_all = new ArrayList<>();
	public static ArrayList<RegressionTestResult> result_success = new ArrayList<>();
	public static ArrayList<RegressionTestResult> result_failure = new ArrayList<>();
	public static ArrayList<RegressionTestResult> result_sselexec_failure = new ArrayList<>();
	public static ArrayList<RegressionTestResult> result_ssqlexec_success = new ArrayList<>();
	private static ArrayList<QueryResults> query_result;
	private static ArrayList<TreeData> tree_data_list;
	static TableColumn column;
	private static CheckBoxNode cnode;
	private static DefaultMutableTreeNode node;
	private static final long serialVersionUID = 1L;
	private JPanel mainPanel, buttonPanel, resultTable_Panel;
	static JPanel tree_Panel;
	private JPanel TreeTestPanel;
	private JPanel leftsidePanel;
	private JPanel Information_Panel;
	private JPanel passPanel;
	private JPanel FailedPanel;
	private JPanel SSQLexecSuccessPanel;
	private JPanel SSQLexecFailedPanel;
	private JPanel queryNumberPanel;
	public static JPanel barPanel;
	private JButton stopButton, updateButton;
	public static JButton execButton;
	private JScrollPane sp;
	private static JLabel FailedLabel, PassLabel, SSQLexecFailedLabel,
	RunTimeLabel, StartTimeLabel, EndTimeLabel, queryNumberLabel,
	SSQLexecSuccessLabel, JTreeLabel, Result_kind_Label;
	private static JTable table;
	private static DefaultTableModel tableModel;
	private final static String[] columnName = { "No", "Testcase_id", "File Name",
		"Name", "Result", "Tag", "Media", "Details", "CSS Test"};
	private final static int rowNum = 0; // 100;
	private JButton kakuninButton = new JButton();
	public static JProgressBar progressBar0;
	public static int get_NUM = 1;
	private static int Ecec_NUM = 0;
	// public static int queruyNum = 0;
	public static int row = 0;
	public ArrayList<SsqlResult> sr;

	private ArrayList<RegressionTestResult> al = null;
	private JComboBox<String> stateCombo;
	private Vector<String> stateVector;
	
	// Backstop Variables
	private static String concatenatedPathString = "";
	private static String backstopRootPath = "/Users/ayushya/Documents/eclipse-workspace/sstest/backstop";
	private static String backstopTestPath = "/Users/ayushya/Documents/eclipse-workspace/sstest/backstop/backstop_data/test_htmls";
	private static String backstopResultPath = "/Users/ayushya/Documents/eclipse-workspace/sstest/backstop/backstop_data/ci_report/xunit.xml";
	
	private static Test_Run_and_Result frame = null;

	public static TreeSet<Integer> qTreeSet = new TreeSet<Integer>();
	public static Iterator<Integer> it = new Iterator<Integer>() {
		@Override
		public Integer next() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			return false;
		}
	};
	public static String partQuery = "";

	/**
	 * Launch the application.
	 */
	public static void display() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					initTable();
					initResultArray();
					if (frame != null)
						return;
					frame = new Test_Run_and_Result();
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
	public Test_Run_and_Result() {
		// setBounds(100, 100, 450, 500);
		mainPanel = new JPanel();
		Information_Panel = new JPanel();
		resultTable_Panel = new JPanel();
		tree_Panel = new JPanel();
		leftsidePanel = new JPanel();

		// すべて表示・成功・失敗のタブ選択
		stateVector = new Vector<String>();
		stateVector.add("All");
		stateVector.add("Pass");
		stateVector.add("Fail");
		stateVector.add("Same SSQL output");
		stateVector.add("Different SSQL output");
		stateCombo = new JComboBox<String>(stateVector);

		// テーブルモデル

		// 　　　　TableColumnModel cm = tb.getColumnModel();
		// 　　　　TableColumn column = cm.getColumn(1);
		//
		// 　　　　DefaultTableCellRenderer renderer = new
		// DefaultTableCellRenderer();
		// 　　　　renderer.setHorizontalAlignment(SwingConstants.RIGHT);
		// 　　　　column.setCellRenderer(renderer);

		// columnName.setCellRenderer(new HorizontalAlignmentTableRenderer());

		tableModel = new DefaultTableModel(columnName, rowNum) {
			@Override
			public Class<?> getColumnClass(int column) {
				return getValueAt(0, column).getClass();
			}
		
		};
		table = new JTable(tableModel) {
			public Component prepareRenderer(TableCellRenderer tcr, int row,
					int column) {
				Component c = super.prepareRenderer(tcr, row, column);
				if (c instanceof JComponent) {
					JComponent l = (JComponent) c;
					Object o = getValueAt(row, column);
					String str = o.toString();
					if (column == 5) {
						// ポップアップ表示してるとこ
						l.setToolTipText(str);
					}
				}
				return c;
			}
			
			@Override
			public boolean isCellEditable(int row, int column) {
				if(column==7 || column==8)	//Editable column: Details
					return true;
				return false;
			}
		};
		TableColumn column;
		DefaultTableCellRenderer rend;
		rend = new DefaultTableCellRenderer();
		rend.setAlignmentX(CENTER_ALIGNMENT);
		table.getColumnModel().getColumn(0).setCellRenderer(rend);
		table.getColumnModel().getColumn(1).setCellRenderer(rend);
		table.getColumnModel().getColumn(2).setCellRenderer(rend);
		table.getColumnModel().getColumn(3).setCellRenderer(rend);
		// table.getColumn("Title").setCellRenderer.setAlignmentX(CENTER_ALIGNMENT);
		table.getTableHeader().setReorderingAllowed(false); // ドラッグ&ドロップによる列の入れ替えを許可しない

		// 確認ボタン
		table.getColumn("Details").setCellRenderer(new ButtonRenderer());
		table.getColumn("Details").setCellEditor(
				new ButtonEditor(new JCheckBox()));
		sp = new JScrollPane(table);
		sp.setPreferredSize(new Dimension(800, 350)); // サイズ設定

		// カラム幅設定
		table.getColumn("No").setPreferredWidth(20);
		table.getColumn("Testcase_id").setPreferredWidth(65);
		table.getColumn("File Name").setPreferredWidth(60);
		table.getColumn("Name").setPreferredWidth(100);
		table.getColumn("Result").setPreferredWidth(30);
		table.getColumn("Media").setPreferredWidth(70);
		table.getColumn("Tag").setPreferredWidth(150);
		table.getColumn("Details").setPreferredWidth(70);
		table.getColumn("CSS Test").setPreferredWidth(100);

		// 値の更新はRowNumをいれている
		barPanel = new JPanel();

		// アイコンの部分

		ImageIcon icon1 = new ImageIcon("." + GlobalEnv.OS_FS + "image"
				+ GlobalEnv.OS_FS + "◯" + ".png");
		ImageIcon icon2 = new ImageIcon("." + GlobalEnv.OS_FS + "image"
				+ GlobalEnv.OS_FS + "×" + ".png");
		ImageIcon icon3 = new ImageIcon("." + GlobalEnv.OS_FS + "image"
				+ GlobalEnv.OS_FS + "△" + ".png");
		ImageIcon icon4 = new ImageIcon("." + GlobalEnv.OS_FS + "image"
				+ GlobalEnv.OS_FS + "▲" + ".png");

		// 結果の表示のところ
		JLabel icon1_Label = new JLabel(icon1);
		JLabel icon2_Label = new JLabel(icon2);
		JLabel icon3_Label = new JLabel(icon3);
		JLabel icon4_Label = new JLabel(icon4);
		JLabel Result_kind_Label_s_w = new JLabel(": Pass  ");
		JLabel Result_kind_Label_sf_w = new JLabel(": Fail  ");
		JLabel Result_kind_Label_ss_w = new JLabel(": Same SSQL output   ");
		JLabel Result_kind_Label_ssf_w = new JLabel(
				": Different SSQL output   ");

		barPanel.add(icon1_Label);
		barPanel.add(Result_kind_Label_s_w);
		barPanel.add(icon2_Label);
		barPanel.add(Result_kind_Label_sf_w);
		barPanel.add(icon3_Label);
		barPanel.add(Result_kind_Label_ss_w);
		barPanel.add(icon4_Label);
		barPanel.add(Result_kind_Label_ssf_w);

		// ボタン関係
		// stopButton = new JButton("Stop"); //ストップボタンは実行おされてから
		// stopButton.setEnabled(false);
		updateButton = new JButton("Update");
		updateButton.setEnabled(false); // 一回目からアップデートはできないから
		execButton = new JButton("Execute");
		// execButton.setEnabled(false);

		buttonPanel = new JPanel();
		buttonPanel.add(execButton);
		// buttonPanel.add(stopButton);
		buttonPanel.add(updateButton);

		// ツリーを作る
		TreeTestPanel = new JTreeTest();
		TreeTestPanel.setPreferredSize(new Dimension(300, 340));
		tree_Panel.add(TreeTestPanel);
		tree_Panel.setPreferredSize(new Dimension(300, 340));
		// tree_Panel.setBackground(Color.BLUE);
		// tree_Panel.setLayout(new BoxLayout(tree_Panel, BoxLayout.Y_AXIS));

		// Information部分の表示
		queryNumberPanel = new JPanel();
		passPanel = new JPanel();
		FailedPanel = new JPanel();
		SSQLexecSuccessPanel = new JPanel();
		SSQLexecFailedPanel = new JPanel();
		progressBar0 = new JProgressBar(); // 最小、最大

		JLabel icon11_Label = new JLabel(icon1);
		JLabel icon21_Label = new JLabel(icon2);
		JLabel icon31_Label = new JLabel(icon3);
		JLabel icon41_Label = new JLabel(icon4);

		queryNumberLabel = new JLabel("Exec Number  : ");
		PassLabel = new JLabel("Pass  : ");
		FailedLabel = new JLabel("Fail  : ");
		SSQLexecSuccessLabel = new JLabel("Same SSQL output : ");
		SSQLexecFailedLabel = new JLabel("Different SSQL output : ");
		// StartTimeLabel = new JLabel("Start Time  : ");
		// EndTimeLabel = new JLabel("End Time  : ");
		// RunTimeLabel = new JLabel("Run Time  : ");

		queryNumberPanel.add(queryNumberLabel);
		queryNumberPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		passPanel.add(icon11_Label);
		passPanel.add(PassLabel);
		passPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		FailedPanel.add(icon21_Label);
		FailedPanel.add(FailedLabel);
		FailedPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		SSQLexecSuccessPanel.add(icon31_Label);
		SSQLexecSuccessPanel.add(SSQLexecSuccessLabel);
		SSQLexecSuccessPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		SSQLexecFailedPanel.add(icon41_Label);
		SSQLexecFailedPanel.add(SSQLexecFailedLabel);
		SSQLexecFailedPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		Information_Panel.add(queryNumberPanel);
		Information_Panel.add(passPanel);
		Information_Panel.add(FailedPanel);
		Information_Panel.add(SSQLexecSuccessPanel);
		Information_Panel.add(SSQLexecFailedPanel);

		Information_Panel.setBorder(new TitledBorder(new EtchedBorder(),
				"Information")); // ボーダーの部分
		Information_Panel.setLayout(new BoxLayout(Information_Panel,
				BoxLayout.Y_AXIS));
		Information_Panel.setPreferredSize(new Dimension(250, 100));

		leftsidePanel.add(tree_Panel);
		leftsidePanel.add(Information_Panel);
		leftsidePanel.setLayout(new BoxLayout(leftsidePanel, BoxLayout.Y_AXIS));
		leftsidePanel.setPreferredSize(new Dimension(300, 530));

		// レイアウト！ボックスレイアウト
		resultTable_Panel.setBorder(new EmptyBorder(20, 20, 20, 20)); // 透明な箱！
		resultTable_Panel.setLayout(new BoxLayout(resultTable_Panel,
				BoxLayout.Y_AXIS));
		// メインパネル
		resultTable_Panel.add(stateCombo);
		resultTable_Panel.add(barPanel);
		resultTable_Panel.add(sp);
		resultTable_Panel.add(buttonPanel);

		mainPanel.add(leftsidePanel);
		mainPanel.add(resultTable_Panel);
		// レイアウト！ボックスレイアウト
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

		add(mainPanel);

		// Details(詳細)ボタンの処理アクション！！
		kakuninButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				row = table.getEditingRow();
				if (al == null)
					al = result_all;
				// Result_Detail rd = new Result_Detail(al,
				// al.get(row).getQueryID());
				result_detail();
				TestResult_Detail.display(al, Integer.parseInt(tableModel
						.getValueAt(row, 1).toString()), query_result);
			}
		});

		// Exec(実行)ボタンの処理
		execButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// 別スレッド処理 (非同期処理)
				new Thread(new Runnable() {
					@Override
					public void run() {

						// 初期化
						get_NUM = 1; // 実行個数を数えている(JProgressBarの最大値)
						Ecec_NUM++; // 実行回数を数えている(JProgressBarの表示)

						Exec.result_count_failed = 0;
						Exec.result_count_pass = 0;
						Exec.result_count_SSQLfailed = 0;
						Exec.result_count_SSQLsuccess = 0;

						// 選択されているテストケースを取得している
						DefaultMutableTreeNode root;
						root = (DefaultMutableTreeNode) JTreeTest.model.getRoot();
						
						Enumeration enu = root.breadthFirstEnumeration();
						while (enu.hasMoreElements()) {
							node = (DefaultMutableTreeNode) enu.nextElement();
							if (node.isLeaf()) {
								// CheckBoxNode cnode = (CheckBoxNode)
								// node.getUserObject();
								cnode = (CheckBoxNode) node.getUserObject();
								if (cnode.status == Status.SELECTED) {
									qTreeSet.add(Integer.parseInt((node
											.toString().substring(0, node
													.toString().indexOf(":")))));
								}
							}
						}
						if(qTreeSet.size()<1)	return;
						
						it = qTreeSet.iterator();
						partQuery = "";
						partQuery += "AND (";
						while (it.hasNext()) {
							partQuery += "q.id = " + it.next();
							if (it.hasNext()) {
								get_NUM++;
								partQuery += " OR ";
							}
						}
						partQuery += ")";
						// queruyNum = DB.getQueryNumber();
						// stopButton.setEnabled(true); //ストップボタン表示
						stateCombo.setEnabled(false); // コンボボックスを選択不可にしている
						stateCombo.setSelectedIndex(0); // コンボボックスのインデックスを0に設定している
						updateButton.setEnabled(false);
						execButton.setEnabled(false);// コンボボックスを選択不可能にする
						execButton.setText("Running...");// 実行ボタンのテキストを変更する

						qTreeSet.clear();
						initTable(); // テーブル初期化
						initResultArray(); // 結果の配列初期化

						// JProgressBar(進捗バー)
						if (Ecec_NUM == 1) { // もし、実行回数が１ならばインスタンス生成

							progressBar0.setMinimum(0);
							progressBar0.setMaximum(get_NUM);
							progressBar0
							.setPreferredSize(new Dimension(300, 40)); // バーのサイズ
							progressBar0.setStringPainted(true);// パーセンテージ表示
							barPanel.add(progressBar0);
						} else { // それ以外ならば、最大値を再設定
							progressBar0.setMaximum(get_NUM);
						}

						progressBar0.setValue(0);
						progressBar0.setVisible(true);
						
						// 回帰テスト実行している
						sstest.ExecRegressionTest.DB.db();
						
						listFilesForFolder(new File(backstopTestPath));
						
						// Backstop plugin
						final String[] Command = {"/usr/local/bin/backstop", "test", "--configPath=backstop-settings.js", "--paths=" + concatenatedPathString};				
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
								HashMap _backstopres = new HashMap();
								
								// Read output here
								try {
									File inputFile = new File(backstopResultPath);
									DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
									DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
									Document doc = dBuilder.parse(inputFile);
									doc.getDocumentElement().normalize();
									NodeList nList = doc.getElementsByTagName("testcase");
									
									for (int temp = 0; temp < nList.getLength(); temp++) {
										Node nNode = nList.item(temp);
										if (nNode.getNodeType() == Node.ELEMENT_NODE) {
											Element eElement = (Element) nNode;
											String _queryid = String.valueOf(Integer.parseInt(eElement.getAttribute("name").replaceAll("[\\D]", "")));
											
											if (eElement.getChildNodes().getLength() == 0) {
												System.out.println("Correct Result" + _queryid);
												_backstopres.put(_queryid, true);
											} else {
												System.out.println("Wrong Result" + _queryid);
												_backstopres.put(_queryid, false);
											}
										}
									}
								} catch (Exception e) {
							         e.printStackTrace();
							    }
								
								// Update cells here
								ImageIcon correct = new ImageIcon("." + GlobalEnv.OS_FS + "image" + GlobalEnv.OS_FS+ "◯.png");
								ImageIcon incorrect = new ImageIcon("." + GlobalEnv.OS_FS + "image" + GlobalEnv.OS_FS+ "×.png");
								for (int count = 0; count < tableModel.getRowCount(); count++){
									String qid = tableModel.getValueAt(count, 1).toString();
									System.out.println("QID is " + qid);
									if ( (boolean) _backstopres.get(qid) )
										tableModel.setValueAt(correct, count, 8);
									else
										tableModel.setValueAt(incorrect, count, 8);
								}
								
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							System.out.println("Test ended.");
							
							in.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						execButton.setEnabled(true); // 実行ボタンを戻す
						execButton.setText("Execute"); // 実行ボタンのテキストを戻す
						updateButton.setEnabled(true); // アップデートボタン表示
						stateCombo.setEnabled(true); // コンボボックスを選択可能にする
						// stopButton.setEnabled(false); //ストップボタン非表示

						if (progressBar0.getPercentComplete() == 1) {
							progressBar0.setVisible(false);
						}

						queryNumberLabel
						.setText("Exec Number  : "
								+ (Exec.result_count_failed
										+ Exec.result_count_pass
										+ Exec.result_count_SSQLfailed + Exec.result_count_SSQLsuccess));
						PassLabel.setText("Pass  : " + Exec.result_count_pass);
						FailedLabel.setText("Fail  : "
								+ Exec.result_count_failed);
						SSQLexecSuccessLabel.setText("Same SSQL output  : "
								+ Exec.result_count_SSQLsuccess);
						SSQLexecFailedLabel.setText("Different SSQL output  : "
								+ Exec.result_count_SSQLfailed);

						System.out.println("%%%%%%%%失敗 : "
								+ Exec.result_count_failed + "    成功 : "
								+ Exec.result_count_pass + "   実行失敗  : "
								+ Exec.result_count_SSQLfailed);
					}
				}).start();
			}
		});

		// Update(更新)ボタン
		updateButton.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				// 別スレッド処理 (非同期処理)
				new Thread(new Runnable() {
					@Override
					public void run() {
						// 初期化
						Exec.result_count_failed = 0;
						Exec.result_count_pass = 0;
						Exec.result_count_SSQLfailed = 0;
						Exec.result_count_SSQLsuccess = 0;

						// stopButton.setEnabled(true); //ストップボタン表示
						stateCombo.setSelectedIndex(0); // コンボボックスのインデックスを0に設定している
						stateCombo.setEnabled(false); // コンボボックスを選択不可にしている
						// progressBar0.setEnabled(true);//反映されてない
						// i = 0;
						updateButton.setEnabled(false);
						updateButton.setText("update..."); // 実行中…
						execButton.setEnabled(false);

						initTable();
						initResultArray();
						sstest.ExecRegressionTest.DB.db();
						// Dialog.NormalDialog("失敗: "+Exec.result_count_failed+"   SSQL失敗: "+Exec.result_count_SSQLfailed+"   成功:  "+Exec.result_count_successed);
						updateButton.setEnabled(true);// 実行ボタンを戻す
						updateButton.setText("Update"); // 再実行ボタンのテキストを戻す
						execButton.setEnabled(true);
						stateCombo.setEnabled(true); // コンボボックスを選択可能にする
						// stopButton.setEnabled(false); //ストップボタン表示

						PassLabel.setText("Pass  : " + Exec.result_count_pass);
						FailedLabel.setText("Fail  : "
								+ Exec.result_count_failed);
						SSQLexecSuccessLabel.setText("Same SSQL output  : "
								+ Exec.result_count_SSQLsuccess);
						SSQLexecFailedLabel.setText("Different SSQL output  : "
								+ Exec.result_count_SSQLfailed);

					}
				}).start();
			}
		});

		// コンボボックスの処理
		stateCombo.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				table.setEditingRow(0); // ※重要※
				// JTable.editingRowの初期化（これをしていないとコンボボックス切替後にボタンを押すとエラーが発生する）
//				al = null;
				sr = null;
				if (stateCombo.getSelectedIndex() == 0) {
					// すべて表示
					sr = Exec.ssqlResultArray;
				} else if (stateCombo.getSelectedIndex() == 1) {
					// 成功
					sr = Exec.ssqlPassResultArray;
				} else if (stateCombo.getSelectedIndex() == 2) {
					// 失敗
					sr = Exec.ssqlFailResultArray;
				} else if (stateCombo.getSelectedIndex() == 3) {
					// エラー検査　成功
					sr = Exec.ssqlSameOutputResultArray;
				} else if (stateCombo.getSelectedIndex() == 4) {
					// エラー検査　失敗
					sr = Exec.ssqlDiffOutputResultArray;
				}

				initTable();
				// 回帰テスト結果テーブルを変更するにはDBとExecも変更する必要がある！
				for (SsqlResult x : sr) {
					insertTableData("" + (x.getQueryID()),
							x.getQueryFileName(), x.getFileTitle(),
							x.getResult(), x.getQueryTag(),
							x.getQueryOutput());
				}
			}
		});
	}

	// クエリ詳細を押す前に情報を取ってきている
	private static void result_detail() {
		Database.connect();
		String sql_result = "SELECT * FROM result r ,query q WHERE r.q_id =q.id AND r.q_id ="
				+ tableModel.getValueAt(row, 1) + " order by a_id;";
		System.out.println(sql_result);
		ResultSet rs_result = Database.select(sql_result);
		query_result = new ArrayList<>();
		QueryResults queryResults = null;
		// テーブル照会結果を出力
		try {
			while (rs_result.next()) {
				String query_title = rs_result.getString("q_title");
				Integer query_id = rs_result.getInt("q_id");
				Integer result_id = rs_result.getInt("a_id");
				String result_contents = rs_result.getString("a_contents");
				String result_author = rs_result.getString("a_author");
				String result_day = rs_result.getString("a_day");

				queryResults = new QueryResults(query_title, query_id,
						result_id, result_contents, result_author, result_day);
				query_result.add(queryResults);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Database.close();
	}

	// テーブルへデータを表示
	public static int tableRowCount = 0;

	public static void insertTableData(String testcaseID, String name_ssql, String titlename,
			String s4, String tag_name, String media_name) {
		System.out.println("s4= " + s4);
		tableModel.insertRow(tableRowCount++, new Object[] {tableRowCount,testcaseID,name_ssql,titlename,new ImageIcon("." + GlobalEnv.OS_FS + "image" + GlobalEnv.OS_FS+ s4 + ".png"), tag_name, media_name, "Details", ""});
		//進捗バー
		progressBar0.setValue(tableRowCount); // ここに現在の処理している行数を入れる
	}

	// テーブルの初期化
	private static void initTable() {
		try {
			tableModel.setRowCount(0);
		} catch (Exception e) {
		}
		tableRowCount = 0;
	}

	// ArrayListに格納されている結果の初期化
	private static void initResultArray() {
		result_all.clear();
		result_success.clear();
		result_failure.clear();
	}

	public void listFilesForFolder(final File folder) {
		boolean flag = true;
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else if (findExtension(fileEntry.getName()).contains("html")){
	        		// To remove ending commas
	        		if (flag) {
	        			concatenatedPathString += fileEntry.getName();
	        			flag = false;
	        		} else {
	        			concatenatedPathString += "," + fileEntry.getName();
	        		}
	        } else {
	        		System.out.println("EXT: " + findExtension(fileEntry.getName()));
	        }
	    }
	}
	
	public String findExtension(String filename) {
		int separator = filename.indexOf(".");
		return (String) filename.subSequence(separator+1, filename.length());
	}

	// テーブルへボタンを表示させるためのクラス1
	class ButtonRenderer extends JButton implements TableCellRenderer {
		private static final long serialVersionUID = 1L;

		public ButtonRenderer() {
			setOpaque(true);
		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			setText((value == null) ? "" : value.toString());
			if (value == null) {
				// this.setEnabled(false);
				return null;
			}
			return this;
		}
	}

	// テーブルへボタンを表示させるためのクラス2
	class ButtonEditor extends DefaultCellEditor {
		private static final long serialVersionUID = 1L;
		private String label;

		public ButtonEditor(JCheckBox checkBox) {
			super(checkBox);
		}

		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			label = (value == null) ? "" : value.toString();
			if (value == null)
				return null;
			kakuninButton.setText(label);
			return kakuninButton;
		}

		public Object getCellEditorValue() {
			// System.out.println("getCellEditorValue  label = "+label);
			return new String(label);
		}
	}

	// ×ボタン
	class MyWindowAdapter extends WindowAdapter {
		public void windowClosing(WindowEvent we) {
			frame = null;
			// System.exit(0);
		}
	}

}
