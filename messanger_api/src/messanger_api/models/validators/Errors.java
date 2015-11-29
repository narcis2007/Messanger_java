package messanger_api.models.validators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Errors {
	protected List<String> errors;
	
	public Errors(){
		errors=new ArrayList<String>();
	}
	public void add(String error){
		errors.add(error);
	}
	public Collection<String> getAll(){
		return errors;
	}
	
	public int size(){
		return errors.size();
	}
}
