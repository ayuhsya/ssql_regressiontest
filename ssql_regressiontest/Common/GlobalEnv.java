package ssql_regressiontest.Common;

import javax.swing.JTextField;


public class GlobalEnv {

	/* [重要] getPropertyメソッドによって、システムプロパティの値(OS、ファイル区切り文字、ホーム、言語など)を取得 */
	/* システムプロパティ値一覧の取得: System.getProperties().list(System.out); */
	// ログインユーザー名
	public final static String LOGIN_USER = System.getProperty("user.name");
	// ユーザのホームディレクトリ
	public final static String USER_HOME = System.getProperty("user.home");
	// Javaプロジェクトのカレントディレクトリ
	public final static String USER_DIR = System.getProperty("user.dir");
	// OSの名前("Mac OS X" 等)
	public final static String OS = System.getProperty("os.name");
	// OSごとの改行コード(Windows:"\r\n",Mac:"\r",UNIX:"\n"等)
	public final static String OS_LS = System.getProperty("line.separator");
	// OSごとのファイル区切り文字(Windows:"\" , MacとLinux:"/"等)
	public final static String OS_FS = System.getProperty("file.separator");
	// OSごとのパス区切り文字(Windows";" MacとLinux":"等)
	public final static String OS_PS = System.getProperty("path.separator");
	// 実行ファイルのパスを全部つなげて表示
	public final static String EXE_FILE_PATHS = System.getProperty("java.class.path");
	// 実行ファイルのパス(実行jarファイル等がどこにあるか)を取得 (※注意:相対パスで返ってくる場合あり)
	public final static String EXE_FILE_PATH = getExeFilePath();

	public final String USER_LANGUAGE = System.getProperty("user.language");
	//ユーザの言語(日本語:ja) 日本語・英語切り替え機能を付けるときに使用？
	public final String USER_COUNTRY = System.getProperty("user.country");
	//ユーザの国名(日本:JP) 日本語・英語切り替え機能を付けるときに使用？

	//設定画面関係(Preference/Config)
	public static boolean configFlag = false;
	public static final String CONFIG_FILE_NAME = EXE_FILE_PATH + GlobalEnv.OS_FS + ".config";
	public static JTextField username_Field;
	public static String ssql_Path = "";
	public static String libs_Path = "";
	public static String outDir_Path = "";
	public static String resultDir_Path = "";
	public static int result_Num = 5;
	public static String user_Name = "";
	public static String sqlite_db_path = "";
	//	static JTextField ssqlPathField = new JTextField();
	//	static JTextField outDirPathField = new JTextField();
	//	static JTextField resultDirPathField = new JTextField();
	//	static JTextField resultNumberField = new JTextField();
	//	static JTextField jarPathField = new JTextField();

	public final static boolean isWindows = OS.contains("Windows");
	public final static boolean isMac = OS.contains("Mac");
	public final static boolean isLinux = OS.contains("Linux");

	public final static String CHARACTER_CODE_FOR_WRITER = "UTF-8";     //ツールで使用する文字コード (PrintWriter)


	public final static String SSQL_DRIVER = "sqlite";
	public final static String SSQL_DB = Functions.getWorkingDir() + "/db/test_data.db";
	public final static String SSQL_EXTENSION = ".ssql";
	
	
	//getExeFilePath
	private static String getExeFilePath() {
		String p = EXE_FILE_PATHS;
		if (p.contains(":")) {		//ビルドバスの追加を行うと参照ライブラリ内のファイルのパスも付け加えてしまう仕様らしいので、:移行カット
			p = p.substring(0, p.indexOf(":"));
		}
		if (p.endsWith(".jar")) { 	//jarファイルを実行した場合（Eclipseから起動した場合は入らない）
			p = p.substring(0, p.lastIndexOf(GlobalEnv.OS_FS));
		}
		return p;
	}


}
