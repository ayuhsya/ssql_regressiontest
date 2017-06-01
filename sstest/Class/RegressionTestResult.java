package sstest.Class;

import java.util.ArrayList;

public class RegressionTestResult {
	// test case id
	private int query_id;
	private String queryFileName;
	private String outFileName;
	private String fileTitle;
	private String result;
	private String diff;
	private ArrayList<Integer> diff_lines_old;
	private ArrayList<Integer> diff_lines_new;
	private String query_tag;
	private String query_output;

	public RegressionTestResult(int num, String queryFileName, String outFileName, String fileTitle, String result, String diff, 
			ArrayList<Integer> diff_lines_old, ArrayList<Integer> diff_lines_new,String query_tag,String query_output) {
		this.query_id = num;
		this.queryFileName = queryFileName;
		this.outFileName = outFileName;
		this.fileTitle = fileTitle;
		this.result = result;
		this.diff = diff;
		this.diff_lines_old = new ArrayList<Integer>(diff_lines_old);
		this.diff_lines_new = new ArrayList<Integer>(diff_lines_new);
		this.query_tag = query_tag;
		this.query_output = query_output;
	}

	public void setNum(int num) {
		this.query_id = num;
	}
	public int getQueryID() {
		return query_id;
	}
	public void setQueryFileName(String fileName) {
		this.queryFileName = fileName;
	}
	public void setOutFileName(String fileName) {
		this.outFileName = fileName;
	}
	public String getQueryFileName() {
		return queryFileName;
	}
	public String getOutFileName() {
		return outFileName;
	}
	public void setFileTitle(String fileTitle) {
		this.fileTitle = fileTitle;
	}
	public String getFileTitle() {
		return fileTitle;
	}
	
	public void setResult(String result) {
		this.result = result;
	}
	public String getResult() {
		return result;
	}
	public void setDiff(String diff) {
		this.diff = diff;
	}
	public String getDiff() {
		return diff;
	}
	public void setDiff_lines_old(ArrayList<Integer> diff_lines_old) {
		this.diff_lines_old = new ArrayList<Integer>(diff_lines_old);
	}
	public ArrayList<Integer> getDiff_lines_old() {
		return diff_lines_old;
	}
	public void setDiff_lines_new(ArrayList<Integer> diff_lines_new) {
		this.diff_lines_new = new ArrayList<Integer>(diff_lines_new);
	}
	public ArrayList<Integer> getDiff_lines_new() {
		return diff_lines_new;
	}
	
	public void setQueryTag(String query_tag) {
		this.query_tag = query_tag;
	}
	public String getQueryTag() {
		return query_tag;
	}
	public void setQueryOutput(String query_output) {
		this.query_output = query_output;
	}
	public String getQueryOutput() {
		return query_output;
	}
}
