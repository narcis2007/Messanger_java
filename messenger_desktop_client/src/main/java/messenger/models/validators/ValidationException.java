package messenger.models.validators;

import messenger.utils.CustomRuntimeException;

public class ValidationException extends CustomRuntimeException {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ValidationException(Errors errors) {
    	super(errors);
    }
    
}
