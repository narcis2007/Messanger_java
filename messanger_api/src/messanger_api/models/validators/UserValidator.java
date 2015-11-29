package messanger_api.models.validators;


import messanger_api.models.User;
import utils.StringUtils;

public class UserValidator implements Validator<User> {

	@Override
	public Errors validate(User user) {
		Errors errors=new Errors();
		if((StringUtils.isNotEmptyAndNotNull(user.getUsername())&&StringUtils.isNotEmptyAndNotNull(user.getUsername()))==false)
			errors.add("invalid username");
		if(StringUtils.isNotEmptyAndNotNull(user.getPassword())==false)
			errors.add("invalid password");
		return errors;
	}
}
