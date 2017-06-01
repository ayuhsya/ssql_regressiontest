package sstest.Class;

import java.util.ArrayList;

public class SsqlResult {
	// test case id
	private int query_id;
	private String queryFileName;
	private String fileTitle;
	private String result;
	private String query_tag;
	private String query_output;

	public SsqlResult(int num, String queryFileName, String fileTitle, String result, String query_tag,String query_output) {
		this.query_id = num;
		this.queryFileName = queryFileName;
		this.fileTitle = fileTitle;
		this.result = result;
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
	public String getQueryFileName() {
		return queryFileName;
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
