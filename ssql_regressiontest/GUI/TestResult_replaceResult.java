package ssql_regressiontest.GUI;

import java.io.File;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import ssql_regressiontest.Common.Common;
import ssql_regressiontest.Common.Database;
import ssql_regressiontest.Common.GlobalEnv;
import ssql_regressiontest.ExecRegressionTest.SSQL_exec;

public class TestResult_replaceResult {

	private static int NUM_OF_RESULTS = 3;	//保持する結果の数
	private static int x = 0;	
	private static Timestamp time;

	//	public static void main(String[] args) {
	//		//個数を変えるとその数だけ保持できるようになる。ここを１とかにすると１つだけ解答保持。個数の切り替えを各種設定とかから行えるようにする。
	//		//配布するバージョンではこれはつけなくて遠山研内での使用のときだけ！
	//		set_NUM_OF_RESULTS(5);
	//		//ここで置き換えている第一引数はテストクエリ番号(q_id)、第二引数は作成者(a_author)、第三引数は内容(a_contents)
	//		replace(7, "rima", "<html>Test2</html>");
	//	}

	//コンストラクタ
	public TestResult_replaceResult() {

	}

	//結果をいくつまで登録するかをセット
	public static boolean set_NUM_OF_RESULTS(int new_NOR) {
		if(new_NOR<1)	return false;
		NUM_OF_RESULTS = new_NOR;
		return true;
	}

	//結果の置換処理
	public static boolean replace(int qid, String author, String day, String resultFig) {
		ArrayList<String> insertQueryArray = new ArrayList<String>();
		//■ 置換処理の流れ (q_id=1の置換の例)
		//int x =  select count(*) from result where q_id=1;		//現在登録されている結果の数
		//if(x == 3){
		//	//結果が既に3つ登録されている場合
		//	delete from result where q_id=1 and a_id=1;				//一番古い結果を削除
		//	update result set a_id=1 where q_id=1 and a_id=2;		//2を1へ
		//	update result set a_id=2 where q_id=1 and a_id=3;		//3を2へ
		//	insert into result values (1, 3, 〜);					//新しい結果をinsert
		//}else if(x == 2 || x == 1){
		//	//結果数が2 or 1のとき
		//	insert into result values (1, x+1, 〜);					//新しい結果をinsert					
		//}else{
		//	//その他、結果数がおかしい場合
		//}

		try {
			Database.connect();
			x = 0;																		
			//現在登録されている結果の数
			//			System.out.println("query: select count(*) from result where q_id="+qid);
			//			ResultSet rs = Database.select("select count(*) from result where q_id="+qid);


			//現在，登録されている結果の世代？数をカウント！
			//			System.out.println("query: select count(distinct a_id) from result where q_id="+qid);
			ResultSet rs = Database.select("select count(distinct a_id) from result where q_id="+qid);

			while(rs.next())  System.out.println("- count = "+(x = rs.getInt("count")));
			String dirStr = GlobalEnv.outDir_Path + GlobalEnv.OS_FS + "q" + qid;

			time = new Timestamp(System.currentTimeMillis());
			if(x >= NUM_OF_RESULTS){
				//NUM_OF_RESULTS以上の結果が既に登録されている場合
				for(int i=1; i<=(x-NUM_OF_RESULTS+1); i++)
					Database.delete("delete from result where q_id="+qid+" and a_id="+i);									
				for(int i=(x-NUM_OF_RESULTS+2); i<=x; i++)
					Database.update("update result set a_id="+(NUM_OF_RESULTS-x+i-1)+" where q_id="+qid+" and a_id="+i);	//結果をupdate

				if(resultFig.equals("◯") || resultFig.equals("△")){
					String sql = "select a_contents, q_id, f_name from result where q_id = "+qid+" AND a_author = '"+author+"' AND a_day = '"+day+"';";
					ResultSet rs2 = Database.select(sql);
					while(rs2.next()){
						String fName = rs2.getString("f_name");
						String aContents = rs2.getString("a_contents");
						aContents = aContents.replace("'", "''");
						String insert_result = 
								"INSERT INTO result (q_id, a_id, f_name ,a_author, a_day, a_contents) VALUES "
										+ "("+qid+ "," + NUM_OF_RESULTS +","
										+ "'"+Database.getEscapedString(fName)+"',"
										+ "'"+Database.getEscapedString(author)+"', "
										+ "'"+time+"',"
										+ "'"+Database.getEscapedString(aContents)+"');";
						insertQueryArray.add(insert_result);
					}
					for (int i = 0; i < insertQueryArray.size(); i++) {
						Database.insert(insertQueryArray.get(i));
					}

				} else if (resultFig.equals("×") || resultFig.equals("▲")) {
					readFolder(new File(dirStr), dirStr, qid, NUM_OF_RESULTS);
				}
			}else if(x > 0 &&  x < NUM_OF_RESULTS){
				//結果数が 1 〜 NUM_OF_RESULTS-1 のとき
				if(resultFig.equals("◯") || resultFig.equals("△")){
					String sql = "select a_contents, q_id, f_name from result where q_id = "+qid+" AND a_author = '"+author+"' AND a_day = '"+day+"';";
					ResultSet rs2 = Database.select(sql);
					while(rs2.next()){
						String fName = rs2.getString("f_name");
						String aContents = rs2.getString("a_contents");
						aContents = aContents.replace("'", "''");
						Timestamp now = new Timestamp(System.currentTimeMillis());
						String insert_result = 
								"INSERT INTO result (q_id, a_id, f_name ,a_author, a_day, a_contents) VALUES "
										+ "("+qid+ "," + (x+1) +","
										+ "'"+Database.getEscapedString(fName)+"',"
										+ "'"+Database.getEscapedString(author)+"', "
										+ "'"+now+"',"
										+ "'"+Database.getEscapedString(aContents)+"');";
						insertQueryArray.add(insert_result);
					}
					for (int i = 0; i < insertQueryArray.size(); i++) {
						Database.insert(insertQueryArray.get(i));
					}

				} else if (resultFig.equals("×") || resultFig.equals("▲")) {
					readFolder(new File(dirStr), dirStr, qid, x+1);
				}
			}else{
				//結果数がおかしい(x<1)場合
				//TODO エラー処理
				return false;
			}
			//			rs.close();

			//			Database.close();
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * ディレクトリを再帰的に読む
	 * 
	 * @param folderPath
	 */
	public static void readFolder(File dir, String tmpFileDirName, int num, int n) {
		File[] files = dir.listFiles();
		if (files == null)
			return;
		for (File file : files) {
			if (!file.exists())
				continue;
			else if (file.isDirectory()){
				readFolder(file, tmpFileDirName, num, n);
			}
			else if (file.isFile()){
				execute(file, tmpFileDirName, num, n);
			}
		}
	}

	/**
	 * ファイルの処理
	 * 
	 * @param filePath
	 */
	public static void execute(File file, String tmpFileDirName, int num, int n) {
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
						+ "("+num+ "," + n +","
						+ "'"+Database.getEscapedString(tmpFileName)+"',"
						+ "'"+Database.getEscapedString(GlobalEnv.user_Name)+"', "
						+ "'"+time+"',"
						+ "'"+Database.getEscapedString(result_contents1)+"');";
		System.out.println("クエリ: " + insert_result);
		Database.insert(insert_result);
	}

}
