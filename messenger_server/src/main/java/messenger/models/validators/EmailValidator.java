package messenger.models.validators;

import messenger.models.Email;
import messenger.utils.StringUtils;

public class EmailValidator implements Validator<Email> {
	
	@Override
	public Errors validate(Email email) {
		Errors errors=new Errors();
		
		if(StringUtils.isNotEmptyAndNotNull(email.getTitle())==false)
				errors.add("invalid title");
		if(StringUtils.isNotEmptyAndNotNull(email.getContent())==false)
				errors.add("invalid content");
		if(StringUtils.isNotEmptyAndNotNull(email.getSender())==false )
			errors.add("the email must belong to someone ");
		return errors;
	}
}
