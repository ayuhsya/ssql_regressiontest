package ssql_regressiontest.Class;

public class TreeData {
	private int query_id;
	private int tag_id;
	private int output_id;
	private String query_title;
	private String tag_name;
	private String output_name;


	public TreeData( int query_id, int tag_id, int output_id, String query_title, String tag_name, String output_name ){
	
		this.query_id = query_id;
		this.tag_id = tag_id;
		this.output_id = output_id;
		this.query_title = query_title;
		this.tag_name = tag_name;
		this.output_name = output_name;

	}


	public void setQueryId(int query_id){
		this.query_id = query_id;
	}
	public void setTagId(int tag_id){
		this.tag_id = tag_id;
	}
	public void setOutputyId(int output_id){
		this.output_id = output_id;
	}
	public void setQueryTitle(String query_title){
		this.query_title = query_title;
	}
	public void setTagName(String tag_name){
		this.tag_name = tag_name;
	}
	public void setOutputName(String output_name){
		this.output_name = output_name;
	}


	public Integer getQueryId(){
		return query_id;
	}
	public Integer getTagId(){
		return tag_id;
	}
	public Integer getOutputId(){
		return output_id;
	}	
	public String getQueryTitle(){
		return query_title;
	}
	public String getTagName(){
		return tag_name;
	}
	public String getOutputName(){
		return output_name;
	}
	

}
