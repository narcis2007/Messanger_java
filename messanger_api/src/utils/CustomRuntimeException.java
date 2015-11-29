package utils;

import messanger_api.models.validators.Errors;

public abstract class CustomRuntimeException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Errors errors;
	
	public CustomRuntimeException(Errors errors){
		this.errors=errors;
	}
	
	public Errors getErrors(){
    	return errors;
    }
}
