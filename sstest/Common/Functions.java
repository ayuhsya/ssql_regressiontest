package sstest.Common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Functions {
	
	public static String fileToString(File file) throws IOException {
		BufferedReader br = null;
		try {
			// ファイルを読み込むバッファドリーダを作成します。
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					file)));
			// 読み込んだ文字列を保持するストリングバッファを用意します。
			StringBuffer sb = new StringBuffer();
			// ファイルから読み込んだ一文字を保存する変数です。
			int c;
			// ファイルから１文字ずつ読み込み、バッファへ追加します。
			while ((c = br.read()) != -1) {
				sb.append((char) c);
			}
			// バッファの内容を文字列化して返します。
			return sb.toString();
		} finally {
			// リーダを閉じます。
			br.close();
		}
	}
	
    //getWorkingDir
    public static String getWorkingDir(){
        String workingDir = new File(GlobalEnv.EXE_FILE_PATH).getAbsolutePath();     //実行jarファイルの絶対パスを取得
        if(!GlobalEnv.isWindows && workingDir.contains(":")) {            //ビルドバスの追加を行うと参照ライブラリ内のファイルのパスも付け加えてしまう仕様らしいので、:移行カット
            workingDir = workingDir.substring(0, workingDir.indexOf(":"));
        }
        if(workingDir.endsWith(".jar")) {     //jarファイルを実行した場合（Eclipseから起動した場合は入らない）
            workingDir = workingDir.substring(0, workingDir.lastIndexOf(GlobalEnv.OS_FS));
        }
        if(workingDir.endsWith("bin")) {     //※ Eclipse上で実行した場合
            workingDir = workingDir.substring(0, workingDir.lastIndexOf(GlobalEnv.OS_FS));
        }
        return workingDir;
    }
    
    //trim (全角スペースもカット)
    public static String trim(String value) {
        if (value == null || value.length() == 0)
            return value;
        int st = 0;
        int len = value.length();
        char[] val = value.toCharArray();
        while ((st < len) && ((val[st] <= ' ') || (val[st] == '　'))) {
            st++;
        }
        while ((st < len) && ((val[len - 1] <= ' ') || (val[len - 1] == '　'))) {
            len--;
        }
        return ((st > 0) || (len < value.length())) ? value.substring(st, len) : value;
    }
	
	/**フォルダ削除**/
	public static void delete(File f){
		if( f.exists()==false ){
			return ;
		}
		if(f.isFile()){
			f.delete();
		}		
		if(f.isDirectory()){
			File[] files=f.listFiles();
			for(int i=0; i<files.length; i++){
				delete( files[i] );
			}
//			f.delete();
		}
	}
}
