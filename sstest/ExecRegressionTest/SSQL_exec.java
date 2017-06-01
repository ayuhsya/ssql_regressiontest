package sstest.ExecRegressionTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;

import sstest.Common.Common;
import sstest.Common.GlobalEnv;
import sstest.GUI.Preference;

public class SSQL_exec implements Runnable {
	public static String errLog = "";
	public static String errLog2 = "";
	static StringWriter sWriter; // 出力された文字列を受けとるためのオブジェクト
	static PrintWriter pWriter; // 出力された文字列を受けとるためのオブジェクト
	static BufferedReader buffReader; // 標準出力
	static BufferedReader errorBuffReader; // エラー出力

	//SuperSQLの実行
	public static boolean execSuperSQL(String filename, String classPath, String driver, String db, String user, String host, String outDir) {
		try{
			System.out.println("driver="+driver+", db="+db+", outDir="+outDir);
			String result = doExec(new String[]{
					"java",
					"-Dfile.encoding=UTF-8",
					"-classpath", classPath,
					"supersql.FrontEnd",
					//20141210 masato -loggerは実習でのみ"-logger", "on"を配列の引数に追加
					"-f", filename,
					"-driver", driver,
					"-db", db,
					"-d", outDir
			});
			System.out.println("result = "+result);
			if(result.equals("// completed normally //"))    return true;
			else                                             return false;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	//
	//	public static boolean execSuperSQL2(String generateFileName, String query) {
	////		System.out.println("実行中...　flag = " + GlobalEnv.runningFlag);
	////		ssqlExecLogs = "";
	//		Common.createFile(generateFileName, query);
	//		try {
	//			String result = doExec(new String[] { "java",
	//					"-Dfile.encoding=UTF-8", "-classpath", libsClassPath,
	//                    //20141210 masato -loggerは実習でのみ"-logger", "on"を配列の引数に追加
	//					"supersql.FrontEnd", "-f", generateFileName}, null, null);
	//			if (result.equals("// completed normally //")) {
	//				errorStr[0] = "";
	//				errorStr[1] = "";
	////				FrontEnd.tmp = GlobalEnv.textPane.getText();
	//				return true;
	//			} else {
	//				Common.createFile(GlobalEnv.outdirPath + GlobalEnv.OS_FS + ".errorlog.txt", ssqlExecLogs);
	//				return false;
	//			}
	//		} catch (Exception e) {
	//			return false;
	//		}
	//	}
	//	

	/**
	 * 外部コマンドを実行する。 配列形式で渡すためにラップしてる
	 * 
	 * @param command
	 *            実行する外部コマンド
	 * @return String 外部コマンドが標準出力に出力する実行結果
	 * @throws IOException
	 */
	public String doExec(String command, JTextPane resultPane, DefaultStyledDocument document)
			throws IOException {
		return doExec(new String[] { command });
	}

	/**
	 * 外部コマンドを実行する。
	 * 
	 * @param commands
	 *            実行する外部コマンド（空白や引数を渡すための配列形式）
	 * @return String 外部コマンドが標準出力に出力する実行結果
	 * @throws IOException
	 */
	public static String doExec(String[] commands)
			throws IOException {
		// ランタイムオブジェクト取得
		Runtime rt = Runtime.getRuntime();
		String workingDir = "";
		if(!GlobalEnv.ssql_Path.equals("")){
			workingDir = GlobalEnv.ssql_Path;
		} else {
			workingDir = Common.getWorkingDir();
		}
		Process proc = rt.exec(commands, null, new File(workingDir));
		

		// 実行結果の取得用のオブジェクトの作成
		buffReader = new BufferedReader(new InputStreamReader(
				proc.getInputStream()));
		errorBuffReader = new BufferedReader(new InputStreamReader(
				proc.getErrorStream()));
		sWriter = new StringWriter();
		pWriter = new PrintWriter(sWriter);

		String line_end = ""; // 結果の最終行を格納
		try {
			String line = "";
			//			String str = "";
			// 正常ログ
			while ((line = buffReader.readLine()) != null) {
				//				ssqlExecLogs += line + "" + GlobalEnv.OS_LS + "";
				//				if (resultPane != null)
				//					resultArea.append(line + "" + GlobalEnv.OS_LS + "");
				//					document.insertString(document.getLength(), line + "" + GlobalEnv.OS_LS + "", null);
				//					resultPane.setCaretPosition(document.getLength());
				System.out.println(line);
				line_end = line;
			}
			// エラーログ
			while ((line = errorBuffReader.readLine()) != null) {
				//				str += line + "" + GlobalEnv.OS_LS + "";
				//				ssqlExecLogs += line + "" + GlobalEnv.OS_LS + "";
				//				if (resultPane != null){
				//					document.insertString(document.getLength(), line + "" + GlobalEnv.OS_LS + "", null);
				//					resultPane.setCaretPosition(document.getLength());
				System.err.println(line);
				errLog += line+"<br>";
				errLog2 += line+"\n";
				//				}
			}	
			//			Log.ggg(str);
			//			System.out.println(str);

		} catch (Exception e) {
			;
		} finally {
			buffReader.close();
			errorBuffReader.close();
			pWriter.close();
		}
		return line_end; // 結果の最終行をreturn
	}

	/**
	 * コマンドの実行結果を読み出す。
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			while (buffReader.ready()) {
				pWriter.println(buffReader.readLine());
			}
			while (errorBuffReader.ready()) {
				pWriter.println(errorBuffReader.readLine());
			}
		} catch (IOException e) {
			// e.printStackTrace();
		}
	}
}
