package sstest.Common;

import javax.swing.JOptionPane;

public class Dialog {
	
	//コンストラクタ
	public Dialog() {
		
	}
	
	//標準ダイアログを表示
	public static void NormalDialog(String message){
		JOptionPane.showMessageDialog(null, message);
	}
	
	//エラーダイアログを表示
	public static void ErrorDialog(String errMessage){
		JOptionPane.showMessageDialog(null, errMessage, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	//YES/NOダイアログを表示
	public static boolean YesNoDialog(String message) {
	    int option = JOptionPane.showConfirmDialog(
	    		null, message, "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if(option == JOptionPane.YES_OPTION)	return true;
		return false;
	}
	
}
