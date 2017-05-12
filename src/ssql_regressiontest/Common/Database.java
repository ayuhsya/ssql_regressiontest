package ssql_regressiontest.Common;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ssql_regressiontest.GUI.Preference;

public class Database {
	
	public static String DBMS = "sqlite";
//	public final static String HOST = "localhost";
//	public final static String DB = "";
//	public final static String DB = GlobalEnv.sqlite_db_path;
	public final static String DB = getDBpath();
//	public final static String USER = "";
//	public final static String PW = "";

	private static Connection con = null;
	private static Statement stmt = null;
	
	//コンストラクタ
	public Database() {

	}
	
	//getDBpath
	private static String getDBpath() {
		String p = GlobalEnv.EXE_FILE_PATH;
		if(p.endsWith("bin"))
			p = new File(p).getParent();
		return p + GlobalEnv.OS_FS + "db" + GlobalEnv.OS_FS + "ssql_regressiontest.db";
	}

	//
	// 機能 : データベース接続ステートメントの作成
	//
	public static boolean connect() {
		try {
			switch (DBMS) {
//			case "postgresql":
//				Class.forName("org.postgresql.Driver");
//				con = DriverManager.getConnection("jdbc:postgresql://"+HOST+"/"+DB, USER, PW);
//				stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
//				break;
			case "sqlite":
				System.out.println("sqlite_db_path: "+DB);
				GlobalEnv.sqlite_db_path = DB;
				Preference.sqlite_db_path_Field.setText(DB);
	            Class.forName("org.sqlite.JDBC");
	            con = DriverManager.getConnection("jdbc:sqlite:"+DB);
				stmt = con.createStatement();
				break;
			default:
				break;
			}
		} catch (SQLException e) {
			System.out.println("c1");
			//TODO
			//Display DB setting
			
			
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	//
	// 機能 : データベースへinsert
	//
	public static int insert(String sql) {
		try {
			return stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	//
	// 機能 : データベースのupdate
	//
	public static int update(String sql) {
		return insert(sql);
	}
	
	//
	// 機能 : データベースからdelete
	//
	public static int delete(String sql) {
		return insert(sql);
	}
	
	//
	// 機能 : データベースからselect
	//
	public static ResultSet select(String sql) {
        ResultSet rs = null;
		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			//TODO
			//Display DB setting
			
			
			e.printStackTrace();
		}
		return rs;
	}

	//
	// 機能 : データベース接続を閉じる
	//
	public static boolean close() {
		if (con != null){
			try {
				stmt.close();
				con.close();
			} catch (SQLException e) {
				return false;
			}
		}
		return true;
	}

}
