package sstest.ExecRegressionTest;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import sstest.Class.RegressionTestResult;
import sstest.Class.SsqlResult;
import sstest.Common.Common;
import sstest.Common.Database;
import sstest.Common.Dialog;
import sstest.Common.Functions;
import sstest.Common.GlobalEnv;
import sstest.GUI.Test_Run_and_Result;
import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;

public class Exec extends JFrame {
	private static final long serialVersionUID = 1L;
	static int count=1;
	public static int result_count_failed;
	public static int result_count_pass;
	public static int result_count_SSQLsuccess;
	public static int result_count_SSQLfailed;

	static DefaultMutableTreeNode root;
	static String finResult = "", ssqlResult = "", diffResult = "";
	static String diff = "";
	public static ArrayList<Integer> errorLines_old = new ArrayList<Integer>();
	public static ArrayList<Integer> errorLines_new = new ArrayList<Integer>();
	public static ArrayList<SsqlResult> ssqlResultArray = new ArrayList<SsqlResult>();
	public static ArrayList<SsqlResult> ssqlPassResultArray = new ArrayList<SsqlResult>();
	public static ArrayList<SsqlResult> ssqlFailResultArray = new ArrayList<SsqlResult>();
	public static ArrayList<SsqlResult> ssqlSameOutputResultArray = new ArrayList<SsqlResult>();
	public static ArrayList<SsqlResult> ssqlDiffOutputResultArray = new ArrayList<SsqlResult>();
	static JPanel panel = new JPanel();
//	public static String resultDir = "";
	static String NAME = "";
	static int q_id;
	static String q_name;
	static String q_title;
	static String q_tag;
	static String q_output;
	//回帰テスト実行
	public static void execRegressionTest(Integer query_id, String query_name, String query_title, String query_tag, String query_output) throws NoSuchAlgorithmException,
	IOException {
		// デフォルトのパラメータとかを設定
		// 初期化(回帰テストの実行のために環境変数を設定)
		// テストに使用するデータベースの作成？？
		// SQLファイル実行
		// 回帰テストクエリを実行
		// 結果のメッセージを表示
		
		/* 変数の定義 */
		String extension = ".ssql";
		// System.out.println(path);
		
		q_id = query_id;
		q_name = query_name;
		q_title = query_title;
		q_tag = query_tag;
		q_output = query_output;

		// Fileクラスのインスタンスを作成
		File file = new File(query_name);

		// フォルダの中身を配列に入れる
		//		File files[] = file.listFiles();
		String workingDir = Functions.getWorkingDir();
		if (!file.isHidden() && file.toString().endsWith(extension)) {
			String queryfilename = file.getName();
			NAME = queryfilename.substring(0,queryfilename.length() - extension.length());
			System.out.println("■ " + (count++) + "：" + queryfilename);
			finResult = "";
			diff = "";

			/* 変数の定義 */
			String classPath = Common.getClassPath();
			String driver = "sqlite";
			//			String workingDir = Functions.getWorkingDir();
			String db = workingDir + GlobalEnv.OS_FS +"db"+GlobalEnv.OS_FS +"test_data.db";
			System.out.println("-----------------------------------------------");


			/*****JS/CSS****実行前にフォルダを作る***********/
//			String outDir = createOutDirFolder();  //フォルダー作成している(出力するフォルダ)
//			if (outDir.equals("")) {
//				return; //フォルダーが作成されなかった場合
//			}
			
			/* SSQL実行 */
			SSQL_exec.errLog2 = "";
			System.out.println("classPath: " + classPath);

			String qfname = query_name.substring(query_name.lastIndexOf(GlobalEnv.OS_FS), query_name.indexOf(".ssql"));
			File f = new File(GlobalEnv.outDir_Path + qfname);
			if (!f.exists()) f.mkdirs();
			
			//SSQL実行成功
			if (SSQL_exec.execSuperSQL(file.toString(), classPath, driver,
					db, null, null, GlobalEnv.outDir_Path)) {
				System.out.println("SSQL実行 成功");
				ssqlResult = "◯";
				finResult = "◯";

				// ****JS/CSS****そのフォルダーの中の名前を取ってきている*****
				//				String path = "C:\\filelist";
				//				File dir = new File(GlobalEnv.outDir_Path); 

				ListPath(f);  //ディレクトリを再帰的に読む

				root = new DefaultMutableTreeNode(queryfilename);
				DefaultTreeModel model = new DefaultTreeModel(root);
				JTree tree = new JTree(model);

				JScrollPane scrollPane = new JScrollPane();
				scrollPane.getViewport().setView(tree);
				scrollPane.setPreferredSize(new Dimension(180, 120));
				panel.add(scrollPane);
			} else {
				ssqlResult = "×";
				//SSQL実行失敗
				diff = SSQL_exec.errLog2;
				NAME += "error";
				String a_error = GlobalEnv.resultDir_Path + GlobalEnv.OS_FS + qfname + GlobalEnv.OS_FS + NAME + ".log"; // test_results内のHTMLファイルの名前 // (絶対パス)
				String b_error = GlobalEnv.outDir_Path + GlobalEnv.OS_FS + qfname + GlobalEnv.OS_FS + NAME + ".log"; // test_files_outdirへ出力されたHTMLファイルの名前// (絶対パス)
				Common.createFile(b_error, SSQL_exec.errLog2);

				// 1. diff
				diff(a_error, b_error);
				store_and_display_Result(query_id, queryfilename, b_error, query_title ,finResult, diff, errorLines_old, errorLines_new, query_tag, query_output);

				DefaultTreeModel model = new DefaultTreeModel(root);
				JTree tree = new JTree(model);
				
				JScrollPane scrollPane = new JScrollPane();
				scrollPane.getViewport().setView(tree);
				scrollPane.setPreferredSize(new Dimension(180, 120));
				panel.add(scrollPane);
			}
			
			DefaultTreeModel model = new DefaultTreeModel(root);
			JTree tree = new JTree(model);
			
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.getViewport().setView(tree);
			scrollPane.setPreferredSize(new Dimension(180, 120));
			panel.add(scrollPane);
			
			if(finResult.equals("◯")){
				Exec.result_count_pass++;
			} else if (finResult.equals("×")) {
				Exec.result_count_failed++;
			} else if (finResult.equals("△")) {
				Exec.result_count_SSQLsuccess++;
			} else if (finResult.equals("▲")) {
				Exec.result_count_SSQLfailed++;
			}
			
			/**フォルダー削除**/
//			Functions.delete(new File(GlobalEnv.outDir_Path));
			String fn_tmp = query_name.substring(query_name.lastIndexOf(GlobalEnv.OS_FS) + 1, query_name.length());
			storeSsqlResult(query_id, fn_tmp, query_title, finResult, query_tag, query_output);
//			/* 結果を格納・テーブルへ表示 */
//			store_and_display_Result(query_id, queryfilename, outfilename, query_title ,result, diff, errorLines_old, errorLines_new, query_tag, query_output);
			sstest.GUI.Test_Run_and_Result.insertTableData("" + query_id, queryfilename, query_title, finResult, query_tag, query_output);
			System.out.println("================================================");
			System.out.println("");
		}
	}

	
	
	/*** 出力先フォルダー作成*****/
	static String createOutDirFolder() {
		String outDir = Functions.getWorkingDir() + GlobalEnv.OS_FS + "test_files_outdir" + GlobalEnv.OS_FS + "outDirFolder" + GlobalEnv.OS_FS;
		// TODO Auto-generated method stub
		File newfile = new File(outDir);
		if (newfile.mkdir()){
//			System.out.println("ディレクトリの作成に成功しました");
			return outDir;
		}else{
//			System.out.println("ディレクトリの作成に失敗しました");
			return ""; 
		}
	}

	/**
	 * JS/CSS****ディレクトリを再帰的に読む
	 * @param folderPath
	 */
	public static void ListPath(File dir) {
		File[] fs = dir.listFiles();
		for (File f : fs) {
			if (f.isDirectory()) {
				ListPath(f);
			} else if (f.isFile()) {
				if (f != null){
					String file1 = f.toString();  //ファイル名
//						System.out.println("生成したファイル: " + f.getCanonicalPath());
					String a = GlobalEnv.resultDir_Path + file1.substring(GlobalEnv.outDir_Path.length(), file1.length());
//						System.out.println("正解のファイル: " + a);
					// // (絶対パス)
					String b = file1;
					/* 1. diff */
//						System.out.println("1. diff: a= " + a + ", b=" + b);
					diff(a, b);
					/* 結果を格納・テーブルへ表示 */
					store_and_display_Result(q_id, q_name, b, q_title ,finResult, diff, errorLines_old, errorLines_new, q_tag, q_output);
				}
			}
		}
	}
	
	// public static void execRegressionTest() throws NoSuchAlgorithmException,
	// IOException {
	// //デフォルトのパラメータとかを設定
	// //初期化(回帰テストの実行のために環境変数を設定)
	// //テストに使用するデータベースの作成？？
	// //SQLファイル実行
	// //回帰テストクエリを実行
	// //結果のメッセージを表示
	//
	// /* 変数の定義 */
	// String path = Functions.getWorkingDir()+"/test_files";
	// String outDir = Functions.getWorkingDir()+"/test_files_outdir/";
	// String resultDir = Functions.getWorkingDir()+"/test_results/";
	// String extension = ".ssql";
	// //System.out.println(path);
	//
	// //Fileクラスのインスタンスを作成
	// File file = new File(path);
	//
	// //フォルダの中身を配列に入れる
	// File files[] = file.listFiles();
	//
	// // List<String> ssqlall = new ArrayList<String>();
	//
	// /* 1ファイルずつ実行 */
	// count = 1;
	//
	//
	// for (int i = 0; i < files.length; i++) {
	// System.out.println("counta = " + (count-1));
	//
	// if (!files[i].isHidden() && files[i].toString().endsWith(extension)) {
	// String filenm = files[i].getName();
	// String NAME = filenm.substring(0, filenm.length() - extension.length());
	// System.out.println("■ "+(count++) + "：" + filenm);
	// result = "";
	// diff = "";
	//
	// //クエリの内容を表示
	// System.out.println(Functions.fileToString(files[i]));
	//
	// /* 変数の定義 */
	// String classPath = Common.getClassPath();
	// String driver = "sqlite";
	// String workingDir = Functions.getWorkingDir();
	// String db = workingDir + "/db/test_data.db";
	// System.out.println("-----------------------------------------------");
	//
	//
	// /* SSQL実行 */
	// SSQL_exec.errLog2 = "";
	// if (SSQL_exec.execSuperSQL(files[i].toString(), classPath, driver, db,
	// null, null, outDir)) {
	// System.out.println("SSQL実行 成功");
	//
	// String a = resultDir + NAME +".html"; //test_results内のHTMLファイルの名前 (絶対パス)
	// String b = outDir + NAME +".html"; //test_files_outdirへ出力されたHTMLファイルの名前
	// (絶対パス)
	// //System.out.println("a = "+a+"\nb = "+b);
	// root = new DefaultMutableTreeNode(filenm);
	//
	// /* 1. diff */
	// diff(a, b);
	// DefaultTreeModel model = new DefaultTreeModel(root);
	//
	// JTree tree = new JTree(model);
	//
	// JScrollPane scrollPane = new JScrollPane();
	// scrollPane.getViewport().setView(tree);
	// scrollPane.setPreferredSize(new Dimension(180, 120));
	// panel.add(scrollPane);
	//
	// /* 2. diff rendered HTML */
	// diff_renderedHTML(a, b);
	//
	// } else {
	// result = "× SSQL実行 失敗";
	// System.out.println(result);
	// diff = SSQL_exec.errLog2;
	// }
	//
	// /* 結果を格納・テーブルへ表示 */
	// store_and_display_Result(count-1, filenm, result, diff, errorLines_old,
	// errorLines_new);
	// System.out.println("================================================");
	// System.out.println("");
	// }
	// }
	// }
	
	// diff: aとbのdiffをとり、違いがある場合はその箇所を表示させる
	public static void diff(String a, String b) {
		diff = "";
		System.out.println("\n<diff>");

		try {
			List<String> x = Files.readAllLines(new File(a).toPath(),
					Charset.defaultCharset());
			List<String> y = Files.readAllLines(new File(b).toPath(),
					Charset.defaultCharset());
//			System.out.println("a=" + x);
//			System.out.println("b=" + y);
			Patch patch = DiffUtils.diff(x, y);
			//Diffのサイズによって場合分けする結果の判定部分
			if (ssqlResult.equals("◯")) {
				if (patch.getDeltas().size() == 0) {
				} else {
					// 一個でもdiffがあれば，結果は×
					finResult = "×";
				}
			} else if (ssqlResult.equals("×")) {
				if (patch.getDeltas().size() == 0) {
					finResult = "△";
				} else {
					// 一個でもdiffがあれば，結果は×
					finResult = "▲";
				}
			}

			int i = 1;
			errorLines_old.clear();
			errorLines_new.clear();
			for (Delta delta : patch.getDeltas()) {
				DefaultMutableTreeNode num = new DefaultMutableTreeNode(i);
				String temp = "", temp2 = "";

				// 変更前diff
				int errorLine = delta.getOriginal().getPosition() + 1;
				temp += String.format("[変更前(%d)行目]", errorLine);
				diff += ((i++) + ":") + "\n";
				diff += (String.format("[変更前(%d)行目]", errorLine));
//				System.out.println("countb = " + (count));
				for (Object line : delta.getOriginal().getLines()) {
					diff += (line) + "\n";
					temp += line;
					errorLines_old.add(errorLine++);
				}

				// 変更後diff
				errorLine = delta.getRevised().getPosition() + 1;
				temp2 += String.format("[変更後(%d)行目]", errorLine);
				diff += ("	↓") + "\n";
				diff += (String.format("[変更後(%d)行目]", errorLine));
				for (Object line : delta.getRevised().getLines()) {
					temp2 += line;
					diff += (line) + "\n";
					errorLines_new.add(errorLine++);
				}

				diff += "\n";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 結果を格納・テーブルへ表示
	private static void store_and_display_Result
	(int c, String qfn, String ofn, String ft, 
			String result, String diff, ArrayList<Integer> diff_lines_old, ArrayList<Integer> diff_lines_new, String query_tag, String query_output) {
		RegressionTestResult rtr = new RegressionTestResult(c, qfn, ofn, ft, result, diff, diff_lines_old, diff_lines_new,query_tag,query_output);
		Test_Run_and_Result.result_all.add(rtr);
//		if (result.startsWith("◯")){
//		Test_Run_and_Result.result_success.add(rtr);
//		System.out.println("◯");
//	}
//	else if(result.startsWith("×")){
//		Test_Run_and_Result.result_failure.add(rtr);
//		System.out.println("×");
//	}
//	else if(result.startsWith("△")){
//		Test_Run_and_Result.result_ssqlexec_success.add(rtr);
//		System.out.println("△");
//	}
//	else if(result.startsWith("▲")){
//		Test_Run_and_Result.result_sselexec_failure.add(rtr);
//		System.out.println("▲");
//	}

	}
	
	private static void storeSsqlResult(int c, String qfn, String ft,
			String result, String query_tag, String query_output) {
		SsqlResult ssqlResult = new SsqlResult(c, qfn, ft, result, query_tag,
				query_output);
		ssqlResultArray.add(ssqlResult);
		if (result.startsWith("◯")) {
			ssqlPassResultArray.add(ssqlResult);
		} else if (result.startsWith("×")) {
			ssqlFailResultArray.add(ssqlResult);
		} else if (result.startsWith("△")) {
			ssqlSameOutputResultArray.add(ssqlResult);
		} else if (result.startsWith("▲")) {
			ssqlDiffOutputResultArray.add(ssqlResult);
		}
	}
}
