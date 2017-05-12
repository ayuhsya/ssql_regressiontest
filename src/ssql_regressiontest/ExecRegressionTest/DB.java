package ssql_regressiontest.ExecRegressionTest;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import ssql_regressiontest.Common.Database;
import ssql_regressiontest.Common.Functions;
import ssql_regressiontest.Common.GlobalEnv;
import ssql_regressiontest.GUI.Test_Run_and_Result;

public class DB {
	
	public static void db() {
		//DB系統かなりやってるここで！
		int query_id = 0, current_query_id = 0;
		String query_name = "", current_query_name = "";
		String query_title = "", current_query_title = "";
		String query_contents = "", current_query_contents = "";
		String result_contents = "", current_result_contents = "";
		String result_name = "", current_result_name = "";

		String workingDir = Functions.getWorkingDir();
		String query_tag = "", current_query_tag = "";
		String query_output = "", current_query_output = "";
		Map<Integer, HashSet<String>> tagMap = new HashMap<Integer, HashSet<String>>();
		HashSet<String> tagSet = new HashSet<String>();

		//ファイル削除(初期化)　
		File F_query_path = new File(workingDir + "/test_files/");
		File F_out_path = new File(GlobalEnv.outDir_Path);
		File F_result_path = new File(GlobalEnv.resultDir_Path);
		//フォルダーの中身の初期化
		readFolder(F_query_path);
		readFolder(F_out_path);
		readFolder(F_result_path);
		//DBに接続
		Database.connect();
		//workingDirはbin以下
		String query_path = workingDir + "/test_files/";
		String result_path = GlobalEnv.resultDir_Path + GlobalEnv.OS_FS;
		try {
			//id, q_title, q_name, f_name, q_contents, a_contents, t_name, o_nameの情報を取得する
			String sql = "SELECT id, q_title, q_name, f_name, q_contents, a_contents, t_name, o_name "
					+ " FROM result r1, query q, tag t, output o ,querytag qt "
					+ " WHERE NOT EXISTS (  SELECT 1  FROM result r2  WHERE r1.q_id = r2.q_id AND r1.f_name = r2.f_name AND r1.a_day < r2.a_day ) "
					+ " AND r1.q_id = q.id AND q.output_id = o.o_id AND q.id = qt.q_id AND t.t_id = qt.t_id "
					+ Test_Run_and_Result.partQuery
					+ " ORDER BY q.id   ";
			System.out.println("sql(DB) = " + sql);
			ResultSet rs = Database.select(sql);
			Exec.count = 1;
			// テーブル照会結果を出力
			//チェックが入っているテストケースの回数分だけ，while文が回る
			int counter = 0;
			while (rs.next()) {
				query_title = rs.getString("q_title");
				query_id = rs.getInt("id");
				query_name = query_path + rs.getString("q_name");
				query_contents = rs.getString("q_contents");
				result_name = result_path + rs.getString("q_name").substring(0, rs.getString("q_name").indexOf(".ssql")) + GlobalEnv.OS_FS + rs.getString("f_name");
				result_contents = rs.getString("a_contents");
				query_tag = rs.getString("t_name");
				query_output = rs.getString("o_name");
				// クエリの作成　AND　解答ファイル群の作成
				if(ssql_regressiontest.Common.Common.createFile(query_name,	query_contents) 
						&& ssql_regressiontest.Common.Common.createFile(result_name,result_contents)){
				}	
				
				
				// 初回
				if(current_query_id == 0){
					current_query_id = query_id;
					current_query_title = query_title;
					current_query_name = query_name;
					current_query_contents = query_contents;
					current_query_tag = query_tag;
					current_query_output = query_output;
					current_result_contents = result_contents;
					current_result_name = result_name;
					tagSet.add(current_query_tag);
					if(rs.isLast()){
						tagMap.put(current_query_id, tagSet);
					}
				} else {
					if(current_query_id == query_id){
						System.out.println(tagSet);
						tagSet.add(current_query_tag);
						current_query_tag = query_tag;
//						current_query_tag+=", " + query_tag;
						if(rs.isLast()){
							tagMap.put(current_query_id, tagSet);
						}
					} else {
						tagSet.add(current_query_tag);
						tagMap.put(current_query_id, tagSet);
						tagSet = new HashSet<String>();
						// 現在のデータの更新
						current_query_id = query_id;
						current_query_title = query_title;
						current_query_name = query_name;
						current_query_contents = query_contents;
						current_query_tag = query_tag;
						current_query_output = query_output;
						current_result_contents = result_contents;
						current_result_name = result_name; 
						if(rs.isLast()){
							tagMap.put(current_query_id, tagSet);
						}
					}
				}
			}
			rs.close();
		} catch (SQLException e) {
			System.err.println("SQL failed.");
			e.printStackTrace();
		}
		//SuprerSQLを実行する回数を決めている(重複しないように実行)
		String distinct_sql = "SELECT distinct id, q_title, q_name, o_name "
				+ " FROM result r1, query q, tag t, output o ,querytag qt "
				+ " WHERE NOT EXISTS (  SELECT 1  FROM result r2  WHERE r1.q_id = r2.q_id AND r1.f_name = r2.f_name AND r1.a_day < r2.a_day ) "
				+ " AND r1.q_id = q.id AND q.output_id = o.o_id "
				+ Test_Run_and_Result.partQuery
				+ " ORDER BY q.id   ";
//		System.out.println("distinct_sql: " + distinct_sql);
		ResultSet distinct_rs = Database.select(distinct_sql);
		try {
			while(distinct_rs.next()){
				query_id = distinct_rs.getInt("id");
				query_name = query_path + distinct_rs.getString("q_name");
				query_title = distinct_rs.getString("q_title");
				query_output = distinct_rs.getString("o_name");
				try {
					//SuperSQL実行
					ArrayList<String> tmpArray = new ArrayList<String>();
					String tag = "";
					Iterator it = tagMap.get(query_id).iterator();
			        while (it.hasNext()) {
			            tmpArray.add((String) it.next());
			        }
			        for(int i = 0; i < tmpArray.size(); i++){
			        	if(i==0){
				        	tag = tmpArray.get(i);
			        	} else {
			        		tag+= ", " + tmpArray.get(i);
			        	}
			        }
					Exec.execRegressionTest(query_id, query_name, query_title, tag, query_output);
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}			
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Database.close();
//		for(int i = 0; i < Test_Run_and_Result.result_all.size(); i++){
//			System.out.println(i+": " + Test_Run_and_Result.result_all.get(i).getQueryID());
//		}
	}


	//getQueryNum
	public static Integer getQueryNumber(){
		int num= 0;

		try {
			Database.connect();
			String sql = "SELECT count(*) FROM query;";
			ResultSet rs = Database.select(sql);
			// テーブル照会結果を出力
			while (rs.next()) {
				num = rs.getInt(1);
			}
			rs.close();
			Database.close();
		} catch (SQLException e) {
			System.err.println("SQL failed.");
			e.printStackTrace();
		}
		System.out.println(num);
		return num;
	}


	//フォルダーの中身初期化
	/**
	 * ディレクトリを再帰的に読む
	 * @param folderPath
	 */
	public static void readFolder( File dir ) {

		File[] files = dir.listFiles();
		if( files == null )
			return;
		for( File file : files ) {
			if( !file.exists() )
				continue;
			else if( file.isDirectory() )
				readFolder( file );
			else if( file.isFile() )
				execute( file );
		}
	}

	/**
	 * ファイルの処理
	 * @param filePath
	 */
	public static void execute( File file ) {
		file.delete();
		//消したフォルダーのパスを表示したい場合
		//System.out.println( file.getPath() ); 
	}

}