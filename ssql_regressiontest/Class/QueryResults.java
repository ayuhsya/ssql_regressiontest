package ssql_regressiontest.Class;

public class QueryResults {
	private String query_title;
	private Integer query_id;
	private Integer result_id;
	private String result_contents;
	private String result_author;
	private String result_day;
//	private String query_tag;
//	private String query_output;


	public QueryResults(String query_title, Integer query_id, Integer result_id, String result_contents, String result_author, String result_day){
		this.query_title = query_title;
		this.query_id = query_id;
		this.result_id = result_id;
		this.result_contents = result_contents;
		this.result_author = result_author;
		this.result_day = result_day;
//		this.query_tag = query_tag;
//		this.query_output = query_output;
	}

	public void setQueryTitle(String query_title){
		this.query_title = query_title;
	}
	public void setQueryId(Integer query_id){
		this.query_id = query_id;
	}
	public void setResultId(Integer result_id){
		this.result_id = result_id;
	}
	public void setQueryContents(String result_contents){
		this.result_contents = result_contents;
	}
	public void setResultAuthor(String result_author){
		this.result_author = result_author;
	}
	public void setResultDay(String result_day){
		this.result_day = result_day;
	}
//	public void setQueryTag(String query_tag) {
//		this.query_tag = query_tag;
//	}
//	public void setQueryOutput(String query_output) {
//		this.query_output = query_output;
//	}



	public String getQueryTitle(){
		return query_title;
	}	
	public Integer getQueryId(){
		return query_id;
	}
	public Integer getResultId(){
		return result_id;
	}
	public String getResultContents(){
		return result_contents;
	}
	public String getResultAuthor(){
		return result_author;
	}
	public String getResultDay(){
		return result_day;
	}
//	public String getQueryTag() {
//		return query_tag;
//	}
//	public String getQueryOutput() {
//		return query_output;
//	}

}
