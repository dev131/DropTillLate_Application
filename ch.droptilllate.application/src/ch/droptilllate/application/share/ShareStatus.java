package ch.droptilllate.application.share;

public enum ShareStatus {

		  OK_NEW("Sharing successfull, new ShareRelation created"),
		  OK_Existing("Sharing successfull, use existing ShareRelation"),
		  ERROR("Sharing failed try again or manually"),
		  CREATE_NEW("Create new shareRelation"),
		  USE_EXISTING("Use existing ShareRelation"),
		  SHARERELATION_EXISTING("All sharemembers have already together a ShareRelation"),
		  ACCOUNT_NOT_EXISTING("No CloudProvider settings found");
		  
		  private String value;
		  
		  private ShareStatus(String value){
			  this.value = value;
		  }

		  public String toString(){
				return value;
			}
}
