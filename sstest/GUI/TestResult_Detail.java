package sstest.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultMutableTreeNode;

import sstest.Class.QueryResults;
import sstest.Class.RegressionTestResult;
import sstest.Common.Common;
import sstest.Common.Database;
import sstest.Common.GlobalEnv;

public class TestResult_Detail extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel mainPanel, outputListPanel, leftPanel, rightPanel, diffPanel, ButtonPanel, comboPanel;
	private JButton replaceButton, stopButton, undoButton, redoButton, exitButton, diffButton;
	private JLabel qname;
	private JLabel output_list_Label, latest_of_expected_result_Label, past_the_expected_value_Label, actual_result_Label, diff_Label;
	private JTextArea text_old_HTML, text_new_HTML, text_diff;
	private JComboBox combo;
	private JScrollPane scrollpane_old, scrollpane_new, scrollpane_diff;
	private HashMap<String, RegressionTestResult> fileDiffMap;
	private ArrayList<RegressionTestResult> currentRTRArray = new ArrayList<RegressionTestResult>();
	RegressionTestResult rtr;
	// クエリのID（テストケースID）
	private int num = 0;
	private String author = "";
	private String day = "";
	private String resultFig = "";

	public static void display(final ArrayList<RegressionTestResult> al, final int queryID,
			final ArrayList<QueryResults> query_result) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestResult_Detail frame = new TestResult_Detail(al, queryID,
							query_result);
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public TestResult_Detail(ArrayList<RegressionTestResult> al, final int queryID,
			ArrayList<QueryResults> query_result) {
		setBounds(200, 200, 1200, 800);

		addWindowListener(new MyWindowAdapter()); // 閉じるボタンを押した時の処理
		boolean flag = true;
		String rootName = null;
		String queryFileNameStr = "";
		fileDiffMap = new HashMap<String, RegressionTestResult>();
		for (int i = 0; i < al.size(); i++) {
			if (al.get(i).getQueryID() == queryID) {
				if (flag) {
					// num = i;
					String tmp = al.get(i).getQueryFileName();
					queryFileNameStr = tmp.substring(
							tmp.lastIndexOf(GlobalEnv.OS_FS) + 1,
							tmp.lastIndexOf(".ssql"));
					flag = false;
				}
				// なんか書く
				rtr = new RegressionTestResult(queryID, al.get(i)
						.getQueryFileName(), al.get(i).getOutFileName(), al
						.get(i).getFileTitle(), al.get(i).getResult(), al
						.get(i).getDiff(), al.get(i).getDiff_lines_old(), al
						.get(i).getDiff_lines_new(), al.get(i).getQueryTag(),
						al.get(i).getQueryOutput());
				String tmp2 = al.get(i).getOutFileName();

				fileDiffMap.put(tmp2.substring(
						tmp2.lastIndexOf(GlobalEnv.OS_FS) + 1, tmp2.length()),
						rtr);
				resultFig = al.get(i).getResult();
			}
		}

		// コンボボックス
		Vector<String> combodata = new Vector<String>();
		Set<String> set = new HashSet<>();
		for (int i = 0; i < query_result.size(); i++) {
			set.add("day: "
					+ query_result.get(i).getResultDay() + "\t author: " + query_result.get(i).getResultAuthor());
			author = query_result.get(i).getResultAuthor();
			day = query_result.get(i).getResultDay();
		}
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			combodata.add(it.next());
		}

		DefaultComboBoxModel model = new DefaultComboBoxModel(combodata);
		combo = new JComboBox(model);
		combo.setPreferredSize(new Dimension(1000, 20));
		comboPanel = new JPanel();
		comboPanel.setPreferredSize(new Dimension(1200, 30));
		comboPanel.add(combo);



		//コンボボックスアクション(処理)
		combo.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				text_old_HTML.setText(""); //表示を初期化
				text_new_HTML.setText(""); //表示を初期化
				String str = combo.getSelectedItem().toString();
				String s1 = "day: ";
				String s2 = "\t author: ";
				//day,authorがcurretに出てくるべき情報
				day = str.substring(s1.length(), str.indexOf(s2));
				author = str.substring(str.indexOf(s2) + s2.length(), str.length());
			}
		});




		// 現在登録されている解答
		latest_of_expected_result_Label = new JLabel("latest of expected result");
		text_old_HTML = new JTextArea();
		scrollpane_old = new JScrollPane(text_old_HTML); // スクロールパネル(解答)
		scrollpane_old.setPreferredSize(new Dimension(450, 400));
		scrollpane_old.setRowHeaderView(new LineNumberView(text_old_HTML)); //行番号表示

		leftPanel = new JPanel();
		leftPanel.setPreferredSize(new Dimension(460, 430));
		leftPanel.add(latest_of_expected_result_Label);
		leftPanel.add(scrollpane_old);



		// 過去の解答
		past_the_expected_value_Label = new JLabel("past the expected result");
		text_new_HTML = new JTextArea();
		scrollpane_new = new JScrollPane(text_new_HTML); // スクロールパネル(実行結果)
		scrollpane_new.setPreferredSize(new Dimension(450, 400));
		scrollpane_new.setRowHeaderView(new LineNumberView(text_new_HTML));

		rightPanel = new JPanel();
		rightPanel.setPreferredSize(new Dimension(460, 430));

		//×▲の時
		if(resultFig.equals("×") || resultFig.equals("▲")){
			combo.setVisible(false);
			actual_result_Label = new JLabel("actual result");
			rightPanel.add(actual_result_Label);
			rightPanel.add(scrollpane_new);
		}else {
			//◯△の時
			rightPanel.add(past_the_expected_value_Label);
			rightPanel.add(scrollpane_new);		
		}


		//差分(diff)
		text_diff = new JTextArea();
		text_diff.setEditable(false);
		text_diff.setForeground(Color.RED);
		text_diff.setLineWrap(false); // まず、テキストエリアの文字列折り返しを無効
		text_diff.setEditable(false);// 編集不可能
		scrollpane_diff = new JScrollPane(text_diff);
		scrollpane_diff.setPreferredSize(new Dimension(1100, 150));
		scrollpane_diff
		.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);// 垂直方向
		scrollpane_diff
		.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);// 水平方向禁止
		diff_Label = new JLabel("diff");
		diffPanel = new JPanel();
		diffPanel.setPreferredSize(new Dimension(1100, 180));
		diffPanel.add(diff_Label);
		diffPanel.add(scrollpane_diff);


		File file = new File(GlobalEnv.resultDir_Path + GlobalEnv.OS_FS
				+ queryFileNameStr);
		rootName = queryFileNameStr;
		DefaultMutableTreeNode top = new DefaultMutableTreeNode(rootName);
		createNodes(top, file);
		final JTree tree = new JTree(top);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
				if(!node.isLeaf()){
//					diffButton.setEnabled(false);
					return;
				} else {

//					diffButton.setEnabled(true);
	
					
					String resultFilePath = GlobalEnv.resultDir_Path;
					String outFilePath = GlobalEnv.outDir_Path;
					String lastPath = "";
					for (int i = 0; i < e.getPath().getPathCount(); i++) {
						resultFilePath += GlobalEnv.OS_FS
								+ e.getPath().getPathComponent(i);
						outFilePath += GlobalEnv.OS_FS
								+ e.getPath().getPathComponent(i);
						if (i == e.getPath().getPathCount() - 1) {
							lastPath = e.getPath().getPathComponent(i).toString();
						}
					}
					//実行結果を表示している
					String newResultStr = Common.readFile(outFilePath);
					text_new_HTML.setText(newResultStr); //実行結果を表示
					text_new_HTML.setCaretPosition( 0 ); //スクロールバーの位置を一番上に
					text_new_HTML.setLineWrap(false); // まず、テキストエリアの文字列折り返しを無効
					text_new_HTML.setEditable(false); // 編集不可にしている

					//情報(author,day,queryID)をつかってDBに接続する
					Database.connect();
					String sql1 = "select a_contents, q_id, f_name from result where q_id = "+queryID+" AND a_author = '"+author+"' AND a_day = '"+day+"' AND f_name like '%"+lastPath+"%';";
					System.out.println("sql1 = " + sql1);
					ResultSet rs = Database.select(sql1);
					try {
						while(rs.next()){
							String result_contents  = rs.getString("a_contents");
							text_old_HTML.setText(result_contents); //解答を表示
							text_old_HTML.setCaretPosition( 0 ); //スクロールバーの位置を一番上に
							text_old_HTML.setLineWrap(false); // まず、テキストエリアの文字列折り返しを無効
							text_old_HTML.setEditable(false); // 編集不可にしている
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					}

					//diff表示
					text_diff.setText(fileDiffMap.get(lastPath).getDiff());
					text_diff.setCaretPosition( 0 ); //スクロールバーの位置を一番上に
					text_diff.setLineWrap(false); // まず、テキストエリアの文字列折り返しを無効
					text_diff.setEditable(false); // 編集不可にしている
					//ハイライト表示
					setHighlight(text_old_HTML, fileDiffMap.get(lastPath)
							.getDiff_lines_old());
					setHighlight(text_new_HTML, fileDiffMap.get(lastPath)
							.getDiff_lines_new());
					
					//diffボタン
//					diffButton.addActionListener(new ActionListener() {
//						@Override
//						public void actionPerformed(ActionEvent e) {
//							System.out.println("ファイル作りたい！笑");
//							System.out.println("ぢりたい！笑");
//							//			Common.createFile(lastPath, result_contents);
//						}
//					});

				}
			}
		});

		//終了ボタン
		exitButton = new JButton("Exit");
		//終了ボタンアクション
		exitButton.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		//期待結果追加ボタン(replace)
		replaceButton = new JButton("replace");
		//期待結果追加ボタン(replace)アクション
		replaceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String author = "";
				if (resultFig.equals("◯") || resultFig.equals("△")) {
					String str = combo.getSelectedItem().toString();
					String s2 = "\t author: ";
					//day,authorがcurrentに出てくるべき情報
					author = str.substring(str.indexOf(s2) + s2.length(), str.length());				
					} else if (resultFig.equals("×") || resultFig.equals("▲")) {
						author = GlobalEnv.user_Name; // UserName
					}
				boolean option = sstest.Common.Dialog.YesNoDialog("Are you sure you want to replace expected result?");
				if (option == true){
				TestResult_replaceResult.replace(queryID, author, day, resultFig);
				dispose();
				}else{
					System.out.println("don't replace");
				};
				// TestResult_replaceResult.replace(queryID, author, html);
				// int qid = query_result.get(combo.getSelectedIndex())
				// .getQueryId();
				// String author = Preference.username; // UserName
				// String html = query_result.get(combo.getSelectedIndex())
				// .getResultContents();
				// TestResult_replaceResult.replace(qid, author, html);
				// Dialog.NormalDialog("Resist!");
			}
		});



		//出力一覧
		outputListPanel = new JPanel(); //出力一覧
		outputListPanel.setPreferredSize(new Dimension(200, 440));
		output_list_Label = new JLabel("Output List");//出力一覧ラベル
		JScrollPane treeView = new JScrollPane(tree);
		treeView.setPreferredSize(new Dimension(190, 410));

		outputListPanel.add(output_list_Label);
		outputListPanel.add(treeView);

		System.out.println("Fuck it!!!! " + num);
		setTitle("Details - " + al.get(1).getFileTitle()); // タイトル名

		mainPanel = new JPanel();
		if(resultFig.equals("◯") || resultFig.equals("△")){
			mainPanel.add(comboPanel);			
		}
		mainPanel.add(outputListPanel);
		mainPanel.add(leftPanel);
		mainPanel.add(rightPanel);
		mainPanel.add(diffPanel);
		ButtonPanel = new JPanel();
//		if(resultFig.equals("◯") || resultFig.equals("△")){
//			diffButton = new JButton("diff");
//			ButtonPanel.add(diffButton);
//		}
		ButtonPanel.add(replaceButton);
		ButtonPanel.add(exitButton);
		mainPanel.add(ButtonPanel);


		// field
		// comboBox_Panel = new JPanel();
		// past_result_Detail_Panel = new JPanel();
		// current_result_Detail_Panel = new JPanel();
		// result_Detail_Panel =new JPanel();

		Container contentPane = getContentPane();
		contentPane.add(mainPanel, BorderLayout.CENTER);

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

	// ハイライト表示
	/**
	 * JTextAreaにハイライトを表示する
	 * 
	 * @param jtc
	 *            JTextArea
	 * @param diff_lines
	 *            行
	 */
	public static void setHighlight(JTextComponent jtc, ArrayList<Integer> el) {
		ArrayList<Integer> errorArray = new ArrayList<Integer>(el);
		Highlighter.HighlightPainter color = new DefaultHighlighter.DefaultHighlightPainter(
				Color.PINK);// 色
		try {
			Highlighter hilite = jtc.getHighlighter();
			hilite.removeAllHighlights();
			javax.swing.text.Document doc = jtc.getDocument();
			String text = doc.getText(0, doc.getLength());
			String[] strs = text.split("\n"); // テキストを1行ずつ分割
			// 行ごとにハイライト表示
			if (errorArray.size() > 0) {
				int j = 0;
				int pos = 0;
				for (int i = 0; i < strs.length; i++) {
					if ((i + 1) == errorArray.get(j)) {
						// System.out.println(""+(i+1)+" "+j);
						hilite.addHighlight(pos, pos + strs[i].length(), color);
						if ((++j) >= errorArray.size())
							break;
					}
					pos += strs[i].length() + 1;
				}
			} else {
				System.err.println("Warning: errorArray.size() = 0");
			}

		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	// 行番号表示
	public static class LineNumberView extends JComponent {
		private static final int MARGIN = 5;
		private final JTextArea textArea;
		private final FontMetrics fontMetrics;
		private final int fontAscent;
		private final int fontHeight;
		private final int fontDescent;
		private final int fontLeading;
		public LineNumberView(JTextArea textArea) {
			this.textArea = textArea;
			Font font = textArea.getFont();
			fontMetrics = getFontMetrics(font);
			fontHeight = fontMetrics.getHeight();
			fontAscent = fontMetrics.getAscent();
			fontDescent = fontMetrics.getDescent();
			fontLeading = fontMetrics.getLeading();
			//			topInset = textArea.getInsets().top;
			textArea.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void insertUpdate(DocumentEvent e) {
					repaint();
				}

				@Override
				public void removeUpdate(DocumentEvent e) {
					repaint();
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
				}
			});
			textArea.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent e) {
					revalidate();
					repaint();
				}
			});
			Insets i = textArea.getInsets();
			setBorder(BorderFactory.createCompoundBorder(BorderFactory
					.createMatteBorder(0, 0, 0, 1, Color.GRAY), BorderFactory
					.createEmptyBorder(i.top, MARGIN, i.bottom, MARGIN - 1)));
			setOpaque(true);
			setBackground(Color.WHITE);
			setFont(font);
		}

		private int getComponentWidth() {
			Document doc = textArea.getDocument();
			Element root = doc.getDefaultRootElement();
			int lineCount = root.getElementIndex(doc.getLength());
			int maxDigits = Math.max(3, String.valueOf(lineCount).length());
			Insets i = getBorder().getBorderInsets(this);
			return maxDigits * fontMetrics.stringWidth("0") + i.left + i.right;
			// return 48;
		}

		private int getLineAtPoint(int y) {
			Element root = textArea.getDocument().getDefaultRootElement();
			int pos = textArea.viewToModel(new Point(0, y));
			return root.getElementIndex(pos);
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(getComponentWidth(), textArea.getHeight());
		}

		@Override
		public void paintComponent(Graphics g) {
			g.setColor(getBackground());
			Rectangle clip = g.getClipBounds();
			g.fillRect(clip.x, clip.y, clip.width, clip.height);

			g.setColor(getForeground());
			int base = clip.y;
			int start = getLineAtPoint(base);
			int end = getLineAtPoint(base + clip.height);
			int y = start * fontHeight;
			int rmg = getBorder().getBorderInsets(this).right;
			for (int i = start; i <= end; i++) {
				String text = String.valueOf(i + 1);
				int x = getComponentWidth() - rmg
						- fontMetrics.stringWidth(text);
				y += fontAscent;
				g.drawString(text, x, y);
				y += fontDescent + fontLeading;
			}
		}
	}

}
