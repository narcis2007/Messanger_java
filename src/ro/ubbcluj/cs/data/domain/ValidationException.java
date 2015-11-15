package ro.ubbcluj.cs.data.domain;

import ro.ubbcluj.cs.data.domain.Errors;

public class ValidationException extends CustomRuntimeException {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ValidationException(Errors errors) {
    	super(errors);
    }
    
}
