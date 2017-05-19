package ssql_regressiontest.Class;

public class ConfigData {
	private String ssql_location_path;
	private String ssqllibs_location_path;
	private String ssql_out_location_path;
	private String ssql_result_location_path;
	private int result_regist_number;
	private String userName;
	private String sqlite_db_path1;
	private String sqlite_db_path2;

	public ConfigData(){
//		this.ssql_location_path = ssql_location_path;
//		this.ssqllibs_location_path = ssqllibs_location_path;
//		this.ssql_out_location_path = ssql_out_location_path;
//		this.ssql_result_location_path = ssql_result_location_path;
//		this.result_regist_number = result_regist_number;
//		this.userName = userName;
	}


	public void setSsqlLocationPath(String ssql_location_path){
		this.ssql_location_path = ssql_location_path;
	}
	public void setSsqlLibsLocationPath(String ssqllibs_location_path){
		this.ssqllibs_location_path = ssqllibs_location_path;
	}
	public void setSsqlOutLocationPath(String ssql_out_location_path){
		this.ssql_out_location_path = ssql_out_location_path;
	}
	public void setSsqlResultLocationPath(String ssql_result_location_path){
		this.ssql_result_location_path = ssql_result_location_path;
	}
	public void setResultRegistNumber(int i){
		this.result_regist_number = i;
	}
	public void setUserName(String userName){
		this.userName = userName;
	}
	public void set_sqlite_db_path1(String sqlite_db_path1){
		this.sqlite_db_path1 = sqlite_db_path1;
	}
	public void set_sqlite_db_path2(String sqlite_db_path2){
		this.sqlite_db_path2 = sqlite_db_path2;
	}

	
	public String getSsqlLocationPath(){
		return ssql_location_path;
	}
	public String getSsqlLibsLocationPath(){
		return ssqllibs_location_path;
	}
	public String getSsqlOutLocationPath(){
		return ssql_out_location_path;
	}
	public String getSsqlResultLocationPath( ){
		return ssql_result_location_path;
	}
	public int getResultRegistNumber( ){
		return result_regist_number;
	}
	public String getUserName( ){
		return userName;
	}
	public String get_sqlite_db_path1( ){
		return sqlite_db_path1;
	}
	public String get_sqlite_db_path2( ){
		return sqlite_db_path2;
	}

}
