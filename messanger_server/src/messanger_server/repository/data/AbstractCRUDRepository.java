package messanger_server.repository.data;

import java.util.ArrayList;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import messanger_api.models.validators.Errors;
import messanger_api.models.validators.ValidationException;
import messanger_api.models.validators.Validator;


public abstract class AbstractCRUDRepository<E,ID> implements CRUDRepository<E, ID> {

	protected List<E> elements=new ArrayList<E>();
	private Validator<E> validator;
	

	protected static final Logger log = Logger.getLogger( AbstractCRUDRepository.class.getName() );
	
	protected AbstractCRUDRepository(Validator<E> validator) {
		this.validator=validator;
	}
	
	
	public AbstractCRUDRepository() {
		;
	}


	@Override
	public synchronized void save(E e) {
		if(validator!=null){
			
			Errors errors=validator.validate(e);
			if (errors!=null&&errors.size() > 0) {
	            throw new ValidationException(errors);
	        }
		}
		if(!idIsSet(e))
			setEntityId(e, elements.size()+1);
		elements.add(e);
		
	}

	protected abstract boolean idIsSet(E e);

	@Override
	public synchronized boolean update(E e) {
		if(validator!=null){
			Errors errors=validator.validate(e);
			if (errors.size() > 0) {
	            throw new ValidationException(errors);
	        }
		}
		
		int size=elements.size();
		elements=elements.parallelStream().filter((element)->!getEntityId(element).equals(getEntityId(e))).collect(Collectors.toList());
		System.out.println(elements.size());
		if (size>elements.size())
		{
			elements.add(e);
			return true;
		}

		return false;
		
	}

	@Override
	public synchronized boolean delete(ID id) {
		int size=elements.size();
		elements=elements.stream().filter((e)->!(getEntityId(e).equals(id))).collect(Collectors.toList());

		return size>elements.size();
	}
	@Override
	public int count() {
		return elements.size();
	}

	@Override
	public E find(ID id) {
		E element;
		try {
			element=elements.stream().filter((e)-> getEntityId(e).equals(id)).findFirst().get();
			
		} catch (NoSuchElementException e) {
			return null;
		}
		
		return element;
	}

	@Override
	public Collection<E> getAll() {
		return elements;
	}
	protected abstract void setEntityId(E e, int id);//cum pot sa scap de parametrul ID (din interfata)?
	protected abstract ID getEntityId(E e);
}
