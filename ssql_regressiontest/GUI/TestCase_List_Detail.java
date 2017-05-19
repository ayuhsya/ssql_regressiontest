package ssql_regressiontest.GUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import ssql_regressiontest.Common.Common;
import ssql_regressiontest.Common.Database;
import ssql_regressiontest.Common.GlobalEnv;

public class TestCase_List_Detail extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTabbedPane tabbedpane_detail;
	private JPanel mainPanel, queryDay_Panel, queryTag_Panel, queryTitle_Panel,
			queryAuthor_Panel, queryDescription_Panel, queryContents_Panel,
			resultAuthor_Panel, resultDay_Panel, resultContents_Panel,
			query_Panel, result_Panel, query_result_Panel, queryOutput_Panel, query_main_Panel, tree_Panel, result_detail_Panel;
	private JLabel q_Title, q_Day, q_Author, q_Tag, q_Description, q_Contents,
			r_Author, r_Day, r_Contents, q_Output, r_Output;
	private JTextField queryTitleField, queryDayField, queryAuthorField,
			queryTagField, resultAuthorField, resultDayField, queryOutputField;
	private JTextArea queryDescriptionArea, queryContentsArea,
			resultContentsArea;
	private JScrollPane scrollpane_contents, scrollpane_description,
			scrollpane_contents_result, tree_scrollpane;
	private JButton exitButton;
	private String queryName, queryTitle, queryDay, queryAuthor, queryOutput,
			queryDescription, queryContents, resultAuthor, resultDay;
	private Map<String, String> resultMap = new HashMap<String, String>();

	public static void display(final int queryID, final String queryTag) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestCase_List_Detail frame = new TestCase_List_Detail(queryID,
							queryTag);
					frame.setLocationRelativeTo(null);// 中央に表示させる
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
	
	public TestCase_List_Detail(int queryID, String queryTag) {
		getDetail(queryID);

		setBounds(100, 100, 1300, 750); // サイズ、位置設定
		addWindowListener(new MyWindowAdapter()); // 閉じるボタンを押した時の処理
		setTitle("Test Case Detail Information - " + queryTitle); // タイトル
		
		// field
		mainPanel = new JPanel();
		queryTitle_Panel = new JPanel();
		queryDay_Panel = new JPanel();
		queryAuthor_Panel = new JPanel();
		queryTag_Panel = new JPanel();
		queryDescription_Panel = new JPanel();
		queryContents_Panel = new JPanel();
		query_Panel = new JPanel();
		query_main_Panel = new JPanel();
		queryOutput_Panel = new JPanel();

		resultAuthor_Panel = new JPanel();
		resultDay_Panel = new JPanel();
		resultContents_Panel = new JPanel();
		result_Panel = new JPanel();
		tree_Panel = new JPanel();
		result_detail_Panel = new JPanel();

		/*******************
		 * 　テストケース
		 ***********************/
		// タイトル
		q_Title = new JLabel("     Title : "); // クエリタイトル名
		queryTitleField = new JTextField("", 30);
		queryTitleField.setText(queryTitle);
		queryTitleField.setEditable(false); // 編集不可にしている
		queryTitle_Panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		queryTitle_Panel.add(q_Title);
		queryTitle_Panel.add(queryTitleField);

		
		// 作成日
		q_Day = new JLabel("      Day : ");
		queryDayField = new JTextField("", 30);
		queryDayField.setText(queryDay);	//queryDay.substring(0, 19)
		queryDayField.setEditable(false); // 編集不可にしている
		queryDay_Panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		queryDay_Panel.add(q_Day);
		queryDay_Panel.add(queryDayField);

		// 作者
		q_Author = new JLabel(" Author : "); // クエリの作成者
		queryAuthorField = new JTextField("", 30);
		queryAuthorField.setText(queryAuthor);
		queryAuthorField.setEditable(false); // 編集不可にしている
		queryAuthor_Panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		queryAuthor_Panel.add(q_Author);
		queryAuthor_Panel.add(queryAuthorField);

		// 出力形式(メディア)
		q_Output = new JLabel("    Media : ");
		queryOutputField = new JTextField("", 30);
		queryOutputField.setText(queryOutput);
		queryOutputField.setEditable(false);
		queryOutput_Panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		queryOutput_Panel.add(q_Output);
		queryOutput_Panel.add(queryOutputField);

		// タグ
		q_Tag = new JLabel("      Tag : ");
		queryTagField = new JTextField("", 30);
		queryTagField.setText(queryTag);
		queryTagField.setEditable(false);
		queryTag_Panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		queryTag_Panel.add(q_Tag);
		queryTag_Panel.add(queryTagField);

		// 説明
		q_Description = new JLabel("Description : "); // 説明・メモ的なもの
		queryDescriptionArea = new JTextArea(4, 2);
		queryDescriptionArea.setText(queryDescription);
		queryDescriptionArea.setCaretPosition( 0 ); //スクロールバーの位置を一番上に
		queryDescriptionArea.setLineWrap(false); // まず、テキストエリアの文字列折り返しを無効
		queryDescriptionArea.setEditable(false); // 編集不可にしている
		scrollpane_description = new JScrollPane(queryDescriptionArea);
		scrollpane_description.setPreferredSize(new Dimension(450, 250));
		queryDescription_Panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		queryDescription_Panel.add(q_Description);
		queryDescription_Panel.add(scrollpane_description);

		// クエリ内容
		q_Contents = new JLabel(" Query : "); // クエリ内容
		queryContentsArea = new JTextArea(10, 5);
		queryContentsArea.setText(queryContents);
		queryContentsArea.setCaretPosition( 0 ); //スクロールバーの位置を一番上に
		queryContentsArea.setLineWrap(false); // まず、テキストエリアの文字列折り返しを無効
		queryContentsArea.setEditable(false); // 編集不可にしている
		scrollpane_contents = new JScrollPane(queryContentsArea); // スクロールパネル
		scrollpane_contents.setPreferredSize(new Dimension(480, 510)); // スクロールパネルサイズ
		queryContents_Panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		queryContents_Panel.add(q_Contents);
		queryContents_Panel.add(scrollpane_contents);
		scrollpane_contents.setRowHeaderView(new TestResult_Detail.LineNumberView(
		queryContentsArea)); // 行番号表示


		// レイアウトボックスレイアウト！
//		query_Panel
//				.setBorder(new TitledBorder(new EtchedBorder(), "Test Case")); // ボーダーの部分
		query_Panel.setLayout(new BoxLayout(query_Panel, BoxLayout.Y_AXIS));
		query_Panel.setPreferredSize(new Dimension(500, 600));
		query_Panel.add(queryTitle_Panel);
		query_Panel.add(queryDay_Panel);
		query_Panel.add(queryAuthor_Panel);
		query_Panel.add(queryOutput_Panel);
		query_Panel.add(queryTag_Panel);
		query_Panel.add(queryDescription_Panel);
		query_main_Panel.add(query_Panel);
		query_main_Panel.add(queryContents_Panel);
		query_main_Panel.setBorder(new EmptyBorder(20, 20, 20, 20));
		query_main_Panel.setLayout(new BoxLayout(query_main_Panel, BoxLayout.X_AXIS));


		
		
		/*******************
		 * 　テスト解答
		 ***********************/
		// 作者
		r_Author = new JLabel("Author : "); // クエリの作成者
		resultAuthorField = new JTextField("", 30);
		resultAuthorField.setText(resultAuthor);
		resultAuthorField.setEditable(false); // 編集不可にしている
		resultAuthor_Panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		resultAuthor_Panel.add(r_Author);
		resultAuthor_Panel.add(resultAuthorField);

		// 作成日
		r_Day = new JLabel("Day : ");
		resultDayField = new JTextField("", 30);
		resultDayField.setText(resultDay.substring(0, 19));
		resultDayField.setEditable(false); // 編集不可にしている
		resultDay_Panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		resultDay_Panel.add(r_Day);
		resultDay_Panel.add(resultDayField);

		// 解答内容
		r_Contents = new JLabel("Result : "); // クエリ内容
		resultContentsArea = new JTextArea();
		scrollpane_contents_result = new JScrollPane(resultContentsArea); // スクロールパネル
		scrollpane_contents_result.setPreferredSize(new Dimension(820, 350)); // スクロールパネルサイズ
		resultContents_Panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		resultContents_Panel.add(r_Contents);
		resultContents_Panel.add(scrollpane_contents_result);
		scrollpane_contents_result
				.setRowHeaderView(new TestResult_Detail.LineNumberView(
						resultContentsArea)); // 行番号表示

		// JTree関係
		String queryFileNameStr = queryName.substring(0,
				queryName.indexOf(".ssql"));
		File file = new File(GlobalEnv.resultDir_Path + GlobalEnv.OS_FS
				+ queryFileNameStr);
		String rootName = queryFileNameStr;
		DefaultMutableTreeNode top = new DefaultMutableTreeNode(rootName);
		createNodes(top, file);
		final JTree tree = new JTree(top);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
						.getLastSelectedPathComponent();
				if (!node.isLeaf()) {
					return;
				} else {
					String lastPath = tree.getSelectionPath().getLastPathComponent().toString();
					 resultContentsArea.setText(resultMap.get(lastPath));
						resultContentsArea.setCaretPosition( 0 ); //スクロールバーの位置を一番上に
						resultContentsArea.setLineWrap(false); // まず、テキストエリアの文字列折り返しを無効
						resultContentsArea.setEditable(false); // 編集不可にしている
				}
			}
		});


		tree_Panel.setLayout(new BoxLayout(tree_Panel, BoxLayout.Y_AXIS));
		tree_Panel.setPreferredSize(new Dimension(300,500));
		tree_scrollpane = new JScrollPane(tree); // スクロールパネル
		tree_scrollpane.setPreferredSize(new Dimension(300, 400)); // スクロールパネルサイズ		
		r_Output = new JLabel("Output List");
		tree_Panel.add(r_Output);
		tree_Panel.add(tree_scrollpane);
		tree_Panel.setBorder(new EmptyBorder(30, 30, 30, 30));
		
		result_detail_Panel.setLayout(new BoxLayout(result_detail_Panel, BoxLayout.Y_AXIS));
		result_detail_Panel.setPreferredSize(new Dimension(1000, 600));
		result_detail_Panel.add(resultDay_Panel);
		result_detail_Panel.add(resultAuthor_Panel);
		result_detail_Panel.add(resultContents_Panel);
		result_detail_Panel.setBorder(new EmptyBorder(30, 30, 30, 30));
		
		// レイアウトボックスレイアウト！

//		result_Panel.setBorder(new TitledBorder(new EtchedBorder(), "Result"));// ボーダーの部分
		result_Panel.setLayout(new BoxLayout(result_Panel, BoxLayout.X_AXIS));
//		result_Panel.setPreferredSize(new Dimension(1000, 600));
		result_Panel.add(tree_Panel);
		result_Panel.add(result_detail_Panel);

		exitButton = new JButton("Exit");
//		query_result_Panel = new JPanel();

//		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
//		query_result_Panel.add(query_Panel);
//		query_result_Panel.add(result_Panel);

		mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
//		mainPanel.add(query_result_Panel);

		
		//タブ管理
		JTabbedPane tabbedpane_detai = new JTabbedPane();
		tabbedpane_detai.addTab("Test Case", query_main_Panel);
		tabbedpane_detai.addTab("Expected result",result_Panel);
		Container contentPane = getContentPane();
		mainPanel.add(tabbedpane_detai);
		mainPanel.add(exitButton);
		contentPane.add(mainPanel, BorderLayout.CENTER);


		// 終了ボタン
		exitButton.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

	}

	// 引数で指定したクエリIDの詳細情報（クエリ情報，結果情報）を取得
	private void getDetail(int queryID) {
		String sql = "SELECT q_name, q_day, q_title, o_name, q_author, q_description, q_contents, f_name, a_author, a_day, a_contents "
				+ "FROM result r1, query q, output o "
				+ "WHERE NOT EXISTS (  SELECT 1  FROM result r2  WHERE r1.q_id = r2.q_id AND r1.a_day < r2.a_day ) "
				+ "AND r1.q_id = q.id AND q.output_id = o.o_id AND "
				+ "id =" + queryID + " ORDER BY q.id ";
		Database.connect();
		ResultSet rs = Database.select(sql);

		try {
			while (rs.next()) {
				queryName = rs.getString("q_name");
				queryTitle = rs.getString("q_title");
				queryDay = rs.getString("q_day");
				queryAuthor = rs.getString("a_author");
				queryOutput = rs.getString("o_name");
				queryDescription = rs.getString("q_description");
				queryContents = rs.getString("q_contents");
				resultAuthor = rs.getString("a_author");
				resultDay = rs.getString("a_day");
				String str = rs.getString("f_name");
				if (str.contains(GlobalEnv.OS_FS)) {
					str = str.substring(str.lastIndexOf(GlobalEnv.OS_FS) + 1,
							str.length());
				}
				resultMap.put(str, rs.getString("a_contents"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Database.close();
	}

	private static void createNodes(DefaultMutableTreeNode tree, File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				DefaultMutableTreeNode subtree = new DefaultMutableTreeNode(
						files[i].getName());
				createNodes(subtree, files[i]);
				tree.add(subtree);
			}
		}
	}

};
