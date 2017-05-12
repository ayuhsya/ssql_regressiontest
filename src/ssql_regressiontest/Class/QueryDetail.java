package ssql_regressiontest.Class;

public class QueryDetail {
	private Integer query_id;
	private String query_title;
	private String query_name;
	private String query_contents;
	private String query_description;
	private String query_author;
	private String query_day;
	private String result_contents;
	private String result_author;
	private String result_day;
	private String query_tag;
	private String query_output;



	public QueryDetail( Integer query_id, String query_title,String query_name, String query_contents, String query_description,
			String query_author, String query_day, String result_contents, String result_author, String result_day, String query_tag,String query_output){
		
		this.query_title = query_title;
		this.query_id = query_id;
		this.query_name = query_name;
		this.query_contents = query_contents;
		this.query_description = query_description;
		this.query_author = query_author;
		this.query_day = query_day;
		this.result_contents = result_contents;
		this.result_author = result_author;
		this.result_day = result_day;
		this.query_tag = query_tag;
		this.query_output = query_output;
	}


	public void setQueryTitle(String query_title){
		this.query_title = query_title;
	}
	public void setQueryId(Integer query_id){
		this.query_id = query_id;
	}
	public void setQueryName(String query_name){
		this.query_name = query_name;
	}
	public void setQueryContents(String query_contents){
		this.query_contents = query_contents;
	}
	public void setQueryDescription(String query_description){
		this.query_description = query_description;
	}
	public void setQueryAuthor(String query_author){
		this.query_author = query_author;
	}
	public void setQueryDay(String query_day){
		this.query_day = query_day;
	}
	public void setResultContents(String result_contents){
		this.result_contents = result_contents;
	}
	public void setResultAuthor(String result_author){
		this.result_author = result_author;
	}
	public void setResultDay(String result_day){
		this.result_day = result_day;
	}
	public void setQueryTag(String query_tag){
		this.query_tag = query_tag;
	}
	public void setQueryOutput(String query_output){
		this.query_output = query_output;
	}


	
	
	public String getQueryTitle(){
		return query_title;
	}
	public Integer getQueryId(){
		return query_id;
	}
	public String getQueryName(){
		return query_name;
	}
	public String getQueryContents(){
		return query_contents;
	}
	public String getQueryDescription( ){
		return query_description;
	}
	public String getQueryAuthor( ){
		return query_author;
	}
	public String getQueryDay( ){
		return query_day;
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
	public String getQueryTag(){
		return query_tag;
	}
	public String getQueryOutput(){
		return query_output;
	}

}
