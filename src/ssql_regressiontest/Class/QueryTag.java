package ssql_regressiontest.Class;

public class QueryTag {
	private String tag_name;
	private int tag_id;

	public QueryTag(String tag_name,int tag_id){
		this.tag_name = tag_name;
		this.tag_id = tag_id;
	}

	public void setQueryTitle(String tag_name){
		this.tag_name = tag_name;
	}
	public void setQueryId(int tag_id){
		this.tag_id = tag_id;
	}
	

	public String getQueryTag(){
		return tag_name;
	}
	public Integer getTagId(){
		return tag_id;
	}


}
