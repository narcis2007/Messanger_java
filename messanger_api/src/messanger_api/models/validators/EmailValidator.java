package messanger_api.models.validators;

import messanger_api.models.Email;
import utils.StringUtils;

public class EmailValidator implements Validator<Email> {
	
	@Override
	public Errors validate(Email email) {
		Errors errors=new Errors();
		
		if(StringUtils.isNotEmptyAndNotNull(email.getTitle())==false)
				errors.add("invalid title");
		if(StringUtils.isNotEmptyAndNotNull(email.getContent())==false)
				errors.add("invalid content");
		if(StringUtils.isNotEmptyAndNotNull(email.getOwner())==false)
			errors.add("the email must belong to someone ");
		return errors;
	}
}
