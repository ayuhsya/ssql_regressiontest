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
	public final static String DB1 = getDBpath1();
	public final static String DB2 = getDBpath2();
//	public final static String USER = "";
//	public final static String PW = "";

	private static Connection con = null;
	private static Statement stmt = null;
	
	//コンストラクタ
	public Database() {

	}
	
	//getDBpath1
	private static String getDBpath1() {
		return getDBdir() + "ssql_regressiontest.db";
	}
	//getDBpath2
	private static String getDBpath2() {
		return getDBdir() + "test_data.db";
	}
	//getDBdir
	private static String getDBdir() {
		String p = GlobalEnv.EXE_FILE_PATH;
		if(p.endsWith("bin"))
			p = new File(p).getParent();
		return p + GlobalEnv.OS_FS + "db" + GlobalEnv.OS_FS;
	}

	//
	// 機能 : データベース接続ステートメントの作成
	//
	public static boolean connect() {
		if(con != null)	close();
		System.out.println("connect() "+(con != null));
		try {
			switch (DBMS) {
//			case "postgresql":
//				Class.forName("org.postgresql.Driver");
//				con = DriverManager.getConnection("jdbc:postgresql://"+HOST+"/"+DB, USER, PW);
//				stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
//				break;
			case "sqlite":
				if(Preference.sqlite_db_path_Field2.getText().isEmpty()){
					System.out.println("sqlite_db_path1: "+DB1);
					System.out.println("sqlite_db_path2: "+DB2);
					GlobalEnv.sqlite_db_path1 = DB1;
					GlobalEnv.sqlite_db_path2 = DB2;
					Preference.sqlite_db_path_Field1.setText(DB1);
					Preference.sqlite_db_path_Field2.setText(DB2);
				}
				
	            Class.forName("org.sqlite.JDBC");
	            con = DriverManager.getConnection("jdbc:sqlite:"+DB1);
				stmt = con.createStatement();
				break;
			default:
				break;
			}
		} catch (SQLException e) {
			System.out.println("c1");
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
		System.out.println("INSERT: "+sql);
		try {
			return stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	//getEscapedString
	public static String getEscapedString(String value) {
		return value.replace("'", "''");
	}
	
	//
	// 機能 : データベースのupdate
	//
	public static int update(String sql) {
		System.out.println("UPDATE: "+sql);
		return insert(sql);
	}
	
	//
	// 機能 : データベースからdelete
	//
	public static int delete(String sql) {
		System.out.println("DELETE: "+sql);
		return insert(sql);
	}
	
	//
	// 機能 : データベースからselect
	//
	public static ResultSet select(String sql) {
		System.out.println("SELECT: "+sql);
        ResultSet rs = null;
		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return rs;
	}

	//
	// 機能 : データベース接続を閉じる
	//
	public static boolean close() {
		System.out.println("close() "+(con != null));
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
