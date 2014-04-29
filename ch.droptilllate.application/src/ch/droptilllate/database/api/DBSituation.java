package ch.droptilllate.database.api;

public enum DBSituation {
	  LOCAL_DATABASE(true),
	  UPDATE_DATABASE(false);
	  
	  private boolean value;
	  
	  private DBSituation(boolean value){
		  this.value = value;
	  }

}
